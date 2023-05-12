package me.bimmr.bimmcore.messages.fancymessage;

/**
 * The interface Fancy click.
 */
@FunctionalInterface
public interface FancyClick {

    /**
     * On click.
     *
     * @param clickEvent the click event
     */
    void onClick(FancyClickEvent clickEvent);
}
