package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * User: SergeMaslyukov
 * Date: 26.05.2009
 * Time: 21:52:54
 * $Id$
 */
public abstract interface DialogBoxWithUpdater {

    DialogBox getDialogBox();

    FieldUpdater[] getFieldUpdater();
}
