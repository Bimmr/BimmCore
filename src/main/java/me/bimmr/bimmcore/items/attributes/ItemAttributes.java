package me.bimmr.bimmcore.items.attributes;

import me.bimmr.bimmcore.reflection.Reflection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Item ItemModifier NMS
 * Used from 1.8.8 - 1.13
 */
@Deprecated
public class ItemAttributes {
    //Static variables
    private static Class<?> classCraftItemStack = Reflection.getCraftClass("inventory.CraftItemStack");
    private static Class<?> classNMSItemStack = Reflection.getNMSClass("ItemStack");
    private static Method methodAsNMSCopy = Reflection.getMethod(classCraftItemStack, "asNMSCopy", ItemStack.class);
    private static Method methodAsBukkitCopy = Reflection.getMethod(classCraftItemStack, "asBukkitCopy");

    private static Class<?> classNBTTagCompound = Reflection.getNMSClass("NBTTagCompound");
    private static Class<?> classNBTBase = Reflection.getNMSClass("NBTBase");

    private static Class<?> classNBTTagList = Reflection.getNMSClass("NBTTagList");
    private static Class<?> classNBTTagString = Reflection.getNMSClass("NBTTagString");
    private static Class<?> classNBTTagInt = Reflection.getNMSClass("NBTTagInt");
    private static Class<?> classNBTTagDouble = Reflection.getNMSClass("NBTTagDouble");
    private static Class<?> classNBTTagLong = Reflection.getNMSClass("NBTTagLong");

    private static Constructor<?> constructorNBTTagCompound = Reflection.getConstructor(classNBTTagCompound);
    private static Constructor<?> constructorNBTTagList = Reflection.getConstructor(classNBTTagList);
    private static Constructor<?> constructorNBTTagString = Reflection.getConstructor(classNBTTagString, String.class);
    private static Constructor<?> constructorNBTTagInt = Reflection.getConstructor(classNBTTagInt, int.class);
    private static Constructor<?> constructorNBTTagDouble = Reflection.getConstructor(classNBTTagDouble, double.class);
    private static Constructor<?> constructorNBTTagLong = Reflection.getConstructor(classNBTTagLong, long.class);

    private static Method methodNMSItemHasTag = Reflection.getMethod(classNMSItemStack, "hasTag");
    private static Method methodNMSItemGetTag = Reflection.getMethod(classNMSItemStack, "getTag");
    private static Method methodNBTTagGetList = Reflection.getMethod(classNBTTagCompound, "getList", String.class, int.class);
    private static Method methodNBTTagListAdd = Reflection.getMethod(classNBTTagList, "add", classNBTBase);
    private static Method methodSetNBTOnCompound = Reflection.getMethod(classNBTTagCompound, "set", String.class, classNBTBase);
    private static Method methodSetNBTOnNMSItemStack = Reflection.getMethod(classNMSItemStack, "setTag", classNBTTagCompound);

    private static Method getListSize = Reflection.getMethod(classNBTTagList, "size");
    private static Method getListItem = Reflection.getMethod(classNBTTagList, "get", int.class);
    private static Method getCompoundValue = Reflection.getMethod(classNBTTagCompound, "get", String.class);


    private static Object getNBTTagCompound(Object nmsItemStack) {
        Object hasTags = Reflection.invokeMethod(methodNMSItemHasTag, nmsItemStack);
        if ((Boolean) hasTags) {
            System.out.println("Getting Tags");
            return Reflection.invokeMethod(methodNMSItemGetTag, nmsItemStack);
        } else {
            System.out.println("Creating Tags");
            return Reflection.newInstance(constructorNBTTagCompound);
        }
    }

    private static Object getNBTAttributeList(Object nbtTagCompound) {
//        0:"END", 1:"BYTE", 2:"SHORT", 3:"INT", 4:"LONG", 5:"FLOAT", 6:"DOUBLE", 7:"BYTE[]", 8:"STRING", 9:"LIST", 10:"COMPOUND", 11:"INT[]"
        Object existingList = Reflection.invokeMethod(methodNBTTagGetList, nbtTagCompound, "AttributeModifiers", 10);
        if (existingList != null)
            return Reflection.invokeMethod(methodNBTTagGetList, nbtTagCompound, "AttributeModifiers", 10);
        else
            return Reflection.newInstance(constructorNBTTagList);
    }


    private static Object createNBTCompound(AttributeType attribute, Slot slot, Object value, Operation operation) {

        Object att = Reflection.newInstance(constructorNBTTagCompound);
        Method set = Reflection.getMethod(classNBTTagCompound, "set", String.class, classNBTBase);
        UUID uuid = UUID.randomUUID();

        Reflection.invokeMethod(set, att, "AttributeName", Reflection.newInstance(constructorNBTTagString, attribute.getName()));
        Reflection.invokeMethod(set, att, "Name", Reflection.newInstance(constructorNBTTagString, attribute.getName()));
        Reflection.invokeMethod(set, att, "Amount", Reflection.newInstance(constructorNBTTagDouble, value));
        Reflection.invokeMethod(set, att, "Operation", Reflection.newInstance(constructorNBTTagInt, operation.getID()));
        Reflection.invokeMethod(set, att, "UUIDLeast", Reflection.newInstance(constructorNBTTagLong, uuid.getLeastSignificantBits()));
        Reflection.invokeMethod(set, att, "UUIDMost", Reflection.newInstance(constructorNBTTagLong, uuid.getMostSignificantBits()));
        if (slot != Slot.ALL)
            Reflection.invokeMethod(set, att, "Slot", Reflection.newInstance(constructorNBTTagString, slot.getName()));
        return att;
    }

    private static Object getNMSItemStack(ItemStack itemStack) {
        return Reflection.invokeMethod(methodAsNMSCopy, classCraftItemStack, itemStack);
    }

    public static ItemStack addAttribute(ItemStack itemStack, Attribute attribute) {

//        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(itemStack);
        Object nmsItemStack = getNMSItemStack(itemStack);

//        net.minecraft.server.v1_15_R1.NBTTagCompound nbtTagCompound = nmsItemStack.hasTag() ? nmsItemStack.getTag() : new net.minecraft.server.v1_15_R1.NBTTagCompound();
        Object nbtTagCompound = getNBTTagCompound(nmsItemStack);

//        0:"END", 1:"BYTE", 2:"SHORT", 3:"INT", 4:"LONG", 5:"FLOAT", 6:"DOUBLE", 7:"BYTE[]", 8:"STRING", 9:"LIST", 10:"COMPOUND", 11:"INT[]"
//        net.minecraft.server.v1_15_R1.NBTTagList nbtTagList = nbtTagCompound.get("AttributeModifiers") != null ? nbtTagCompound.getList("AttributeModifiers", 10) : new net.minecraft.server.v1_15_R1.NBTTagList();
        Object nbtTagList = getNBTAttributeList(nbtTagCompound);

//        NBTTagCompound attributeDetails = (NBTTagCompound) createNBTCompound(attribute.getAttribute(), attribute.getSlot(), attribute.getValue(), attribute.getOperation());
        Object attributeDetails = createNBTCompound(attribute.getAttribute(), attribute.getSlot(), attribute.getValue(), attribute.getOperation());

//        nbtTagList.add(attributeDetails);
        Reflection.invokeMethod(methodNBTTagListAdd, nbtTagList, attributeDetails);

//        nbtTagCompound.set("AttributeModifiers", nbtTagList);
        Reflection.invokeMethod(methodSetNBTOnCompound, nbtTagCompound, "AttributeModifiers", nbtTagList);

//        nmsItemStack.setTag(nbtTagCompound);
        Reflection.invokeMethod(methodSetNBTOnNMSItemStack, nmsItemStack, nbtTagCompound);


        return (ItemStack) Reflection.invokeMethod(methodAsBukkitCopy, null, nmsItemStack);
    }


    public static List<Attribute> getAttributes(ItemStack itemStack) {
        List<Attribute> list = new ArrayList<>();
        Object nmsItemStack = getNMSItemStack(itemStack);
        Object nbtTagCompound = getNBTTagCompound(nmsItemStack);
        Object attributes = getNBTAttributeList(nbtTagCompound);

        int listSize = (int) Reflection.invokeMethod(getListSize, attributes);
        for (int i = 0; i < listSize; i++) {
            Object attribute = Reflection.invokeMethod(getListItem, attributes, i);

            Object name = Reflection.invokeMethod(getCompoundValue, attribute, "AttributeName");
            name = Reflection.get(classNBTTagString, "data", name);

            Object amount = Reflection.invokeMethod(getCompoundValue, attribute, "Amount");
            amount = Reflection.get(classNBTTagDouble, "data", amount);

            Object operation = Reflection.invokeMethod(getCompoundValue, attribute, "Operation");
            operation = Reflection.get(classNBTTagInt, "data", operation);

            Object slot = Reflection.invokeMethod(getCompoundValue, attribute, "Slot");
            if (slot != null)
                slot = Reflection.get(classNBTTagString, "data", slot);

            Attribute attr = new Attribute(AttributeType.getByName((String) name), slot == null ? Slot.ALL : Slot.getByName((String) slot), (double) amount, Operation.getByID((int) operation));
            list.add(attr);
        }

        return list;
    }

}