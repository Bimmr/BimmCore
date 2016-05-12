package me.bimmr.bimmcore.items.attributes;

/**
 * Created by Randy on 05/12/16.
 */
public class Attribute {
    private AttributeType attribute;
    private Slot          slot;
    private Operation     operation;
    private double        value;

    public Attribute(AttributeType attribute, Slot slot, double value, Operation operation) {
        this.attribute = attribute;
        this.slot = slot;
        this.value = value;
        this.operation = operation;
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public Slot getSlot() {
        return slot;
    }

    public Operation getOperation() {
        return operation;
    }

    public double getValue() {
        return value;
    }
}
