package org.riverock.gwt.client.commons;

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
    private HTML body = new HTML("");

    public SimpleDialogBox() {
        super();
        //noinspection GWTStyleCheck
        setStylePrimaryName("gwt-DialogBox");
        final Button closeButton = new Button("Close", this);
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
