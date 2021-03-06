/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.container.portlet;

import java.io.Serializable;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 19:51:54
 *         $Id: PortletContainerException.java 1239 2007-07-04 09:05:29Z serg_main $
 */
public class PortletContainerException extends RuntimeException implements Serializable {
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


