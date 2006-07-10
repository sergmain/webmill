/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.MainTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.template.PortalTemplateParameter;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.portlet.schema.menu.MenuModuleType;
import org.riverock.portlet.schema.menu.MenuSimpleType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.tools.PortletService;

/**
 * $Author$
 * 
 * $Id$
 */
public final class MenuSimple implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger(MenuSimple.class);

    public final static int UNKNOWN_LEVEL = 0;
    public final static int EQUAL_LEVEL = 1;
    public final static int LESS_THAN_LEVEL = 2;
    public final static int GREAT_THAN_LEVEL = 3;
    public final static int LESS_OR_EQUAL_LEVEL = 4;
    public final static int GREAT_OR_EQUAL_LEVEL = 5;

    public MenuSimpleType menuSimple = new MenuSimpleType();

    private MenuModuleType currentMenuModule = null;

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;

    public void setParameters(final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
    }

    protected void destroy() {
        renderRequest = null;
        renderResponse = null;
        menuSimple = null;
        currentMenuModule = null;
    }

    public MenuSimple() {
    }

    public PortletResultContent getInstance(final Long id) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode(final String portletCode_) throws PortletException {

        try {
            if (log.isDebugEnabled()) {
                Object obj = renderRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
                if (obj != null) {
                    log.debug("PortalInfo class name: " + obj.getClass().getName());
                    log.debug("PortalInfo class: " + obj.getClass());

                    log.debug("PortalInfo class loader: " + obj.getClass().getClassLoader());
                    log.debug("Current class loader: " + this.getClass().getClassLoader());
                }
                else
                    log.debug("PortalInfo is null");
            }
            PortalInfo portalInfo = (PortalInfo) renderRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
            Menu catalog =
                portalInfo.getMenu(renderRequest.getLocale().toString()).
                getCatalogByCode(portletCode_);

//            Long defaultCatalogId = (Long)renderRequest.getAttribute( ContainerConstants.PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE );
            Long defaultCatalogId = null;

            if (log.isDebugEnabled()) {
                log.debug("defaultCatalogId: " + defaultCatalogId);
            }

            processInstance( catalog, defaultCatalogId  );

            return this;
        }
        catch (Throwable e) {
            final String es = "Error create MenuSimple object";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    public PortletResultContent getInstance()
        throws PortletException {

        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            PortalInfo portalInfo = (PortalInfo) renderRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
            Menu menu = portalInfo.getMenu(renderRequest.getLocale().toString()).getDefault();

            if (menu == null) {
                return null;
            }

//            Long defaultCatalogId = (Long)renderRequest.getAttribute( ContainerConstants.PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE );
            Long defaultCatalogId = null;

            if (log.isDebugEnabled()) {
                log.debug("defaultCatalogId: " + defaultCatalogId);
            }

            processInstance( menu, defaultCatalogId  );
            return this;
        }
        catch (Exception e) {
            final String es = "Error create MenuSimple object";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    private void processInstance(Menu menu, Long currentCtxId) throws PortletException {
        initMenuSimple(menu, currentCtxId);
        processPortletParameters();
    }

    private final static Object objSync4 = new Object();

    void initMenuSimple(Menu menu, Long currentCtxId) throws PortletException {
        if (menu != null) {
            int treeId = 0;
            for (MenuItem ci : menu.getMenuItem()) {
                MenuModuleType tempMenu = getMenuModule(ci, 1, currentCtxId, treeId, ++treeId);
                menuSimple.addMenuModule(tempMenu);
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug("List of menu items is null");
            }
        }
        menuSimple.setMenuName("");

        markAsCurrentThread(currentMenuModule);

        if (log.isDebugEnabled()) {
            synchronized (objSync4) {
                try {
                    String fileName =
                        GenericConfig.getGenericDebugDir() +
                        File.separatorChar +
                        "menu-simple-" + System.currentTimeMillis() + ".xml";

                    log.debug("Write menu to " + fileName);
                    XmlTools.writeToFile(menuSimple, fileName);
                }
                catch (Throwable e) {
                    // catch debug exception
                }
            }
        }
    }

    private void processPortletParameters() throws PortletException {

        List<PortalTemplateParameter> templateParameters =
            (List) renderRequest.getAttribute(ContainerConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE);

        if (log.isDebugEnabled()) {
            log.debug("param.getParameters(): " + templateParameters);
        }

        try {                 
            String levelTemp = PortletService.getString(templateParameters, "level", null);
            if (levelTemp == null)
                return;
            String compareTemp = PortletService.getString(templateParameters, "type_level", null);
            if (compareTemp == null)
                return;

            if (log.isDebugEnabled()) {
                log.debug("levelTemp - " + levelTemp + ", compareTemp " + compareTemp);
            }

            int level = new Integer(levelTemp);
            int compareLevel = decodeLevel(compareTemp);

            if (log.isDebugEnabled())
                log.debug("level - " + level + ", compareLevel " + compareLevel);

            processMenuLevel(level, compareLevel);

        }
        catch (Throwable e) {
            final String es = "Erorr parse level of menu, level - levelTemp. ";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    static int decodeLevel(String compareTemp) {
        int compareLevel = UNKNOWN_LEVEL;

        if (compareTemp == null)
            return UNKNOWN_LEVEL;

        if ("less".equalsIgnoreCase(compareTemp))
            compareLevel = LESS_THAN_LEVEL;
        else if ("equal".equalsIgnoreCase(compareTemp))
            compareLevel = EQUAL_LEVEL;
        else if ("great".equalsIgnoreCase(compareTemp))
            compareLevel = GREAT_THAN_LEVEL;
        else if ("less_or_equal".equalsIgnoreCase(compareTemp))
            compareLevel = LESS_OR_EQUAL_LEVEL;
        else if ("great_or_equal".equalsIgnoreCase(compareTemp))
            compareLevel = GREAT_OR_EQUAL_LEVEL;
        return compareLevel;
    }

    private void markAsCurrentThread(MenuModuleType temp) {
        if (temp == null)
            return;
        for (int i = 0; i < temp.getMenuModuleCount(); i++) {
            MenuModuleType tempMenuModule = temp.getMenuModule(i);
            tempMenuModule.setIsCurrentThread(1);
            markAsCurrentThread(tempMenuModule);
        }
    }

    private static Logger log1 = Logger.getLogger("org.riverock.portlet.menu.MenuSimple-1");

    private final static Object objSync = new Object();

    MenuModuleType[] getMenuModuleWithLevel(MenuModuleType[] menuModuleArray, int level, int currentLevel) {
        if (menuModuleArray == null)
            return null;

        if (log1.isDebugEnabled()) {
            log1.debug("level " + level + ", currentLevel " + currentLevel);
            synchronized (objSync) {
                String fileName = GenericConfig.getGenericDebugDir() + File.separatorChar + "menu-level-" + currentLevel + ".xml";
                log.debug("Write menu to " + fileName);
                try {
                    XmlTools.writeToFile(menuModuleArray, fileName);
                }
                catch (Exception e) {
                    // catch debug exception
                }
            }
        }

        if (currentLevel == 0 && currentLevel == level && menuModuleArray != null)
            return menuModuleArray;

        if (currentLevel == level && menuModuleArray != null) {
            for (final MenuModuleType newVar : menuModuleArray) {
                if (log1.isDebugEnabled())
                    log1.debug("menuModuleArray[k].getIncludeLevel() " + newVar.getIncludeLevel());

                if (newVar.getIsCurrentThread() == 1)
                    return menuModuleArray;
            }
        }

        for (final MenuModuleType newVar1 : menuModuleArray) {

            MenuModuleType[] array = newVar1.getMenuModule();
            if (array != null) {
                for (MenuModuleType menuModule : array) {
                    if (menuModule.getIsCurrentThread() == 1) {
                        MenuModuleType[] temp = getMenuModuleWithLevel(array, level, currentLevel + 1);
                        if (temp != null)
                            return temp;
                    }
                }
            }
        }
        return null;
    }

    private void remarkLevel(List v, int level) {
        for (Object aV : v) {
            MenuModuleType menu = (MenuModuleType) aV;
            menu.setIncludeLevel(level);
            remarkLevel(menu.getMenuModuleAsReference(), level + 1);
        }
    }

    private final static Object objSync2 = new Object();
    private final static Object objSync3 = new Object();

    void processMenuLevel(final int level, final int compareLevel) throws ConfigException {
        long tempLong = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("processMenuLevel(), level: " + level + ", compareLevel: " + compareLevel);

            synchronized (objSync2) {
                String fileName = GenericConfig.getGenericDebugDir() + File.separatorChar + "menu-simple-" + tempLong + ".xml";
                log.debug("Write menu to " + fileName);
                try {
                    XmlTools.writeToFile(menuSimple, fileName);
                }
                catch (Exception e) {
                    //catch debug exception
                }
            }
        }

        switch (compareLevel) {
            case EQUAL_LEVEL:
                {
                    if (log.isDebugEnabled())
                        log.debug("Start 'equals' level");

                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), level, 0);

                    if (log.isDebugEnabled())
                        log.debug("Result menu - " + result);

                    if (result == null)
                        menuSimple = new MenuSimpleType();
                    else {
                        for (final MenuModuleType newVar : result)
                            newVar.setMenuModule(new ArrayList());

                        menuSimple.setMenuModule(result);
                    }

                }
                break;
            case LESS_THAN_LEVEL:
                break;
            case GREAT_THAN_LEVEL:
                {
                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), (level < 0 ? 1 : level + 1), 0);

                    if (log.isDebugEnabled()) {
                        log.debug("result = " + result);
                        log.debug("currentMenuModule = " + currentMenuModule);
                        if (currentMenuModule != null)
                            log.debug("currentMenuModule.getMenuModuleCount() = " + currentMenuModule.getMenuModuleCount());
                    }

                    menuSimple = new MenuSimpleType();

                    if (result != null && result.length > 0 && currentMenuModule != null) {
                        int currLevel = currentMenuModule.getIncludeLevel();
                        int topMenuLevel = result[0].getIncludeLevel();

                        if (log.isDebugEnabled()) {
                            log.debug("currLevel = " + Math.abs(currLevel));
                            log.debug("topMenuLevel = " + topMenuLevel);
                            log.debug("Substruct level = " + Math.abs(topMenuLevel - currLevel));

                        }
                        menuSimple.setMenuModule(result);
                    }
                }
                break;
            case LESS_OR_EQUAL_LEVEL:
                break;
            case GREAT_OR_EQUAL_LEVEL:
                {
                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), (level < 0 ? 0 : level), 0);

                    if (log.isDebugEnabled()) {
                        log.debug("result = " + result);
                        log.debug("currentMenuModule = " + currentMenuModule);
                        if (currentMenuModule != null)
                            log.debug("currentMenuModule.getMenuModuleCount() = " + currentMenuModule.getMenuModuleCount());
                    }

                    menuSimple = new MenuSimpleType();

                    if (result != null && result.length > 0 && currentMenuModule != null) {
                        int currLevel = currentMenuModule.getIncludeLevel();
                        int topMenuLevel = result[0].getIncludeLevel();

                        if (log.isDebugEnabled()) {
                            log.debug("currLevel = " + Math.abs(currLevel));
                            log.debug("topMenuLevel = " + topMenuLevel);
                            log.debug("Substruct level = " + Math.abs(topMenuLevel - currLevel));

                        }

                        menuSimple.setMenuModule(result);
                    }
                }
                break;
        }
        remarkLevel(menuSimple.getMenuModuleAsReference(), 1);

        if (log.isDebugEnabled()) {
            synchronized (objSync3) {
                String fileName = GenericConfig.getGenericDebugDir() + File.separatorChar + "menu-simple-result-" + tempLong + ".xml";
                log.debug("Write menu to " + fileName);
                try {
                    XmlTools.writeToFile(menuSimple, fileName);
                }
                catch (Exception e) {
                    //catch debug exception
                }
            }
        }
    }

    MenuModuleType getMenuModule(MenuItem item, int level, Long currentCtxId, int treeId, int orderNumber)
        throws PortletException {

        try {

            MenuModuleType m = new MenuModuleType();

            m.setIncludeLevel(level);
            m.setIsCurrentThread(0);
            m.setIsCurrent(0);
            m.setUrlAlias(item.getUrl());
            m.setCatalodId(item.getId());
            m.setTreeId(treeId);
            m.setOrderNumber(orderNumber);

            if (log.isDebugEnabled()) {
                log.debug("item - " + item.toString());
            }

            // check was menu item is current
            if (currentCtxId != null && item.getId() != null && item.getId().equals(currentCtxId)) {
                m.setIsCurrent(1);
            }
            else {
                m.setIsCurrent(0);
            }

            if (log.isDebugEnabled()) {
                log.debug("isCurrent - " + m.getIsCurrent());
            }

            m.setModuleName(item.getMenuName());
            // set menu URL
            if (item.getUrl() == null)
                m.setModuleUrl(renderResponse.encodeURL(PortletService.pageid(renderRequest) + '/' + renderRequest.getLocale().toString() + '/' + item.getId()));
            else
                m.setModuleUrl(renderResponse.encodeURL(PortletService.page(renderRequest) + '/' + renderRequest.getLocale().toString() + '/' + item.getUrl()));

            //////

            if (m.getIsCurrent() == 1) {
                currentMenuModule = m;
                m.setIsCurrentThread(1);
            }

            if (log.isDebugEnabled()) {
                log.debug("Size of catalogItems - " + item.getCatalogItems().size());
            }

            ArrayList<MenuModuleType> vv = new ArrayList<MenuModuleType>(item.getCatalogItems().size());
            boolean isCurrentThread = false;
            Iterator<MenuItem> it = item.getCatalogItems().iterator();
            int j = 0;
            while (it.hasNext()) {
                MenuItem menuItem = it.next();

                MenuModuleType menuModule = getMenuModule(menuItem, level + 1, currentCtxId, treeId, j++);

                if (menuModule.getIsCurrent() == 1 || menuModule.getIsCurrentThread() == 1) {
                    isCurrentThread = true;
                }

                vv.add(menuModule);
            }
            if (isCurrentThread)
                m.setIsCurrentThread(1);

            m.setMenuModule(vv);

            if (log.isDebugEnabled())
                log.debug("Size of result list - " + vv.size());

            return m;
        }
        catch (Exception e) {
            String es = "Error getMenuModule()";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    public byte[] getPlainHTML() {
        return getMenuModule(menuSimple.getMenuModuleAsReference()).getBytes();
    }

    private final static Object syncDebug = new Object();

    public byte[] getXml(String rootElement) throws Exception {
        if (log.isDebugEnabled())
            log.debug("start get XmlByte array, rootElement: " + rootElement);

        MenuSimpleType menu = menuSimple;

        byte b[];
        try {
            b = XmlTools.getXml(menu, rootElement);
        }
        catch (Exception e) {
            log.error("menu name " + menu.getMenuName());
            for (int k = 0; k < menu.getMenuModuleCount(); k++) {
                MenuModuleType item = menu.getMenuModule(k);
                log.error("menu item #" + k + " getModuleName() " + item.getModuleName());
                log.error("menu item #" + k + " getModuleUrl() " + item.getModuleUrl());
            }
            try {
                log.error("info Xerces version - " + org.apache.xerces.impl.Version.getVersion());
            }
            catch (Exception e2) {
                log.error("Error get version of xerces " + e.getMessage());
            }
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("end get XmlByte array. length of array - " + b.length);

            final String testFile = GenericConfig.getGenericDebugDir() + File.separatorChar + "menu-simple-url.xml";
            log.debug("Start output test data to file " + testFile);
            synchronized (syncDebug) {
                MainTools.writeToFile(testFile, b);
            }
            log.debug("end output data");
        }

        return b;
    }

    public byte[] getXml() throws Exception {
        return getXml("MenuSimple");
    }

    private String getMenuModule(List v) {
        if (v == null || v.isEmpty())
            return "";

        String s = "";
        s += ("<table cellpadding=\"0\" cellspacing=\"0\" widht=\"100%\" border=\"0\">");
        for (Object aV : v) {
            MenuModuleType item = (MenuModuleType) aV;

            s += "<tr><td class=\"menuData\" nowrap><a href=\"" +
                item.getModuleUrl() +

                "\">" + item.getModuleName() + "</a></td></tr>";

            s += getMenuModule(item.getMenuModuleAsReference());

        }
        s += ("</table>");

        return s;
    }

    public List<ClassQueryItem> getList(Long idSiteCtxLangCatalog, Long idContext) {
        return null;
    }

/*
    private String checkSubMenuHTML( MenuItem item, MenuItem catItem )
     throws Exception
    {
//System.out.println("#4.06.01 "+item.str.getString( ctxInstance.page.p.defaultLocale)+" "+catItem.str.getString( ctxInstance.page.p.defaultLocale) );
     String s = "";
     if ( item==catItem || Menu.checkCurrentThread( item.catalogItems, catItem ) )
     {
//System.out.println("#4.06.02 ");

         for (int j=0; j<item.catalogItems.size(); j++)
         {
             MenuItem ci = (MenuItem)item.catalogItems.elementAt(j);

             s +=
                 "<tr><td class=\"menuData\" nowrap>"+
                 "<a href=\""+
                 res.encodeURL( ctxInstance.ctx()+'?'+jspPage.addURL+Constants.NAME_ID_CONTEXT_PARAM+'='+ci.id )+
                 "\">"+ ci.str.getString( renderRequest.getLocale() )+"</a></td></tr>";

             s += checkSubMenuHTML( ci, catItem );
         }
     }
     return s;
    }
*/

}
