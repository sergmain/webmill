package org.riverock.gwt.client.commons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * User: SergeMaslyukov
 * Date: 19.06.2009
 * Time: 13:22:41
 */
public abstract class FilteredDynaTableWidget extends DynaTableWidget {
	public FilteredDynaTableWidget() {
	}

    private static final FilteredDynaTableConstants filteredDynaTableConstants = GWT.create(FilteredDynaTableConstants.class);

    protected static final String processingStatusText = filteredDynaTableConstants.loading();

    public abstract Widget createFilterWidget();

    private final HTML processingStatus = new HTML();
    private Widget filterWidget;
    private boolean isResultPaged=false;
    private boolean isFilterShowed = true;

    protected final Widget getFilterShowedButton() {
        final Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText(filteredDynaTableConstants.hideFilter());
        hyperlink.setStyleName("asButton");
        hyperlink.addClickHandler(
                new ClickHandler() {
                    public void onClick(ClickEvent sender) {
                        isFilterShowed = !isFilterShowed;
                        filterWidget.setVisible(isFilterShowed);
                        hyperlink.setText(isFilterShowed? filteredDynaTableConstants.hideFilter() : filteredDynaTableConstants.showFilter());
                    }
                }
        );
        return hyperlink;
    }

    protected void initFilterWidget() {
        initFilterWidget(false);
    }

    protected void initFilterWidget(final boolean isResultPaged) {
        this.isResultPaged = isResultPaged;
        filterWidget = createFilterWidget();
        outer.insert(filterWidget, 0);
        outer.insert(processingStatus, 1);
    }

    protected boolean isResultPaged() {
        return isResultPaged;
    }

    protected void setFilterWidget(Widget filterWidget) {
        this.filterWidget = filterWidget;
    }

    protected Widget getFilterWidget() {
        return filterWidget;
    }

    protected void startRequestData(String text) {
/*
        filterWidget.setVisible(false);
        mainWidget.setVisible(false);
        processingStatus.setVisible(true);
        processingStatus.setText(text);
*/
//        mainWidget.setVisible(false);
        waitDialog.show();
    }

    protected void doneRequestData() {
/*
        processingStatus.setVisible(false);
        filterWidget.setVisible(true);
        mainWidget.setVisible(true);
*/
        mainWidget.setVisible(true);
        waitDialog.hide();
    }

}
