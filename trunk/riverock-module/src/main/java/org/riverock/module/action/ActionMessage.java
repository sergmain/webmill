/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
