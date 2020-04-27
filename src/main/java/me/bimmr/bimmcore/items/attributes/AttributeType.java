package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Types
 *
 * Used from 1.8.8 - 1.13
 */
@Deprecated
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

    /**
     *
     * @return Get Attribute Name
     */
    public String getName() {
        return this.name;
    }
}