package me.bimmr.bimmcore.items.attributes;

/**
 * Attribute Class
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public class Attribute {
    private AttributeType attribute;
    private Slot          slot;
    private Operation     operation;
    private double        value;

    /**
     * Create a new Attribute
     * @param attribute Attribute Type
     * @param slot Attribute Slot
     * @param value Attribute Level
     * @param operation Attribute Opperation
     */
    public Attribute(AttributeType attribute, Slot slot, double value, Operation operation) {
        this.attribute = attribute;
        this.slot = slot;
        this.value = value;
        this.operation = operation;
    }

    /**
     *
     * @return Get Attribute type
     */
    public AttributeType getAttribute() {
        return attribute;
    }

    /**
     *
     * @return Get Attribute slot
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     *
     * @return Get Attribute Operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     *
     * @return Get Attribute Level
     */
    public double getValue() {
        return value;
    }
}