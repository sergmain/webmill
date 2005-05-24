package org.riverock.module.web.context;

import javax.portlet.PortletContext;

import org.riverock.module.web.dispatcher.ModuleRequestDispatcher;
import org.riverock.module.web.dispatcher.PortletModuleRequestDispatcherImpl;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 15:31:00
 *         $Id$
 */
public class PortletModuleContextImpl implements ModuleContext {
    private PortletContext portletContext = null;

    public PortletModuleContextImpl(PortletContext portletContext){
        this.portletContext = portletContext;
    }


    public ModuleRequestDispatcher getRequestDispatcher(String url) {
        return new PortletModuleRequestDispatcherImpl(portletContext.getRequestDispatcher(url));
    }
}
