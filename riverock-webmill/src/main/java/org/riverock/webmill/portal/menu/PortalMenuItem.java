/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.menu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.template.PortalTemplate;
import org.riverock.webmill.portal.template.PortalTemplateManagerFactory;

/**
 *
 * $Id: PortalMenuItem.java 1375 2007-08-28 19:35:44Z serg_main $
 *
 */
public final class PortalMenuItem implements MenuItem {
    private final static Logger log = Logger.getLogger( MenuItem.class );

    private CatalogItem ctx = null;
    private PortalTemplate template = null;
    private PortletName portletName = null;

    private String menuName = null;
    private List<MenuItem> catalogItems = new LinkedList<MenuItem>();

    protected void finalize() throws Throwable {
        destroy();

        super.finalize();
    }

    public void destroy() {
        ctx=null;
        template = null;
        portletName = null;
        menuName = null;
        if (catalogItems !=null) {
            for (MenuItem catalogItem : catalogItems) {
                ((PortalMenuItem)catalogItem).destroy();
            }
            catalogItems.clear();
            catalogItems = null;
        }
    }

    public Map<String, String> getMetadata() {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(ctx.getMetadata())) {
            return map;
        }
        Properties properties = new Properties();
        try {
            properties.load( new ByteArrayInputStream(ctx.getMetadata().getBytes()) );
        }
        catch (IOException e) {
            String es = "Error load properties from stream";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return map;
    }

    public Long getTopId() { return ctx.getTopCatalogId(); }
    public Long getId() { return ctx.getCatalogId(); }
    public List getSubTree() { return this.catalogItems;}
    public void setSubTree(List list){ this.catalogItems = list;}

    public String toString() {
        return
            "[id: "+getId()+",idTop: "+getIdTop()+",portletName: "+portletName +",portletId: "+getIdPortlet()+"," +
            "template: "+template +",name: "+menuName+",url: "+getUrl()+"]";
    }

    public PortalMenuItem(Long siteId, CatalogItem catalogBean) {
        this.ctx = catalogBean;
        if (this.ctx==null) {
            throw new IllegalStateException("catalogBean is empty");
        }

        if (log.isDebugEnabled()){
            log.debug("ctxItem.getCatalogItemId(): "+ctx.getCatalogId());
            log.debug("ctxItem.getCatalogLanguageId(): "+ctx.getCatalogLanguageId() );
            log.debug("ctxItem.getIdSiteTemplate(): "+ctx.getTemplateId() );
            log.debug("ctxItem.getIdSiteCtxType(): "+ctx.getPortletId() );
        }

        this.menuName = ctx.getKeyMessage();
        this.template = PortalTemplateManagerFactory.getInstance(siteId).getTemplate(ctx.getTemplateId());
//        this.template = InternalDaoFactory.getInternalTemplateDao().getTemplate( ctx.getTemplateId() );
        this.portletName = InternalDaoFactory.getInternalPortletNameDao().getPortletName( ctx.getPortletId() );
    }

    public String getPortletRole() {
        return ctx.getPortletRole();
    }

    public Long getIdTop(){
        return ctx.getTopCatalogId();
    }

    public Long getIdPortlet(){
        return ctx.getContextId();
    }

    public Long getIdTemplate() {
        return ctx.getTemplateId();
    }

    public String getNameTemplate(){
        return template !=null?template.getTemplateName():null;
    }

    public Long getIdType() {
        return ctx.getPortletId();
    }

    public String getType(){
        return portletName.getPortletName();
    }

    public String getMenuName(){
        return menuName;
    }

    public List<MenuItem> getCatalogItems(){
        return catalogItems;
    }

    public String getUrl(){
        return ctx.getUrl();
    }
}