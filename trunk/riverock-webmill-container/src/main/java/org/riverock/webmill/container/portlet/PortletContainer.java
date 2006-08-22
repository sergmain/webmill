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

import javax.portlet.*;
import javax.servlet.ServletConfig;

import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.container.bean.PortletWebApplication;
import org.riverock.webmill.container.impl.PortletConfigImpl;
import org.riverock.webmill.container.impl.PortletContextImpl;
import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.bean.Preferences;
import org.riverock.webmill.container.resource.PortletResourceBundle;

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

    // List with portlet.xml files which waiting for digest
    private static List<WaitDigestFile> digestWaitList = new ArrayList<WaitDigestFile>();

    //
    private Map<String, PortletWebApplication> portletItems = new HashMap<String, PortletWebApplication>();

    // Masp of portlet containers, key is path to dir, where placed all web application
    private static Map<String, PortletContainer> portletContainers = new HashMap<String, PortletContainer>();

    //
    private static PortletDefinitionProcessor portletDefinitionProcessor = new PortletDefinitionProcessorImpl();

    // in this map as key used unique name of web-application
    // unique name initialized in PortletRegisterServlet.init() method
    private Map<String, List<PortletEntry>> portletInstanceUniqueNameMap = new HashMap<String, List<PortletEntry>>();

    // as key used full portlet name
    private Map<String, PortletEntry> portletInstanceMap = new HashMap<String, PortletEntry>();

    // in this map as key used unique name of web-application
    // unique name initialized in PortletRegisterServlet.init() method
    private Map<String, PortletContext> portletContextMap = new HashMap<String, PortletContext>();

    private PortalInstance portalInstance = null;
    private PortletContentCache contentCache = null;
    private String portalPath = null;
    private boolean isNewPortlet = false;

    private final static Object syncObect = new Object();

    private static class WaitDigestFile {
        private File file = null;
        private ServletConfig servletConfig = null;
        private ClassLoader classLoader = null;
        private String uniqueName = null;
        private String portalPath = null;

        private WaitDigestFile(String uniqueName, File file, ServletConfig servletConfig, ClassLoader classLoader, String portalPath) {
            this.file = file;
            this.servletConfig = servletConfig;
            this.classLoader = classLoader;
            this.uniqueName = uniqueName;
            this.portalPath = portalPath;
        }
    }

    private PortletContainer(PortalInstance portalInstance, String portalPath) {
        this.contentCache = new PortletContentCacheImpl();
        this.portalInstance = portalInstance;
        this.portalPath = portalPath;
    }

    private PortletContainer( String portalPath) {
        this.contentCache = new PortletContentCacheImpl();
        this.portalPath = portalPath;
    }

    // static methods

    public static PortletContainer getInstance(PortalInstance portalInstance, String portalPath) {

        PortletContainer container = portletContainers.get(portalPath);
        if (container!=null) {
            // portlet was registered before portal
            if (container.portalInstance==null) {
                container.portalInstance=portalInstance;
            }
            return container;
        }

        container = new PortletContainer(portalInstance, portalPath);
        portletContainers.put(portalPath, container );
        return container;
    }

    public static void registerPortletFile(
        String uniqueName, File portletFile, ServletConfig servletConfig, ClassLoader classLoader, String portalPath)
        throws PortletContainerException {

        synchronized(syncObect) {
            System.out.println("Register and prepare portlet file: " + portletFile.getName());

            digestWaitList.add(new WaitDigestFile(uniqueName, portletFile, servletConfig, classLoader, portalPath));
            digestWaitedPortletFile();
        }
    }

    public static void destroy(String uniqueName, String portalPath) {
        PortletContainer container = portletContainers.get(portalPath);
        if (container==null) {
            System.out.println("Container for path " + portalPath + " not found");
            return;
        }
        System.out.println("Portlet container instance: " + container);
        System.out.println("Undeploy uniqueName: " + uniqueName);
        synchronized(syncObect) {
            Iterator<Map.Entry<String,PortletWebApplication>> iterator = container.portletItems.entrySet().iterator();
            while (iterator.hasNext()) {
                PortletWebApplication portletWebApplication = iterator.next().getValue();

                if ( portletWebApplication.getUniqueName().equals( uniqueName ) ) {
                    System.out.println( "Remove portlet entry: " + portletWebApplication.getPortletDefinition().getFullPortletName() );
                    iterator.remove();
                }
            }

            Iterator<WaitDigestFile> iter = digestWaitList.iterator();
            while (iter.hasNext()) {
                WaitDigestFile waitDigestFile = iter.next();
                if (waitDigestFile.uniqueName.equals( uniqueName )) {
                    iter.remove();
                }
            }

            List<PortletEntry> portletEntries = container.portletInstanceUniqueNameMap.get(uniqueName);
            System.out.println("Portlet entries for unique name '" + uniqueName + "': " + portletEntries);
            if (portletEntries != null) {
                for (PortletEntry portletEntry : portletEntries) {
                    try {
                        destroyPortlet(container, portletEntry, uniqueName);
                    }
                    catch (Throwable th) {
                        th.printStackTrace(System.out);
                    }
                }
                container.portletInstanceUniqueNameMap.remove(uniqueName);
                container.portletContextMap.remove(uniqueName);
            }
        }
    }

    private static void digestWaitedPortletFile() throws PortletContainerException {
            Iterator<WaitDigestFile> iterator = digestWaitList.iterator();
            while (iterator.hasNext()) {
                WaitDigestFile waitDigestFile = iterator.next();

                PortletContainer container = portletContainers.get(waitDigestFile.portalPath);
                if (container==null) {
                    container = new PortletContainer(waitDigestFile.portalPath);
                    portletContainers.put(waitDigestFile.portalPath, container);
                }

                List<PortletDefinition> portletList = processPortletFile(waitDigestFile.file);
                for (PortletDefinition portletType : portletList) {
                    PortletWebApplication portletWebApplication = new PortletWebApplication();
                    portletWebApplication.setPortletDefinition(portletType);
                    portletWebApplication.setServletConfig(waitDigestFile.servletConfig);
                    portletWebApplication.setClassLoader(waitDigestFile.classLoader);
                    portletWebApplication.setUniqueName(waitDigestFile.uniqueName);

                    container.portletItems.put(portletType.getFullPortletName(), portletWebApplication);
                    container.isNewPortlet=true;
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
                    portletDefinition.setApplicationName(
                        StringUtils.isBlank(portletApp.getId()) ?"":portletApp.getId()
                    );
                    if (portletDefinition.getPreferences()==null) {
                        portletDefinition.setPreferences( new Preferences() );
                    }

                    System.out.println("Add new portlet: " + portletDefinition.getFullPortletName());
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


    private static void destroyPortlet( PortletContainer container, PortletEntry portletEntry, String uniqueName ) {

        if (portletEntry.getUniqueName()==null || !portletEntry.getUniqueName().equals(uniqueName)) {
            throw new IllegalStateException("Portlet entry have invalid unique name. " +
                "Portlet entry unique name: "+portletEntry.getUniqueName()+", real unique name: " + uniqueName);
        }

        System.out.println("Undeploy portlet context in classLoader: " + portletEntry.getClassLoader() +"\nhashCode: " + portletEntry.getClassLoader().hashCode() );


        container.contentCache.invalidate( portletEntry.getPortletDefinition().getFullPortletName() );
        container.portletInstanceMap.remove( portletEntry.getPortletDefinition().getFullPortletName() );

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

    // instance methods

    public PortletEntry getPortletInstance(final String queryPortletName) throws PortletContainerException {
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
            if( obj.getPortlet()!=null || obj.getIsWait() ) {
                return obj;
            }
        }
        synchronized (syncObect) {
            digestWaitedPortletFile();

            PortletEntry newPortlet;
            try {
                newPortlet = createPortletInstance(portletName);
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

            addPortletEntry( portletInstanceUniqueNameMap, newPortlet.getUniqueName(), newPortlet );
            portletInstanceMap.put( portletName, newPortlet );
            return newPortlet;
        }
    }

    private void registerNewPortlet() {
        synchronized(syncObect) {
            for (PortletWebApplication portletWebApplication : portletItems.values()) {
                if (!portletWebApplication.isRegistered()) {
                    portalInstance.registerPortlet(portletWebApplication.getPortletDefinition().getFullPortletName());
                    portletWebApplication.setRegistered(true);
                }
            }
            isNewPortlet=false;
        }
    }

    private PortletEntry createPortletInstance(final String portletName) throws Exception {
        PortletWebApplication portletWebApplication = searchPortletItem( portletName );
        if (portletWebApplication == null) {
            String es = "Portlet '"+ portletName + "' not registered.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        PortletDefinition portletDefinition = portletWebApplication.getPortletDefinition();
        if (portletDefinition == null) {
            String es = "Portlet definition for portlet '"+ portletName + "' not found.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        PortletEntry portletEntry = portletInstanceMap.get( portletName );

        if (portletEntry!=null) {
            throw new IllegalStateException(
                "Portlet '"+portletDefinition.getPortletName()+"' " +
                "for context unique name '"+portletWebApplication.getUniqueName()+"' already created."
            );
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader classLoader = portletWebApplication.getClassLoader();
            Thread.currentThread().setContextClassLoader( classLoader );

            PortletContext portletContext = getPortletContext( portletWebApplication, portalInstance.getPortalName() );
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
            catch (Throwable e) {
                if (e instanceof UnavailableException) {
                    if (((UnavailableException)e).isPermanent()) {
                        // Todo
/*
If a permanent unavailability is indicated by the UnavailableException, the portlet
container must remove the portlet from service immediately, call the portletís destroy
method, and release the portlet object.xviii A portlet that throws a permanent
UnavailableException must be considered unavailable until the portlet application
containing the portlet is restarted.
*/
                        destroy( portletName, portalPath );
                    }
                    return new PortletEntry(portletDefinition, (UnavailableException) e);
                }
                UnavailableException ue = new UnavailableException(
                    "portlet '"+portletName+"' permanent unavailable. Exception: " + e.toString(), -1);

                return new PortletEntry(portletDefinition, ue);
            }

            return new PortletEntry(
                portletDefinition, portletConfig, object,
                portletWebApplication.getServletConfig(),
                portletWebApplication.getClassLoader(),
                portletWebApplication.getUniqueName(),
                portalPath
            );
        }
        catch (Exception e) {
            String es = "Error create instance of portlet "+ portletName + ".";
            e.printStackTrace( System.out );
            throw new PortletContainerException(es, e);
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

    private static void addPortletEntry( Map<String, List<PortletEntry>> map, final String key, final PortletEntry value ) {
        List<PortletEntry> list = map.get( key );
        if (list==null) {
            List<PortletEntry> portletEntries = new ArrayList<PortletEntry>();
            portletEntries.add( value );
            map.put( key, portletEntries );
        }
        else {
            list.add( value );
        }
    }

    public PortletContentCache getContentCache() {
        return contentCache;
    }

    public PortalInstance getPortalInstance() {
        return portalInstance;
    }

}
