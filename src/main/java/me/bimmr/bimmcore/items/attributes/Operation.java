package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Operations
 *
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public enum Operation {
    ADD_NUMBER(0),
    MULTIPLY_PERCENTAGE(1),
    ADD_PERCENTAGE(2);

    private int id;

    private Operation(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}