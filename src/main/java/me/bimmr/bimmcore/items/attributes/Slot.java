package me.bimmr.bimmcore.items.attributes;

/**
 * Created by Randy on 05/12/16.
 */
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

    public String getName() {
        return this.name;
    }
}