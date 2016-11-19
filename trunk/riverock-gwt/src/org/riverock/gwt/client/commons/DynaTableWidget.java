package org.riverock.gwt.client.commons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.*;
import org.riverock.gwt.client.exception.Http403ForbiddenException;

import java.util.List;

/**
 * A composite Widget that implements the main interface for the dynamic table,
 * including the data table, status indicators, and paging buttons.
 */
@SuppressWarnings("GWTStyleCheck")
public abstract class DynaTableWidget extends Composite implements Refreshable {

    private static final DynaTableConstants dynaTableConstants = GWT.create(DynaTableConstants.class);

    private static final HTML separator = new HTML();
    static {
    	separator.setHTML("&nbsp;");
    	separator.setWidth("5");
    }

    private static String loadingImage=null;
    protected static class WaitDialog extends DialogBox {
        
        public WaitDialog() {
            super();
            setWidth("65px");
            setHeight("65px");
            if (loadingImage==null) {
                loadingImage = "<img src=\""+ GWT.getModuleBaseURL()+"images/ring64.gif\" />";
            }
            final HTML html = new HTML(loadingImage);
            add(html);
            center();
            hide();
        }
    }

    /*
     * A dialog box for displaying an error.
     */
/*
    protected static class ErrorDialog extends SimpleDialogBox {
        public ErrorDialog() {
            super();
        }
    }
*/

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

            final HorizontalPanel buttons = new HorizontalPanel();
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
            // putGlobalError( "startRow #1.1: "+ startRow  );
            final Object source = event.getSource();
            if (source == gotoNext) {
                startRow += rowCount;
            }
            else if (source == gotoPrev) {
                startRow -= rowCount;
                if (startRow < 0) {
                    startRow = 0;
                }
            }
            else if (source == gotoFirst) {
                startRow = 0;
            }
            // putGlobalError( "startRow #1.2: "+ startRow  );
            refresh();
        }
    }

    private class RowDataAcceptorImpl implements TableDataProvider.RowDataAcceptor {
        public void accept(int startRow, List<TableRow> data) {

            // dynamically resize table
            if (rowCount==0) {
                grid.resizeRows(data.size()+1);
            }

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
            for (int srcRowIndex = 0; srcRowIndex < srcRowCount; ++srcRowIndex, ++destRowIndex) {

                final TableRow tableRow = data.get(srcRowIndex);

                if (tableRow.getDataCells().length != destColCount) {
                    putGlobalError( "Fatal error. Column count mismatch. Expected: "+ destColCount+",  actual: "+ (tableRow.getDataCells().length)  );
                }

                int srcColIndex = 0;
                for (TableRow.DataCell dataCell : tableRow.getDataCells()) {
                    switch (dataCell.getCellType()) {
                        case STRING:
                            grid.setText(destRowIndex, srcColIndex, dataCell.getString());
                            if (dataCell.getCellStyles()!=null) {
                                grid.getCellFormatter().setStyleName(destRowIndex, srcColIndex, dataCell.getCellStyles());
                            }
                            srcColIndex++;
                            break;
                        case WIDGET:
                            grid.getColumnFormatter().setStyleName(srcColIndex, "buttonColumn");
                            grid.setWidget(destRowIndex, srcColIndex++, dataCell.getWidget());
                            break;
                        default:
                            throw new IllegalStateException("Error");
                    }
                    if (tableRow.getRowStyle()!=null) {
                        grid.getRowFormatter().setStyleName(destRowIndex, tableRow.getRowStyle());
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

            if (isNavBarEnabled) {
                // Synchronize the nav buttons.
                navbar.gotoFirst.setEnabled(startRow > 0);
                navbar.gotoPrev.setEnabled(startRow > 0);
//            putGlobalError( "getDataRowCount(): "+ getDataRowCount()+", data.size(): " + data.size() );
                navbar.gotoNext.setEnabled(getDataRowCount() == data.size());
            }

            // Update the status message.
            final String statusText = ""+(startRow + (data.size() != 0 ? 1 : 0)) + " - " + (startRow + srcRowCount);
            setStatusText(statusText);
        }

        public void failed(Throwable caught) {
            setStatusText(dynaTableConstants.error());
            if((caught instanceof com.google.gwt.user.client.rpc.StatusCodeException) &&
                    ((com.google.gwt.user.client.rpc.StatusCodeException)caught).getStatusCode()==403) {
                throw new Http403ForbiddenException();
            }
            else if (caught instanceof InvocationException) {
                errorDialog.makeVisible("The server could not be reached", dynaTableConstants.noConnection() );
            }
            else {
                errorDialog.makeVisible("The unexpected error while processing remote call", caught.getMessage());
            }
        }
    }

    protected final TableDataProvider.RowDataAcceptor acceptor = new RowDataAcceptorImpl();

    protected final Grid grid = new Grid();

    private final NavBar navbar = new NavBar();

    protected final SimpleDialogBox errorDialog = new SimpleDialogBox();
    protected final WaitDialog waitDialog = new WaitDialog();

    protected final VerticalPanel outer = new VerticalPanel();
    protected final VerticalPanel mainWidget = new VerticalPanel();
    protected LookupWidget[] lookupWidgets = null;
    protected Hyperlink closeLookup = null;
    protected DeleteItemDialog deleteDialogBox = null;
    protected UpdateItemDialog updateDialogBox = null;
    private boolean isNavBarEnabled;
    protected final HTML processingStatus = new HTML();
    private TableToolbarItem[] customToolbarItems=null;

    private TableDataProvider provider;

    protected int startRow = 0;
    protected int rowCount = 0;
    private String widgetName = null;

    public DynaTableWidget() {
        this(null);
    }
    public DynaTableWidget(String widgetName) {
        super();
        initWidget(outer);
        this.widgetName = widgetName;
    }

    public SimpleDialogBox getErrorDialog() {
        return errorDialog;
    }

    /*
        @Deprecated
        public void setCustomToolbarItems(TableToolbarItem[] customToolbarItems) {
            this.customToolbarItems = customToolbarItems;
        }

    */
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
        final Label l = new  Label(text);
        l.setStyleName("errorText");
        outer.insert(l, 0);
    }

/*
     * use something like that
     *
     final TableToolbarItem[] items = new TableToolbarItem[]{
        new TableToolbarItem(createDialogBox(), "New User")
     };
     initializeNew(new UserProvider(), COLUMNS_NAME, null, 20, items, null, false);
     mainWidget.setVisible(true);

     *
     *
     * @param provider
     * @param columns
     * @param columnStyles
     * @param rowCount
     * @param createDialogBox
     * @param lookupWidget
     * @param isNavBarEnabled

    @Deprecated
    public void initialize(
        final TableDataProvider provider, final String[] columns, final String[] columnStyles, final int rowCount,
        final CreateItemDialog createDialogBox, final LookupWidget lookupWidget, final boolean isNavBarEnabled) {


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
                items[i+1] = new TableToolbarItem(createDialogBox, "New record");
            }
        }

        // putGlobalError( "rowCount: "+ rowCount  );
        initializeNew(provider, columns, columnStyles,  rowCount, items, lookupWidget, isNavBarEnabled);
    }
*/
    @Deprecated
    public void initializeNew(
            final TableDataProvider provider, final String[] columns, final String[] columnStyles, final int rowCount,
            final TableToolbarItem[] items, final LookupWidget lookupWidget, final boolean isNavBarEnabled) {


        LookupWidget[] lookupWidgets;
        if (lookupWidget!=null) {
            lookupWidgets = new LookupWidget[]{lookupWidget};
        }
        else {
            lookupWidgets = new LookupWidget[0];
        }

//        putGlobalError( "#11.1 rowCount: "+ rowCount );
        init(provider, columns, columnStyles, rowCount, items, lookupWidgets, isNavBarEnabled);
    }

    public void init(
        final TableDataProvider provider, final String[] columns, final String[] columnStyles, final int rowCount,
        final TableToolbarItem[] items, final LookupWidget[] _lookupWidgets, final boolean isNavBarEnabled) {

        if (columns.length == 0) {
            throw new IllegalArgumentException("expecting a positive number of columns");
        }

        if (columnStyles != null && columns.length != columnStyles.length) {
            throw new IllegalArgumentException("expecting as many styles as columns");
        }

        if (rowCount!=0 && !isNavBarEnabled) {
            putGlobalError( "Navigation bar can be disabled only for dynamic tables. Set 'rowCount' to 0 or disable navigation bar" );
        }

        this.rowCount = rowCount;
        this.isNavBarEnabled = isNavBarEnabled;
        this.provider = provider;

        grid.setWidth("100%");
        outer.setWidth("100%");
        
        mainWidget.setWidth("100%");
        this.lookupWidgets = _lookupWidgets!=null ?  _lookupWidgets : new LookupWidget[0];

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
                    hyperlink.addHandler( handler -> {
                                for (final FieldUpdater fieldUpdater : item.getWidget().getFieldUpdater()) {
                                    fieldUpdater.update();
                                }
                                item.getWidget().getDialogBox().center();
                                item.getWidget().getDialogBox().show();
                            },
                            ClickEvent.getType()
                    );
                    item.setButton(hyperlink);
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

//        Window.alert("Step #10.10 " + _lookupWidgets);

        if (this.lookupWidgets!=null && this.lookupWidgets.length>0) {
            closeLookup = new Hyperlink();
            if (this.widgetName!=null && this.widgetName.trim().length()>0) {
                closeLookup.setText(dynaTableConstants.backTo() + ' ' +this.widgetName);
            }
            else {
                closeLookup.setText(dynaTableConstants.back());
            }
            closeLookup.setStyleName("asButton");
            closeLookup.addHandler( handler -> hideLookupWidget(), ClickEvent.getType() );
            closeLookup.setVisible(false);
            outer.add(closeLookup);
            
            final HTML html = new HTML("&nbsp;");
            outer.add(html);

//            Window.alert("Step #20.10 ");
            for (LookupWidget lookupWidget : this.lookupWidgets) {
//                Window.alert("Step #20.20 " + lookupWidget);
                lookupWidget.setWidth("100%");
                lookupWidget.setVisible(false);
                outer.add(lookupWidget);
            }
        }

        initTable(columns, columnStyles, rowCount);

        mainWidget.setVisible(true);
    }

    public void hideLookupWidget() {
        int i=0;
        // Window.alert("#30.0");
        for (LookupWidget lookupWidget : lookupWidgets) {
            // Window.alert("Step #30.1 " + (i++) + " " + lookupWidget);

            if (lookupWidget!=null) {
                lookupWidget.hideAllWidget();
                closeLookup.setVisible(false);
                lookupWidget.setVisible(false);
                lookupWidget.startRow=0;
            }
        }
        // Window.alert("#30.10");
        mainWidget.setVisible(true);
        // Window.alert("#30.15");
    }

    protected void hideAllWidget() {
        mainWidget.setVisible(false);
        if (lookupWidgets==null) {
            return;
        }
        // Window.alert("#40.0");
        int i=0;
        for (LookupWidget lookupWidget : lookupWidgets) {
            // Window.alert("Step #40.1 " + (i++) + " " + lookupWidget);

            if (lookupWidget!=null) {
                closeLookup.setVisible(false);
                lookupWidget.setVisible(false);
                lookupWidget.startRow=0;
                lookupWidget.hideAllWidget();
                // Window.alert("Step #40.5 ");
            }
        }
        // Window.alert("Step #40.10 ");
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
            setStatusText( dynaTableConstants.pleaseWait() );
        }
        provider.updateRowData(startRow, rowCount, acceptor);
    }

/*
     * @deprecated
     * @param rows
    public void setRowCount(int rows) {
        grid.resizeRows(rows);
    }
*/

    public void setStatusText(String text) {
        if (isNavBarEnabled) {
            navbar.status.setText(text);
        }
    }

    private int getDataRowCount() {
        // putGlobalError( "grid.getRowCount(): "+ grid.getRowCount()  );
        // 1 - size of table header
        return grid.getRowCount() - 1;
    }

    protected void initTable(String[] columns, String[] columnStyles, int rowCount) {
        // Set up the header row. It's one greater than the number of visible rows.
        //
        if (isNavBarEnabled && this.rowCount!=rowCount) {
            putGlobalError( "Wrong reinitialize table with navigation bar. Origin count of rows: "+ this.rowCount+", new value: " + rowCount);
        }

        grid.resize(rowCount + 1, columns.length);
//        putGlobalError( "#11.6 grid.getRowCount(): "+ grid.getRowCount() );

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
