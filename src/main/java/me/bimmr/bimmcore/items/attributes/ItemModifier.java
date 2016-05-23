package me.bimmr.bimmcore.items.attributes;

import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Randy on 05/12/16.
 */
public class ItemModifier {
    //Static variables
    private static Class<?>       craftItemStack;
    private static Method         asNMSCopy;
    private static Class<?>       nbtTagCompound;
    private static Class<?>       nbtTagList;
    private static Class<?>       nbtBase;
    private static Constructor<?> nbtTagString;
    private static Constructor<?> nbtTagInt;
    private static Constructor<?> nbtTagDouble;
    private static Constructor<?> nbtTagLong;

    //Non-Static Variables
    private Object         nmsItemStack;
    private Object         nbtTag;
    private Object         nbtTags;
    private ItemAttributes itemAttributes;

    /**
     * Create an ItemModifier
     *
     * @param itemStack
     * @param itemAttributes
     */
    public ItemModifier(ItemStack itemStack, ItemAttributes itemAttributes) {
        nmsItemStack = getNMSItemStack(itemStack);
        this.itemAttributes = itemAttributes;
    }

    static {
        craftItemStack = Reflection.getCraftClass("inventory.CraftItemStack");
        asNMSCopy = Reflection.getMethod(craftItemStack, "asNMSCopy", ItemStack.class);
        nbtTagCompound = Reflection.getNMSClass("NBTTagCompound");
        nbtBase = Reflection.getNMSClass("NBTBase");
        nbtTagList = Reflection.getNMSClass("NBTTagList");
        try {
            nbtTagString = Reflection.getNMSClass("NBTTagString").getConstructor(String.class);
            nbtTagInt = Reflection.getNMSClass("NBTTagInt").getConstructor(int.class);
            nbtTagDouble = Reflection.getNMSClass("NBTTagDouble").getConstructor(double.class);
            nbtTagLong = Reflection.getNMSClass("NBTTagLong").getConstructor(long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Object getNMSItemStack(ItemStack itemStack) {
        try {
            return asNMSCopy.invoke(craftItemStack, itemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getNBTTagCompound(Object nmsItem) {
        boolean hasNBTTagCompound = false;
        try {
            Object tagCompound = Reflection.getPrivateField(nmsItem.getClass(), "tag");

            if (tagCompound == null)
                tagCompound = nbtTagCompound.newInstance();

            return tagCompound;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object createNBTCompound(AttributeType attribute, Slot slot, Object value, Operation operation) {
        try {
            Object att = nbtTagCompound.newInstance();
            Method set = att.getClass().getMethod("set", String.class, nbtBase);
            UUID uuid = UUID.randomUUID();

            set.invoke(att, "AttributeName", nbtTagString.newInstance(attribute.getName()));
            set.invoke(att, "Name", nbtTagString.newInstance(attribute.getName()));
            set.invoke(att, "Amount", nbtTagDouble.newInstance(value));
            set.invoke(att, "Operation", nbtTagInt.newInstance(operation.getID()));
            set.invoke(att, "UUIDLeast", nbtTagLong.newInstance(uuid.getLeastSignificantBits()));
            set.invoke(att, "UUIDMost", nbtTagLong.newInstance(uuid.getMostSignificantBits()));
            set.invoke(att, "Slot", nbtTagString.newInstance(slot.getName()));
            return att;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemStack build() {
        try {
            nbtTag = getNBTTagCompound(nmsItemStack);
            nbtTags = nbtTagList.newInstance();

            Method add = nbtTags.getClass().getMethod("add", nbtBase);
            for (Attribute attribute : itemAttributes.getAttributes())
                add.invoke(nbtTags, createNBTCompound(attribute.getAttribute(), attribute.getSlot(), attribute.getValue(), attribute.getOperation()));

            nmsItemStack.getClass().getMethod("a", String.class, nbtBase).invoke(nmsItemStack, "AttributeModifiers", nbtTags);

            return (ItemStack) Reflection.getMethod(craftItemStack, "asBukkitCopy").invoke(null, nmsItemStack);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
