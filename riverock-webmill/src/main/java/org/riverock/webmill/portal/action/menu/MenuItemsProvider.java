package org.riverock.webmill.portal.action.menu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.webmill.container.bean.PortletWebApplication;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.spi.PortalSpiProviderImpl;

/**
 * User: SMaslyukov
 * Date: 28.06.2007
 * Time: 12:25:55
 */
public class MenuItemsProvider {
    private final static Logger log = Logger.getLogger( MenuItemsProvider.class );

    public static List<ClassQueryItem> getMenuItems(PortalInstance portalInstance, PortalRequest portalRequest, Long siteId, Map<String, Object> parameters) {
        Long portletId = (Long)parameters.get("portletId");
        Long catalogLanguageId = (Long)parameters.get("catalogLanguageId");
        Long contextId = (Long)parameters.get("contextId");

        PortletName bean = InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletId);
        if (log.isDebugEnabled()) {
            log.debug("portletId: " + portletId);
            log.debug("bean: " + bean);
            if (bean!=null) {
                log.debug("namePortlet: "+bean.getPortletName());
            }
        }
        if (bean==null) {
            return new ArrayList<ClassQueryItem>(0);
        }

        PortletWebApplication portletWebApplication = portalInstance.getPortletContainer().searchPortletItem( bean.getPortletName() );

        if (log.isDebugEnabled()) {
            log.debug("portletWebApplication "+portletWebApplication);
        }

        if ( portletWebApplication ==null ) {
            return new ArrayList<ClassQueryItem>(0);
        }

        String classNameTemp =
            PortletService.getStringParam(
                portletWebApplication.getPortletDefinition(), ContainerConstants.class_name_get_list
            );
        if (classNameTemp==null) {
            return new ArrayList<ClassQueryItem>(0);
        }

        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( portletWebApplication.getClassLoader() );

            Constructor constructor;
            try {
                constructor = Class.forName(classNameTemp, false, portletWebApplication.getClassLoader()).getConstructor();
            }
            catch (Exception e) {
                String es = "Error getConstructor()";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }

            if (log.isDebugEnabled()) {
                log.debug("#12.12.005  constructor is " + constructor);
            }

            if (constructor != null) {
                PortletGetList obj;
                Object o = null;
                try {
                    o = constructor.newInstance();
                    obj = (PortletGetList)o;
                }
                catch (ClassCastException e) {
                    if (o!=null) {
                        log.error("ClassCastException to PortletGetList.class  from "+o.getClass().getName(), e);
                    }
                    else {
                        log.error("ClassCastException to PortletGetList.class  from null", e);
                    }
                    throw e;
                }
                catch (Throwable e) {
                    String es = "Error invoke constructor ";
                    log.error(es, e);
                    throw new IllegalStateException(es, e);
                }

                if (log.isDebugEnabled()) {
                    log.debug("#12.12.008 object " + obj);
                }
                ClassLoader classLoader = portalInstance.getPortalClassLoader();
                obj.setPortalDaoProvider(new PortalSpiProviderImpl(portalRequest.getAuth(), classLoader, siteId));
                return obj.getList( catalogLanguageId, contextId);
            }
            return new ArrayList<ClassQueryItem>(0);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
