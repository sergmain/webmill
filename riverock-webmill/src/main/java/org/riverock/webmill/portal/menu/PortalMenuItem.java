/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.menu;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 *
 * $Id$
 *
 */
public final class PortalMenuItem implements MenuItem{
    private final static Logger log = Logger.getLogger( MenuItem.class );

    private CatalogItem ctx = null;
    private Template template = null;
    private PortletName portletName = null;

    private String menuName = null;
    private List<MenuItem> catalogItems = new LinkedList<MenuItem>();  // List of MenuItem

    protected void finalize() throws Throwable {
        menuName = null;
        portletName = null;
        template = null;
        if (catalogItems !=null) {
            catalogItems.clear();
            catalogItems = null;
        }

        super.finalize();
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

    public PortalMenuItem(CatalogItem catalogBean) {
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
        this.template = InternalDaoFactory.getInternalTemplateDao().getTemplate( ctx.getTemplateId() );
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