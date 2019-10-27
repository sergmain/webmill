package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.ui.Widget;

/**
 * User: SergeMaslyukov
 * Date: 21.06.2009
 * Time: 12:00:31
 */
public class TableToolbarItem {
    private DialogBoxWithUpdater widget;
    private String buttonText;
    private Widget simpleWidget;
    private Widget button;

    public TableToolbarItem(DialogBoxWithUpdater widget, String buttonText) {
        this.widget = widget;
        this.buttonText = buttonText;
    }

    public TableToolbarItem(Widget simpleWidget) {
        this.simpleWidget = simpleWidget;
    }

    public TableToolbarItem() {
    }

    public Widget getButton() {
        return button;
    }

    public void setButton(Widget button) {
        this.button = button;
    }

    public Widget getSimpleWidget() {
        return simpleWidget;
    }

    public DialogBoxWithUpdater getWidget() {
        return widget;
    }

    public String getButtonText() {
        return buttonText;
    }
}
