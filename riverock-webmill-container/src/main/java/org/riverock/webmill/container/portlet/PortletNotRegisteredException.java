package org.riverock.webmill.container.portlet;

import java.io.Serializable;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 19:51:54
 *         $Id: PortletContainerException.java 1055 2006-11-14 17:56:15Z serg_main $
 */
public class PortletNotRegisteredException extends PortletContainerException {
    private static final long serialVersionUID = 50434672384237126L;

    public PortletNotRegisteredException(){
        super();
    }

    public PortletNotRegisteredException(String s){
        super(s);
    }

    public PortletNotRegisteredException(String s, Throwable cause){
        super(s, cause);
    }
}
