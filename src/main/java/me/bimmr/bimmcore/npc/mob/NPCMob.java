package me.bimmr.bimmcore.npc.mob;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class NPCMob extends NPC {

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
    public void renamed(String name) {
        this.entity.setCustomName(name);
    }

    @Override
    public void teleported(Location location) {
        this.entity.teleport(location);
    }

    @Override
    public void equipped(ItemSlots itemSlot, ItemStack itemStack) {
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
    public void destroyed() {
        this.entity.remove();
    }

    @Override
    public void created() {
        this.entity = (LivingEntity) getLocation().getWorld().spawnEntity(getLocation(), this.entityType);
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(getName());
        this.entity.setGravity(false);
        this.entity.setAI(false);
        this.entity.setRemoveWhenFarAway(false);

        if (this.entity instanceof Ageable)
            ((Ageable) this.entity).setAdult();
    }

    @Override
    public int getId() {
        return entity.getEntityId();
    }
}
