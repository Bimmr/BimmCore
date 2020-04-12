package me.bimmr.bimmcore.items.attributes;

/**
 * Created by Randy on 05/12/16.
 */
public enum AttributeType {
    MAX_HEALTH("generic.maxHealth"),
    FOLLOW_RANGE("generic.followRange"),
    KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    MOVEMENT_SPEED("generic.movementSpeed"),
    ATTACK_DAMAGE("generic.attackDamage"),
    ARMOR("generic.armor"),
    ARMOR_THOUGHNESS("generic.armorToughness"),
    ATTACK_SPEED("generic.attackSpeed"),
    LUCK("generic.luck"),
    JUMP_STRENGTH("horse.jumpStrength"),
    SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

    private String name;

    private AttributeType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}