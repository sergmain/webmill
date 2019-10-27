/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.module.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:26:41
 *         $Id: ActionMessage.java 1450 2007-10-01 14:50:54Z serg_main $
 */
public class ActionMessage implements Serializable {
    private String value = null;

    /**
     * Constructor, Create a new ActionMessage object.
     *
     * @param bundle resource bundle
     * @param key    a certain key define in properties file.
     * @param params params
     */
    public ActionMessage(ResourceBundle bundle, String key, Object ... params) {
        try {
            this.value = bundle.getString(key);
            this.value = MessageFormat.format(value, params);
        }
        catch (Exception e) {
            this.value = key;
        }
    }

    /**
     * Constructor, Create a new ActionMessage object.
     *
     * @param bundle resource bundle
     * @param key    a certain key define in properties file.
     */
    public ActionMessage(ResourceBundle bundle, String key) {
        try {
            this.value = bundle.getString(key);
        }
        catch (Exception e) {
            this.value = key;
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
