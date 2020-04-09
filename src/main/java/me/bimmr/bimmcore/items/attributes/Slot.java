/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Slot
/*    */ {
/*  7 */   MAIN_HAND("mainhand"),
/*  8 */   OFF_HAND("offhand"),
/*  9 */   FEET("feet"),
/* 10 */   LEGS("legs"),
/* 11 */   CHEST("chest"),
/* 12 */   HEAD("head");
/*    */   
/*    */   private String name;
/*    */   
/*    */   Slot(String name) {
/* 17 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\Slot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */