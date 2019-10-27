/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.jsf;

import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergei Maslyukov
 *         Date: 30.08.2006
 *         Time: 17:40:19
 *         <p/>
 *         $Id$
 */
public class FacesTools {

    public static boolean isUserInRole(String role) {
         return getFacesContext().getExternalContext().isUserInRole(role);
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static Object getAttribute(String name) {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof PortletRequest) {
            return ((PortletRequest)obj).getAttribute( name );
        }
        else if (obj instanceof HttpServletRequest) {
            return ((HttpServletRequest)obj).getAttribute( name );
        }

        throw new IllegalStateException("request type is not HttpServletRequest and not PortletRequest. Type: " + obj.getClass().getName() );
    }

    public static PortletRequest getPortletRequest() {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof PortletRequest) {
            return (PortletRequest)obj;
        }

        throw new IllegalStateException("request type is not PortletRequest");
    }
}
