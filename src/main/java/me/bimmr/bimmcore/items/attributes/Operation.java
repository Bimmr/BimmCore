package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Operations
 * <p>
 * Used from 1.8.8 - 1.13
 */
public enum Operation {
    ADD_NUMBER(0),
    @Deprecated
    ADD(0),

    MULTIPLY_SCALAR_1(1),
    @Deprecated
    MULTIPLY(1),
    @Deprecated
    MULTIPLY_PERCENTAGE(1),

    ADD_SCALAR(2),
    @Deprecated
    PERCENT(2),
    @Deprecated
    SCALAR(2),
    @Deprecated
    PERCENTAGE(2),
    @Deprecated
    ADD_PERCENTAGE(2);

    private int id;

    Operation(int id) {
        this.id = id;
    }

    public static Operation getByID(int id) {
        for (Operation operation : Operation.values())
            if (operation.id == id)
                return operation;
        return Operation.ADD_NUMBER;
    }

    public int getID() {
        return this.id;
    }
}