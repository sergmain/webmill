/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.portlet;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Constructor;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.UnavailableException;

import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portlet.impl.PortletConfigImpl;
import org.riverock.webmill.portlet.impl.PortletContextImpl;
import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.log4j.Logger;

/**
 * User: serg_main
 * Date: 28.01.2004
 * Time: 17:49:32
 * @author Serge Maslyukov
 * $Id$
 */
public final class PortletContainer {
    private final static Logger log = Logger.getLogger( PortletContainer.class );

    private static Map portletsMap = new HashMap();

    public final static class PortletEntry {
        private Portlet portlet = null;
        private boolean isPermanent = false;
        private int interval = 0;
        private long lastInitTime = 0;
        private PortletConfig portletConfig = null;
        private PortletType portletDefinition = null;

        private PortletEntry( PortletType portletDefinition, PortletConfig portletConfig, Portlet portlet ) {
            this.portletDefinition = portletDefinition;
            this.portletConfig = portletConfig;
            this.portlet = portlet;
        }

        private PortletEntry(UnavailableException e) {
            this.lastInitTime = System.currentTimeMillis();
            this.interval = e.getUnavailableSeconds();
            this.isPermanent = e.isPermanent();
            if (interval<=0)
                this.isPermanent = true;
        }

        public boolean getIsWait() {
            if (getIsPermanent())
                return true;

            if (getInterval() > (System.currentTimeMillis() - getLastInitTime())/1000 )
                return true;

            return false;
        }

        public PortletType getPortletDefinition() {
            return portletDefinition;
        }

        public Portlet getPortlet() {
            return portlet;
        }

        public long getLastInitTime() {
            return lastInitTime;
        }

        public boolean getIsPermanent() {
            return isPermanent;
        }

        public int getInterval() {
            return interval;
        }

        protected void setPortlet( Portlet portlet ) {
            this.portlet = portlet;
        }

        protected void setPermanent( boolean permanent ) {
            isPermanent = permanent;
        }

        protected void setInterval( int interval ) {
            this.interval = interval;
        }

        public PortletConfig getPortletConfig() {
            return portletConfig;
        }
    }

    private static Object syncObj = new Object();
    public static PortletEntry getPortletInstance( final String namePortlet ) throws PortalException {
        PortletEntry obj = (PortletEntry)portletsMap.get( namePortlet );
        if (obj == null) {
            synchronized( syncObj ) {
                return createPortlet( namePortlet );
            }
        }
        else {
            if (obj.getIsWait())
                return obj;

            synchronized( syncObj ) {
                return createPortlet( namePortlet );
            }
        }
    }

    private static PortletEntry createPortlet( final String namePortlet ) throws PortalException {
        PortletEntry temp = (PortletEntry)portletsMap.get( namePortlet );
        if (temp!= null && temp.getPortlet()!=null)
            return (PortletEntry)temp;

        // last create portlet was failed
        PortletEntry newPortlet = null;
        try {
            newPortlet = createInstance( namePortlet );
        } catch (Exception e) {
            String es = "Erorr create instance of portlet '"+namePortlet+"'";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        portletsMap.put( namePortlet, newPortlet );
        return newPortlet;
    }

    private static PortletEntry createInstance( final String namePortlet )
        throws Exception {
        PortletType portletDefinition = PortletManager.getPortletDescription( namePortlet );

        Constructor constructor = null;
        try {
            constructor = Class.forName(portletDefinition.getPortletClass()).getConstructor(null);
        }
        catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException, class: "+portletDefinition.getPortletClass(), e);
            throw e;
        }
        catch (NoClassDefFoundError e) {
            log.error("NoClassDefFoundError, class: "+portletDefinition.getPortletClass(), e);
            throw e;
        }

        if (log.isDebugEnabled())
            log.debug("#12.12.005  constructor is " + constructor);

        Portlet object = null;
        if (constructor != null)
            object = (Portlet) constructor.newInstance(null);
        else
            throw new PortalException("Error get constructor for " + portletDefinition.getPortletClass());

        PortletContext portletContext =
            new PortletContextImpl( ContextNavigator.portalServletConfig.getServletContext() );

        if ( log.isDebugEnabled() ) {
            log.debug( "Create resource bundle" );
        }
        PortletResourceBundle resourceBundle =
            PortletResourceBundleProvider.getInstance( portletDefinition );

        if ( log.isDebugEnabled() ) {
            log.debug( "Resource bundle: " + resourceBundle );
        }
        PortletConfig portletConfig =
            new PortletConfigImpl( portletContext, portletDefinition, resourceBundle );

        if ( log.isDebugEnabled() ) {
            if ( portletConfig!=null ) {
                log.debug( "PortletConfig: " + portletConfig );
                log.debug( "Resource bundle from PortletConfig: " + resourceBundle );
            }
            else {
                log.debug( "PortletConfig is null" );
            }
        }
        try {
            object.init( portletConfig );
        }
        catch( PortletException e ) {
            if ( e instanceof UnavailableException ) {
                return new PortletEntry((UnavailableException)e);
            }
            else
                throw e;
        }
        catch( RuntimeException e ) {
            String es = "Error init portlet '"+namePortlet+"'";
            log.error( es, e );
            throw new PortletException( es, e );
        }

        return new PortletEntry( portletDefinition , portletConfig, object );
    }
}
