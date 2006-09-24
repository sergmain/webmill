/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
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
package org.riverock.webmill.container.portlet;

import java.io.Serializable;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 19:51:54
 *         $Id$
 */
public class PortletContainerException extends Exception implements Serializable {
    private static final long serialVersionUID = 50434672384237127L;

    public PortletContainerException(){
        super();
    }

    public PortletContainerException(String s){
        super(s);
    }

    public PortletContainerException(String s, Throwable cause){
        super(s, cause);
    }
}


