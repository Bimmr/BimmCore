package me.bimmr.bimmcore.npc.mob;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.npc.NPCBase;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class NPCMob extends NPCBase {

    private EntityType entityType;
    private LivingEntity entity;

    public NPCMob(String name, Location location) {
        this(name, location, EntityType.ZOMBIE);
    }

    public NPCMob(String name, Location location, EntityType entityType) {
        super(NPCType.MOB, name, location);

        if (!BimmCore.supports(9)) {
            BimmCore.getInstance().getLogger().log(Level.SEVERE, "Unable to create NPC Mob before MC 1.9");
            return;
        }
        this.entityType = entityType;
        create();
    }

    public void setType(EntityType type) {
        this.entityType = type;
        destroy();
        create();
    }


    @Override
    protected void renamed(String name) {
        this.entity.setCustomName(name);
    }

    @Override
    protected void teleported(Location location) {
        this.entity.teleport(location);
    }

    @Override
    protected void equipped(ItemSlots itemSlot, ItemStack itemStack) {
        switch (itemSlot) {
            case MAINHAND:
                this.entity.getEquipment().setItemInMainHand(itemStack);
                break;
            case OFFHAND:
                this.entity.getEquipment().setItemInOffHand(itemStack);
                break;
            case FEET:
                this.entity.getEquipment().setBoots(itemStack);
                break;
            case LEGS:
                this.entity.getEquipment().setLeggings(itemStack);
                break;
            case CHEST:
                this.entity.getEquipment().setChestplate(itemStack);
                break;
            case HEAD:
                this.entity.getEquipment().setHelmet(itemStack);
                break;
        }
    }

    @Override
    protected void destroyed() {
        this.entity.remove();
    }

    @Override
    protected void created() {
        this.entity = (LivingEntity) getLocation().getWorld().spawnEntity(getLocation(), this.entityType);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(getName());
        this.entity.setGravity(false);
        this.entity.setAI(false);
        this.entity.setRemoveWhenFarAway(false);

        if (this.entity instanceof Ageable)
            ((Ageable) this.entity).setAdult();
        if (this.entity instanceof Zombie)
            ((Zombie) this.entity).setBaby(false);
    }

    @Override
    public int getId() {
        return entity.getEntityId();
    }
}
