package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 16.05.2009
 * Time: 13:44:02
 * $Id$
 */
public class TableRow {
    public static enum DataCellType { STRING, WIDGET }

    public static class DataCell {
        private DataCellType cellType;
        private String str;
        private Widget widget;
        private String cellStyles =null;

        public DataCell(Widget widget, String cellStyles) {
            this.widget = widget;
            this.cellStyles = cellStyles;
            this.cellType = DataCellType.WIDGET;
        }

        public DataCell(Widget widget) {
            this(widget, null);
        }

        public DataCell(String str) {
            this(str, null);
        }

        public DataCell(BigDecimal value) {
            this(value.toString(), null);
        }

        public DataCell(long value) {
            this(Long.toString(value), null);
        }

        public DataCell(String str, String cellStyles) {
            this.str = str;
            this.cellStyles = cellStyles;
            this.cellType = DataCellType.STRING;
        }

        public DataCellType getCellType() {
            return cellType;
        }

        public String getString() {
            if (cellType!=DataCellType.STRING) {
                throw new IllegalStateException("isn't string");
            }
            return str;
        }

        public Widget getWidget() {
            if (cellType!=DataCellType.WIDGET) {
                throw new IllegalStateException("isn't widget");
            }
            return widget;
        }

        public String getCellStyles() {
            return cellStyles;
        }
    }

    private DataCell[] dataCells;
    private String rowStyle=null;


    public TableRow(DataCell[] dataCells, String rowStyle) {
        this.dataCells = dataCells;
        this.rowStyle = rowStyle;
    }

    /**
     * user   TableRow(DataCell[], rowStyle)
     * @param cols
     * @param buttons
     * @param cellStyles
     * @param rowStyle
     */
    @Deprecated
    public TableRow(String[] cols, Widget[] buttons, String[] cellStyles, String rowStyle) {
        if (cellStyles!=null && cellStyles.length!= ((cols!=null?cols.length:0)+(buttons!=null?buttons.length:0))) {
            throw new IllegalArgumentException("Wrong count of parameters");
        }

        List<DataCell> dc = new ArrayList<DataCell>();
        if (cols!=null) {
            int i=0;
            for (String col : cols) {
                if (cellStyles!=null) {
                    dc.add( new DataCell(col, cellStyles[i++]) );
                }
                else {
                    dc.add( new DataCell(col) );
                }
            }
        }
        if (buttons!=null) {
            int i=0;
            for (Widget widget : buttons) {
                if (cellStyles!=null) {
                    dc.add( new DataCell(widget, cellStyles[i++]) );
                }
                else {
                    dc.add( new DataCell(widget) );
                }
            }
        }

        dataCells = dc.toArray(new DataCell[dc.size()]);

        this.rowStyle = rowStyle;
    }

    @Deprecated
    public TableRow(String[] cols, Widget[] buttons, String[] cellStyles) {
        this(cols, buttons, cellStyles, null);
    }

    @Deprecated
    public TableRow(String[] cols, Widget[] buttons) {
        this(cols, buttons, null, null);
    }

    @Deprecated
    public TableRow(String[] cols) {
        this(cols, new Widget[0], null, null);
    }

    public DataCell[] getDataCells() {
        return dataCells;
    }

    public String getRowStyle() {
        return rowStyle;
    }
}
