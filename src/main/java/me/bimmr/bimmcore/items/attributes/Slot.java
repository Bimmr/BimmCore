package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Slots
 * Used from 1.8.8 - 1.13
 */
public enum Slot {
    HAND("mainhand"),
    OFF_HAND("offhand"),
    @Deprecated
    MAIN_HAND("mainhand"),
    FEET("feet"),
    @Deprecated
    BOOTS("feet"),
    LEGS("legs"),
    @Deprecated
    LEGGINGS("legs"),
    CHEST("chest"),
    @Deprecated
    CHESTPLATE("chest"),
    HEAD("head"),
    @Deprecated
    HELMET("head"),
    ALL(null);
    private String name;

    Slot(String name) {
        this.name = name;
    }

    /**
     * @return Get Slot name
     */
    public String getName() {
        return this.name;
    }


    public static Slot getByName(String name) {
        for (Slot slot : Slot.values())
            if (slot.name.equals(name))
                return slot;
        return null;
    }
}