/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.utils;

import java.util.List;
import java.security.Principal;
import java.io.UnsupportedEncodingException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.commons.lang.CharEncoding;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class FacesTools {
    private final static Logger log = Logger.getLogger(FacesTools.class);

    public static Long getLong(UIComponent component, String name) {
        return getLong(component, name, null);
    }

    public static Long getLong(UIComponent component, String name, Long defValue) {
        List list = component.getChildren();
        if (list != null) {
            for (Object aList : list) {
                UIComponent c1 = (UIComponent) aList;
                if (c1 instanceof UIParameter) {
                    UIParameter parameter = (UIParameter) c1;
                    if (parameter.getName().equals(name)) {
                        return Long.valueOf(parameter.getValue().toString());
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
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }

    public static Object getAttribute(String name) {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest) {
            return ((HttpServletRequest) obj).getAttribute(name);
        }

        throw new IllegalStateException("request type is not HttpServletRequest and not PortletRequest. Type: " + obj.getClass().getName());
    }

    public static HttpServletRequest getRequest() {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest) {
            return (HttpServletRequest) obj;
        }

        throw new IllegalStateException("request type is not HttpServletRequest");
    }

    public static HttpServletResponse getHttpServletResponse() {
        Object obj = getFacesContext().getExternalContext().getResponse();
        if (obj instanceof HttpServletResponse) {
            return (HttpServletResponse) obj;
        }

        throw new IllegalStateException("responce type is not HttpServletResponse");
    }

    public static Principal getUserPrincipal() {
        return getFacesContext().getExternalContext().getUserPrincipal();
    }

    public static String convertParameter(String parameter) {
        if (parameter == null) {
            return null;
        }
        try {
            String s = new String(parameter.getBytes(CharEncoding.ISO_8859_1), CharEncoding.UTF_8);
            if (log.isDebugEnabled()) {
                log.debug("old parameter: " + parameter);
                log.debug("new parameter: " + s);
            }
            return s;
        }
        catch (UnsupportedEncodingException e) {
            String es = "error convert string";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }
}
