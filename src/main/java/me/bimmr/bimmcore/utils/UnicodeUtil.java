package me.bimmr.bimmcore.utils;

/**
 * The enum Unicode util.
 */
public enum UnicodeUtil {
    /**
     * Pencil unicode util.
     */
    PENCIL("✎"),
    /**
     * X thin unicode util.
     */
    X_THIN("✕"),
    /**
     * X thick unicode util.
     */
    X_THICK("✖"),
    /**
     * Arrow filled right unicode util.
     */
    ARROW_FILLED_RIGHT("➥"),
    /**
     * Arrow empty left unicode util.
     */
    ARROW_EMPTY_LEFT("⇦"),
    /**
     * Arrow empty right unicode util.
     */
    ARROW_EMPTY_RIGHT("⇨"),
    /**
     * Arrow empty up unicode util.
     */
    ARROW_EMPTY_UP("⇧"),
    /**
     * Arrow empty down unicode util.
     */
    ARROW_EMPTY_DOWN("⇩"),
    /**
     * Triangle empty left unicode util.
     */
    TRIANGLE_EMPTY_LEFT("◁"),
    /**
     * Triangle empty right unicode util.
     */
    TRIANGLE_EMPTY_RIGHT("▷"),
    /**
     * Triangle empty up unicode util.
     */
    TRIANGLE_EMPTY_UP("△"),
    /**
     * Triangle empty down unicode util.
     */
    TRIANGLE_EMPTY_DOWN("▽"),
    /**
     * Triangle filled up unicode util.
     */
    TRIANGLE_FILLED_UP("⏶"),
    /**
     * Triangle filled down unicode util.
     */
    TRIANGLE_FILLED_DOWN("⏷"),
    /**
     * Eject unicode util.
     */
    EJECT("⏏"),
    /**
     * Bullet circled empty unicode util.
     */
    BULLET_CIRCLED_EMPTY("⦾"),
    /**
     * Bullet circled filled unicode util.
     */
    BULLET_CIRCLED_FILLED("⦿"),
    /**
     * Bullet unicode util.
     */
    BULLET("•"),
    /**
     * Copyright unicode util.
     */
    COPYRIGHT("©"),
    /**
     * Registered unicode util.
     */
    REGISTERED("®"),
    /**
     * Swords crossed unicode util.
     */
    SWORDS_CROSSED("⚔"),
    /**
     * Gun unicode util.
     */
    GUN("┏┳╸"),
    /**
     * Caution unicode util.
     */
    CAUTION("⚠"),
    /**
     * Info unicode util.
     */
    INFO("ℹ"),
    /**
     * Unknown unicode util.
     */
    UNKNOWN("⍰"),
    /**
     * Envelope unicode util.
     */
    ENVELOPE("✉"),
    /**
     * Del unicode util.
     */
    DEL("␡"),
    /**
     * Scissors empty unicode util.
     */
    SCISSORS_EMPTY("✄"),
    /**
     * Scissors filled unicode util.
     */
    SCISSORS_FILLED("✂"),
    /**
     * Diamond x unicode util.
     */
    DIAMOND_X("❖"),
    /**
     * Diamond filled unicode util.
     */
    DIAMOND_FILLED("◆"),
    /**
     * Diamond empty unicode util.
     */
    DIAMOND_EMPTY("◇"),
    /**
     * Arrow unicode util.
     */
    ARROW("➳"),
    /**
     * Fire unicode util.
     */
    FIRE("♨"),
    /**
     * Star empty unicode util.
     */
    STAR_EMPTY("✧"),
    /**
     * Star filled unicode util.
     */
    STAR_FILLED("✦"),
    /**
     * Enter unicode util.
     */
    ENTER("⎆")
    ;

    /**
     * The String.
     */
    String string;

    UnicodeUtil(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
