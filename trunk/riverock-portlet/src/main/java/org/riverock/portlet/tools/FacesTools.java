/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.tools;

import java.security.Principal;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:32:20
 *         $Id$
 */
public class FacesTools {
    public static PortalDaoProvider getPortalDaoProvider() {
        return (PortalDaoProvider)FacesTools.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
    }

    public static Long getLong( UIComponent component, String name ) {
        return getLong( component, name, null);
    }

    public static Long getLong( UIComponent component, String name, Long defValue ) {
        List list = component.getChildren();
        if( list != null ) {
            for (Object aList : list) {
                UIComponent c1 = (UIComponent) aList;
                if (c1 instanceof UIParameter) {
                    UIParameter parameter = (UIParameter) c1;
                    if (parameter.getName().equals(name)) {
                        return new Long(parameter.getValue().toString());
                    }
                }
            }
        }
        return defValue;
    }

    public static boolean isUserInRole(String role) {
        return getFacesContext().getExternalContext().isUserInRole(role);
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static Application getApplication() {
        return getFacesContext().getApplication();
    }

    public static HttpSession getSession() {
        return ( HttpSession ) getFacesContext().getExternalContext().getSession( false );
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

    public static HttpServletRequest getRequest() {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest) {
            return (HttpServletRequest)obj;
        }

        throw new IllegalStateException("request type is not HttpServletRequest");
    }

    public static HttpServletResponse getHttpServletResponse() {
        Object obj = getFacesContext().getExternalContext().getResponse();
        if (obj instanceof HttpServletResponse) {
            return (HttpServletResponse)obj;
        }

        throw new IllegalStateException("responce type is not HttpServletResponse");
    }

    public static Principal getUserPrincipal() {
        return getFacesContext().getExternalContext().getUserPrincipal();
    }

}
