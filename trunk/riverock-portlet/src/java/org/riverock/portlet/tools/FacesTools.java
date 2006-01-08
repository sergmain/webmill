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

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:32:20
 *         $Id$
 */
public class FacesTools {

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

    public static final HttpServletRequest getRequest() {
        return ( HttpServletRequest ) getFacesContext().getExternalContext().getRequest();
    }

    public static final HttpServletResponse getResponse() {
        return ( HttpServletResponse ) getFacesContext().getExternalContext().getResponse();
    }

    public static final Principal getUserPrincipal() {
        return getFacesContext().getExternalContext().getUserPrincipal();
    }

}
