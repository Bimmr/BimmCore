/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Operation
/*    */ {
/*  7 */   ADD_NUMBER(0),
/*  8 */   MULTIPLY_PERCENTAGE(1),
/*  9 */   ADD_PERCENTAGE(2);
/*    */   
/*    */   private int id;
/*    */   
/*    */   Operation(int id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getID() {
/* 18 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\Operation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */