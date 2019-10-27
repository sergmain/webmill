package org.riverock.gwt.client.commons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: Serg
 * Date: 25.04.13
 * Time: 23:24
 */
public class SimpleDialogBox extends DialogBox implements ClickHandler {
    private final HTML body = new HTML("");

    private static final SimpleDialogBoxConstants SIMPLE_DIALOG_BOX_CONSTANTS = GWT.create(SimpleDialogBoxConstants.class);

    public SimpleDialogBox(String text, String bodyStr) {
        this();
        makeVisible(text, bodyStr);
    }

    public void makeVisible(String text, String bodyStr) {
        setText(text);
        setBody(bodyStr);
        center();
        show();
    }


    public SimpleDialogBox() {
        super();
        //noinspection GWTStyleCheck
        setStylePrimaryName("gwt-DialogBox");
        final Button closeButton = new Button( SIMPLE_DIALOG_BOX_CONSTANTS.close(), this);
        final VerticalPanel panel = new VerticalPanel();
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
