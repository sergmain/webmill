package org.riverock.portlet.register;

import java.util.List;
import java.util.ArrayList;

import org.riverock.module.action.ActionMessage;

/**
 * User: SMaslyukov
 * Date: 28.09.2007
 * Time: 13:55:47
 */
public class ActionMessages {
    private List<ActionMessage> messages = new ArrayList<ActionMessage>();

    public List<ActionMessage> getMessages() {
        return messages;
    }
}
