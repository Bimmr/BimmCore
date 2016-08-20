package me.bimmr.bimmcore.items;

import me.bimmr.bimmcore.StringUtil;
import me.bimmr.bimmcore.items.attributes.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Randy on 9/23/2015.
 */
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

    @Override
    public String toString() {
        //Default the string to just air
        String string = "item:AIR";
        if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null && Items_Crackshot.getGunName(getItem()) != null) {
            return Items_Crackshot.getGunName(getItem());
        }
        if (getItem().getType() != Material.AIR) {

            //Set the string to the item name element
            string = "item:" + getItem().getType().name();

            if (getItem().hasItemMeta()) {

                //Name
                if (getItem().getItemMeta().hasDisplayName())
                    string += " name:" + StringUtil.replaceToYAMLFriendlyColors(getItem().getItemMeta().getDisplayName());

                //Lore
                if (getItem().getItemMeta().hasLore() && !(getItem().getType() == Material.POTION)) {
                    string += " lore:";
                    for (String line : getItem().getItemMeta().getLore())
                        string += StringUtil.replaceToYAMLFriendlyColors(line.replaceAll(" ", "_") + "|");
                    string = string.substring(0, string.length() - 1);
                }
            }

            //Durability/Data
            if (getItem().getDurability() > 0 && !isPotion())
                string += " data:" + getItem().getDurability();

            //Amount/Quantity
            if (getItem().getAmount() > 1)
                string += " amount:" + getItem().getAmount();

            //Glow
            if (EnchantGlow.isGlow(getItem()))
                string += " Glow";

            //Enchant/Enchantment
            if (!getItem().getEnchantments().isEmpty())
                for (Map.Entry<Enchantment, Integer> enchantment : getItem().getEnchantments().entrySet())
                    if (!enchantment.getKey().getName().equals("Glow"))
                        string += " enchantment:" + enchantment.getKey().getName() + (enchantment.getValue() > 1 ? "-" + enchantment.getValue() : "");

            //Potions
            if (isPotion()) {
                PotionMeta pm = (PotionMeta) getItem().getItemMeta();
                for (PotionEffect p : pm.getCustomEffects())
                    string += " potion:" + p.getType().getName() + "," + p.getDuration() / 20 + "," + (p.getAmplifier() + 1);
            }

            //Leather colors
            if (getItem().getType().name().contains("LEATHER_") && (((LeatherArmorMeta) getItem().getItemMeta()).getColor() != null)) {
                LeatherArmorMeta im = (LeatherArmorMeta) getItem().getItemMeta();
                string += " color:" + im.getColor().getRed() + "," + im.getColor().getGreen() + "," + im.getColor().getBlue();
            }

            //Written Books
            if (getItem().getType() == Material.WRITTEN_BOOK) {
                BookMeta im = (BookMeta) getItem().getItemMeta();
                if (im.hasAuthor())
                    string += " author:" + im.getAuthor();
                if (im.hasTitle())
                    string += " title:" + im.getTitle().replaceAll(" ", "_").replaceAll("" + ChatColor.COLOR_CHAR, "&");

                if (im.hasPages()) {
                    string += " pages:";
                    for (String page : im.getPages())
                        string += page.replaceAll(" ", "_").replaceAll("" + ChatColor.COLOR_CHAR, "&") + "|";

                    string = string.substring(0, string.length() - 1);
                }

                //Unbreakable
                if (im.spigot().isUnbreakable())
                    string += " unbreakable";

                //Flags
                if (!im.getItemFlags().isEmpty()) {
                    string += " flags:";
                    for (ItemFlag flag : im.getItemFlags())
                        string += flag.name();
                }

                //Item Attributes
                if (itemAttributes != null) {
                    for (Attribute attribute : itemAttributes.getAttributes()) {
                        string += attribute.getAttribute().toString() + "," + attribute.getSlot().toString() + "," + attribute.getValue() + "," + attribute.getOperation().toString();
                    }
                }
            }
        }
        return string;
    }

    /**
     * Generates the Item from the string
     *
     * @param string
     * @return
     */
    @SuppressWarnings("deprecation")
    public Items fromString(String string) {

        if (string != null)
            if (string.contains(" ")) {
                String[] line = string.split(" ");
                // Loop through every part of the code
                for (String data : line)

                    if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null && (data.startsWith("crackshot") || data.startsWith("gun"))) {
                        String name = data.split(":", 2)[1];
                        item = Items_Crackshot.getGunItemStack(name);
                    } else {
                        // Item Variables
                        if (data.startsWith("id") || data.startsWith("item")) {

                            Material mat = null;
                            String itemName = data.split(":", 2)[1];
                            try {
                                mat = Material.getMaterial(Integer.valueOf(itemName));
                            } catch (NumberFormatException e) {
                                if (Material.getMaterial(itemName.toUpperCase()) != null)
                                    mat = Material.getMaterial(itemName.toUpperCase());

                                    //So 1.9 Splash_Potion and Lingering_Potion don't cause issues
                                else if (itemName.toUpperCase().contains("POTION"))
                                    mat = Material.POTION;

                                else
                                    mat = Material.AIR;
                            }
                            item.setType(mat);
                        }

                        // Amount Variables
                        else if (data.startsWith("amount") || data.startsWith("quantity"))
                            setAmount(Integer.parseInt(data.split(":", 2)[1]));

                            // Durability Variables
                        else if (data.startsWith("data") || data.startsWith("durability") || data.startsWith("damage"))
                            setDurability(Short.parseShort(data.split(":", 2)[1]));

                            // Enchantment variables
                        else if (data.startsWith("enchantment") || data.startsWith("enchant")) {
                            String s = data.split(":", 2)[1];
                            Enchantment enchantment;
                            try {
                                enchantment = Enchantment.getById(Integer.parseInt(s.split("-")[0]));
                            } catch (NumberFormatException e) {
                                enchantment = Enchantment.getByName(s.split("-")[0].toUpperCase());
                            }
                            if (enchantment != null) {
                                // Level Stated
                                if (s.contains("-"))
                                    addEnchantment(enchantment, Integer.parseInt(s.split("-")[1]));
                                    // No Level Stated
                                else
                                    addEnchantment(enchantment, 1);
                            }
                        } else if (data.startsWith("glow"))
                            addGlow();

                            // Name Variables
                        else if (data.startsWith("name") || data.startsWith("title"))
                            setDisplayName(StringUtil.addColor(data.split(":", 2)[1]).replaceAll("_", " "));

                            // Owner Variables
                        else if (data.startsWith("owner") || data.startsWith("player"))
                            setSkullOwner(data.split(":", 2)[1]);

                            // Color Variables(Leather Only)
                        else if (data.startsWith("color") || data.startsWith("colour"))
                            try {
                                String[] s = data.replaceAll("color:", "").replaceAll("colour:", "").split(",");
                                setLeatherColor(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                            } catch (ClassCastException notLeather) {
                                Bukkit.getLogger().severe("An item that is not leather has attempted to be dyed in the item: " + string);
                            }
                            // Potion Effect Variables(Potions Only)
                        else if (data.startsWith("potion")) {
                            String[] s = data.replaceAll("potion:", "").split(",");
                            PotionEffectType type = PotionEffectType.SPEED;
                            try {
                                type = PotionEffectType.getById((Integer.valueOf(s[0])));
                            } catch (NumberFormatException e) {
                                if (PotionEffectType.getByName(s[0].toUpperCase()) != null)
                                    type = PotionEffectType.getByName(s[0].toUpperCase());
                            }
                            if (type != null) {

                                //If they are still using the splash tag(not supported since 1.9) try and make the potion a splash potion still
                                if (string.contains("splash"))
                                    try {
                                        Potion potion = new Potion(PotionType.getByEffect(type));
                                        potion.setSplash(true);
                                        potion.apply(item);
                                    } catch (Exception e) {

                                        //Looks like they're using 1.9 and still have the splash tag
                                        Bukkit.getLogger().severe("Spigot 1.9 and newer doesn't support the splash tag in the item: " + string);
                                        item.setType(Material.SPLASH_POTION);
                                    }
                                int time = Integer.parseInt(s[1]) * 20;
                                int level = Integer.parseInt(s[2]) - 1;
                                addPotionEffect(new PotionEffect(type, time, level));
                            }
                        }

                        // Lore Variables
                        else if (data.startsWith("lore") || data.startsWith("desc") || data.startsWith("description")) {
                            String s = data.split(":", 2)[1];
                            for (String lore : s.split("\\|"))
                                addLore(StringUtil.addColor(lore.replaceAll("_", " ")));
                        }
                        // Page Variables
                        else if (data.startsWith("page") || data.startsWith("pages")) {
                            String s = data.split(":", 2)[1];
                            List<String> pages = new ArrayList<String>();
                            for (String lore : s.split("\\|"))
                                pages.add(StringUtil.addColor(lore.replaceAll("_", " ")));

                            BookMeta meta = (BookMeta) item.getItemMeta();
                            meta.setPages(pages);
                            item.setItemMeta(meta);
                        }

                        // Author Variables
                        else if (data.startsWith("author") || data.startsWith("writter"))
                            setBookAuthor(StringUtil.addColor(data.split(":", 2)[1]));

                            // Title Variables
                        else if (data.startsWith("title"))
                            setBookTitle(StringUtil.addColor(data.split(":", 2)[1]));

                            // Unbreakable Variables
                        else if (data.startsWith("unbreakable"))
                            setUnbreakable(true);

                        else if (data.startsWith("flag"))
                            try {
                                addFlag(ItemFlag.valueOf(data.split(":")[1].toUpperCase()));
                            } catch (IllegalArgumentException notflag) {
                                Bukkit.getLogger().severe("An invalid flag name has been entered in the item: " + string);
                            }
                        else if (data.startsWith("attribute")) {
                            String[] elements = data.split(":", 2)[1].split(",");
                            String attribute = elements[0];
                            String slot = elements[1];
                            Double value = Double.parseDouble(elements[2]);
                            String operation = elements[3];

                            addAttribute(AttributeType.valueOf(attribute), Slot.valueOf(slot), value, Operation.valueOf(operation));
                        }
                    }

            }

            // Item Variables
            else if (string.startsWith("id") || string.startsWith("item")) {

                Material mat;
                String itemName = string.split(":", 2)[1];
                try {
                    mat = Material.getMaterial(Integer.valueOf(itemName));
                } catch (NumberFormatException e) {
                    if (Material.getMaterial(itemName.toUpperCase()) != null)
                        mat = Material.getMaterial(itemName.toUpperCase());
                    else
                        mat = Material.AIR;
                }
                item.setType(mat);
            }

            // Crackshot Variables else if (itemCode.startsWith("crackshot") ||
            else if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null && (string.startsWith("crackshot") || string.startsWith("gun"))) {
                String name = string.split(":", 2)[1];
                item = Items_Crackshot.getGunItemStack(name);
            }

        return this;
    }

    /**
     * Get the ItemStack
     *
     * @return
     */
    public ItemStack getItem() {
        if (itemAttributes != null) {
            itemAttributes.setItemStack(this.item);
            this.item = itemAttributes.build();
        }
        return this.item;
    }

    /**
     * Sets the display name
     *
     * @param name
     * @return
     */
    public Items setDisplayName(String name) {
        ItemMeta im = getItem().getItemMeta();

        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        getItem().setItemMeta(im);
        return this;
    }

    /**
     * Set the skull skin
     *
     * @param owner
     * @return
     */
    public Items setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta) item.getItemMeta();

        im.setOwner(owner);

        item.setItemMeta(im);
        return this;
    }

    /**
     * Sets a book's title
     *
     * @param title
     * @return
     */
    public Items setBookTitle(String title) {

        BookMeta im = (BookMeta) item.getItemMeta();

        im.setTitle(title);

        item.setItemMeta(im);
        return this;
    }

    /**
     * @param author
     * @return
     */
    public Items setBookAuthor(String author) {
        BookMeta im = (BookMeta) item.getItemMeta();

        im.setAuthor(author);

        item.setItemMeta(im);
        return this;
    }

    /**
     * Sets if the item is unbreakable
     *
     * @param unbreakable
     * @return
     */
    public Items setUnbreakable(boolean unbreakable) {
        ItemMeta im = getItem().getItemMeta();

        im.spigot().setUnbreakable(true);

        getItem().setItemMeta(im);
        return this;
    }

    /**
     * Sets the color value of leather items
     *
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public Items setLeatherColor(int red, int green, int blue) {
        LeatherArmorMeta im = (LeatherArmorMeta) item.getItemMeta();

        im.setColor(Color.fromRGB(red, green, blue));

        item.setItemMeta(im);
        return this;
    }

    /**
     * Set the amount
     *
     * @param amount
     * @return
     */
    public Items setAmount(int amount) {
        getItem().setAmount(amount);
        return this;
    }

    /**
     * Sets the durability
     *
     * @param durability
     * @return
     */
    public Items setDurability(int durability) {
        getItem().setDurability((short) durability);
        return this;
    }

    /**
     * Adds an enchantment
     *
     * @param enchantment
     * @param level
     * @return
     */
    public Items addEnchantment(Enchantment enchantment, int level) {
        getItem().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Add a potion effect to an itemstack
     *
     * @param potionEffect
     * @return
     */
    public Items addPotionEffect(PotionEffect potionEffect) {
        if (potionEffect != null) {
            PotionMeta potionMeta = (PotionMeta) getItem().getItemMeta();
            potionMeta.addCustomEffect(potionEffect, true);
            getItem().setItemMeta(potionMeta);
        }
        return this;
    }

    /**
     * Checks if the items is either a potion, a splash poton, or a lingering potion
     *
     * @return
     */
    public boolean isPotion() {
        Material material = getItem().getType();
        return material.name().contains("POTION");
    }

    /**
     * Adds a line to the lore
     *
     * @param line
     * @return
     */
    public Items addLore(String line) {
        ItemMeta im = getItem().getItemMeta();
        List<String> lore = new ArrayList<String>();

        if (im.hasLore())
            lore = im.getLore();

        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        getItem().setItemMeta(im);
        return this;
    }

    /**
     * Sets the item's lore
     *
     * @param lore
     * @return
     */
    public Items setLore(List<String> lore) {
        ItemMeta im = getItem().getItemMeta();

        im.setLore(lore);

        getItem().setItemMeta(im);
        return this;
    }

    public void addAttribute(AttributeType attribute, Slot slot, double value, Operation operation) {
        if (itemAttributes == null)
            itemAttributes = new ItemAttributes(getItem());

        itemAttributes.addAttribute(new Attribute(attribute, slot, value, operation));
    }


    /**
     * Makes the item glow
     *
     * @return
     */
    public Items addGlow() {
        EnchantGlow.addGlow(getItem());
        return this;
    }

    /**
     * Returns if the two items are equal
     *
     * @param item
     * @param useLore
     * @return
     */
    public boolean equals(Items item, boolean useLore) {
        if (useLore) {
            try {
                Items i1 = (Items) this.clone();
                Items i2 = (Items) item.clone();

                ItemMeta im1 = i1.getItem().getItemMeta();
                ItemMeta im2 = i2.getItem().getItemMeta();

                List<String> nothing = new ArrayList<String>();
                im1.setLore(nothing);
                im2.setLore(nothing);

                i1.getItem().setItemMeta(im1);
                i2.getItem().setItemMeta(im2);

                return i1.equals(i2);

            } catch (CloneNotSupportedException e) {
                return false;
            }
        }
        return this.equals(item);
    }

    public void addFlag(ItemFlag flag) {
        ItemMeta im = getItem().getItemMeta();
        im.addItemFlags(flag);
        getItem().setItemMeta(im);
    }

    private static class EnchantGlow extends EnchantmentWrapper {

        private static Enchantment glow;

        public EnchantGlow(int id) {
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
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            glow = new EnchantGlow(255);
            try {
                Enchantment.registerEnchantment(glow);
            } catch (IllegalArgumentException iae) {
            }
            return glow;
        }

        public static boolean isGlow(ItemStack item) {
            return item.getEnchantments().containsKey(glow);
        }

        @Override
        public boolean canEnchantItem(ItemStack item) {
            return true;
        }

        @Override
        public boolean conflictsWith(Enchantment other) {
            return false;
        }

        @Override
        public EnchantmentTarget getItemTarget() {
            return null;
        }

        @Override
        public int getMaxLevel() {
            return 10;
        }

        @Override
        public String getName() {
            return "Glow";
        }

        @Override
        public int getStartLevel() {
            return 1;
        }
    }


}
