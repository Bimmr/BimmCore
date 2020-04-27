package me.bimmr.bimmcore.items.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemAttributes
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public class ItemAttributes {

    private List<Attribute> attributes = new ArrayList<>();
    private ItemStack itemStack;

    /**
     * Create an ItemAttributes
     *
     * @param itemStack The ItemStack
     */
    public ItemAttributes(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Set an itemstack
     *
     * @param itemStack The ItemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * @return Get all the Attributes
     */
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Add an Attribute
     *
     * @param attribute The Attribute to add
     * @return The ItemAttribute
     */
    public ItemAttributes addAttribute(Attribute attribute) {
        attributes.add(attribute);
        return this;
    }

    /**
     * @return Build the ItemStack with the attributes
     */
    public ItemStack build() {
        return new ItemModifier(this.itemStack, this).build();
    }
}