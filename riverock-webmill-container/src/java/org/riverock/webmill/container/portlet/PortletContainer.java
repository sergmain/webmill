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
import org.riverock.webmill.container.portlet.bean.PortletPreferencesImpl;
import org.riverock.webmill.container.resource.PortletResourceBundle;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

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

    public static final String PORTLET_ID_NAME_SEPARATOR = "::";

    private static final boolean DEBUG = true;

    // List with portlet.xml files which waiting for digest
    private static List<WaitDigestFile> digestWaitList = new ArrayList<WaitDigestFile>();

    //
    private static List<PortletItem> portletItems = new ArrayList<PortletItem>();

    //
    private static List<PortletContainer> portletContainers = new ArrayList<PortletContainer>();

    //
    private static PortletDefinitionProcessor portletDefinitionProcessor = new PortletDefinitionProcessorImpl();

    // in this map as key used unique name
    private Map<String, List<PortletEntry>> portletInstanceUniqueNameMap = new HashMap<String, List<PortletEntry>>();
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
                    portletDefinition.setApplicationName(
                        PortletService.isEmpty( portletApp.getId() )?"":portletApp.getId() 
                    );
                    if (portletDefinition.getPortletPreferences()==null) {
                        portletDefinition.setPortletPreferences( new PortletPreferencesImpl() );
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

    public static void destroy(String uniqueName) {
        System.out.println("Undeploy uniqueName: " + uniqueName);
        synchronized(syncObect) {
            Iterator<PortletItem> iterator = portletItems.iterator();
            while (iterator.hasNext()) {
                PortletItem portletItem = iterator.next();

                if ( portletItem.getUniqueName().equals( uniqueName ) ) {
                    System.out.println( "Remove portlet entry: " + portletItem.getPortletDefinition().getFullPortletName() );
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

            Iterator<PortletContainer> it = portletContainers.iterator();
            while (it.hasNext()) {
                PortletContainer container = it.next();
                System.out.println( "Portlet container instance: " + container );

                List<PortletEntry> portletEntries = container.portletInstanceUniqueNameMap.get( uniqueName );
                System.out.println( "Portlet entries for unique name '"+uniqueName+"': " + portletEntries );
                if (portletEntries!=null) {
                    Iterator<PortletEntry> listIterator = portletEntries.iterator();
                    while (listIterator.hasNext()) {
                        PortletEntry portletEntry = listIterator.next();
			try {
                        	destroyPortlet( container, portletEntry, uniqueName );
			}
			catch(Throwable th){
				th.printStackTrace( System.out);
			}
                    }
                    container.portletInstanceUniqueNameMap.remove( uniqueName );
                }
            }
        }
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
        if (queryPortletName==null) {
            String es = "Parameter 'portletName' cant be null.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }


//        System.out.println("get portlet instance for name: " + queryPortletName );


        String portletName = queryPortletName;
        if ( portletName.indexOf( PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PORTLET_ID_NAME_SEPARATOR + queryPortletName;
        }

        PortletEntry obj = portletInstanceMap.get( portletName );
//        System.out.println("portlet name: "+portletName+", instance in map: " + obj);
        if (obj!=null) {
            boolean isNotUrl = !PortletService.getBooleanParam(obj.getPortletDefinition(), ContainerConstants.is_url, Boolean.FALSE);
            if (isNotUrl) {
                if( obj.getPortlet()!=null || obj.getIsWait() ) {
                    return obj;
                }
            }
            else {
                return obj;
            }
        }
        synchronized (syncObect) {
            digestWaitedPortletFile();

            PortletEntry newPortlet = null;
            try {
                newPortlet = createPortletInstance(portletName);
                String s = (newPortlet!=null? ""+newPortlet.getPortlet(): "null");
//                System.out.println( "Result of create new portlet entry: " + portletName + ", portlet: " + s );
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

    private PortletEntry createPortletInstance(final String portletName) throws Exception {
        PortletItem portletItem = searchPortletItem( portletName );
        if (portletItem == null) {
            String es = "Portlet '"+ portletName + "' not registered.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        PortletDefinition portletDefinition = portletItem.getPortletDefinition();
        if (portletDefinition == null) {
            String es = "Portlet definition for portlet '"+ portletName + "' not found.";
            System.out.println( es );
            throw new PortletContainerException(es);
        }

        PortletEntry portletEntry = portletInstanceMap.get( portletName );

        if (portletEntry!=null) {
            throw new IllegalStateException("Portlet '"+portletDefinition.getPortletName()+"' for context unique name '"+portletItem.getUniqueName()+"' already created.");
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            final ClassLoader classLoader = portletItem.getClassLoader();

            boolean isNotUrl = !PortletService.getBooleanParam(portletDefinition, ContainerConstants.is_url, Boolean.FALSE);

//            System.out.println("oldLoader\n" + oldLoader+"\nhashCode: " + oldLoader.hashCode() );
//            System.out.println("classLoader\n" + classLoader+"\nhashCode: " + classLoader.hashCode() );
//            System.out.println("isNotUrl portlet: " + isNotUrl );


            Thread.currentThread().setContextClassLoader( classLoader );

            Portlet object = null;
            PortletConfig portletConfig = null;

	// Todo. instance of PortletContext is one per 'war'? I.e. same for all portlets in application?
            PortletContext portletContext =
                new PortletContextImpl(portletItem.getServletConfig().getServletContext(), portalInstance.getPortalName(), portalInstance.getPortalMajorVersion(), portalInstance.getPortalMinorVersion());

            PortletResourceBundle resourceBundle =
                PortletResourceBundle.getInstance( portletDefinition, classLoader );

            portletConfig = new PortletConfigImpl(portletContext, portletDefinition, resourceBundle);

            if ( isNotUrl ) {
                Constructor constructor = null;
                try {
                    final Class<?> aClass = classLoader.loadClass( portletDefinition.getPortletClass());
                    constructor = aClass.getConstructor(new Class[]{});
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
                    	return new PortletEntry(portletDefinition, (UnavailableException) e);
                    }
			UnavailableException ue = new UnavailableException( 
				"portlet '"+portletName+"' permanent unavailable. Exception: " + e.toString(), -1);

		  	return new PortletEntry(portletDefinition, ue);
                }
            }
            else {
//        	System.out.println("Portlet is url, resourceBundle: " + resourceBundle );
            }

            final PortletEntry entry = new PortletEntry(portletDefinition, portletConfig, object, portletItem.getServletConfig(), portletItem.getClassLoader(), portletItem.getUniqueName() );
            return entry;
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

    private PortletItem searchPortletItem(final String queryPortletName) {
        if (queryPortletName == null) {
            return null;
        }

        String portletName = queryPortletName;
        if ( portletName.indexOf( PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PORTLET_ID_NAME_SEPARATOR + queryPortletName;
        }

/*
        if (DEBUG) {
            Iterator<PortletItem> it = portletItems.iterator();
            while (it.hasNext()) {
                PortletItem portletItem = it.next();
                System.out.println("portletName = " + portletItem.getPortletDefinition().getPortletName()+ ", portlet context: " + portletItem.getUniqueName() );
            }
        }
*/
        Iterator<PortletItem> it = portletItems.iterator();
        while (it.hasNext()) {
            PortletItem portletItem = it.next();
            if ( portletItem.getPortletDefinition().getFullPortletName().equals(portletName) ) {
                return portletItem;
            }
        }

        return null;
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
