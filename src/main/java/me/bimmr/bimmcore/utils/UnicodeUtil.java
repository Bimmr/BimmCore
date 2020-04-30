package me.bimmr.bimmcore.utils;

public enum UnicodeUtil {
    PENCIL("✎"),
    X_THIN("✕"),
    X_THICK("✖"),
    ARROW_RIGHT("➥"),
    SWORDS_CROSSED("⚔"),
    GUN("┏┳╸"),
    CAUTION("⚠"),
    ARROW("➳"),
    FIRE("♨"),
    STAR("✧")
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
