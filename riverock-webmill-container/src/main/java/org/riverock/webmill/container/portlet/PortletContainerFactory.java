package org.riverock.webmill.container.portlet;

import org.riverock.webmill.container.bean.PortletWebApplication;
import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet_definition.JaxbPortletDefinitionProcessorImpl;
import org.riverock.webmill.container.portlet_definition.PortletDefinitionProcessor;
import org.riverock.webmill.container.tools.ContainertStringUtils;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * User: serg_main
 * Date: 28.01.2004
 * Time: 17:49:32
 *
 * @author Serge Maslyukov
 *         $Id: PortletContainer.java 901 2006-08-16 14:21:42Z serg_main $
 */
public final class PortletContainerFactory implements Serializable {
    private static final long serialVersionUID = 50434672384237805L;

    // List with portlet.xml files which waiting for digest
    private static List<WaitDigestFile> digestWaitList = new ArrayList<WaitDigestFile>();

    // Masp of portlet containers, key is path to dir, where placed all web application
    private static Map<String, PortletContainer> portletContainers = new HashMap<String, PortletContainer>();

    private static PortletDefinitionProcessor portletDefinitionProcessor = new JaxbPortletDefinitionProcessorImpl();

    /**
     * get new instance of portlet container
     * @param portalInstance portal instance
     * @param portalPath portal path. All web contextsportlets) in same web application must returm same portalPath  
     * @return portlet container
     */
    public synchronized static PortletContainer getInstance(PortalInstance portalInstance, String portalPath) {
        if (portalInstance==null) {
            throw new IllegalStateException("PortalInstance is null");
        }
        if (portalPath==null) {
            throw new IllegalStateException("PortalPath is null");
        }

        for (Map.Entry<String, PortletContainer> entry : portletContainers.entrySet()) {
            System.out.println("PortletContainer registered entry: " + entry);
        }
        PortletContainer container = portletContainers.get(portalPath);
        if (container==null) {
            synchronized(PortletContainerFactory.class) {
                container = portletContainers.get(portalPath);
                if (container==null) {
                    container = new PortletContainer(portalInstance, portalPath);
                    portletContainers.put(portalPath, container );
                }
            }
        }
        // portlet was registered before portal
        if (container.portalInstance==null) {
            container.portalInstance=portalInstance;
        }
        System.out.println("PortletContainer for "+portalPath+" is "+container);
        return container;
    }

    /**
     * register new web context 
     *
     * @param uniqueName unique value for this web context
     * @param portletFile  portlet.xml file
     * @param servletConfig servlet config
     * @param classLoader class loader for this web context
     * @param portalPath portal path
     * @throws PortletContainerException on errro
     */
    public synchronized static void registerPortletFile(
        String uniqueName, File portletFile, ServletConfig servletConfig, ClassLoader classLoader, String portalPath)
        throws PortletContainerException {

            System.out.println("Register and prepare portlet file: " + portletFile.getName());

            digestWaitList.add(new WaitDigestFile(uniqueName, portletFile, servletConfig, classLoader, portalPath));
            digestWaitedPortletFile();
    }

    public synchronized static void destroy(String uniqueName, String portalPath) {
        PortletContainer container = portletContainers.get(portalPath);
        if (container==null) {
            System.out.println("Container for path " + portalPath + " not found");
            return;
        }
        System.out.println("Portlet container instance: " + container);
        System.out.println("Undeploy uniqueName: " + uniqueName);
        container.destroy(uniqueName);

        Iterator<WaitDigestFile> iter = digestWaitList.iterator();
        while (iter.hasNext()) {
            WaitDigestFile waitDigestFile = iter.next();
            if (waitDigestFile.uniqueName.equals( uniqueName )) {
                iter.remove();
            }
        }
    }

    static synchronized void digestWaitedPortletFile() throws PortletContainerException {
            Iterator<WaitDigestFile> iterator = digestWaitList.iterator();
            while (iterator.hasNext()) {
                WaitDigestFile waitDigestFile = iterator.next();

                PortletContainer container = portletContainers.get(waitDigestFile.portalPath);
                if (container==null) {
                    container = new PortletContainer(waitDigestFile.portalPath);
                    portletContainers.put(waitDigestFile.portalPath, container);
                }

                List<PortletDefinition> portletList = processPortletFile(waitDigestFile.file);
                Map<String, PortletWebApplication> map = new HashMap<String, PortletWebApplication>();
                for (PortletDefinition portletType : portletList) {
                    PortletWebApplication portletWebApplication = new PortletWebApplication();
                    portletWebApplication.setPortletDefinition(portletType);
                    portletWebApplication.setServletConfig(waitDigestFile.servletConfig);
                    portletWebApplication.setClassLoader(waitDigestFile.classLoader);
                    portletWebApplication.setUniqueName(waitDigestFile.uniqueName);
                    map.put(portletType.getFullPortletName(), portletWebApplication);
                }
                container.addNewWebApplication(map);
                iterator.remove();
            }
    }

    private static List<PortletDefinition> processPortletFile(File portletFile) throws PortletContainerException {

        System.out.println("Start process portlet file: " + portletFile.getName());
        List<PortletDefinition> portletList = new ArrayList<PortletDefinition>();

        if (portletFile.exists()) {
            try {

                PortletApplication portletApp = portletDefinitionProcessor.process(portletFile);
                System.out.println("portletApp: " + portletApp);
                if (portletApp!=null) {
                    System.out.println("Count of portlets: : " + portletApp.getPortlet().size());
                }
                else {
                    return portletList;
                }
                for (PortletDefinition portletDefinition : portletApp.getPortlet()) {
                    portletDefinition.setApplicationName(
                        ContainertStringUtils.isBlank(portletApp.getId()) ?"":portletApp.getId()
                    );
                    System.out.println("Add new portlet: " + portletDefinition.getFullPortletName());
                    portletList.add(portletDefinition);
                }
            }
            catch (Throwable e) {
                String errorString = "Error processing portlet file " + portletFile.getName();
                e.printStackTrace();
                throw new PortletContainerException(errorString, e);
            }
        }
        return portletList;
    }
}
