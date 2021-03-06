package org.riverock.gwt.client.commons;

/**
 * User: SergeMaslyukov
 * Date: 25.11.2009
 * Time: 17:42:11
 */
public abstract class LookupWidget extends DynaTableWidget {

    public LookupWidget() {
        super();
    }

    public LookupWidget(String widgetName) {
        super(widgetName);
    }

    public void turnVisibleOn() {
        mainWidget.setVisible(true);
        this.setVisible(true);
    }
}
