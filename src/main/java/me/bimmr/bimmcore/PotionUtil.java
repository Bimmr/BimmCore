package me.bimmr.bimmcore;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionUtil {
    public static PotionEffect getPotionEffect(String potionCode) {
        PotionEffectType type = PotionEffectType.SPEED;
        int dur = 1, amp = 1;
        if (potionCode != null) {
            String[] strings = potionCode.split(" ");
            for (String data : strings) {
                if (data.startsWith("type") || data.startsWith("id") || data.startsWith("potion")) {
                    PotionEffectType pet = null;
                    String str = data.split(":")[1];
                    if (PotionEffectType.getByName(str) != null) {
                        pet = PotionEffectType.getByName(str);
                    } else {
                        pet = PotionEffectType.SPEED;
                    }
                    type = pet;
                } else if (data.startsWith("length") || data.startsWith("time") || data.startsWith("duration")) {
                    dur = Integer.parseInt(data.split(":")[1]) - 1;
                    if (dur == -1)
                        dur = Integer.MAX_VALUE;
                } else if (data.startsWith("power") || data.startsWith("strength") || data.startsWith("amplifier")) {
                    amp = Integer.parseInt(data.split(":")[1]) - 1;
                    if (amp == -1)
                        amp = Integer.MAX_VALUE;
                }
            }
        }
        PotionEffect p = new PotionEffect(type, dur * 20, amp - 1);
        return p;
    }

    public static ArrayList<PotionEffect> getPotionEffects(List<String> potionCodes) {
        ArrayList<PotionEffect> effects = new ArrayList<>();
        if (potionCodes != null && !potionCodes.isEmpty())
            for (String code : potionCodes)
                effects.add(getPotionEffect(code));
        return effects;
    }

    public static ArrayList<String> getPotionEffectsToString(List<PotionEffect> potions) {
        ArrayList<String> effects = new ArrayList<>();
        if (potions != null && !potions.isEmpty())
            for (PotionEffect potion : potions)
                effects.add(getPotionEffectToString(potion));
        return effects;
    }

    public static String getPotionEffectToString(PotionEffect pe) {
        String potion = "type:" + pe.getType().getName();
        potion = potion + " duration:" + (pe.getDuration() / 20);
        potion = potion + " amplifier:" + (pe.getAmplifier() + 1);
        return potion;
    }
}
