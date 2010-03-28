package org.riverock.gwt.client.commons;

import java.util.List;

/**
 * An interface for providing row-level updates of data, intended here to used
 * to update a DynaTableWidget.
 */
public interface TableDataProvider {

  /**
   * An interface allow a widget to accept or report failure when a row data
   * is issued for update.
   */
  interface RowDataAcceptor {
    void accept(int startRow, List<TableRow> rows);
    void failed(Throwable caught);
  }

  void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor);
}
