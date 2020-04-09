/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Attribute
/*    */ {
/*    */   private AttributeType attribute;
/*    */   private Slot slot;
/*    */   private Operation operation;
/*    */   private double value;
/*    */   
/*    */   public Attribute(AttributeType attribute, Slot slot, double value, Operation operation) {
/* 13 */     this.attribute = attribute;
/* 14 */     this.slot = slot;
/* 15 */     this.value = value;
/* 16 */     this.operation = operation;
/*    */   }
/*    */   
/*    */   public AttributeType getAttribute() {
/* 20 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public Slot getSlot() {
/* 24 */     return this.slot;
/*    */   }
/*    */   
/*    */   public Operation getOperation() {
/* 28 */     return this.operation;
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 32 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\Attribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */