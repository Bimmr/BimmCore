package me.bimmr.bimmcore.reflection.tinyprotocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.bimmr.bimmcore.reflection.tinyprotocol.TinyProtocolReflection.FieldAccessor;
import me.bimmr.bimmcore.reflection.tinyprotocol.TinyProtocolReflection.MethodInvoker;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;

/**
 * The type Tiny protocol.
 */
public abstract class TinyProtocol {
    private static final AtomicInteger ID = new AtomicInteger(0);

    // Used in order to lookup a channel
    private static final MethodInvoker getPlayerHandle = TinyProtocolReflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
    private static final FieldAccessor<Object> getConnection = TinyProtocolReflection.getField("{nms}.EntityPlayer", "playerConnection", Object.class);
    private static final FieldAccessor<Object> getManager = TinyProtocolReflection.getField("{nms}.PlayerConnection", "networkManager", Object.class);
    private static final FieldAccessor<Channel> getChannel = TinyProtocolReflection.getField("{nms}.NetworkManager", Channel.class, 0);

    // Looking up ServerConnection
    private static final Class<Object> minecraftServerClass = TinyProtocolReflection.getUntypedClass("{nms}.MinecraftServer");
    private static final Class<Object> serverConnectionClass = TinyProtocolReflection.getUntypedClass("{nms}.ServerConnection");
    private static final FieldAccessor<Object> getMinecraftServer = TinyProtocolReflection.getField("{obc}.CraftServer", minecraftServerClass, 0);
    private static final FieldAccessor<Object> getServerConnection = TinyProtocolReflection.getField(minecraftServerClass, serverConnectionClass, 0);
    private static final MethodInvoker getNetworkMarkers = TinyProtocolReflection.getTypedMethod(serverConnectionClass, null, List.class, serverConnectionClass);

    // Packets we have to intercept
    private static final Class<?> PACKET_LOGIN_IN_START = TinyProtocolReflection.getMinecraftClass("PacketLoginInStart");
    private static final FieldAccessor<GameProfile> getGameProfile = TinyProtocolReflection.getField(PACKET_LOGIN_IN_START, GameProfile.class, 0);

    // Speedup channel lookup
    private Map<String, Channel> channelLookup = new MapMaker().weakValues().makeMap();
    private Listener listener;

    // Channels that have already been removed
    private Set<Channel> uninjectedChannels = Collections.newSetFromMap(new MapMaker().weakKeys().<Channel, Boolean>makeMap());

    // List of network markers
    private List<Object> networkManagers;

    // Injected channel handlers
    private List<Channel> serverChannels = Lists.newArrayList();
    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> beginInitProtocol;
    private ChannelInitializer<Channel> endInitProtocol;

    // Current handler name
    private String handlerName;

    /**
     * The Closed.
     */
    protected volatile boolean closed;
    /**
     * The Plugin.
     */
    protected Plugin plugin;

    /**
     * Instantiates a new Tiny protocol.
     *
     * @param plugin the plugin
     */
    public TinyProtocol(final Plugin plugin) {
        this.plugin = plugin;

        // Compute handler name
        this.handlerName = getHandlerName();

        // Prepare existing players
        registerBukkitEvents();

        try {
            registerChannelHandler();
            registerPlayers(plugin);
        } catch (IllegalArgumentException ex) {
            // Damn you, late bind
            plugin.getLogger().info("[TinyProtocol] Delaying server channel injection due to late bind.");

            new BukkitRunnable() {
                @Override
                public void run() {
                    registerChannelHandler();
                    registerPlayers(plugin);
                    plugin.getLogger().info("[TinyProtocol] Late bind injection successful.");
                }
            }.runTask(plugin);
        }
    }

    private void createServerChannelHandler() {
        // Handle connected channels
        endInitProtocol = new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                try {
                    // This can take a while, so we need to stop the main thread from interfering
                    synchronized (networkManagers) {
                        // Stop injecting channels
                        if (!closed) {
                            channel.eventLoop().submit(() -> injectChannelInternal(channel));
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Cannot inject incomming channel " + channel, e);
                }
            }

        };

        // This is executed before Minecraft's channel handler
        beginInitProtocol = new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(endInitProtocol);
            }

        };

        serverChannelHandler = new ChannelInboundHandlerAdapter() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                Channel channel = (Channel) msg;

                // Prepare to initialize ths channel
                channel.pipeline().addFirst(beginInitProtocol);
                ctx.fireChannelRead(msg);
            }

        };
    }

    private void registerBukkitEvents() {
        listener = new Listener() {

            @EventHandler(priority = EventPriority.LOWEST)
            public final void onPlayerLogin(PlayerLoginEvent e) {
                if (closed)
                    return;

                Channel channel = getChannel(e.getPlayer());

                // Don't inject players that have been explicitly uninjected
                if (!uninjectedChannels.contains(channel)) {
                    injectPlayer(e.getPlayer());
                }
            }

            @EventHandler
            public final void onPluginDisable(PluginDisableEvent e) {
                if (e.getPlugin().equals(plugin)) {
                    close();
                }
            }

        };

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @SuppressWarnings("unchecked")
    private void registerChannelHandler() {
        Object mcServer = getMinecraftServer.get(Bukkit.getServer());
        Object serverConnection = getServerConnection.get(mcServer);
        boolean looking = true;

        // We need to synchronize against this list
        networkManagers = (List<Object>) getNetworkMarkers.invoke(null, serverConnection);
        createServerChannelHandler();

        // Find the correct list, or implicitly throw an exception
        for (int i = 0; looking; i++) {
            List<Object> list = TinyProtocolReflection.getField(serverConnection.getClass(), List.class, i).get(serverConnection);

            for (Object item : list) {
                if (!ChannelFuture.class.isInstance(item))
                    break;

                // Channel future that contains the server connection
                Channel serverChannel = ((ChannelFuture) item).channel();

                serverChannels.add(serverChannel);
                serverChannel.pipeline().addFirst(serverChannelHandler);
                looking = false;
            }
        }
    }

    private void unregisterChannelHandler() {
        if (serverChannelHandler == null)
            return;

        for (Channel serverChannel : serverChannels) {
            final ChannelPipeline pipeline = serverChannel.pipeline();

            // Remove channel handler
            serverChannel.eventLoop().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        pipeline.remove(serverChannelHandler);
                    } catch (NoSuchElementException e) {
                        // That's fine
                    }
                }

            });
        }
    }

    private void registerPlayers(Plugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            injectPlayer(player);
        }
    }

    /**
     * On packet out async object.
     *
     * @param receiver the receiver
     * @param channel  the channel
     * @param packet   the packet
     * @return the object
     */
    public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
        return packet;
    }

    /**
     * On packet in async object.
     *
     * @param sender  the sender
     * @param channel the channel
     * @param packet  the packet
     * @return the object
     */
    public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
        return packet;
    }

    /**
     * Send packet.
     *
     * @param player the player
     * @param packet the packet
     */
    public void sendPacket(Player player, Object packet) {
        sendPacket(getChannel(player), packet);
    }

    /**
     * Send packet.
     *
     * @param channel the channel
     * @param packet  the packet
     */
    public void sendPacket(Channel channel, Object packet) {
        channel.pipeline().writeAndFlush(packet);
    }

    /**
     * Receive packet.
     *
     * @param player the player
     * @param packet the packet
     */
    public void receivePacket(Player player, Object packet) {
        receivePacket(getChannel(player), packet);
    }

    /**
     * Receive packet.
     *
     * @param channel the channel
     * @param packet  the packet
     */
    public void receivePacket(Channel channel, Object packet) {
        channel.pipeline().context("encoder").fireChannelRead(packet);
    }

    /**
     * Gets handler name.
     *
     * @return the handler name
     */
    protected String getHandlerName() {
        return "tiny-" + plugin.getName() + "-" + ID.incrementAndGet();
    }

    /**
     * Inject player.
     *
     * @param player the player
     */
    public void injectPlayer(Player player) {
        injectChannelInternal(getChannel(player)).player = player;
    }

    /**
     * Inject channel.
     *
     * @param channel the channel
     */
    public void injectChannel(Channel channel) {
        injectChannelInternal(channel);
    }

    private PacketInterceptor injectChannelInternal(Channel channel) {
        try {
            PacketInterceptor interceptor = (PacketInterceptor) channel.pipeline().get(handlerName);

            // Inject our packet interceptor
            if (interceptor == null) {
                interceptor = new PacketInterceptor();
                channel.pipeline().addBefore("packet_handler", handlerName, interceptor);
                uninjectedChannels.remove(channel);
            }

            return interceptor;
        } catch (IllegalArgumentException e) {
            // Try again
            return (PacketInterceptor) channel.pipeline().get(handlerName);
        }
    }

    /**
     * Gets channel.
     *
     * @param player the player
     * @return the channel
     */
    public Channel getChannel(Player player) {
        Channel channel = channelLookup.get(player.getName());

        // Lookup channel again
        if (channel == null) {
            Object connection = getConnection.get(getPlayerHandle.invoke(player));
            Object manager = getManager.get(connection);

            channelLookup.put(player.getName(), channel = getChannel.get(manager));
        }

        return channel;
    }

    /**
     * Uninject player.
     *
     * @param player the player
     */
    public void uninjectPlayer(Player player) {
        uninjectChannel(getChannel(player));
    }

    /**
     * Uninject channel.
     *
     * @param channel the channel
     */
    public void uninjectChannel(final Channel channel) {
        // No need to guard against this if we're closing
        if (!closed) {
            uninjectedChannels.add(channel);
        }

        // See ChannelInjector in ProtocolLib, line 590
        channel.eventLoop().execute(new Runnable() {

            @Override
            public void run() {
                channel.pipeline().remove(handlerName);
            }

        });
    }

    /**
     * Has injected boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasInjected(Player player) {
        return hasInjected(getChannel(player));
    }

    /**
     * Has injected boolean.
     *
     * @param channel the channel
     * @return the boolean
     */
    public boolean hasInjected(Channel channel) {
        return channel.pipeline().get(handlerName) != null;
    }

    /**
     * Close.
     */
    public final void close() {
        if (!closed) {
            closed = true;

            // Remove our handlers
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                uninjectPlayer(player);
            }

            // Clean up Bukkit
            HandlerList.unregisterAll(listener);
            unregisterChannelHandler();
        }
    }

    private final class PacketInterceptor extends ChannelDuplexHandler {
        /**
         * The Player.
         */
// Updated by the login event
        public volatile Player player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Intercept channel
            final Channel channel = ctx.channel();
            handleLoginStart(channel, msg);

            try {
                msg = onPacketInAsync(player, channel, msg);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketInAsync().", e);
            }

            if (msg != null) {
                super.channelRead(ctx, msg);
            }
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            try {
                msg = onPacketOutAsync(player, ctx.channel(), msg);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketOutAsync().", e);
            }

            if (msg != null) {
                super.write(ctx, msg, promise);
            }
        }

        private void handleLoginStart(Channel channel, Object packet) {
            if (PACKET_LOGIN_IN_START.isInstance(packet)) {
                GameProfile profile = getGameProfile.get(packet);
                channelLookup.put(profile.getName(), channel);
            }
        }
    }
}