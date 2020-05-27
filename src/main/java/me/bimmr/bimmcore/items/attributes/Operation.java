package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Operations
 * <p>
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public enum Operation {
    ADD_NUMBER(0),
    @Deprecated
    MULTIPLY_PERCENTAGE(1),
    MULTIPLY_SCALAR_1(1),
    @Deprecated
    ADD_PERCENTAGE(2),
    ADD_SCALAR(2);

    private int id;

    private Operation(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}