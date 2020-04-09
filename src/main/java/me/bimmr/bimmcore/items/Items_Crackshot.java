/*    */ package me.bimmr.bimmcore.items;
/*    */ 
/*    */ import com.shampaggon.crackshot.CSUtility;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Items_Crackshot
/*    */ {
/*    */   public static ItemStack getGunItemStack(String code) {
/* 13 */     CSUtility cs = new CSUtility();
/* 14 */     return cs.generateWeapon(code);
/*    */   }
/*    */   
/*    */   public static String getGunName(ItemStack itemStack) {
/* 18 */     CSUtility cs = new CSUtility();
/* 19 */     if (cs.getWeaponTitle(itemStack) != null)
/* 20 */       return "gun:" + cs.getWeaponTitle(itemStack); 
/* 21 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\bimmr\Downloads\BimmCore (1).jar!\me\bimmr\bimmcore\items\Items_Crackshot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */