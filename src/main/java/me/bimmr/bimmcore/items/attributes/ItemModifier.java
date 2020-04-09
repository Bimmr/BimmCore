/*     */ package me.bimmr.bimmcore.items.attributes;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.UUID;
/*     */ import me.bimmr.bimmcore.reflection.Reflection;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemModifier
/*     */ {
/*  27 */   private static Class<?> craftItemStack = Reflection.getCraftClass("inventory.CraftItemStack");
/*  28 */   private static Method asNMSCopy = Reflection.getMethod(craftItemStack, "asNMSCopy", new Class[] { ItemStack.class });
/*  29 */   private static Class<?> nbtTagCompound = Reflection.getNMSClass("NBTTagCompound"); private static Class<?> nbtTagList;
/*  30 */   private static Class<?> nbtBase = Reflection.getNMSClass("NBTBase"); static {
/*  31 */     nbtTagList = Reflection.getNMSClass("NBTTagList");
/*     */     try {
/*  33 */       nbtTagString = Reflection.getNMSClass("NBTTagString").getConstructor(new Class[] { String.class });
/*  34 */       nbtTagInt = Reflection.getNMSClass("NBTTagInt").getConstructor(new Class[] { int.class });
/*  35 */       nbtTagDouble = Reflection.getNMSClass("NBTTagDouble").getConstructor(new Class[] { double.class });
/*  36 */       nbtTagLong = Reflection.getNMSClass("NBTTagLong").getConstructor(new Class[] { long.class });
/*  37 */     } catch (NoSuchMethodException e) {
/*  38 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Constructor<?> nbtTagString;
/*     */   
/*     */   private static Constructor<?> nbtTagInt;
/*     */   
/*     */   private static Constructor<?> nbtTagDouble;
/*     */   private static Constructor<?> nbtTagLong;
/*     */   private Object nmsItemStack;
/*     */   private Object nbtTag;
/*     */   private Object nbtTags;
/*     */   private ItemAttributes itemAttributes;
/*     */   
/*     */   public ItemModifier(ItemStack itemStack, ItemAttributes itemAttributes) {
/*  55 */     this.nmsItemStack = getNMSItemStack(itemStack);
/*  56 */     this.itemAttributes = itemAttributes;
/*     */   }
/*     */   
/*     */   private static Object getNMSItemStack(ItemStack itemStack) {
/*     */     try {
/*  61 */       return asNMSCopy.invoke(craftItemStack, new Object[] { itemStack });
/*  62 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*  63 */       e.printStackTrace();
/*     */       
/*  65 */       return null;
/*     */     } 
/*     */   }
/*     */   private static Object getNBTTagCompound(Object nmsItem) {
/*  69 */     boolean hasNBTTagCompound = false;
/*     */     try {
/*  71 */       Object tagCompound = Reflection.getPrivateField(nmsItem.getClass(), "tag");
/*     */       
/*  73 */       if (tagCompound == null) {
/*  74 */         tagCompound = nbtTagCompound.newInstance();
/*     */       }
/*  76 */       return tagCompound;
/*  77 */     } catch (IllegalAccessException|InstantiationException e) {
/*  78 */       e.printStackTrace();
/*     */       
/*  80 */       return null;
/*     */     } 
/*     */   }
/*     */   private static Object createNBTCompound(AttributeType attribute, Slot slot, Object value, Operation operation) {
/*     */     try {
/*  85 */       Object att = nbtTagCompound.newInstance();
/*  86 */       Method set = att.getClass().getMethod("set", new Class[] { String.class, nbtBase });
/*  87 */       UUID uuid = UUID.randomUUID();
/*     */       
/*  89 */       set.invoke(att, new Object[] { "AttributeName", nbtTagString.newInstance(new Object[] { attribute.getName() }) });
/*  90 */       set.invoke(att, new Object[] { "Name", nbtTagString.newInstance(new Object[] { attribute.getName() }) });
/*  91 */       set.invoke(att, new Object[] { "Amount", nbtTagDouble.newInstance(new Object[] { value }) });
/*  92 */       set.invoke(att, new Object[] { "Operation", nbtTagInt.newInstance(new Object[] { Integer.valueOf(operation.getID()) }) });
/*  93 */       set.invoke(att, new Object[] { "UUIDLeast", nbtTagLong.newInstance(new Object[] { Long.valueOf(uuid.getLeastSignificantBits()) }) });
/*  94 */       set.invoke(att, new Object[] { "UUIDMost", nbtTagLong.newInstance(new Object[] { Long.valueOf(uuid.getMostSignificantBits()) }) });
/*  95 */       set.invoke(att, new Object[] { "Slot", nbtTagString.newInstance(new Object[] { slot.getName() }) });
/*  96 */       return att;
/*  97 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|InstantiationException|NoSuchMethodException e) {
/*  98 */       e.printStackTrace();
/*     */       
/* 100 */       return null;
/*     */     } 
/*     */   }
/*     */   public ItemStack build() {
/*     */     try {
/* 105 */       this.nbtTag = getNBTTagCompound(this.nmsItemStack);
/* 106 */       this.nbtTags = nbtTagList.newInstance();
/*     */       
/* 108 */       Method add = this.nbtTags.getClass().getMethod("add", new Class[] { nbtBase });
/* 109 */       for (Attribute attribute : this.itemAttributes.getAttributes()) {
/* 110 */         add.invoke(this.nbtTags, new Object[] { createNBTCompound(attribute.getAttribute(), attribute.getSlot(), Double.valueOf(attribute.getValue()), attribute.getOperation()) });
/*     */       } 
/* 112 */       this.nmsItemStack.getClass().getMethod("a", new Class[] { String.class, nbtBase }).invoke(this.nmsItemStack, new Object[] { "AttributeModifiers", this.nbtTags });
/*     */       
/* 114 */       return (ItemStack)Reflection.getMethod(craftItemStack, "asBukkitCopy").invoke(null, new Object[] { this.nmsItemStack });
/* 115 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|InstantiationException e) {
/* 116 */       e.printStackTrace();
/*     */       
/* 118 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\ItemModifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */