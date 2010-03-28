package org.riverock.gwt.client.commons;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.GWT;

/**
 * A composite Widget that implements the main interface for the dynamic table,
 * including the data table, status indicators, and paging buttons.
 */
public abstract class DynaTableWidget extends Composite {

    private static final HTML separator = new HTML();
    static {
    	separator.setHTML("&nbsp;");
    	separator.setWidth("5");
    }

    private static String loadingImage=null;
    protected static class WaitDialog extends DialogBox {
        
        public WaitDialog() {
            setWidth("65px");
            setHeight("65px");
            if (loadingImage==null) {
                loadingImage = "<img src=\""+ GWT.getModuleBaseURL()+"images/ring64.gif\" />";
            }
            HTML html = new HTML(loadingImage);
            add(html);
            center();
            hide();
        }
    }

    /**
     * A dialog box for displaying an error.
     */
    protected static class ErrorDialog extends DialogBox implements ClickHandler {
        private HTML body = new HTML("");

        public ErrorDialog() {
            setStylePrimaryName("gwt-DialogBox");
            Button closeButton = new Button("Закрыть", this);
            VerticalPanel panel = new VerticalPanel();
            panel.setSpacing(4);
            panel.add(body);
            panel.add(closeButton);
            panel.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);
            setWidget(panel);
        }

        public String getBody() {
            return body.getHTML();
        }

        public void onClick(ClickEvent event) {
            hide();
        }

        public void setBody(String html) {
            body.setHTML(html);
        }
    }

    private class NavBar extends Composite implements ClickHandler {

        public final DockPanel bar = new DockPanel();
        public final Button gotoFirst = new Button("&lt;&lt;", this);
        public final Button gotoNext = new Button("&gt;", this);
        public final Button gotoPrev = new Button("&lt;", this);
        public final HTML status = new HTML();

        public NavBar() {
            initWidget(bar);
            
            bar.setStyleName("navbar");
            status.setStyleName("status");

            HorizontalPanel buttons = new HorizontalPanel();
            buttons.add(gotoFirst);
            buttons.add(gotoPrev);
            buttons.add(gotoNext);
            bar.add(buttons, DockPanel.EAST);
            bar.setCellHorizontalAlignment(buttons, DockPanel.ALIGN_RIGHT);
            bar.add(status, DockPanel.CENTER);
            bar.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
            bar.setCellHorizontalAlignment(status, HasAlignment.ALIGN_RIGHT);
            bar.setCellVerticalAlignment(status, HasAlignment.ALIGN_MIDDLE);
            bar.setCellWidth(status, "100%");

            // Initialize prev & first button to disabled.
            //
            gotoPrev.setEnabled(false);
            gotoFirst.setEnabled(false);
        }

        public void onClick(ClickEvent event) {
            Object source = event.getSource();
            if (source == gotoNext) {
                startRow += getDataRowCount();
                refresh();
            }
            else if (source == gotoPrev) {
                startRow -= getDataRowCount();
                if (startRow < 0) {
                    startRow = 0;
                }
                refresh();
            }
            else if (source == gotoFirst) {
                startRow = 0;
                refresh();
            }
        }
    }

    private class RowDataAcceptorImpl implements TableDataProvider.RowDataAcceptor {
        public void accept(int startRow, List<TableRow> data) {


            int destRowCount = getDataRowCount();

            if (data.size() > destRowCount) {
                putGlobalError( "Fatal error. Too many rows in result. result: "+ data.size()+", grid: "+ destRowCount  );
            }

            grid.getRowFormatter().setStyleName(0, "defaultTable");
            for (int i = 1; i<destRowCount+1; ++i) {
                grid.getRowFormatter().setStyleName(i, "simple");
            }

            int actionColumn = 0;

            int destColCount = grid.getCellCount(0);


            int srcRowCount = data.size();
            int destRowIndex = 1; // skip navbar row
            int startButtons=-1;
            int countButtons =0;
            for (int srcRowIndex = 0; srcRowIndex < srcRowCount; ++srcRowIndex, ++destRowIndex) {

                TableRow tableRow = data.get(srcRowIndex);
                String[] srcRowData = tableRow.getCols();

                if ((srcRowData.length+tableRow.getButtons().length) != destColCount) {
                    putGlobalError( "Fatal error. Column count mismatch. "+ (srcRowData.length+tableRow.getButtons().length)+",  "+ destColCount  );
                }

                int srcColIndex = 0;
                for (String cellHTML : srcRowData) {
                    grid.setText(destRowIndex, srcColIndex, cellHTML);
                    if (tableRow.getCellStyles() != null && tableRow.getCellStyles()[srcColIndex] != null) {
                        grid.getCellFormatter().setStyleName(destRowIndex, srcColIndex, tableRow.getCellStyles()[srcColIndex]);
                    }
                    srcColIndex++;
                }
                if (tableRow.getRowStyle()!=null) {
                    grid.getRowFormatter().setStyleName(destRowIndex, tableRow.getRowStyle());
                }
                startButtons=srcColIndex;
                countButtons = tableRow.getButtons().length;
                for (Widget widget : tableRow.getButtons()) {
                    if (widget==null) {
                        grid.setWidget(destRowIndex, srcColIndex++, new InlineHTML("&nbsp;"));
                    }
                    else {
                        grid.setWidget(destRowIndex, srcColIndex++, widget);
                    }
                }
            }

            // Clear remaining table rows.
            //
            boolean isLastPage = false;
            for (; destRowIndex < destRowCount + 1; ++destRowIndex) {
                isLastPage = true;
                for (int destColIndex = 0; destColIndex < destColCount+actionColumn; ++destColIndex) {
                    grid.clearCell(destRowIndex, destColIndex);
                }
            }

            if (startButtons!=-1) {
                for (int i=0; i<countButtons; i++) {
                    grid.getColumnFormatter().setStyleName(startButtons+i, "buttonColumn");
                }
            }

            // Synchronize the nav buttons.
            navbar.gotoNext.setEnabled(!isLastPage);
            navbar.gotoFirst.setEnabled(startRow > 0);
            navbar.gotoPrev.setEnabled(startRow > 0);

            // Update the status message.
            final String statusText = ""+(startRow + (data.size() != 0 ? 1 : 0)) + " - " + (startRow + srcRowCount);
            setStatusText(statusText);
        }

        public void failed(Throwable caught) {
            setStatusText("Error");
            if (errorDialog == null) {
                errorDialog = new ErrorDialog();
            }
            if (caught instanceof InvocationException) {
                errorDialog.setText("An RPC server could not be reached");
                errorDialog.setBody(NO_CONNECTION_MESSAGE);
            }
            else {
                errorDialog.setText("Unexcepted Error processing remote call");
                errorDialog.setBody(caught.getMessage());
            }
            errorDialog.center();
        }
    }

    private static final String NO_CONNECTION_MESSAGE = "<p>No connection</p>";

    protected final TableDataProvider.RowDataAcceptor acceptor = new RowDataAcceptorImpl();

    protected final Grid grid = new Grid();

    private final NavBar navbar = new NavBar();

    protected ErrorDialog errorDialog = new ErrorDialog();
    protected final WaitDialog waitDialog = new WaitDialog();

    protected final VerticalPanel outer = new VerticalPanel();
    protected final VerticalPanel mainWidget = new VerticalPanel();
    protected Widget lookupWidget = null;
    protected Hyperlink closeLookup = null;
    protected DialogBoxWithUpdater deleteDialogBox = null;
    protected DialogBoxWithUpdater updateDialogBox = null;
    private boolean isNavBarEnabled;
    protected final HTML processingStatus = new HTML();
    protected TableToolbarItem[] customToolbarItems=null;

    private TableDataProvider provider;

    private int startRow = 0;

    public DynaTableWidget() {
        super();
    }

    public void setCustomToolbarItems(TableToolbarItem[] customToolbarItems) {
        this.customToolbarItems = customToolbarItems;
    }

    protected void showMainWidget() {
        processingStatus.setVisible(false);
        mainWidget.setVisible(true);
    }

    protected void hideMainWidget() {
        mainWidget.setVisible(false);
        processingStatus.setVisible(true);
    }

    protected void initTitle(Widget title) {
        outer.insert(title, 0);
    }

    protected void putGlobalError(String text) {
        Label l = new  Label(text);
        l.setStyleName("errorText");
        outer.insert(l, 0);
    }

    public void initialize(
        final TableDataProvider provider, final String[] columns, final String[] columnStyles, final int rowCount,
        final DialogBoxWithUpdater createDialogBox, final LookupWidget lookupWidget, final boolean isNavBarEnabled) {


        int size = customToolbarItems!=null?customToolbarItems.length:0;
        size += (createDialogBox!=null?1:0);
        TableToolbarItem[] items = null;
        if (size>0) {
            int i=0;
            items = new TableToolbarItem[size];
            if (customToolbarItems!=null) {
                for(TableToolbarItem item : customToolbarItems) {
                    items[i++] = item;
                }
            }
            if (createDialogBox!=null) {
                items[i++] = new TableToolbarItem(createDialogBox, "Новая запись");
            }
        }

        // putGlobalError( "rowCount: "+ rowCount  );
        initializeNew(provider, columns, columnStyles,  rowCount, items, lookupWidget, isNavBarEnabled);
    }

    public void initializeNew(
        final TableDataProvider provider, final String[] columns, final String[] columnStyles, final int rowCount,
        final TableToolbarItem[] items, final DynaTableWidget lookupWidget, final boolean isNavBarEnabled) {

        if (columns.length < 1) {
            throw new IllegalArgumentException("expecting a positive number of columns");
        }

        if (columnStyles != null && columns.length != columnStyles.length) {
            throw new IllegalArgumentException("expecting as many styles as columns");
        }

        this.isNavBarEnabled = isNavBarEnabled;
        this.provider = provider;
        initWidget(outer);
        grid.setWidth("100%");
        outer.setWidth("100%");
        
        mainWidget.setWidth("100%");
        this.lookupWidget = lookupWidget;

        processingStatus.setVisible(false);
        mainWidget.add(processingStatus);

        if (items!=null && items.length>0) {
            final HorizontalPanel hp = new HorizontalPanel();
            boolean isNotFirst = false;
            for (final TableToolbarItem item : items) {
            	if (isNotFirst) {
            		hp.add(separator);
            	}
            	else {
            		isNotFirst = true;
            	}

                if (item.getSimpleWidget()!=null) {
                    hp.add(item.getSimpleWidget());
                }
                else {
                    // Create a button to show the dialog Box
                    final Hyperlink hyperlink = new Hyperlink();
                    hyperlink.setText(item.getButtonText());
                    hyperlink.setStyleName("asButton");
                    hyperlink.addClickHandler(
                        new ClickHandler() {
                            public void onClick(ClickEvent sender) {
                                for (final FieldUpdater fieldUpdater : item.getWidget().getFieldUpdater()) {
                                    fieldUpdater.update();
                                }
                                item.getWidget().getDialogBox().center();
                                item.getWidget().getDialogBox().show();
                            }
                        }
                    );
                    hp.add(hyperlink);
                }
            }
            mainWidget.add(hp);
        }
        
        mainWidget.add(grid);
        if (isNavBarEnabled) {
            mainWidget.add(navbar);
        }

        outer.add(mainWidget);

        if (lookupWidget!=null) {
            closeLookup = new Hyperlink();
            closeLookup.setText("Вернуться");
            closeLookup.setStyleName("asButton");
            closeLookup.addClickHandler(
                new ClickHandler() {
                    public void onClick(ClickEvent sender) {
                        mainWidget.setVisible(true);
                        closeLookup.setVisible(false);
                        lookupWidget.setVisible(false);
                        lookupWidget.startRow = 0;
                    }
                }
            );
            closeLookup.setVisible(false);
            outer.add(closeLookup);
            
            HTML html = new HTML("&nbsp;");
            outer.add(html);
            
            lookupWidget.setWidth("100%");
            lookupWidget.setVisible(false);
            outer.add(lookupWidget);
        }

        initTable(columns, columnStyles, rowCount);
    }


    public void clearStatusText() {
        if (isNavBarEnabled) {
            navbar.status.setHTML("&nbsp;");
        }
    }

    public void refresh() {
        if (isNavBarEnabled) {
            // Disable buttons temporarily to stop the user from running off the end.
            //
            navbar.gotoFirst.setEnabled(false);
            navbar.gotoPrev.setEnabled(false);
            navbar.gotoNext.setEnabled(false);
            setStatusText("Please wait...");
        }
        provider.updateRowData(startRow, grid.getRowCount() - 1, acceptor);
    }

    public void setRowCount(int rows) {
        grid.resizeRows(rows);
    }

    public void setStatusText(String text) {
        if (isNavBarEnabled) {
            navbar.status.setText(text);
        }
    }

    private int getDataRowCount() {
        return grid.getRowCount() - 1;
    }

    protected void initTable(String[] columns, String[] columnStyles, int rowCount) {
        // Set up the header row. It's one greater than the number of visible rows.
        //
        grid.resize(rowCount + 1, columns.length);
        for (int i = 0, n = columns.length; i < n; i++) {
            if (StringUtils.isBlank(columns[i])) {
                grid.setHTML(0, i, "&nbsp;");
            }
            else {
                grid.setText(0, i, columns[i]);
            }
            if (columnStyles != null) {
                grid.getCellFormatter().setStyleName(0, i, columnStyles[i] + " header");
            }
        }
    }
}
