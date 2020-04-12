package me.bimmr.bimmcore.items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.StringUtil;
import me.bimmr.bimmcore.items.attributes.Attribute;
import me.bimmr.bimmcore.items.attributes.AttributeType;
import me.bimmr.bimmcore.items.attributes.ItemAttributes;
import me.bimmr.bimmcore.items.attributes.Operation;
import me.bimmr.bimmcore.items.attributes.Slot;
import me.zombie_striker.qg.api.QualityArmory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.*;

class Example {
    public Example() {
        String itemCode = "id:Leather_Sword data:100 name:&5Sword lore:This|Is|Sparta color:150,5,100 attribute:ATTACK_SPEED,MAIN_HAND,10,ADD_NUMBER";
        Items item = new Items(itemCode);
        ItemStack itemStack = item.getItem();
    }
}

public class Items {
    private ItemStack item = new ItemStack(Material.AIR);

    private ItemAttributes itemAttributes;

    public Items(ItemStack stack) {
        this.item = stack;
    }

    public Items(String string) {
        this.item = fromString(string).getItem();
    }

    public Items() {
        this.item = new ItemStack(Material.AIR);
    }

    public String toString() {

        StringBuilder string = new StringBuilder("item:AIR");
        if (this.item == null || this.item.getType() == null)
            return string.toString();

        //Check Crackshot
        if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null && Items_Crackshot.getGunName(getItem()) != null)
            return Items_Crackshot.getGunName(getItem());

        //Check QualityArmory
        if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null && Items_QualityArmory.getGunName(getItem()) != null)
            return Items_Crackshot.getGunName(getItem());

        //Make sure item isn't air
        if (getItem().getType() != Material.AIR) {

            //Add Item
            string = new StringBuilder("item:" + getItem().getType().name());

            //Make sure item has ItemMeta
            if (getItem().hasItemMeta()) {

                //Add Item name
                if (getItem().getItemMeta().hasDisplayName())
                    string.append(" name:").append(StringUtil.replaceToYAMLFriendlyColors(getItem().getItemMeta().getDisplayName()));

                //Add Item Lore
                if (getItem().getItemMeta().hasLore() && getItem().getType() != Material.POTION) {
                    string.append(" lore:");
                    for (String line : getItem().getItemMeta().getLore())
                        string.append(StringUtil.replaceToYAMLFriendlyColors(line.replaceAll(" ", "_") + "|"));
                    string = new StringBuilder(string.substring(0, string.length() - 1));
                }
            }
            //If the item has a durability
            if (getItem().getDurability() > 0 && !isPotion())
                string.append(" data:").append(getItem().getDurability());

            //Get the amount
            if (getItem().getAmount() > 1)
                string.append(" amount:").append(getItem().getAmount());

            //Add Glow
            if (EnchantGlow.isGlow(getItem()))
                string.append(" Glow");

            //Add Enchantments
            if (!getItem().getEnchantments().isEmpty())
                for (Map.Entry<Enchantment, Integer> enchantment : (Iterable<Map.Entry<Enchantment, Integer>>) getItem().getEnchantments().entrySet()) {
                    if (!((Enchantment) enchantment.getKey()).getName().equals("Glow"))
                        string.append(" enchantment:").append(((Enchantment) enchantment.getKey()).getName()).append((((Integer) enchantment.getValue()).intValue() > 1) ? ("-" + enchantment.getValue()) : "");
                }

            //If potion
            if (isPotion()) {
                PotionMeta pm = (PotionMeta) getItem().getItemMeta();

                //Add potion effects
                if (pm.getBasePotionData() != null)
                    string.append(" potion:").append(pm.getBasePotionData().getType().name()).append(",").append(pm.getBasePotionData().isExtended()).append(",").append(pm.getBasePotionData().isUpgraded());
                if (pm.hasCustomEffects())
                    for (PotionEffect p : pm.getCustomEffects())
                        string.append(" potion:").append(p.getType().getName()).append(",").append(p.getDuration() / 20).append(",").append(p.getAmplifier() + 1);
            }

            //Add Leather Colour options
            if (getItem().getType().name().contains("LEATHER_") && ((LeatherArmorMeta) getItem().getItemMeta()).getColor() != null) {
                LeatherArmorMeta im = (LeatherArmorMeta) getItem().getItemMeta();
                string.append(" color:").append(im.getColor().getRed()).append(",").append(im.getColor().getGreen()).append(",").append(im.getColor().getBlue());
            }

            //Add written book
            if (getItem().getType() == Material.WRITTEN_BOOK) {
                BookMeta im = (BookMeta) getItem().getItemMeta();

                //Add author
                if (im.hasAuthor())
                    string.append(" author:").append(im.getAuthor());

                //Add title
                if (im.hasTitle())
                    string.append(" title:").append(im.getTitle().replaceAll(" ", "_").replaceAll("" + ChatColor.COLOR_CHAR, "&"));

                //Add Pages
                if (im.hasPages()) {
                    string.append(" pages:");
                    for (String page : im.getPages())
                        string.append(page.replaceAll(" ", "_").replaceAll("" + ChatColor.COLOR_CHAR, "&")).append("|");

                    string = new StringBuilder(string.substring(0, string.length() - 1));
                }
                try {
                    if (im.isUnbreakable())
                        string.append(" unbreakable");
                } catch (Exception e) {
                    BimmCore.getInstance().getLogger().log(Level.INFO, "Due to old API, Unable to check for unbreakable tag");
                }
                //Add Flags
                if (!im.getItemFlags().isEmpty()) {
                    string.append(" flags:");
                    for (ItemFlag flag : im.getItemFlags())
                        string.append(flag.name());
                }

                //Add Item Attributes
                if (this.itemAttributes != null)
                    for (Attribute attribute : this.itemAttributes.getAttributes())
                        string.append(attribute.getAttribute().toString()).append(",").append(attribute.getSlot().toString()).append(",").append(attribute.getValue()).append(",").append(attribute.getOperation().toString());

            }
        }
        return string.toString();
    }

    public Items fromString(String string) {

        try {
            if (string != null)
                if (string.contains(" ")) {
                    String[] line = string.split(" ");
                    for (String data : line) {
                        if (data.startsWith("gun")) {
                            if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null) {
                                String name = data.split(":", 2)[1];
                                this.item = Items_Crackshot.getGunItemStack(name);
                            } else if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null) {
                                String name = data.split(":", 2)[1];
                                this.item = Items_QualityArmory.getGunItemStack(name);
                            }
                        } else if (data.startsWith("id") || data.startsWith("item")) {
                            Material mat = null;
                            String itemName = data.split(":", 2)[1];
                            if (Material.getMaterial(itemName.toUpperCase()) != null) {
                                mat = Material.getMaterial(itemName.toUpperCase());
                            } else if (itemName.toUpperCase().contains("POTION")) {
                                mat = Material.POTION;
                            } else {
                                mat = Material.AIR;
                            }
                            this.item.setType(mat);
                        } else if (data.startsWith("amount") || data.startsWith("quantity")) {
                            setAmount(Integer.parseInt(data.split(":", 2)[1]));
                        } else if (data.startsWith("data") || data.startsWith("durability") || data.startsWith("damage")) {
                            setDurability(Short.parseShort(data.split(":", 2)[1]));
                        } else if (data.startsWith("enchantment") || data.startsWith("enchant")) {
                            String s = data.split(":", 2)[1];
                            Enchantment enchantment = Enchantment.getByName(s.split("-")[0].toUpperCase());
                            if (enchantment != null)
                                if (s.contains("-")) {
                                    addEnchantment(enchantment, Integer.parseInt(s.split("-")[1]));
                                } else {
                                    addEnchantment(enchantment, 1);
                                }
                        } else if (data.startsWith("glow")) {
                            if (!BimmCore.oldAPI) {
                                addGlow();
                            } else {
                                BimmCore.getInstance().getLogger().log(Level.INFO, "Due to old API, Custom Glow has been disabled");
                            }
                        } else if (data.startsWith("name") || data.startsWith("title")) {
                            setDisplayName(StringUtil.addColor(data.split(":", 2)[1]).replaceAll("_", " "));
                        } else if (data.startsWith("owner") || data.startsWith("player")) {
                            String v = data.split(":", 2)[1];
                            try {
                                UUID id = UUID.fromString(v);
                                setSkullOwner(id);
                            } catch (Exception e) {
                                setSkullOwner(data.split(":", 2)[1]);
                            }
                        } else if (data.startsWith("color") || data.startsWith("colour")) {
                            try {
                                String[] s = data.replaceAll("color:", "").replaceAll("colour:", "").split(",");
                                setLeatherColor(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                            } catch (ClassCastException notLeather) {
                                Bukkit.getLogger().severe("An item that is not leather has attempted to be dyed in the item: " + string);
                            }
                        } else if (data.startsWith("potion")) {
                            String[] s = data.replaceAll("potion:", "").split(",");
                            PotionEffectType type = PotionEffectType.SPEED;
                            try {
                                type = PotionEffectType.getById(Integer.valueOf(s[0]).intValue());
                            } catch (NumberFormatException e) {
                                if (PotionEffectType.getByName(s[0].toUpperCase()) != null)
                                    type = PotionEffectType.getByName(s[0].toUpperCase());
                            }
                            if (type != null) {
                                if (string.contains("splash"))
                                    try {
                                        Potion potion = new Potion(PotionType.getByEffect(type));
                                        potion.setSplash(true);
                                        potion.apply(this.item);
                                    } catch (Exception e) {
                                        Bukkit.getLogger().severe("Spigot 1.9 and newer doesn't support the splash tag in the item: " + string);
                                        if (!BimmCore.oldAPI)
                                            this.item.setType(Material.SPLASH_POTION);
                                    }
                                try {
                                    int time = Integer.parseInt(s[1]) * 20;
                                    int level = Integer.parseInt(s[2]) - 1;
                                    addPotionEffect(new PotionEffect(type, time, level));
                                } catch (Exception e) {
                                    boolean extended = Boolean.valueOf(s[1]);
                                    boolean upgraded = Boolean.valueOf(s[2]);
                                    PotionMeta pm = (PotionMeta) getItem().getItemMeta();
                                    pm.setBasePotionData(new PotionData(PotionType.valueOf(s[0].toUpperCase()), extended, upgraded));
                                    getItem().setItemMeta(pm);
                                }
                            }
                        } else if (data.startsWith("lore") || data.startsWith("desc") || data.startsWith("description")) {
                            String s = data.split(":", 2)[1];
                            for (String lore : s.split("\\|"))
                                addLore(StringUtil.addColor(lore.replaceAll("_", " ")));
                        } else if (data.startsWith("page") || data.startsWith("pages")) {
                            String s = data.split(":", 2)[1];
                            List<String> pages = new ArrayList<>();
                            for (String lore : s.split("\\|"))
                                pages.add(StringUtil.addColor(lore.replaceAll("_", " ")));
                            BookMeta meta = (BookMeta) this.item.getItemMeta();
                            meta.setPages(pages);
                            this.item.setItemMeta((ItemMeta) meta);
                        } else if (data.startsWith("author") || data.startsWith("writter")) {
                            setBookAuthor(StringUtil.addColor(data.split(":", 2)[1]));
                        } else if (data.startsWith("title")) {
                            setBookTitle(StringUtil.addColor(data.split(":", 2)[1]));
                        } else if (data.startsWith("unbreakable")) {
                            setUnbreakable(true);
                        } else if (data.startsWith("flag")) {
                            try {
                                addFlag(ItemFlag.valueOf(data.split(":")[1].toUpperCase()));
                            } catch (IllegalArgumentException notflag) {
                                Bukkit.getLogger().severe("An invalid flag name has been entered in the item: " + string);
                            }
                        } else if (data.startsWith("attribute")) {
                            String[] elements = data.split(":", 2)[1].split(",");
                            String attribute = elements[0];
                            String slot = elements[1];
                            Double value = Double.valueOf(Double.parseDouble(elements[2]));
                            String operation = elements[3];
                            addAttribute(AttributeType.valueOf(attribute), Slot.valueOf(slot), value.doubleValue(), Operation.valueOf(operation));
                        }
                    }
                } else if (string.startsWith("id") || string.startsWith("item")) {
                    Material mat;
                    String itemName = string.split(":", 2)[1];
                    if (Material.getMaterial(itemName.toUpperCase()) != null) {
                        mat = Material.getMaterial(itemName.toUpperCase());
                    } else {
                        mat = Material.AIR;
                    }
                    this.item.setType(mat);
                } else if (string.startsWith("gun")) {
                    if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null) {
                        String name = string.split(":", 2)[1];
                        this.item = Items_Crackshot.getGunItemStack(name);
                    }
                    if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null) {
                        String name = string.split(":", 2)[1];
                        this.item = Items_QualityArmory.getGunItemStack(name);
                    }
                }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "BimmCore is unable to get the item from: " + string);
            e.printStackTrace();
            this.item = new ItemStack(Material.AIR);
        }
        return this;
    }


    /**
     * Get Item
     *
     * @return the itemstack
     */
    public ItemStack getItem() {
        if (this.itemAttributes != null) {
            this.itemAttributes.setItemStack(this.item);
            this.item = this.itemAttributes.build();
        }
        return this.item;
    }

    /**
     * Set the item's displayname
     *
     * @param name
     * @return the item
     */
    public Items setDisplayName(String name) {
        ItemMeta im = getItem().getItemMeta();
        if (im != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            getItem().setItemMeta(im);
        }
        return this;
    }

    public Items setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta) this.item.getItemMeta();
        if (!BimmCore.oldAPI) {
            im.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        } else {
            im.setOwner(owner);
        }
        this.item.setItemMeta((ItemMeta) im);
        return this;
    }

    public Items setSkullOwner(UUID owner) {
        SkullMeta im = (SkullMeta) this.item.getItemMeta();
        if (!BimmCore.oldAPI)
            im.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        this.item.setItemMeta((ItemMeta) im);
        return this;
    }

    public Items setBookTitle(String title) {
        BookMeta im = (BookMeta) this.item.getItemMeta();
        im.setTitle(title);
        this.item.setItemMeta((ItemMeta) im);
        return this;
    }

    public Items setBookAuthor(String author) {
        BookMeta im = (BookMeta) this.item.getItemMeta();
        im.setAuthor(author);
        this.item.setItemMeta((ItemMeta) im);
        return this;
    }

    public Items setUnbreakable(boolean unbreakable) {
        ItemMeta im = getItem().getItemMeta();
        im.setUnbreakable(unbreakable);

        getItem().setItemMeta(im);
        return this;
    }

    public Items setLeatherColor(int red, int green, int blue) {
        LeatherArmorMeta im = (LeatherArmorMeta) this.item.getItemMeta();
        im.setColor(Color.fromRGB(red, green, blue));
        this.item.setItemMeta((ItemMeta) im);
        return this;
    }

    public Items setAmount(int amount) {
        getItem().setAmount(amount);
        return this;
    }

    public Items setDurability(int durability) {
        getItem().setDurability((short) durability);
        return this;
    }

    public Items addEnchantment(Enchantment enchantment, int level) {
        getItem().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Items addPotionEffect(PotionEffect potionEffect) {
        if (potionEffect != null) {
            PotionMeta potionMeta = (PotionMeta) getItem().getItemMeta();
            potionMeta.addCustomEffect(potionEffect, true);
            getItem().setItemMeta((ItemMeta) potionMeta);
        }
        return this;
    }

    public boolean isPotion() {
        Material material = getItem().getType();
        return material.name().contains("POTION");
    }

    public Items addLore(String line) {
        ItemMeta im = getItem().getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = im.getLore();
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        getItem().setItemMeta(im);
        return this;
    }

    public Items setLore(List<String> lore) {
        ItemMeta im = getItem().getItemMeta();
        im.setLore(lore);
        getItem().setItemMeta(im);
        return this;
    }

    public void addAttribute(AttributeType attribute, Slot slot, double value, Operation operation) {
        if (this.itemAttributes == null)
            this.itemAttributes = new ItemAttributes(getItem());
        this.itemAttributes.addAttribute(new Attribute(attribute, slot, value, operation));
    }

    public Items addGlow() {
        if (!BimmCore.oldAPI) {
            EnchantGlow.addGlow(getItem());
        } else {
            BimmCore.getInstance().getLogger().log(Level.INFO, "Due to old API, Custom Glow has been disabled");
        }
        return this;
    }

    public boolean equals(Items item, boolean useLore) {
        if (useLore)
            try {
                Items i1 = (Items) clone();
                Items i2 = (Items) item.clone();
                ItemMeta im1 = i1.getItem().getItemMeta();
                ItemMeta im2 = i2.getItem().getItemMeta();
                List<String> nothing = new ArrayList<>();
                im1.setLore(nothing);
                im2.setLore(nothing);
                i1.getItem().setItemMeta(im1);
                i2.getItem().setItemMeta(im2);
                return i1.equals(i2);
            } catch (CloneNotSupportedException e) {
                return false;
            }
        return equals(item);
    }

    public Items addFlag(ItemFlag flag) {
        ItemMeta im = getItem().getItemMeta();
        im.addItemFlags(new ItemFlag[]{flag});
        getItem().setItemMeta(im);
        return this;
    }

    private static class EnchantGlow extends EnchantmentWrapper {
        private static Enchantment glow;

        public EnchantGlow(String id) {
            super(id);
        }

        public static void addGlow(ItemStack item) {
            Enchantment glow = getGlow();
            if (!item.containsEnchantment(glow))
                item.addEnchantment(glow, 1);
        }

        public static Enchantment getGlow() {
            if (glow != null)
                return glow;
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, Boolean.valueOf(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
            glow = (Enchantment) new EnchantGlow("bcoreglow");
            try {
                Enchantment.registerEnchantment(glow);
            } catch (IllegalArgumentException illegalArgumentException) {
            }
            return glow;
        }

        public static boolean isGlow(ItemStack item) {
            return item.getEnchantments().containsKey(glow);
        }

        public boolean canEnchantItem(ItemStack item) {
            return true;
        }

        public boolean conflictsWith(Enchantment other) {
            return false;
        }

        public EnchantmentTarget getItemTarget() {
            return null;
        }

        public int getMaxLevel() {
            return 10;
        }

        public String getName() {
            return "Glow";
        }

        public int getStartLevel() {
            return 1;
        }
    }
}
