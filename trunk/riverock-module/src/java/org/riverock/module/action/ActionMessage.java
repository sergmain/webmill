package org.riverock.module.action;

import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:26:41
 *         $Id$
 */
public class ActionMessage implements Serializable {
    private String value = null;

    /**
     * Constructor, Create a new ActionMessage object.
     *
     * @param bundle
     * @param key    a certain key define in properties file.
     */
    public ActionMessage(ResourceBundle bundle, String key) {
        try {
            this.value = bundle.getString(key);
        }
        catch (Exception e) {
            this.value = "[" + key + ":null]";
        }
    }

    /**
     * Return value for a certain key.
     *
     * @return value for a certain key.
     */
    public String getValue() {
        return (this.value);
    }
}
