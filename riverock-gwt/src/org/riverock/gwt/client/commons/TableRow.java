package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.ui.Widget;

/**
 * User: SergeMaslyukov
 * Date: 16.05.2009
 * Time: 13:44:02
 * $Id$
 */
public class TableRow {
    private String cols[];
    private Widget[] buttons;
    private String cellStyles[]=null;
    private String rowStyle=null;


    public TableRow(String[] cols, Widget[] buttons, String[] cellStyles, String rowStyle) {
        this.cols = cols;
        this.buttons = buttons;
        this.cellStyles = cellStyles;
        this.rowStyle = rowStyle;
    }

    public TableRow(String[] cols, Widget[] buttons, String[] cellStyles) {
        this.cols = cols;
        this.buttons = buttons;
        this.cellStyles = cellStyles;
    }

    public TableRow(String[] cols, Widget[] buttons) {
        this.cols = cols;
        this.buttons = buttons;
    }

    public TableRow(String[] cols) {
        this.cols = cols;
        this.buttons = new Widget[0];
    }

    public String[] getCols() {
        return cols;
    }

    public Widget[] getButtons() {
        return buttons;
    }

    public String[] getCellStyles() {
        return cellStyles;
    }

    public String getRowStyle() {
        return rowStyle;
    }
}
