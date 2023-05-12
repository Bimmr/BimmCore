package me.bimmr.bimmcore.gui.inventory;

/**
 * The interface Menu click.
 */
@FunctionalInterface
public interface MenuClick {

    /**
     * Click.
     *
     * @param clickEvent the click event
     */
    void click(ClickEvent clickEvent);

}
