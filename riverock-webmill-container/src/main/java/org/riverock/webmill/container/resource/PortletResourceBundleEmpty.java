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
package org.riverock.webmill.container.resource;

import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Collections;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:24:14
 * $Id: PortletResourceBundleEmpty.java 1055 2006-11-14 17:56:15Z serg_main $
 */
public class PortletResourceBundleEmpty extends ResourceBundle {

    public PortletResourceBundleEmpty() {
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration( new ArrayList<String>() );
    }

    protected Object handleGetObject( final String key ) {
        return null;
    }
}
