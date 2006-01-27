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

package org.riverock.webmill.portal.menu;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.dao.PortalDaoFactory;

/**
 *
 * $Id$
 *
 */
public final class PortalMenuItem implements MenuItem{
    private final static Logger log = Logger.getLogger( MenuItem.class );

    private CatalogBean ctx = null;
    private TemplateBean templateBean = null;
    private PortletNameBean portletNameBean = null;

    private String menuName = null;
    private List<MenuItem> catalogItems = new LinkedList<MenuItem>();  // List of MenuItem

    protected void finalize() throws Throwable{
        menuName = null;
        portletNameBean = null;
        templateBean = null;
        if (getCatalogItems()!=null) {
            getCatalogItems().clear();
            catalogItems = null;
        }

        super.finalize();
    }

    public Long getTopId() { return ctx.getTopCatalogId(); }
    public Long getId() { return ctx.getCatalogId(); }
    public List getSubTree() { return this.catalogItems;}
    public void setSubTree(List list){ this.catalogItems = list;}

    public String toString() {
        return
            "[id: "+getId()+",idTop: "+getIdTop()+",portletNameBean: "+portletNameBean+",portletId: "+getIdPortlet()+"," +
            "template: "+templateBean+",name: "+menuName+",url: "+getUrl()+"]";
    }

    public PortalMenuItem(CatalogBean catalogBean) {

        this.ctx = catalogBean;
        if (log.isDebugEnabled()){
            log.debug("ctxItem: "+ctx);
            if (ctx!=null){
                log.debug("ctxItem.getCatalogId(): "+ctx.getCatalogId());
                log.debug("ctxItem.getCatalogLanguageId(): "+ctx.getCatalogLanguageId() );
                log.debug("ctxItem.getIdSiteTemplate(): "+ctx.getTemplateId() );
                log.debug("ctxItem.getIdSiteCtxType(): "+ctx.getPortletId() );
            }
        }

        this.menuName = ctx.getKeyMessage();
        this.templateBean = PortalDaoFactory.getPortalDao().getTemplateBean( ctx.getTemplateId() );
        this.portletNameBean = PortalDaoFactory.getPortalDao().getPortletNameBean( ctx.getPortletId() );
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
        return templateBean!=null?templateBean.getTemplateName():null;
    }

    public Long getIdType() {
        return ctx.getPortletId();
    }

    public String getType(){
        return portletNameBean.getName();
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

    public String getUrlResource() {
        throw new IllegalStateException("method not supported");
    }
}