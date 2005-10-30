/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.UnavailableException;
import javax.servlet.ServletConfig;

import org.riverock.webmill.container.bean.PortletItem;
import org.riverock.webmill.container.impl.PortletConfigImpl;
import org.riverock.webmill.container.impl.PortletContextImpl;
import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.resource.PortletResourceBundle;
import org.riverock.webmill.container.resource.PortletResourceBundleProvider;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: serg_main
 * Date: 28.01.2004
 * Time: 17:49:32
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public final class PortletContainer implements Serializable {
    private static final long serialVersionUID = 50434672384237805L;

    // List with portlet.xml files which waiting for digest
    private static List<WaitDigestFile> digestWaitList = new ArrayList<WaitDigestFile>();

    //
    private static List<PortletItem> portletItems = new ArrayList<PortletItem>();

    //
    private static List<PortletContainer> portletContainers = new ArrayList<PortletContainer>();

    //
    private static PortletDefinitionProcessor portletDefinitionProcessor = new PortletDefinitionProcessorImpl();

    // in this map as key used unique name
    private Map<String, PortletEntry> portletInstanceUniqueNameMap = new HashMap<String, PortletEntry>();
    //
    private Map<String, PortletEntry> portletInstanceMap = new HashMap<String, PortletEntry>();

    private PortalInstance portalInstance = null;
    private PortletContentCache contentCache = null;

    private static class WaitDigestFile {
        private File file = null;
        private ServletConfig servletConfig = null;
        private ClassLoader classLoader = null;
        private String uniqueName = null;

        private WaitDigestFile(String uniqueName, File file, ServletConfig servletConfig, ClassLoader classLoader) {
            this.file = file;
            this.servletConfig = servletConfig;
            this.classLoader = classLoader;
            this.uniqueName = uniqueName;
        }
    }

    private static Object syncObect = new Object();
    private PortletContainer(PortalInstance portalInstance) {
        this.contentCache = new PortletContentCacheImpl();
        this.portalInstance = portalInstance;
    }

    // static methods

    public static PortletContainer getInstance(PortalInstance portalInstance) {
        final PortletContainer container = new PortletContainer(portalInstance);
        portletContainers.add( container );
        return container;
    }

    public static void registerPortletFile(String uniqueName, File portletFile, ServletConfig servletConfig, ClassLoader classLoader)
        throws PortletContainerException {

        synchronized(syncObect) {
            System.out.println("Register and prepare portlet file: " + portletFile.getName());

            digestWaitList.add(new WaitDigestFile(uniqueName, portletFile, servletConfig, classLoader));
            digestWaitedPortletFile();
        }
    }

    private static void digestWaitedPortletFile() throws PortletContainerException {
            Iterator<WaitDigestFile> iterator = digestWaitList.iterator();
            while (iterator.hasNext()) {
                WaitDigestFile waitDigestFile = iterator.next();

                List<PortletDefinition> portletList = processPortletFile(waitDigestFile.file);
                Iterator<PortletDefinition> it = portletList.iterator();
                while (it.hasNext()) {
                    PortletDefinition portletType = it.next();
                    PortletItem portletItem = new PortletItem();
                    portletItem.setPortletDefinition( portletType );
                    portletItem.setServletConfig( waitDigestFile.servletConfig );
                    portletItem.setClassLoader( waitDigestFile.classLoader );
                    portletItem.setUniqueName( waitDigestFile.uniqueName );
                    portletItems.add(portletItem);
                }
                iterator.remove();
            }
    }

    private static List<PortletDefinition> processPortletFile(File portletFile) throws PortletContainerException {

        System.out.println("Start process portlet file: " + portletFile.getName());
        List<PortletDefinition> portletList = new ArrayList<PortletDefinition>();

        if (portletFile.exists()) {
            try {

                PortletApplication portletApp = portletDefinitionProcessor.digest(portletFile);

                for (int i = 0; i < portletApp.getPortletCount(); i++) {
                    PortletDefinition portletDefinition = portletApp.getPortlet(i);
                    System.out.println("Add new portlet: " + portletDefinition.getPortletName());

                    portletList.add(portletDefinition);
                }

            }
            catch (Exception e) {
                String errorString = "Error processing portlet file " + portletFile.getName();
                throw new PortletContainerException(errorString, e);
            }
        }
        return portletList;
    }

    public static void destroy(String uniqueName) {
        System.out.println("Undeploy uniqueName: " + uniqueName);
        synchronized(syncObect) {
            Iterator<PortletContainer> it = portletContainers.iterator();
            while (it.hasNext()) {
                PortletContainer container = it.next();
                System.out.println( "Portlet container instance: " + container );

                Object object = container.portletInstanceUniqueNameMap.get( uniqueName );
                System.out.println( "Portlet entries for unique name '"+uniqueName+"': " + object );
                if (object!=null) {
                    if (object instanceof List) {
                        Iterator iterator = ((List)object).iterator();
                        while (iterator.hasNext()) {
                            PortletEntry portletEntry = (PortletEntry) iterator.next();
                            destroyPortlet( container, portletEntry, uniqueName );
                        }
                    }
                    else if ( object instanceof PortletEntry ) {
                        destroyPortlet( container, (PortletEntry)object, uniqueName );
                    }
                    else {
                        throw new IllegalStateException( "Unknown type of object: " + object.getClass().getName() );
                    }
                    container.portletInstanceUniqueNameMap.remove( uniqueName );
                }
            }
        }
    }

    private static void destroyPortlet( PortletContainer container, PortletEntry portletEntry, String uniqueName ) {
        String portletName = portletEntry.getPortletDefinition().getPortletName();
        if (portletEntry.getUniqueName()==null || !portletEntry.getUniqueName().equals(uniqueName)) {
            throw new IllegalStateException("Portlet entry have invalid unique name. " +
                "Portlet entry unique name: "+portletEntry.getUniqueName()+", real unique name: " + uniqueName);
        }
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader classLoader = portletEntry.getClassLoader();
            Thread.currentThread().setContextClassLoader( classLoader );

            portletEntry.destroy();
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }

        Iterator<PortletItem> it = portletItems.iterator();
        while (it.hasNext()) {
            PortletItem portletItem = it.next();
            if (portletItem.getPortletDefinition().getPortletName().equals( portletName )) {
                it.remove();
            }
        }

        Iterator<WaitDigestFile> iterator = digestWaitList.iterator();
        while (iterator.hasNext()) {
            WaitDigestFile waitDigestFile = iterator.next();
            if (waitDigestFile.uniqueName.equals( uniqueName )) {
                iterator.remove();
            }
        }

        container.contentCache.invalidate( portletName );
        container.portletInstanceMap.remove( portletName );
    }

    // instance methods

    public PortletEntry getPortletInstance(final String portletName) throws PortletContainerException {

        PortletEntry obj = portletInstanceMap.get(portletName);
        System.out.println("portlet name: "+portletName+", instance in map: " + obj);
        if (obj!=null && (obj.getPortlet()!=null || obj.getIsWait() ) ) {
            return obj;
        }
        synchronized (syncObect) {
            digestWaitedPortletFile();

            PortletEntry newPortlet = null;
            try {
                newPortlet = createPortletInstance(portletName);
                String s = (newPortlet!=null? ""+newPortlet.getPortlet(): "null");
                System.out.println( "Result of create new portlet entry: " + newPortlet+", portlet: " + s );
            }
            catch (Exception e) {
                String es = "Erorr create instance of portlet '" + portletName + "'";
                e.printStackTrace( System.out );
                throw new PortletContainerException(es, e);
            }
            PortletService.put( portletInstanceUniqueNameMap, newPortlet.getUniqueName(), newPortlet );
            portletInstanceMap.put( portletName, newPortlet );
            return newPortlet;
        }
    }

    private PortletEntry createPortletInstance(final String portletName) throws Exception {
        PortletItem portletItem = searchPortletItem(portletName);
        if (portletItem == null) {
            return null;
        }

        PortletDefinition portletDefinition = portletItem.getPortletDefinition();
        PortletEntry portletEntry = portletInstanceMap.get( portletDefinition.getPortletName() );

        if (portletEntry!=null) {
            throw new IllegalStateException("Portlet '"+portletDefinition.getPortletName()+"' for context unique name '"+portletItem.getUniqueName()+"' already created.");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader classLoader = portletItem.getClassLoader();

            System.out.println("oldLoader = " + oldLoader);
            System.out.println("classLoader = " + classLoader);

            Thread.currentThread().setContextClassLoader( classLoader );

            Constructor constructor = null;
            try {
                final Class<?> aClass = classLoader.loadClass(portletDefinition.getPortletClass());
                constructor = aClass.getConstructor(new Class[]{});
            }
            catch (ClassNotFoundException e) {
                throw e;
            }
            catch (NoClassDefFoundError e) {
                throw e;
            }

            Portlet object = null;
            final Object[] initargs = new Object[]{};
            if (constructor != null) {
                object = (Portlet) constructor.newInstance(initargs);
            }
            else
                throw new PortletContainerException("Error get constructor for " + portletDefinition.getPortletClass());

            PortletContext portletContext =
                new PortletContextImpl(portletItem.getServletConfig().getServletContext(), portalInstance.getPortalName(), portalInstance.getPortalMajorVersion(), portalInstance.getPortalMinorVersion());

            PortletResourceBundle resourceBundle =
                PortletResourceBundleProvider.getInstance( portletDefinition, portalInstance.getSupportedLocales() );

            PortletConfig portletConfig = new PortletConfigImpl(portletContext, portletDefinition, resourceBundle);

            try {
                object.init(portletConfig);
            }
            catch (PortletException e) {
                if (e instanceof UnavailableException) {
                    return new PortletEntry((UnavailableException) e);
                }
                else
                    throw e;
            }
            catch (RuntimeException e) {
                String es = "Error init portlet '" + portletName + "'";
                throw new PortletException(es, e);
            }

            final PortletEntry entry = new PortletEntry(portletDefinition, portletConfig, object, portletItem.getServletConfig(), portletItem.getClassLoader(), portletItem.getUniqueName() );
            portletInstanceMap.put( portletDefinition.getPortletName(), entry);
            return entry;
        }
        catch (Exception e) {
            String es = "Error create instance of portlet "+ portletName + ".";
            throw new PortletContainerException(es, e);
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    private PortletItem searchPortletItem(String portletName) {
        if (portletName == null) {
            return null;
        }

        Iterator<PortletItem> it = portletItems.iterator();
        while (it.hasNext()) {
            PortletItem portletItem = it.next();
            if (portletItem.getPortletDefinition().getPortletName().equals(portletName)) {
                return portletItem;
            }
        }

        return null;
    }

    public PortletContentCache getContentCache() {
        return contentCache;
    }

    public PortalInstance getPortalInstance() {
        return portalInstance;
    }

}
