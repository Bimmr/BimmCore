package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Slots
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public enum Slot {
    MAIN_HAND("mainhand"),
    OFF_HAND("offhand"),
    FEET("feet"),
    LEGS("legs"),
    CHEST("chest"),
    HEAD("head");

    private String name;

    private Slot(String name) {
        this.name = name;
    }

    /**
     * @return Get Slot name
     */
    public String getName() {
        return this.name;
    }
}