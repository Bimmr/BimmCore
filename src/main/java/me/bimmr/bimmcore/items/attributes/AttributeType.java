package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Types
 * <p>
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public enum AttributeType {
    @Deprecated
    MAX_HEALTH("generic.maxHealth"),
    GENERIC_MAX_HEALTH("generic.maxHealth"),
    @Deprecated
    FOLLOW_RANGE("generic.followRange"),
    GENERIC_FOLLOW_RANGE("generic.followRange"),
    @Deprecated
    KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    @Deprecated
    MOVEMENT_SPEED("generic.movementSpeed"),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed"),
    @Deprecated
    ATTACK_DAMAGE("generic.attackDamage"),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
    @Deprecated
    ARMOR("generic.armor"),
    GENERIC_ARMOR("generic.armor"),
    @Deprecated
    ARMOR_THOUGHNESS("generic.armorToughness"),
    GENERIC_ARMOR_THOUGHNESS("generic.armorToughness"),
    @Deprecated
    ATTACK_SPEED("generic.attackSpeed"),
    GENERIC_ATTACK_SPEED("generic.attackSpeed"),
    @Deprecated
    LUCK("generic.luck"),
    GENERIC_LUCK("generic.luck"),
    @Deprecated
    JUMP_STRENGTH("horse.jumpStrength"),
    GENERIC_JUMP_STRENGTH("horse.jumpStrength"),
    @Deprecated
    SPAWN_REINFORCEMENTS("zombie.spawnReinforcements"),
    GENERIC_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

    private String name;

    private AttributeType(String name) {
        this.name = name;
    }

    /**
     * @return Get Attribute Name
     */
    public String getName() {
        return this.name;
    }
}