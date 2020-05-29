package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Types
 * <p>
 * Used from 1.8.8 - 1.13
 */
public enum AttributeType {
    GENERIC_MAX_HEALTH("generic.maxHealth"),
    @Deprecated
    MAX_HEALTH("generic.maxHealth"),
    GENERIC_FOLLOW_RANGE("generic.followRange"),
    @Deprecated
    FOLLOW_RANGE("generic.followRange"),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    @Deprecated
    KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed"),
    @Deprecated
    MOVEMENT_SPEED("generic.movementSpeed"),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage"),
    @Deprecated
    ATTACK_DAMAGE("generic.attackDamage"),
    GENERIC_ARMOR("generic.armor"),
    GENERIC_ARMOR_THOUGHNESS("generic.armorToughness"),
    @Deprecated
    ARMOR("generic.armor"),
    @Deprecated
    ARMOR_THOUGHNESS("generic.armorToughness"),
    GENERIC_ATTACK_SPEED("generic.attackSpeed"),
    @Deprecated
    ATTACK_SPEED("generic.attackSpeed"),
    GENERIC_LUCK("generic.luck"),
    @Deprecated
    LUCK("generic.luck"),
    GENERIC_JUMP_STRENGTH("horse.jumpStrength"),
    @Deprecated
    JUMP_STRENGTH("horse.jumpStrength"),
    GENERIC_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements"),
    @Deprecated
    SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

    private String name;

    AttributeType(String name) {
        this.name = name;
    }

    public static AttributeType getByName(String name) {
        for (AttributeType type : AttributeType.values())
            if (type.name.equals(name))
                return type;
        return null;
    }

    /**
     * @return Get Attribute Name
     */
    public String getName() {
        return this.name;
    }
}