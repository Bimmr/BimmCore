package me.bimmr.bimmcore.items.attributes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 05/12/16.
 * <p>
 * ItemStack item;
 * ItemAttributes ia = new ItemAttributes(item);
 * ia.add(new Attribute());
 * item = ia.build();
 */
class Example {
    public Example() {
        ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
        ItemAttributes ia = new ItemAttributes(stack);
        ia.addAttribute(new Attribute(AttributeType.ATTACK_DAMAGE, Slot.MAIN_HAND, 10, Operation.ADD_NUMBER));
        ia.addAttribute(new Attribute(AttributeType.ATTACK_SPEED, Slot.MAIN_HAND, 100, Operation.ADD_PERCENTAGE));
        stack = ia.build();
    }
}

public class ItemAttributes {

    private List<Attribute> attributes = new ArrayList<>();
    private ItemStack itemStack;

    /**
     * Create an ItemAttributes
     *
     * @param itemStack
     */
    public ItemAttributes(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Load the attributes that are already on the ItemStack
     */
    public void loadAttributes() {

    }

    /**
     * Set an itemstack
     *
     * @param itemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Get all the Attributes
     *
     * @return
     */
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Add an Attribute
     *
     * @param attribute
     * @return
     */
    public ItemAttributes addAttribute(Attribute attribute) {
        attributes.add(attribute);
        return this;
    }

    /**
     * Build the ItemStack with the attributes
     *
     * @return
     */
    public ItemStack build() {
        return new ItemModifier(this.itemStack, this).build();
    }
}
