package me.bimmr.bimmcore.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bimmr.bimmcore.BimmCore;
import me.bimmr.bimmcore.items.attributes.AttributeType;
import me.bimmr.bimmcore.items.attributes.ItemAttributes;
import me.bimmr.bimmcore.items.attributes.Operation;
import me.bimmr.bimmcore.items.attributes.Slot;
import me.bimmr.bimmcore.items.helpers.GlowEnchant;
import me.bimmr.bimmcore.items.helpers.Items_Crackshot;
import me.bimmr.bimmcore.items.helpers.Items_QualityArmory;
import me.bimmr.bimmcore.reflection.Reflection;
import me.bimmr.bimmcore.utils.StringUtil;
import org.bukkit.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.util.*;

//class Example {
//    public Example() {
//        String itemCode = "id:LEATHER_SWORD damage:100 name:&5Sword lore:This lore:Is lore:Sparta leather-color:150,5,100 attribute:ATTACK_SPEED,MAIN_HAND,10,ADD_NUMBER";
//        Items item = new Items(itemCode);
//        ItemStack itemStack = item.getItem();
//    }
//}

/**
 * The type Items.
 */
public class Items {

    public static final Items INVALID_ITEM = new Items(BimmCore.supports(13) ? Material.getMaterial("PLAYER_HEAD") : Material.getMaterial("SKULL_ITEM"))
            .setDamage(3)
            .setDisplayName("Unknown Item")
            .setSkullOwner("MHF_Question");

    private ItemStack item = new ItemStack(Material.AIR);
    private ItemMeta itemMeta;
    private ItemAttributes itemAttributes;

    /**
     * Instantiates a new Items.
     *
     * @param stack the stack
     */
    public Items(ItemStack stack) {
        this.item = stack;
        if (this.item != null && this.item.hasItemMeta())
            itemMeta = this.item.getItemMeta();
    }

    /**
     * Instantiates a new Items.
     *
     * @param string the string
     */
    public Items(String string) {
        this.item = fromString(string).getItem();
    }

    /**
     * Instantiates a new Items.
     *
     * @param material the material
     */
    public Items(Material material) {
        setMaterial(material);
    }

    /**
     * Instantiates a new Items.
     */
    public Items() {
        this.item = new ItemStack(Material.AIR);
    }

    /**
     * Load the item from a string
     * <p>
     * item:TYPE | gun:GUNNAME<br>
     * amount:AMOUNT<br>
     * damage:DAMAGE<br>
     * name:NAME<br>
     * lore:LORE<br>
     * enchantment:ENCHANTMENT,VALUE<br>
     * flag:FLAG<br>
     * display:DISPLAY<br>
     * attribute:ATTRIBUTE,SLOT,VALUE,OPERATION<br>
     * potion:POTION,[DURATION|EXTENDED],[STRENGTH|UPGRADED]<br>
     * leather-colour:RRR,GGG,BBB<br>
     * book-author:BOOK_AUTHOR<br>
     * book-title:BOOK_TITLE<br>
     * book-page:BOOK_PAGE<br>
     * banner:PATTERN,COLOR<br>
     * firework:FIREWORK,TRAIL,FLICKER<br>
     * firework-color:RRR,GGG,BBB<br>
     * firework-fade-color:RRR,GGG,BBB<br>
     * owner:[OWNER|UUID|URL]<br>
     * stored-enchantment:STORED_ENCHANTMENT,VALUE<br>
     * tropical-fish:COLOR,PATTERN,PATTERN_COLOR<br>
     * unbreakable<br>
     * glow
     *
     * @param string The Item Code
     * @return Items items
     */
    public Items fromString(String string) {

        if (string == null || string.length() == 0)
            return this;

        string += " ";

        for (String data : string.split(" ")) {

            try {
                String[] dataSplit = data.split(":", 2);
                String prefix = dataSplit[0];
                String value = dataSplit.length > 1 ? StringUtil.addColor(dataSplit[1]) : "";

                //Crackshot or QualityArmory
                if (StringUtil.equalsStrings(prefix, "gun", "weapon")) {
                    if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null)
                        this.item = Items_Crackshot.getGunItemStack(value);
                    if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null)
                        this.item = Items_QualityArmory.getGunItemStack(value);
                }

                //Material
                else if (StringUtil.equalsStrings(prefix, "id", "item", "material", "type")) {
                    if (Material.matchMaterial(value) != null)
                        this.item = new ItemStack(Material.matchMaterial(value));
                    else {
                        return INVALID_ITEM;
                    }
                }
                //Amount
                else if (StringUtil.equalsStrings(prefix, "amount", "quantity", "number", "count")) {
                    getItem().setAmount(Integer.parseInt(value));
                }

                //Damage
                else if (StringUtil.equalsStrings(prefix, "data", "damage", "durability")) {
                    setDamage(Integer.parseInt(value));
                }

                //Enchantments
                else if (StringUtil.equalsStrings(prefix, "enchantment", "enchant")) {
                    String[] valueSplit = value.split(",");
                    if (valueSplit.length < 2)
                        valueSplit = value.split("-");

                    String name = valueSplit[0];
                    int level = valueSplit.length >= 2 ? Integer.parseInt(valueSplit[1]) : 1;
                    Enchantment enchantment = null;

                    if (Enchantment.getByName(name.toUpperCase()) != null)
                        enchantment = Enchantment.getByName(name.toUpperCase());
                    else if (BimmCore.supports(13))
                        enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));

                    if (enchantment != null)
                        addEnchantment(enchantment, level - 1);
                }

                //Custom Glow Enchantment
                else if (StringUtil.equalsStrings(prefix, "glow", "glowing")) {
                    addGlow();
                }

                //Display Name
                else if (StringUtil.equalsStrings(prefix, "name", "displayname", "display-name", "customname", "custom-name")) {
                    setDisplayName(value.replaceAll("_", " "));
                }

                //Lore
                else if (StringUtil.equalsStrings(prefix, "lore", "desc", "description")) {
                    if (value.contains("|")) {
                        for (String item : value.split("\\|"))
                            addLore(item.replaceAll("_", " "));
                    } else
                        addLore(value.replaceAll("_", " "));
                }

                //Player Skull
                else if (StringUtil.equalsStrings(prefix, "owner", "player", "uuid", "skin")) {
                    setSkullOwner(value);
                }
                //Potion
                else if (StringUtil.equalsStrings(prefix, "potion", "effect")) {
                    String[] valueSplit = value.split(",");
                    PotionMeta potionMeta = (PotionMeta) getItemMeta();
                    if (BimmCore.supports(12) && StringUtil.equalsStrings(valueSplit[1], "true", "false")) {
                        if (PotionType.valueOf(valueSplit[0].toUpperCase()) != null)
                            potionMeta.setBasePotionData(new PotionData(PotionType.valueOf(valueSplit[0].toUpperCase()), Boolean.parseBoolean(valueSplit[1]), Boolean.parseBoolean(valueSplit[2])));
                        setItemMeta(potionMeta);
                    } else if (PotionEffectType.getByName(valueSplit[0]) != null)
                        addPotionEffect(new PotionEffect(PotionEffectType.getByName(valueSplit[0].toUpperCase()), Integer.parseInt(valueSplit[1]) * 20, Integer.parseInt(valueSplit[2]) - 1));
                }

                //Splash Potion
                else if (StringUtil.equalsStrings(prefix, "splash", "splashPotion", " splash-potion")) {
                    getItem().setType(Material.SPLASH_POTION);
                }

                //Author
                else if (StringUtil.equalsStrings(prefix, "author", "authour", "book-author", "book-authour")) {
                    setBookAuthor(value.replaceAll("_", " "));
                }

                //Title
                else if (StringUtil.equalsStrings(prefix, "title", "book-title")) {
                    setBookTitle(value.replaceAll("_", " "));
                }

                //Pages
                else if (StringUtil.equalsStrings(prefix, "page, book-page", "pages", "book-pages")) {
                    if (value.contains("|")) {
                        for (String item : value.split("\\|"))
                            addPage(item.replaceAll("_", " "));
                    } else
                        addPage(value.replaceAll("_", " "));
                }

                //Colored Leather
                else if (StringUtil.equalsStrings(prefix, "color", "colour", "leather-color", "leather-colour")) {
                    if (value.contains(",")) {
                        String[] valueSplit = value.split(",");
                        setLeatherColor(Integer.parseInt(valueSplit[0]), Integer.parseInt(valueSplit[1]), Integer.parseInt(valueSplit[2]));
                    } else
                        setLeatherColor(Integer.parseInt(value));
                }

                //Banners
                else if (StringUtil.equalsStrings(prefix, "banner", "pattern", "bannerpattern", "banner-pattern")) {
                    String[] valueSplit = value.split(",");
                    addBannerPattern(new Pattern(DyeColor.valueOf(valueSplit[1]), PatternType.valueOf(valueSplit[0])));
                }

                //Fireworks
                else if (StringUtil.equalsStrings(prefix, "firework")) {
                    String[] valueSplit = value.split(",");

                    FireworkEffect.Builder fireworkEffect = FireworkEffect.builder();
                    fireworkEffect.with(FireworkEffect.Type.valueOf(valueSplit[0]));
                    fireworkEffect.trail(Boolean.parseBoolean(valueSplit[1]));
                    fireworkEffect.flicker(Boolean.parseBoolean(valueSplit[2]));
                    addFireworkEffect(fireworkEffect.build());
                }

                //Firework Colours
                else if (StringUtil.equalsStrings("fireworkcolor", "firework-color", "fireworkcolour", "firework-colour")) {
                    FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) getItemMeta();
                    String[] valueSplit = value.split(",");
                    fireworkEffectMeta.getEffect().getColors().add(Color.fromRGB(Integer.parseInt(valueSplit[0]), Integer.parseInt(valueSplit[1]), Integer.parseInt(valueSplit[2])));
                }

                //Firework Fade Colors
                else if (StringUtil.equalsStrings("fireworkfadecolor", "firework-fadecolor", "fireworkfade-color", "firework-fade-colour", "fireworkfadecolour", "firework-fadecolour", "fireworkfade-colour", "firework-fade-colour")) {
                    FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) getItemMeta();
                    String[] valueSplit = value.split(",");
                    fireworkEffectMeta.getEffect().getFadeColors().add(Color.fromRGB(Integer.parseInt(valueSplit[0]), Integer.parseInt(valueSplit[1]), Integer.parseInt(valueSplit[2])));
                }

                //Unbreakable
                else if (StringUtil.equalsStrings(prefix, "unbreakable", "invincible", "nobreak", "no-break")) {
                    setUnbreakable(true);
                }

                //Tropical Fish Bucket
                else if (StringUtil.equalsStrings(prefix, "fish", "tropicalfish", "tropical-fish")) {
                    TropicalFishBucketMeta tropicalFishBucketMeta = (TropicalFishBucketMeta) getItemMeta();
                    String[] valueSplit = value.split(",");
                    tropicalFishBucketMeta.setBodyColor(DyeColor.getByColor(Color.fromRGB(Integer.parseInt(valueSplit[0]))));
                    tropicalFishBucketMeta.setPattern(TropicalFish.Pattern.valueOf(valueSplit[1]));
                    tropicalFishBucketMeta.setPatternColor(DyeColor.getByColor(Color.fromRGB(Integer.parseInt(valueSplit[2]))));
                }

                //Enchantment Storage
                else if (StringUtil.equalsStrings(prefix, "storedenchantment", "stored-enchantment", "storedenchant", "stored-enchant", "enchantmentstorage", "enchantment-storage", "enchantstorage", "enchant-storage")) {
                    EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) getItemMeta();
                    String[] valueSplit = value.split(",");
                    if (Enchantment.getByKey(NamespacedKey.minecraft(valueSplit[0])) != null)
                        enchantmentStorageMeta.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(valueSplit[0])), Integer.parseInt(valueSplit[1]), true);
                }

                //Item Flags
                else if (StringUtil.equalsStrings(prefix, "flag", "flags")) {
                    try {
                        addFlag(ItemFlag.valueOf(value.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        Bukkit.getLogger().severe("An invalid flag name has been entered in an item: " + value);
                    }
                }

                //Display
                else if (StringUtil.equalsStrings(prefix, "display", "model", "custommodel", "custom-model")) {
                    ItemMeta itemMeta = getItemMeta();
                    itemMeta.setCustomModelData(Integer.parseInt(value));
                    setItemMeta(itemMeta);
                }

                //Attributes
                else if (StringUtil.equalsStrings(prefix, "attribute")) {
                    String[] valueSplit = value.split(",");
                    String attribute = valueSplit[0];
                    String slot = valueSplit[1];
                    Double level = Double.parseDouble(valueSplit[2]);
                    String operation = valueSplit[3];
                    if (BimmCore.supports(13)) {
                        if (this.itemAttributes == null)
                            this.itemAttributes = new ItemAttributes(getItem());
                        addAttribute(attribute, slot, level, operation);
                    }
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("Error while parsing " + ChatColor.RED + data + ChatColor.GRAY + " from " + ChatColor.RED + string);
                e.printStackTrace();
            }
        }

        return this;
    }

    /**
     * Set the material
     *
     * @param material The Material
     * @return The Item
     */
    public Items setMaterial(Material material) {
        this.item = new ItemStack(material);
        if (this.item.hasItemMeta())
            itemMeta = this.item.getItemMeta();
        return this;
    }


    /**
     * Gets item.
     *
     * @return Get the item
     */
    public ItemStack getItem() {
        if (this.itemMeta != null)
            this.item.setItemMeta(itemMeta);
        return this.item;
    }

    /**
     * Gets item meta.
     *
     * @return Get the item meta
     */
    public ItemMeta getItemMeta() {
        if (this.itemMeta == null)
            this.itemMeta = getItem().getItemMeta();
        return this.itemMeta;
    }

    /**
     * Set the Item Meta
     *
     * @param itemMeta The ItemMeta
     */
    public void setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    /**
     * Has item meta boolean.
     *
     * @return Get if ItemMeta is valid
     */
    public boolean hasItemMeta() {
        return this.itemMeta != null;
    }

    /**
     * Add FireworkEffect
     *
     * @param fireworkEffect FireworkEffect
     * @return Items items
     */
    public Items addFireworkEffect(FireworkEffect fireworkEffect) {
        FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) getItemMeta();
        fireworkEffectMeta.setEffect(fireworkEffect);
        setItemMeta(fireworkEffectMeta);
        return this;
    }

    /**
     * Add Banner Pattern
     *
     * @param pattern Pattern
     * @return Items items
     */
    public Items addBannerPattern(Pattern pattern) {
        BannerMeta bannerMeta = (BannerMeta) getItemMeta();
        bannerMeta.addPattern(pattern);
        setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Add Item to Pages
     *
     * @param value Page as a string
     * @return Items items
     */
    public Items addPage(String value) {
        if (!(getItemMeta() instanceof BookMeta))
            return this;

        BookMeta bookMeta = (BookMeta) getItemMeta();
        List<String> pages = bookMeta.hasPages() ? bookMeta.getPages() : new ArrayList<>();
        pages.add(value);
        setItemMeta(bookMeta);
        return this;
    }

    /**
     * Set Item Pages
     *
     * @param value Pages as a list
     * @return Items pages
     */
    public Items setPages(List<String> value) {
        if (!(getItemMeta() instanceof BookMeta))
            return this;

        BookMeta bookMeta = (BookMeta) getItemMeta();
        bookMeta.setPages(value);
        setItemMeta(bookMeta);
        return this;
    }

    /**
     * Set Item Pages
     * Calls {@link #setPages(List)}
     *
     * @param value Pages as an array
     * @return Items pages
     */
    public Items setPages(String... value) {
        return setPages(Arrays.asList(value));
    }

    /**
     * Set Book Title
     *
     * @param value The book's title
     * @return Items book title
     */
    public Items setBookTitle(String value) {
        if (!(getItemMeta() instanceof BookMeta))
            return this;

        BookMeta bookMeta = (BookMeta) getItemMeta();
        bookMeta.setTitle(value);
        setItemMeta(bookMeta);
        return this;
    }

    /**
     * Set Book Author
     *
     * @param value The book's author
     * @return Items book author
     */
    public Items setBookAuthor(String value) {
        if (!(getItemMeta() instanceof BookMeta))
            return this;

        BookMeta bookMeta = (BookMeta) getItemMeta();
        bookMeta.setAuthor(value);
        setItemMeta(bookMeta);
        return this;
    }

    /**
     * Set Item unbreakable
     * Only supports versions since MC 1.12
     *
     * @param value Boolean
     * @return Items unbreakable
     */
    public Items setUnbreakable(boolean value) {
        ItemMeta itemMeta = getItemMeta();
        if (BimmCore.supports(12))
            itemMeta.setUnbreakable(value);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set Leather Color
     *
     * @param red   The red
     * @param green The green
     * @param blue  The blue
     * @return Items leather color
     */
    public Items setLeatherColor(int red, int green, int blue) {
        if (!(getItemMeta() instanceof LeatherArmorMeta))
            return this;

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getItemMeta();
        leatherArmorMeta.setColor(Color.fromRGB(red, green, blue));
        setItemMeta(leatherArmorMeta);
        return this;
    }

    /**
     * Set Leather Color
     *
     * @param value The RGB Color
     * @return Items leather color
     */
    public Items setLeatherColor(int value) {
        if (!(getItemMeta() instanceof LeatherArmorMeta))
            return this;

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getItemMeta();
        leatherArmorMeta.setColor(Color.fromRGB(value));
        setItemMeta(leatherArmorMeta);
        return this;
    }

    /**
     * Set Amount
     *
     * @param value The amount
     * @return Items amount
     */
    public Items setAmount(int value) {
        getItem().setAmount(value);
        return this;
    }

    /**
     * Add Enchantment
     *
     * @param value Enchantment
     * @param level The level
     * @return Items items
     */
    public Items addEnchantment(Enchantment value, int level) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addEnchant(value, level, true);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Add PotionEffect
     *
     * @param value PotionEffect
     * @return Items items
     */
    public Items addPotionEffect(PotionEffect value) {
        if (!(getItemMeta() instanceof PotionMeta))
            return this;

        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.addCustomEffect(value, true);
        setItemMeta(potionMeta);
        return this;
    }

    /**
     * Add Item glow
     *
     * @return Items items
     */
    public Items addGlow() {
        ItemMeta itemMeta = getItemMeta();
        if (BimmCore.supports(13)) {
            itemMeta.addEnchant(GlowEnchant.getGlowEnchantment(), 1, true);
        } else {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            addEnchantment(Enchantment.values()[0], 1);
        }
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Add Item to Lore
     * Calls {@link #addLore(String...)}
     *
     * @param value Lore as a String
     * @return Items items
     */
    public Items addLore(String value) {
        return addLore(new String[]{value});
    }

    /**
     * Add Item to Lore
     *
     * @param value Lore as an Array
     * @return Items items
     */
    public Items addLore(String... value) {
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(value));
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set Item Lore
     *
     * @param value Lore as a list
     * @return Items lore
     */
    public Items setLore(List<String> value) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setLore(value);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Ste Item Lore
     * Calls {@link #setLore(List)}
     *
     * @param value Lore as an array
     * @return Items lore
     */
    public Items setLore(String... value) {
        return setLore(value != null ? Arrays.asList(value) : new ArrayList<String>());
    }

    /**
     * Add Item Attribute
     *
     * @param attribute Attribute's Name
     * @param slot      Attribute's Slot
     * @param level     Attribute's Level
     * @param operation Attribute's Operation
     * @return Items items
     */
    public Items addAttribute(String attribute, String slot, double level, String operation) {
        ItemMeta itemMeta = getItemMeta();
        if (BimmCore.supports(13)) {
            itemMeta.addAttributeModifier(org.bukkit.attribute.Attribute.valueOf(attribute), new AttributeModifier(UUID.randomUUID(), "bimmcore" + attribute, level, AttributeModifier.Operation.valueOf(operation), EquipmentSlot.valueOf(slot)));

        } else {
            if (this.itemAttributes == null)
                this.itemAttributes = new ItemAttributes(getItem());
            this.itemAttributes.addAttribute(new me.bimmr.bimmcore.items.attributes.Attribute(AttributeType.valueOf(attribute), Slot.valueOf(slot), level, Operation.valueOf(operation)));
        }
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Add Item Attribute using BimmCore
     *
     * @param attribute AttributeType
     * @param slot      Attribute's Slot
     * @param level     Attribute's level
     * @param operation Attribute's Operation
     * @return Items items
     */
    public Items addAttribute(AttributeType attribute, Slot slot, double level, Operation operation) {

        if (this.itemAttributes == null)
            this.itemAttributes = new ItemAttributes(getItem());
        this.itemAttributes.addAttribute(new me.bimmr.bimmcore.items.attributes.Attribute(attribute, slot, level, operation));
        return this;
    }

    /**
     * Add Item Attribute using Bukkit
     *
     * @param attribute Attribute
     * @param slot      Attribute's EquipmentSlot
     * @param level     Attribute's level
     * @param operation Attribute's Operation
     * @return Items items
     */
    public Items addAttribute(org.bukkit.attribute.Attribute attribute, EquipmentSlot slot, double level, AttributeModifier.Operation operation) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), "bimmcore" + attribute, level, operation, slot));
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Set Skull Owner (Supports UUID and Names)
     *
     * @param value The Owner's name or UUID
     * @return Items skull owner
     */
    public Items setSkullOwner(String value) {
        if (!(getItemMeta() instanceof SkullMeta))
            return this;
        if (value.startsWith("http://") || value.startsWith("https://") || value.length() > 16)
            return setSkullSkin(value);
        else {
            SkullMeta skullMeta = (SkullMeta) getItemMeta();
            if (BimmCore.supports(13)) {
                if (value.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"))
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(value)));
                else
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(value));
            } else
                skullMeta.setOwner(value);
            setItemMeta(skullMeta);
        }
        return this;
    }

    /**
     * Set Skull Owner
     * Calls {@link #setSkullOwner(String)}
     *
     * @param value Owner's UUID
     * @return Items skull owner
     */
    public Items setSkullOwner(UUID value) {
        return setSkullOwner(value.toString());
    }

    /**
     * Set Skull Owner to a Custom URL
     *
     * @param value The URL
     * @return Items skull skin
     */
    public Items setSkullSkin(String value) {
        SkullMeta meta = (SkullMeta) getItemMeta();
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            if (value.startsWith("http://") || value.startsWith("https://"))
                profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + value + "\"}}}")));
            else
                profile.getProperties().put("textures", new Property("textures", value));

            Reflection.setField(meta.getClass(), "profile", meta, profile);
        } catch (Exception e) {
            System.out.println("Unable to apply custom Skull Skin");
        }
        setItemMeta(meta);
        return this;
    }

    /**
     * Set Item Durability
     * Use {@link #setDamage(int)} instead
     *
     * @param value durability
     * @return Items durability
     */
    @Deprecated
    public Items setDurability(int value) {
        return setDamage(value);
    }

    /**
     * Set Damage
     *
     * @param value damage
     * @return Items damage
     */
    public Items setDamage(int value) {
        if (BimmCore.supports(13)) {

            if (!(getItemMeta() instanceof Damageable))
                return this;

            Damageable damageable = (Damageable) getItemMeta();
            damageable.setDamage(value);
            setItemMeta((ItemMeta) damageable);
        } else
            getItem().setDurability((short) value);
        return this;
    }

    /**
     * Set Item Display Name
     *
     * @param value Name
     * @return Items display name
     */
    public Items setDisplayName(String value) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(value);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Add a Flag to an Item
     *
     * @param flag ItemFlag to add
     * @return Items items
     */
    public Items addFlag(ItemFlag flag) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.addItemFlags(flag);
        setItemMeta(itemMeta);
        return this;
    }

    /**
     * Check if 2 items are equal
     *
     * @param item    Items to check against
     * @param useLore check using item's lore
     * @return If the items match
     */
    public boolean equals(Items item, boolean useLore) {
        if (useLore)
            try {
                Items i1 = (Items) clone();
                Items i2 = (Items) item.clone();
                ItemMeta im1 = i1.getItemMeta();
                ItemMeta im2 = i2.getItemMeta();
                List<String> nothing = new ArrayList<>();
                im1.setLore(nothing);
                im2.setLore(nothing);
                i1.setItemMeta(im1);
                i2.setItemMeta(im2);
                return i1.equals(i2);
            } catch (CloneNotSupportedException e) {
                return false;
            }
        return equals(item);
    }

    /**
     * Get the item as a string
     * <p>
     * item:TYPE | gun:GUNNAME<br>
     * amount:AMOUNT<br>
     * damage:DAMAGE<br>
     * name:NAME<br>
     * lore:LORE<br>
     * enchantment:ENCHANTMENT,VALUE<br>
     * flag:FLAG<br>
     * display:DISPLAY<br>
     * attribute:ATTRIBUTE,SLOT,VALUE,OPERATION<br>
     * potion:POTION,[DURATION|EXTENDED],[STRENGTH|UPGRADED]<br>
     * leather-colour:RRR,GGG,BBB<br>
     * book-author:BOOK_AUTHOR<br>
     * book-title:BOOK_TITLE<br>
     * book-page:BOOK_PAGE<br>
     * banner:PATTERN,COLOR<br>
     * firework:FIREWORK,TRAIL,FLICKER<br>
     * firework-color:RRR,GGG,BBB<br>
     * firework-fade-color:RRR,GGG,BBB<br>
     * owner:[OWNER|UUID|URL]<br>
     * stored-enchantment:STORED_ENCHANTMENT,VALUE<br>
     * tropical-fish:COLOR,PATTERN,PATTERN_COLOR<br>
     * unbreakable<br>
     * glow<br>
     *
     * @return Get the item as a string
     */
    public String toString() {
        StringBuilder string = new StringBuilder("item:AIR");

        if (getItem() == null || getItem().getType() == Material.AIR)
            return string.toString();

        //Check Crackshot
        if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") != null && Items_Crackshot.getGunName(getItem()) != null)
            return Items_Crackshot.getGunName(getItem());

        //Check QualityArmory
        if (Bukkit.getServer().getPluginManager().getPlugin("QualityArmory") != null && Items_QualityArmory.getGunName(getItem()) != null)
            return Items_QualityArmory.getGunName(getItem());

        string = new StringBuilder("item:" + getItem().getType().name());

        if (getItem().getAmount() > 1)
            string.append(" amount:").append(getItem().getAmount());

        if (hasItemMeta()) {
            ItemMeta itemMeta = getItemMeta();

            //Durability/Damage
            if (BimmCore.supports(13) && itemMeta instanceof Damageable) {
                Damageable damageable = (Damageable) itemMeta;
                if (damageable.hasDamage())
                    string.append(" damage:").append(damageable.getDamage());
            } else {
                if (getItem().getDurability() != 0)
                    string.append(" damage:").append(getItem().getDurability());

            }

            //Custom Name
            if (itemMeta.hasDisplayName())
                string.append(" name:").append(StringUtil.replaceToYAMLFriendlyColors(itemMeta.getDisplayName().replaceAll(" ", "_")));

            //Custom Lore
            if (itemMeta.hasLore()) {
                for (String line : itemMeta.getLore())
                    string.append(" lore:").append(StringUtil.replaceToYAMLFriendlyColors(line.replaceAll(" ", "_")));
            }

            //Enchantments
            if (getItem().getEnchantments().size() > 0) {

                if (BimmCore.supports(13))
                    for (Map.Entry<Enchantment, Integer> entry : getItemMeta().getEnchants().entrySet())
                        if (!entry.getKey().getKey().getKey().equals("bimmcore_glow"))
                            string.append(" enchantment:").append(entry.getKey().getKey().getKey()).append(",").append(entry.getValue());
                        else
                            string.append(" glow");
                else
                    for (Map.Entry<Enchantment, Integer> entry : getItemMeta().getEnchants().entrySet())
                        if (!entry.getKey().getName().equals("bimmcore_glow"))
                            string.append(" enchantment:").append(entry.getKey().getName()).append(",").append(entry.getValue());
                        else
                            string.append(" glow");

            }

            if (itemMeta.getItemFlags().size() != 0)
                for (ItemFlag itemFlag : itemMeta.getItemFlags())
                    string.append(" flag:").append(itemFlag.name());

            if (BimmCore.supports(12) && itemMeta.isUnbreakable())
                string.append(" unbreakable");

            if (BimmCore.supports(14) && itemMeta.hasCustomModelData()) {
                string.append(" display:").append(itemMeta.getCustomModelData());
            }

            if (BimmCore.supports(13)) {
                if (itemMeta.hasAttributeModifiers()) {
                    for (Map.Entry<org.bukkit.attribute.Attribute, AttributeModifier> entry : itemMeta.getAttributeModifiers().entries()) {
                        AttributeModifier attributeModifier = entry.getValue();
                        string.append(" attribute:").append(attributeModifier.getName()).append(",").append(attributeModifier.getSlot()).append(",").append(attributeModifier.getAmount()).append(",").append(attributeModifier.getOperation().name());
                    }
                } else {
                    if (this.itemAttributes != null)
                        for (me.bimmr.bimmcore.items.attributes.Attribute attribute : this.itemAttributes.getAttributes())
                            string.append(" attribute:").append(attribute.getAttribute().toString()).append(",").append(attribute.getSlot().toString()).append(",").append(attribute.getValue()).append(",").append(attribute.getOperation().toString());

                }
            }

            //Potions
            if (itemMeta instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) itemMeta;
                if (BimmCore.supports(12))
                    string.append(" potion:").append(potionMeta.getBasePotionData().getType().name()).append(",").append(potionMeta.getBasePotionData().isExtended()).append(",").append(potionMeta.getBasePotionData().isUpgraded());
                if (potionMeta.hasCustomEffects())
                    for (PotionEffect p : potionMeta.getCustomEffects())
                        string.append(" potion:").append(p.getType().getName()).append(",").append(p.getDuration() / 20).append(",").append(p.getAmplifier() + 1);

            }

            //Leather Armour Color
            if (itemMeta instanceof LeatherArmorMeta) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
                string.append(" leather-color:").append(leatherArmorMeta.getColor().getRed()).append(",").append(leatherArmorMeta.getColor().getGreen()).append(",").append(leatherArmorMeta.getColor().getBlue());
            }

            //Books
            if (itemMeta instanceof BookMeta) {
                BookMeta bookMeta = (BookMeta) itemMeta;
                if (bookMeta.hasAuthor())
                    string.append(" book-author:").append(bookMeta.getAuthor());

                if (bookMeta.hasTitle())
                    string.append(" book-title:").append(StringUtil.replaceToYAMLFriendlyColors(bookMeta.getTitle().replaceAll(" ", "_")));

                if (bookMeta.hasPages()) {
                    for (String page : bookMeta.getPages())
                        string.append(" book-page:").append(StringUtil.replaceToYAMLFriendlyColors(page.replaceAll(" ", "_")));
                }
            }
            //Banners
            if (itemMeta instanceof BannerMeta) {
                BannerMeta bannerMeta = (BannerMeta) itemMeta;
                if (bannerMeta.getPatterns().size() != 0)
                    for (Pattern pattern : bannerMeta.getPatterns())
                        string.append(" banner:").append(pattern.getPattern().name()).append(",").append(pattern.getColor().name());
            }

            //Fireworks
            if (itemMeta instanceof FireworkEffectMeta) {
                FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemMeta;
                FireworkEffect fireworkEffect = fireworkEffectMeta.getEffect();
                string.append(" firework:").append(fireworkEffect.getType().name()).append(",").append(fireworkEffect.hasTrail()).append(",").append(fireworkEffect.hasFlicker()).append(",");
                for (Color fireworkColor : fireworkEffect.getColors())
                    string.append(" firework-color:").append(fireworkColor.getRed()).append(",").append(fireworkColor.getGreen()).append(",").append(fireworkColor.getBlue()).append(",");
                for (Color fireworkFadeColor : fireworkEffect.getFadeColors())
                    string.append(" firework-fade-color:").append(fireworkFadeColor.getRed()).append(",").append(fireworkFadeColor.getGreen()).append(",").append(fireworkFadeColor.getBlue()).append(",");
            }

            //Skulls
            if (itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                if (BimmCore.supports(12)) {
                    if (skullMeta.getOwningPlayer() != null)
                        string.append(" owner:").append(skullMeta.getOwningPlayer().getUniqueId());
                    else
                        string.append(" owner:").append(getTexture());

                } else {
                    if (skullMeta.getOwner() != null)
                        string.append(" owner:").append(skullMeta.getOwner());
                    else
                        string.append(" owner:").append(getTexture());
                }
            }

            //Enchantment Storage
            if (itemMeta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemMeta;

                if (BimmCore.supports(13))
                    for (Map.Entry<Enchantment, Integer> entry : enchantmentStorageMeta.getStoredEnchants().entrySet())
                        string.append(" stored-enchantment:").append(entry.getKey().getKey().getKey()).append(",").append(entry.getValue());
                else
                    for (Map.Entry<Enchantment, Integer> entry : enchantmentStorageMeta.getStoredEnchants().entrySet())
                        string.append(" stored-enchantment:").append(entry.getKey().getName()).append(",").append(entry.getValue());
            }

            //Tropical Fish
            if (BimmCore.supports(13) && itemMeta instanceof TropicalFishBucketMeta) {
                TropicalFishBucketMeta tropicalFishBucketMeta = (TropicalFishBucketMeta) itemMeta;
                string.append(" tropical-fish:").append(tropicalFishBucketMeta.getBodyColor().name()).append(",").append(tropicalFishBucketMeta.getPattern().name()).append(",").append(tropicalFishBucketMeta.getPatternColor());
            }

            if (itemMeta instanceof MapMeta) {
                MapMeta mapMeta = (MapMeta) itemMeta;
                //TODO: MapMeta
            }
            if (BimmCore.supports(14) && itemMeta instanceof CrossbowMeta) {
                CrossbowMeta crossbowMeta = (CrossbowMeta) itemMeta;
                //TODO: CrossbowMeta
            }
            if (BimmCore.supports(12) && itemMeta instanceof SpawnEggMeta) {
                SpawnEggMeta spawnEggMeta = (SpawnEggMeta) itemMeta;
                //TODO: SpawnEggMeta
            }

            if (BimmCore.supports(15) && itemMeta instanceof SuspiciousStewMeta) {
                SuspiciousStewMeta suspiciousStewMeta = (SuspiciousStewMeta) itemMeta;
                //TODO: SuspiciousStewMeta
            }
        }

        return string.toString();
    }

    public String getTexture() {
        try {
            SkullMeta meta = (SkullMeta) getItemMeta();
            GameProfile profile = (GameProfile) Reflection.get(meta.getClass(), "profile", meta);
            Property property = (Property) profile.getProperties().get("textures").toArray()[0];
            return property.getValue();
        } catch (Exception e) {
            return null;
        }
    }
}
