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

import java.util.List;
import java.util.Locale;
import java.util.LinkedList;

import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.interfaces.generic.LocalizedStringInterface;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.port.LocalizedString;
import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;
import org.riverock.webmill.schema.core.SiteTemplateItemType;
import org.riverock.webmill.schema.core.SiteCtxTypeItemType;
import org.riverock.webmill.core.GetSiteTemplateItem;
import org.riverock.webmill.core.GetSiteCtxTypeItem;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.portlet.context.CtxContextFactory;
import org.apache.log4j.Logger;

/**
 *
 * $Id$
 *
 */
public class MenuItem implements MenuItemInterface{
    private static Logger log = Logger.getLogger(CtxContextFactory.class);

    private SiteCtxCatalogItemType ctx = null;
    private String nameTemplate = null;
    private String type = "";

    private LocalizedStringInterface str = null;
    private List catalogItems = new LinkedList();  // List of MenuItem

    protected void finalize() throws Throwable{
        str = null;
        type = null;
        nameTemplate = null;
        if (getCatalogItems() != null){
            getCatalogItems().clear();
            catalogItems = null;
        }

        super.finalize();
    }

    public Long getTopId() { return ctx.getIdTopCtxCatalog(); }
    public Long getId() { return ctx.getIdSiteCtxCatalog(); }
    public List getSubTree() { return this.catalogItems;}
    public void setSubTree(List list){ this.catalogItems = list;}

    public String toString()
    {
        String st;
        try{
            st = str.getString(Locale.ENGLISH);
        }
        catch (java.io.UnsupportedEncodingException e){
            st = "error";
        }
        return
            "[id:"+getId()+", idTop:"+getIdTop()+" type:"+type+", portletId:"+getIdPortlet()+", " +
            "template:"+nameTemplate+", str:"+st+", url:"+getUrl()+"]";
    }

    public MenuItem(DatabaseAdapter db_, SiteCtxCatalogItemType ctxItem) throws PortalException{

        this.ctx = ctxItem;
        if (log.isDebugEnabled()){
            log.debug("ctxItem: "+ctx);
            if (ctx!=null){
                log.debug("ctxItem.getIdSiteCtxCatalog(): "+ctx.getIdSiteCtxCatalog());
                log.debug("ctxItem.getIdSiteCtxLangCatalog(): "+ctx.getIdSiteCtxLangCatalog());
                log.debug("ctxItem.getIdSiteTemplate(): "+ctx.getIdSiteTemplate());
                log.debug("ctxItem.getIdSiteCtxType(): "+ctx.getIdSiteCtxType());
            }
        }

        this.str = new LocalizedString(
            Boolean.TRUE.equals(ctx.getIsUseProperties()),
            ctx.getKeyMessage(),
            ctx.getStorage()
        );

        try {
            SiteTemplateItemType template = GetSiteTemplateItem.getInstance(db_, ctx.getIdSiteTemplate()).item;
            if (template!=null)
                this.nameTemplate = template.getNameSiteTemplate();

            SiteCtxTypeItemType ctxType = GetSiteCtxTypeItem.getInstance(db_, ctx.getIdSiteCtxType()).item;
            if (ctxType!=null)
                this.type = ctxType.getType();

        } catch (PortalPersistenceException e) {
            String es = "Error create MenuItem object";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }

    public Long getIdTop(){
        return ctx.getIdTopCtxCatalog();
    }

    public Long getIdPortlet(){
        return ctx.getIdContext();
    }

    public Long getIdTemplate() {
        return ctx.getIdSiteTemplate();
    }

    public String getNameTemplate(){
        return this.nameTemplate;
    }

    public Long getIdType() {
        return ctx.getIdSiteCtxType();
    }

    public String getType(){
        return this.type;
    }

    public LocalizedStringInterface getStr(){
        return this.str;
    }

    public List getCatalogItems(){
        return this.catalogItems;
    }

    public String getUrl(){
        return ctx.getCtxPageUrl();
    }

    public String getUrlResource() {
        return ctx.getUrlResource();
    }
}