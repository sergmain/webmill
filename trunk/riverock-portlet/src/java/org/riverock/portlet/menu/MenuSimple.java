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

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.portlet.menu;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.servlet.http.HttpServletResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.schema.portlet.menu.MenuModuleType;
import org.riverock.portlet.schema.portlet.menu.MenuSimpleType;
import org.riverock.portlet.portlets.PortletException;
import org.riverock.common.tools.MainTools;
import org.riverock.common.config.ConfigException;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletParameter;
import org.riverock.webmill.portlet.Portlet;
import org.riverock.webmill.portlet.PortletManager;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portal.menu.MenuInterface;
import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.log4j.Logger;

public class MenuSimple implements Portlet, PortletResultObject, PortletGetList
{
    private static Logger log = Logger.getLogger(MenuSimple.class);

    public final static int UNKNOWN_LEVEL = 0;
    public final static int EQUAL_LEVEL = 1;
    public final static int LESS_THAN_LEVEL = 2;
    public final static int GREAT_THAN_LEVEL = 3;
    public final static int LESS_OR_EQUAL_LEVEL = 4;
    public final static int GREAT_OR_EQUAL_LEVEL = 5;


    private static Object syncDebug = new Object();

//    public String name = null;
    private Long id = null;
    Long getId() {return id;} // for tests

    public MenuSimpleType menuSimple = new MenuSimpleType();

    public PortletParameter param = null;
    private CtxInstance ctxInstance = null;
    private MenuModuleType currentMenuModule = null;

    private boolean isCurrentIsFound = false;  // used, when type of portlet is unknown. i.e. mill.index

    protected void finalize() throws Throwable
    {
        param = null;
        ctxInstance = null;
        super.finalize();
    }

    public MenuSimple()
    {
    }

    public void setParameter(PortletParameter param_)
    {
        this.param = param_;
    }

    public boolean isXml()
    {
        return true;
    }

    public boolean isHtml()
    {
        return true;
    }

    public PortletResultObject getInstance( DatabaseAdapter db_ , Long id)
        throws Exception
    {
        return getInstance( db_ );
    }

    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String portletCode_)
        throws Exception
    {
        try
        {
            ctxInstance = (CtxInstance)param.getPortletRequest().getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            String typePortlet = param.getCtxType();

            if (log.isDebugEnabled())
                log.debug("Portlet code - " + portletCode_+", ctxType "+param.getCtxType());

            MenuInterface catalog =
                ctxInstance.getPortalInfo().getMenu(
                    ctxInstance.getPortletRequest().getLocale().toString()
                ).getCatalogByCode( portletCode_ );

            processInstance( catalog, typePortlet );

            return this;
        }
        catch(Exception e)
        {
            log.error("Error create MenuSimple object", e);
        }
        return null;
    }

    public PortletResultObject getInstance( DatabaseAdapter db_ )
        throws Exception
    {
        try
        {
            ctxInstance = (CtxInstance)param.getPortletRequest().getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            String typePortlet = param.getCtxType();

            if (log.isDebugEnabled())
                log.debug("Portlet type - " + typePortlet);

            MenuInterface menu = ctxInstance.getPortalInfo().getMenu(
                ctxInstance.getPortletRequest().getLocale().toString()
            ).getDefault();

            if (menu!=null)
            {
                processInstance( menu, typePortlet );
                return this;
            }

            return null;
        }
        catch(Exception e)
        {
            log.error("Error create MenuSimple object", e);
        }
        return null;
    }

    private void processInstance( MenuInterface menu,  String typePortlet)
        throws Exception
    {
        try
        {
            PortletType desc = searchPortletDescription(typePortlet);
            initMenuSimple(menu, desc, typePortlet);

        }
        catch(Exception e) {
            log.error("Exception #1 in processInstance()", e);
            throw e;
        }
        catch(Error e) {
            log.error("Error #1 in processInstance()", e);
            throw e;
        }

        processPortletParameters();

    }

    void initMenuSimple(MenuInterface menu, PortletType desc, String typePortlet)
            throws Exception
    {
        if (menu != null)
        {
            ListIterator it = menu.getMenuItem().listIterator();
            while (it.hasNext()) {
                MenuItemInterface ci = (MenuItemInterface)it.next();
                MenuModuleType tempMenu = getMenuModule(ci, param.getResponse(), 1, id, desc, typePortlet);
                menuSimple.addMenuModule(tempMenu);
            }
        }
        else
            if (log.isDebugEnabled()) log.debug("List of menu items is null");

        menuSimple.setMenuName( "" );

        markAsCurrentThread(currentMenuModule);
    }

    PortletType searchPortletDescription(String typePortlet)
            throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("#typePortlet - " + typePortlet);

        PortletType desc = PortletManager.getPortletDescription( typePortlet );
        if (log.isDebugEnabled())
        {
            log.debug("#desc - " + desc);
            if (desc != null)
                log.debug("#desc.namePortletId " +
                    PortletTools.getStringParam(
                        desc, PortletTools.name_portlet_id
                    )
                );
        }

        if (desc != null )
        {
            String namePortletId =
                PortletTools.getStringParam(
                    desc, PortletTools.name_portlet_id
                );

            if (namePortletId != null)
            {
                if (log.isDebugEnabled())
                    log.debug("namePortletId - " + namePortletId);

                id = PortletTools.getLong( param.getPortletRequest(), namePortletId);
            }
        }
        return desc;
    }

    private void processPortletParameters() throws PortletException {
        if (log.isDebugEnabled())
            log.debug("param.getParameters() - " + param.getTemplateParameters());

        try {
            String levelTemp = PortletTools.getString(param.getTemplateParameters(), "level", null);
            if (levelTemp==null)
                return;
            String compareTemp = PortletTools.getString(param.getTemplateParameters(), "type_level", null);
            if (compareTemp==null)
                return;

            if (log.isDebugEnabled())
                log.debug("levelTemp - " +levelTemp+", compareTemp "+compareTemp);

            int level = 0;
            level = new Integer(levelTemp).intValue();

            int compareLevel = decodeLevel(compareTemp);

            if (log.isDebugEnabled())
                log.debug("level - " +level+", compareLevel "+compareLevel);

            processMenuLevel(level, compareLevel);

        }
        catch(Exception e) {
            log.error("Exception #2 in processInstance()", e);
            throw new PortletException("Exception parse level of menu, level - levelTemp. "+e.getMessage() );
        }
        catch(Error e) {
            log.error("Error #2 in processInstance()", e);
            throw new PortletException("Error parse level of menu, level - levelTemp. "+e.getMessage() );
        }
    }

    static int decodeLevel(String compareTemp)
    {
        int compareLevel = UNKNOWN_LEVEL;

        if (compareTemp==null)
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

    private void markAsCurrentThread( MenuModuleType temp ) {
        if (temp==null)
            return;
        for (int i=0; i<temp.getMenuModuleCount(); i++)
        {
            MenuModuleType tempMenuModule = temp.getMenuModule(i);
            tempMenuModule.setIsCurrentThread( new Integer(1) );
            markAsCurrentThread(tempMenuModule);
        }
    }

    private static Logger log1 = Logger.getLogger("org.riverock.portlet.MenuSimple-1");

    MenuModuleType[] getMenuModuleWithLevel(MenuModuleType[] menuModuleArray, int level, int currentLevel)
        throws ConfigException
    {
        if (menuModuleArray==null)
            return null;

        if (log1.isDebugEnabled())
        {
            log1.debug("level "+ level+ ", currentLevel "+currentLevel);
            Object objSync = new Object();
            synchronized(objSync)
            {
                String fileName = WebmillConfig.getWebmillTempDir()+File.separatorChar+"menu-level-"+currentLevel+".xml";
                log.debug("Write menu to "+fileName);
                try {
                    XmlTools.writeToFile(menuModuleArray, fileName);
                }
                catch(Exception e){}
            }
        }

        if (currentLevel==0 && currentLevel==level && menuModuleArray!=null)
            return menuModuleArray;

        if (currentLevel==level && menuModuleArray!=null)
        {
            for (int k=0; k<menuModuleArray.length; k++)
            {
                if (log1.isDebugEnabled())
                    log1.debug("menuModuleArray[k].getIncludeLevel() "+ menuModuleArray[k].getIncludeLevel());

                if ( new Integer(1).equals( menuModuleArray[k].getIsCurrentThread()) )
                    return menuModuleArray;
            }
        }

        for (int i=0; i<menuModuleArray.length; i++)
        {

            MenuModuleType[] array = menuModuleArray[i].getMenuModule();
            if (array!=null)
            {
                for (int k=0; k<array.length; k++)
                {
                    MenuModuleType menuModule = array[k];
                    if ( new Integer(1).equals( menuModule.getIsCurrentThread()) )
                    {
                        MenuModuleType[] temp = getMenuModuleWithLevel(array, level, currentLevel+1);
                        if (temp!=null)
                            return temp;
                    }
                }
            }
        }
        return null;
    }

    private void remarkLevel(List v, int level)
    {
        for (int i=0; i<v.size(); i++)
        {
            MenuModuleType menu = (MenuModuleType)v.get(i);
            menu.setIncludeLevel( new Integer(level) );
            remarkLevel(menu.getMenuModuleAsReference(), level+1);
        }
    }

    void processMenuLevel( int level, int compareLevel )
        throws ConfigException
    {
        if (log.isDebugEnabled())
        {
            Object objSync = new Object();
            synchronized(objSync)
            {
                String fileName = WebmillConfig.getWebmillTempDir()+File.separatorChar+"menu-simple.xml";
                log.debug("Write menu to "+fileName);
                try {
                    XmlTools.writeToFile(menuSimple, fileName);
                }
                catch(Exception e){}
            }
        }
        switch(compareLevel) {
            case EQUAL_LEVEL:
                {
                    if (log.isDebugEnabled())
                        log.debug("Start 'equals' level");

                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), level, 0);

                    if (log.isDebugEnabled())
                        log.debug("Result menu - "+result);

                    if (result==null)
                        menuSimple = new MenuSimpleType();
                    else
                    {
                        for (int i=0; i<result.length; i++)
                            result[i].setMenuModule( new ArrayList() );

                        menuSimple.setMenuModule(result);
                    }

                }
                break;
            case LESS_THAN_LEVEL:
                break;
            case GREAT_THAN_LEVEL:
                {
                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), (level<0?1:level+1), 0);

                    if (log.isDebugEnabled())
                    {
                        log.debug("result = " + result);
                        log.debug("currentMenuModule = " + currentMenuModule);
                        if (currentMenuModule!=null)
                            log.debug("currentMenuModule.getMenuModuleCount() = " + currentMenuModule.getMenuModuleCount());
                    }

                    menuSimple = new MenuSimpleType();

                    if (result!=null && result.length>0 && currentMenuModule!=null)
                    {
                        int currLevel =  currentMenuModule.getIncludeLevel().intValue();
                        int topMenuLevel = result[0].getIncludeLevel().intValue();

                        if (log.isDebugEnabled())
                        {
                            log.debug("currLevel = " + Math.abs(currLevel));
                            log.debug("topMenuLevel = " + topMenuLevel);
                            log.debug("Substruct level = " + Math.abs(topMenuLevel-currLevel));

                        }
                        menuSimple.setMenuModule( result );
                    }
                }
                break;
            case LESS_OR_EQUAL_LEVEL:
                break;
            case GREAT_OR_EQUAL_LEVEL:
                {
                    MenuModuleType[] result =
                        getMenuModuleWithLevel(menuSimple.getMenuModule(), (level<0?0:level), 0);

                    if (log.isDebugEnabled())
                    {
                        log.debug("result = " + result);
                        log.debug("currentMenuModule = " + currentMenuModule);
                        if (currentMenuModule!=null)
                            log.debug("currentMenuModule.getMenuModuleCount() = " + currentMenuModule.getMenuModuleCount());
                    }

                    menuSimple = new MenuSimpleType();

                    if (result!=null && result.length>0 && currentMenuModule!=null)
                    {
                        int currLevel =  currentMenuModule.getIncludeLevel().intValue();
                        int topMenuLevel = result[0].getIncludeLevel().intValue();

                        if (log.isDebugEnabled())
                        {
                            log.debug("currLevel = " + Math.abs(currLevel));
                            log.debug("topMenuLevel = " + topMenuLevel);
                            log.debug("Substruct level = " + Math.abs(topMenuLevel-currLevel));

                        }

                        menuSimple.setMenuModule( result );
                    }
                }
                break;
        }
        remarkLevel(menuSimple.getMenuModuleAsReference(), 1);

        if (log.isDebugEnabled())
        {
            Object objSync = new Object();
            synchronized(objSync)
            {
                String fileName = WebmillConfig.getWebmillDebugDir()+"menu-simple-result.xml";
                log.debug("Write menu to "+fileName);
                try {
                    XmlTools.writeToFile(menuSimple, fileName);
                }
                catch(Exception e){}
            }
        }
    }

    MenuModuleType getMenuModule(
        MenuItemInterface item,
        HttpServletResponse response,
        int level,
        Long id_current_item,
        PortletType desc_current,
        String typePortlet)
    throws Exception
    {

        try
        {
            if (log.isDebugEnabled())
                log.debug("getInstance of MenuSimpleModule. Level - "+level+
                    ", ID current item - "+id_current_item);

            MenuModuleType m = new MenuModuleType();

            m.setIncludeLevel( new Integer(level) );
            m.setIsCurrentThread( new Integer(0) );
            m.setIsCurrent( new Integer(0) );

            if (log.isDebugEnabled())
            {
                log.debug("item - "+ item.toString());
                log.debug("id_current_item - "+ id_current_item);

                if (desc_current != null)
                {
                    log.debug("desc_current.portlet - "+ desc_current.getPortletName().getContent());
                    log.debug("desc_current.namePortletID - "+
                        PortletTools.getStringParam(
                            desc_current, PortletTools.name_portlet_id
                        )
                    );
                }
                else
                    log.debug("desc_current is null" );
            }

            if (item.getType()!=null && item.getType().equals( typePortlet ) )
            {
                if (id_current_item!=null && desc_current != null )
                {
                    String namePortletIdTemp =
                            PortletTools.getStringParam(
                                    desc_current,
                                    PortletTools.name_portlet_id
                            );

                    if ( namePortletIdTemp == null || id_current_item.equals(item.getIdPortlet()) )
                        m.setIsCurrent( new Integer(1) );
                }
                else
                    if (!isCurrentIsFound)
                    {
                        isCurrentIsFound = true;
                        m.setIsCurrent( new Integer(1) );
                    }
            }

            if (log.isDebugEnabled())
                log.debug("isCurrent - "+ m.getIsCurrent() );

            try
            {
                m.setModuleName( item.getStr().getString( param.getPortletRequest().getLocale() ) );
            }
            catch (Exception e)
            {
                log.warn("Error get localized message", e);
                m.setModuleName( "error" );
            }

            // set menu URL
            if (item.getUrl()==null)
                m.setModuleUrl(
                    response.encodeURL(CtxInstance.pageid() + '/' + item.getId()+'/'+ctxInstance.getPortletRequest().getLocale().toString())
                );
            else
                m.setModuleUrl(response.encodeURL(CtxInstance.page() + '/' + item.getUrl()+'/'+ctxInstance.getPortletRequest().getLocale().toString()));
/*
            m.setModuleUrl(
                response.encodeURL(ctxInstance.ctx() + '?' +
                                   page.getAsURL() +
                Constants.NAME_TYPE_CONTEXT_PARAM
                + '='+
                item.getType()+'&'+
                Constants.NAME_TEMPLATE_CONTEXT_PARAM
                + '='+ item.getNameTemplate()
                )
            );

            PortletType desc = PortletManager.getPortletDescription( item.getType() );
            if ( desc != null )
            {
                String nameId =
                    PortletTools.getStringParam(
                        desc, PortletTools.name_portlet_id
                    );
                if (nameId != null)
                {
                    m.setModuleUrl(
                        m.getModuleUrl()+'&'+
                        nameId + '='+
                        item.getIdPortlet()
                    );
                }
            }
*/

            //////

            if (new Integer(1).equals(m.getIsCurrent()) )
            {
                currentMenuModule = m;
                m.setIsCurrentThread( new Integer(1) );
            }

            if (log.isDebugEnabled())
                log.debug("Size of catalogItems - "+item.getCatalogItems().size()    );

            ArrayList vv = new ArrayList(item.getCatalogItems().size());
            boolean isCurrentThread = false;
            for (int j = 0; j < item.getCatalogItems().size(); j++)
            {
                MenuItemInterface ci = (MenuItemInterface) item.getCatalogItems().get(j);

                MenuModuleType menuModule = getMenuModule(
                    ci, response, level + 1, id_current_item,
                    desc_current, typePortlet
                );

                if (new Integer(1).equals(menuModule.getIsCurrent()) ||
                    new Integer(1).equals(menuModule.getIsCurrentThread())
                )
                    isCurrentThread = true;

                vv.add( menuModule );
            }
            if (isCurrentThread)
                m.setIsCurrentThread( new Integer(1) );

            m.setMenuModule( vv );

            if (log.isDebugEnabled())
                log.debug("Size of result list - "+vv.size()    );

            return m;
        }
        catch(Exception e)
        {
            log.error("Error getMenuModule()", e);
            throw e;
        }
    }

    public byte[] getPlainHTML()
    {
        return getMenuModule( menuSimple.getMenuModuleAsReference() ).getBytes();
    }

    public byte[] getXml(String rootElement) throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("start get XmlByte array");

        MenuSimpleType menu = menuSimple;

        byte b[] = null;
        try
        {
            b = XmlTools.getXml( menu, rootElement );
        }
        catch(Exception e)
        {
            log.error("menu name id "+id);
            log.error("menu name "+menu.getMenuName());
            for (int k=0; k<menu.getMenuModuleCount(); k++)
            {
                MenuModuleType item = menu.getMenuModule(k);
                log.error("menu item #"+k+" getModuleName() "+item.getModuleName());
                log.error("menu item #"+k+" getModuleUrl() "+item.getModuleUrl());
            }
            try
            {
                log.error("info Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
            }
            catch(Exception e2)
            {
                log.error("Error get version of xerces "+e.getMessage());
            }
            throw e;
        }

        if (log.isDebugEnabled())
            log.debug("end get XmlByte array. length of array - " + b.length);

        if ( log.isDebugEnabled() )
        {
            log.debug("Start output test data to file");
            String testFile = WebmillConfig.getWebmillDebugDir()+"menu-simple-url.xml";
            synchronized(syncDebug)
            {
                MainTools.writeToFile(testFile, b);
            }
            log.debug("end output data");
        }

        return b;
    }

    public byte[] getXml()
        throws Exception
    {
        return getXml( "MenuSimple" );
    }

    private String getMenuModule(List v)
    {
        if (v==null || v.size()==0)
            return "";

        String s = "";
        s += ("<table cellpadding=\"0\" cellspacing=\"0\" widht=\"100%\" border=\"0\">");
        for (int i = 0; i < v.size(); i++)
        {
            MenuModuleType item = (MenuModuleType)v.get(i);


            s += "<tr><td class=\"menuData\" nowrap><a href=\"" +
                item.getModuleUrl() +

                "\">" + item.getModuleName() + "</a></td></tr>";

            s += getMenuModule(item.getMenuModuleAsReference());

        }
        s += ("</table>");

        return s;
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }

/*
    private String checkSubMenuHTML( MenuItem item, MenuItem catItem )
     throws Exception
    {
//System.out.println("#4.06.01 "+item.str.getString( ctxInstance.page.p.defaultLocale)+" "+catItem.str.getString( ctxInstance.page.p.defaultLocale) );
     String s = "";
     if ( item==catItem || Menu.checkCurrentTread( item.catalogItems, catItem ) )
     {
//System.out.println("#4.06.02 ");

         for (int j=0; j<item.catalogItems.size(); j++)
         {
             MenuItem ci = (MenuItem)item.catalogItems.elementAt(j);

             s +=
                 "<tr><td class=\"menuData\" nowrap>"+
                 "<a href=\""+
                 res.encodeURL( ctxInstance.ctx()+'?'+jspPage.addURL+Constants.NAME_ID_CONTEXT_PARAM+'='+ci.id )+
                 "\">"+ ci.str.getString( ctxInstance.getPortletRequest().getLocale() )+"</a></td></tr>";

             s += checkSubMenuHTML( ci, catItem );
         }
     }
     return s;
    }
*/

}
