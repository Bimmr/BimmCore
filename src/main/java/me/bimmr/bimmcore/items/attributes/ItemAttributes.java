/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemAttributes
/*    */ {
/* 29 */   private List<Attribute> attributes = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private ItemStack itemStack;
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemAttributes(ItemStack itemStack) {
/* 38 */     this.itemStack = itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadAttributes() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setItemStack(ItemStack itemStack) {
/* 54 */     this.itemStack = itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Attribute> getAttributes() {
/* 63 */     return this.attributes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemAttributes addAttribute(Attribute attribute) {
/* 73 */     this.attributes.add(attribute);
/* 74 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack build() {
/* 83 */     return (new ItemModifier(this.itemStack, this)).build();
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\ItemAttributes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */