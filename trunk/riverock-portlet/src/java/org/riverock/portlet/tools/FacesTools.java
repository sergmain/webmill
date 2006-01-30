package org.riverock.portlet.tools;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.portlet.PortletRequest;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:32:20
 *         $Id$
 */
public class FacesTools {

    public static PortalDaoProvider getPortalDaoProvider() {
        return (PortalDaoProvider)FacesTools.getAttribute( ContainerConstants.PORTAL_PORTAL_DATA_MANAGER );
    }

    public static Long getLong( UIComponent component, String name ) {
        return getLong( component, name, null);
    }

    public static Long getLong( UIComponent component, String name, Long defValue ) {
        List list = component.getChildren();
        if( list != null ) {
            Iterator it = list.iterator();
            while( it.hasNext() ) {
                UIComponent c1 = ( UIComponent ) it.next();
                if( c1 instanceof UIParameter ) {
                    UIParameter parameter = ( UIParameter ) c1;
                    if( parameter.getName().equals( name ) ) {
                        return new Long( parameter.getValue().toString() );
                    }
                }
            }
        }
        return defValue;
    }

    public static final FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static final Application getApplication() {
        return getFacesContext().getApplication();
    }

    public static final HttpSession getSession() {
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

    public static final HttpServletRequest getRequest() {
        Object obj = getFacesContext().getExternalContext().getRequest();
        if (obj instanceof HttpServletRequest) {
            return (HttpServletRequest)obj;
        }

        throw new IllegalStateException("request type is not HttpServletRequest");
    }

    public static final HttpServletResponse getHttpServletResponse() {
        Object obj = getFacesContext().getExternalContext().getResponse();
        if (obj instanceof HttpServletResponse) {
            return (HttpServletResponse)obj;
        }

        throw new IllegalStateException("responce type is not HttpServletResponse");
    }

    public static final Principal getUserPrincipal() {
        return getFacesContext().getExternalContext().getUserPrincipal();
    }

}
