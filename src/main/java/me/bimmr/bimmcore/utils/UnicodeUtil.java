package me.bimmr.bimmcore.utils;

/**
 * Unicode characters
 */
public enum UnicodeUtil {
    PENCIL("✎"),
    X_THIN("✕"),
    X_THICK("✖"),
    ARROW_FILLED_RIGHT("➥"),
    ARROW_EMPTY_LEFT("⇦"),
    ARROW_EMPTY_RIGHT("⇨"),
    ARROW_EMPTY_UP("⇧"),
    ARROW_EMPTY_DOWN("⇩"),
    TRIANGLE_EMPTY_LEFT("◁"),
    TRIANGLE_EMPTY_RIGHT("▷"),
    TRIANGLE_EMPTY_UP("△"),
    TRIANGLE_EMPTY_DOWN("▽"),
    TRIANGLE_FILLED_UP("⏶"),
    TRIANGLE_FILLED_DOWN("⏷"),
    EJECT("⏏"),
    BULLET_CIRCLED_EMPTY("⦾"),
    BULLET_CIRCLED_FILLED("⦿"),
    BULLET("•"),
    COPYRIGHT("©"),
    REGISTERED("®"),
    SWORDS_CROSSED("⚔"),
    GUN("┏┳╸"),
    CAUTION("⚠"),
    INFO("ℹ"),
    UNKNOWN("⍰"),
    ENVELOPE("✉"),
    DEL("␡"),
    SCISSORS_EMPTY("✄"),
    SCISSORS_FILLED("✂"),
    DIAMOND_X("❖"),
    DIAMOND_FILLED("◆"),
    DIAMOND_EMPTY("◇"),
    ARROW("➳"),
    FIRE("♨"),
    STAR_EMPTY("✧"),
    STAR_FILLED("✦"),
    ENTER("⎆")
    ;

    String string;

    UnicodeUtil(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
