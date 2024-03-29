package me.bimmr.bimmcore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * The type Potion util.
 */
public class PotionUtil {

    /**
     * Gets potion effect.
     *
     * @param potionCode the potion code
     * @return the potion effect
     */
    public static PotionEffect getPotionEffect(String potionCode) {
        PotionEffectType type = PotionEffectType.SPEED;
        int dur = 1, amp = 1;
        if (potionCode != null) {
            String[] strings = potionCode.split(" ");

            for (String data : strings)
                if (data.startsWith("type") || data.startsWith("id") || data.startsWith("potion")) {
                    PotionEffectType pet = null;

                    String p = data.split(":")[1];

                    try {
                        pet = PotionEffectType.getById(Integer.valueOf(p));
                    } catch (NumberFormatException e) {
                        if (PotionEffectType.getByName(p) != null) pet = PotionEffectType.getByName(p);
                        else pet = PotionEffectType.SPEED;
                    }

                    type = pet;
                } else if (data.startsWith("length") || data.startsWith("time") || data.startsWith("duration")) {
                    dur = Integer.parseInt(data.split(":")[1]);
                    if (dur == -1) dur = Integer.MAX_VALUE;
                } else if (data.startsWith("power") || data.startsWith("strength") || data.startsWith("amplifier")) {
                    amp = Integer.parseInt(data.split(":")[1]);
                    if (amp == -1) amp = Integer.MAX_VALUE;

                }

        }
        if (type != null)
            return new PotionEffect(type, dur * 20, amp-1);
        else return null;
    }

    /**
     * Gets potion effects.
     *
     * @param potionCodes the potion codes
     * @return the potion effects
     */
    public static ArrayList<PotionEffect> getPotionEffects(List<String> potionCodes) {

        ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        if ((potionCodes != null) && !potionCodes.isEmpty())
            for (String code : potionCodes) {
                PotionEffect pe = getPotionEffect(code);
                if (pe != null)
                    effects.add(getPotionEffect(code));
            }
        return effects;
    }

    /**
     * Gets potion effects to string.
     *
     * @param potions the potions
     * @return the potion effects to string
     */
    public static ArrayList<String> getPotionEffectsToString(List<PotionEffect> potions) {
        ArrayList<String> effects = new ArrayList<String>();
        if ((potions != null) && !potions.isEmpty())
            for (PotionEffect potion : potions)
            effects.add(getPotionEffectToString(potion));

        return effects;
    }

    /**
     * Gets potion effect to string.
     *
     * @param pe the pe
     * @return the potion effect to string
     */
    public static String getPotionEffectToString(PotionEffect pe) {
        String potion = "type:" + pe.getType().getName();
        potion += " duration:" + (pe.getDuration() / 20);
        potion += " amplifier:" + (pe.getAmplifier() + 1);
        return potion;
    }
}
