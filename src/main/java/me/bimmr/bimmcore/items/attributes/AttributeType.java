/*    */ package me.bimmr.bimmcore.items.attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AttributeType
/*    */ {
/*  7 */   MAX_HEALTH("generic.maxHealth"),
/*  8 */   FOLLOW_RANGE("generic.followRange"),
/*  9 */   KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
/* 10 */   MOVEMENT_SPEED("generic.movementSpeed"),
/* 11 */   ATTACK_DAMAGE("generic.attackDamage"),
/* 12 */   ARMOR("generic.armor"),
/* 13 */   ARMOR_THOUGHNESS("generic.armorToughness"),
/* 14 */   ATTACK_SPEED("generic.attackSpeed"),
/* 15 */   LUCK("generic.luck"),
/* 16 */   JUMP_STRENGTH("horse.jumpStrength"),
/* 17 */   SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");
/*    */   
/*    */   private String name;
/*    */   
/*    */   AttributeType(String name) {
/* 22 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\attributes\AttributeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */