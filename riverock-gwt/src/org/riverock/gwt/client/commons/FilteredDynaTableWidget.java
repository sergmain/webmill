package org.riverock.gwt.client.commons;

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

    protected static final String processingStatusText = "Идет загрузка. Пожалуйста подождите.";

    public abstract Widget createFilterWidget();

    private final HTML processingStatus = new HTML();
    private Widget filterWidget;
    private boolean isResultPaged=false;
    private boolean isFilterShowed = true;
    private final static String SHOW_FILTER = "Показать фильтр";
    private final static String HIDE_FILTER = "Убрать фильтр";

    protected final Widget getFilterShowedButton() {
        final Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText(HIDE_FILTER);
        hyperlink.setStyleName("asButton");
        hyperlink.addClickHandler(
                new ClickHandler() {
                    public void onClick(ClickEvent sender) {
                        isFilterShowed = !isFilterShowed;
                        filterWidget.setVisible(isFilterShowed);
                        hyperlink.setText(isFilterShowed?HIDE_FILTER:SHOW_FILTER);
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
