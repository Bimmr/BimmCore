/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ import org.bukkit.Material;
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
/*    */ class Example
/*    */ {
/*    */   public Example() {
/* 19 */     ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
/* 20 */     ItemAttributes ia = new ItemAttributes(stack);
/* 21 */     ia.addAttribute(new Attribute(AttributeType.ATTACK_DAMAGE, Slot.MAIN_HAND, 10.0D, Operation.ADD_NUMBER));
/* 22 */     ia.addAttribute(new Attribute(AttributeType.ATTACK_SPEED, Slot.MAIN_HAND, 100.0D, Operation.ADD_PERCENTAGE));
/* 23 */     stack = ia.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\Example.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */