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

import org.riverock.webmill.container.bean.PortletWebApplication;
import org.riverock.webmill.container.impl.PortletConfigImpl;
import org.riverock.webmill.container.impl.PortletContextImpl;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.resource.PortletResourceBundle;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.UnavailableException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: serg_main
 * Date: 28.01.2004
 * Time: 17:49:32
 *
 * @author Serge Maslyukov
 *         $Id: PortletContainer.java 901 2006-08-16 14:21:42Z serg_main $
 */
public final class PortletContainer implements Serializable {
    private static final long serialVersionUID = 50434672384237805L;

    public static final String PORTLET_ID_NAME_SEPARATOR = "::";

    // map with prepare web application, but portlet not instantiated
    // as a key used full portlet name (portlet-appl-id :: portlet-id :: portlet-name)
    private Map<String, PortletWebApplication> portletItems = new ConcurrentHashMap<String, PortletWebApplication>();

    // in this map as a key used unique name of web-application
    // unique name initialized in PortletRegisterServlet.init() method
    private PortletEntryListByUniqueNameMap portletInstanceUniqueNameMap = new PortletEntryListByUniqueNameMap();

    // as key used full portlet name
    private Map<String, PortletEntry> portletInstanceMap = new ConcurrentHashMap<String, PortletEntry>();

    // in this map as a key used unique name of web-application
    // unique name initialized in PortletRegisterServlet.init() method
    private Map<String, PortletContext> portletContextMap = new HashMap<String, PortletContext>();

    private PortalInstanceBase portalInstanceBase = null;
    private PortletContentCache contentCache = null;
    private String portalPath = null;
    boolean isNewPortlet = false;

    private final static Object syncObect = new Object();

    PortletContainer(PortalInstanceBase portalInstanceBase, String portalPath) {
        this.contentCache = new PortletContentCacheImpl();
        this.setPortalInstanceBase(portalInstanceBase);
        this.portalPath = portalPath;
    }

    PortletContainer( String portalPath) {
        this.contentCache = new PortletContentCacheImpl();
        this.portalPath = portalPath;
    }

    public PortletEntry getPortletInstance(final String queryPortletName) {
        if (isNewPortlet) {
            registerNewPortlet();
        }
        if (queryPortletName==null) {
            String es = "Parameter 'portletName' cant be null.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        String portletName = queryPortletName;
        if ( portletName.indexOf( PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PORTLET_ID_NAME_SEPARATOR + queryPortletName;
        }

        PortletEntry obj = portletInstanceMap.get( portletName );
        if (obj!=null) {
            if( obj.getPortlet()!=null || obj.getIsWait() || obj.getIsPermanent()) {
                return obj;
            }
        }

        synchronized (syncObect) {
            PortletContainerFactory.digestWaitedPortletFile();

            PortletEntry newPortlet;
            try {
                newPortlet = createPortletInstance(portletName);
            }
            catch (PortletContainerException e) {
                throw e;
            }
            catch (Throwable e) {
                String es = "Erorr create instance of portlet '" + portletName + "'";
                e.printStackTrace( System.out );
                throw new PortletContainerException(es, e);
            }
            if (newPortlet==null) {
                String es = "Erorr create instance of portlet '" + portletName + "'. Result is null";
                System.out.println( es );
                throw new PortletContainerException(es);
            }
            if (newPortlet.getUniqueName()==null) {
                String es = "Erorr create instance of portlet '" + portletName + "'. UniqueName is null";
                System.out.println( es );
                throw new PortletContainerException(es);
            }

            addPortletEntry( portletInstanceUniqueNameMap, newPortlet.getUniqueName(), newPortlet );
            portletInstanceMap.put( portletName, newPortlet );
            return newPortlet;
        }
    }

    public void destroyContextForName(String uniqueName) {
        System.out.println("Undeploy all for unique name: " + uniqueName);
        synchronized(syncObect) {
            Iterator<Map.Entry<String, PortletWebApplication>> iterator = portletItems.entrySet().iterator();
            while (iterator.hasNext()) {
                PortletWebApplication portletWebApplication = iterator.next().getValue();

                if ( portletWebApplication.getUniqueName().equals( uniqueName ) ) {
                    System.out.println( "  Remove portlet entry: " + portletWebApplication.getPortletDefinition().getFullPortletName() );
                    iterator.remove();
                }
            }

            List<PortletEntry> portletEntries = portletInstanceUniqueNameMap.get(uniqueName);
//            System.out.println("Portlet entries for unique name '" + uniqueName + "': " + portletEntries);
            if (portletEntries != null) {
                for (PortletEntry portletEntry : portletEntries) {
                    try {
                        if (portalInstanceBase!=null) {
                            portalInstanceBase.destroyPortlet(portletEntry.getPortletDefinition().getFullPortletName());
                        }
                        destroyPortlet(portletEntry, uniqueName);
                    }
                    catch (Throwable th) {
                        th.printStackTrace(System.out);
                    }
                }
                
                portletInstanceUniqueNameMap.remove(uniqueName);
                portletContextMap.remove(uniqueName);
            }
        }
    }

    public void unregisterPortalInstance() {
        this.setPortalInstanceBase(null);
    }

/*
    public boolean destroyContainer() {
        if (!portletItems.isEmpty() )
        this.portletInstanceMap=null;
        this.portletContextMap
        this.portalInstanceBase
        this.contentCache
        this.portalPath
    }
*/

    private void destroyPortlet( PortletEntry portletEntry, String uniqueName ) {

        if (portletEntry.getUniqueName()==null || !portletEntry.getUniqueName().equals(uniqueName)) {
            throw new IllegalStateException("Portlet entry have invalid unique name. " +
                "Portlet entry unique name: "+portletEntry.getUniqueName()+", real unique name: " + uniqueName);
        }

        contentCache.invalidate( portletEntry.getPortletDefinition().getFullPortletName() );
        portletInstanceMap.remove( portletEntry.getPortletDefinition().getFullPortletName() );

        if (portletEntry.getClassLoader()!=null) {
//            System.out.println("Undeploy portlet context in classLoader: " + portletEntry.getClassLoader() +"\nhashCode: " + portletEntry.getClassLoader().hashCode() );
            ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
            try {
                final ClassLoader classLoader = portletEntry.getClassLoader();
                Thread.currentThread().setContextClassLoader( classLoader );

                portletEntry.destroy();
            }
            finally {
                Thread.currentThread().setContextClassLoader( oldLoader );
            }
        }
        else {
            System.out.println("ClassLoader is null, try to undeploy using portlet's class loader.");
            try {
                ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                try {
                    final ClassLoader classLoader = portletEntry.getPortlet().getClass().getClassLoader();
                    Thread.currentThread().setContextClassLoader( classLoader );

                    portletEntry.destroy();
                }
                finally {
                    Thread.currentThread().setContextClassLoader( oldLoader );
                }
            }
            catch (Throwable th) {
                System.out.println("Error undeploy using portlet's class loader");
                th.printStackTrace();  
            }
        }
    }

    void addNewWebApplication(Map<String, PortletWebApplication> map) {
        synchronized(syncObect) {
            portletItems.putAll(map);
            isNewPortlet=true;
        }
    }

    private void registerNewPortlet() {
        synchronized(syncObect) {
            for (PortletWebApplication portletWebApplication : portletItems.values()) {
                if (!portletWebApplication.isRegistered()) {
                    portalInstanceBase.registerPortlet(portletWebApplication.getPortletDefinition().getFullPortletName());
                    portletWebApplication.setRegistered(true);
                }
            }
            isNewPortlet=false;
        }
    }

    private PortletEntry createPortletInstance(final String portletName) {
        PortletWebApplication portletWebApplication = searchPortletItem( portletName );
        if (portletWebApplication == null) {
            String es = "Portlet '"+ portletName + "' not registered.";
            System.out.println( es );
            throw new PortletNotRegisteredException(es);
        }

        PortletDefinition portletDefinition = portletWebApplication.getPortletDefinition();
        if (portletDefinition == null) {
            String es = "Portlet definition for portlet '"+ portletName + "' not found.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        PortletEntry portletEntry = portletInstanceMap.get( portletName );


        if (portletEntry!=null && portletEntry.getExceptionMessage()==null) {
            throw new IllegalStateException(
                "Portlet '"+portletDefinition.getPortletName()+"' " +
                "for context unique name '"+portletWebApplication.getUniqueName()+"' already created."
            );
        }
        else if (portletEntry!=null && portletEntry.getIsPermanent()) {
            return portletEntry;
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader classLoader = portletWebApplication.getClassLoader();
            Thread.currentThread().setContextClassLoader( classLoader );

            PortletContext portletContext = getPortletContext( portletWebApplication, portalInstanceBase.getPortalName() );
            if (portletWebApplication.getUniqueName()==null) {
                String es = "Erorr create instance of portlet '" + portletName + "'. UniqueName in portletWebApplication is null";
                System.out.println( es );
                throw new PortletContainerException(es);
            }
            PortletResourceBundle resourceBundle = PortletResourceBundle.getInstance( portletDefinition, classLoader );
            PortletConfig portletConfig = new PortletConfigImpl(portletContext, portletDefinition, resourceBundle);
            
            // forcce init preference validator
            if (portletDefinition.getPreferences()!=null) {
                portletDefinition.getPreferences().getPreferencesValidator();
            }

            Portlet object;
            Constructor constructor;
            try {
                final Class<?> aClass = classLoader.loadClass( portletDefinition.getPortletClass());
                constructor = aClass.getConstructor();
            }
            catch (ClassNotFoundException e) {
                throw e;
            }
            catch (NoClassDefFoundError e) {
                throw e;
            }

            final Object[] initargs = new Object[]{};
            if (constructor != null) {
                object = (Portlet) constructor.newInstance(initargs);
            }
            else {
                throw new PortletContainerException("Error get constructor for " + portletDefinition.getPortletClass());
            }

            try {
                object.init(portletConfig);
            }
            catch (UnavailableException e) {
                if (e.isPermanent()) {
                    // Todo
/*
If a permanent unavailability is indicated by the UnavailableException, the portlet
container must remove the portlet from service immediately, call the portlet’s destroyContextForName
method, and release the portlet object.xviii A portlet that throws a permanent
UnavailableException must be considered unavailable until the portlet application
containing the portlet is restarted.
*/
                    destroyContextForName( portletName );
                }
                return new PortletEntry(portletDefinition, e, portletWebApplication.getUniqueName());
            }

            return new PortletEntry(
                portletDefinition, portletConfig, object,
                portletWebApplication.getServletConfig(),
                portletWebApplication.getClassLoader(),
                portletWebApplication.getUniqueName(),
                portalPath
            );
        }
        catch (Throwable e) {
            e.printStackTrace( System.out );
            UnavailableException ue = new UnavailableException(
                "Portlet '"+portletName+"' permanent unavailable. Exception: " + e.toString(), -1);
            return new PortletEntry(portletDefinition, ue, portletWebApplication.getUniqueName());
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    private PortletContext getPortletContext(PortletWebApplication portletWebApplication, String portalName) {
        PortletContext portletContext = portletContextMap.get( portletWebApplication.getUniqueName() );
        if (portletContext!=null) {
            return portletContext;
        }
        synchronized( syncObect ) {
            portletContext = portletContextMap.get( portletWebApplication.getUniqueName() );
            if (portletContext!=null) {
                return portletContext;
            }

            portletContext =  new PortletContextImpl(
                portletWebApplication.getServletConfig().getServletContext(), portalName
            );

            portletContextMap.put(portletWebApplication.getUniqueName(), portletContext );
            return portletContext;
        }
    }

    public PortletWebApplication searchPortletItem(final String queryPortletName) {
        if (queryPortletName == null) {
            return null;
        }

        String portletName = queryPortletName;
        if ( portletName.indexOf( PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PORTLET_ID_NAME_SEPARATOR + queryPortletName;
        }

        return portletItems.get( portletName );
    }

    private static void addPortletEntry( PortletEntryListByUniqueNameMap map, final String portletName, final PortletEntry value ) {
        PortletEntryList list = map.get( portletName );
        if (list==null) {
            PortletEntryList portletEntries = new PortletEntryList();
            portletEntries.add( value );
            map.put( portletName, portletEntries );
        }
        else {
            list.add( value );
        }
    }

    public PortletContentCache getContentCache() {
        return contentCache;
    }

    public String getPortalPath() {
        return portalPath;
    }

    void setPortalInstanceBase(PortalInstanceBase portalInstanceBase) {
        this.portalInstanceBase = portalInstanceBase;
    }

    PortalInstanceBase getPortalInstanceBase() {
        return portalInstanceBase;
    }
}
