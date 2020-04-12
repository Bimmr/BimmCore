package me.bimmr.bimmcore.items.attributes;

/**
 * Created by Randy on 05/12/16.
 */
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