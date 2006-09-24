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
package org.riverock.webmill.google.sitemap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.google.sitemap.schema.sitemap.ObjectFactory;
import org.riverock.webmill.google.sitemap.schema.sitemap.Url;
import org.riverock.webmill.google.sitemap.schema.sitemap.Urlset;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 22.09.2006
 *         Time: 18:01:41
 *         <p/>
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class GoogleSitemapService {
    private final static Logger log = Logger.getLogger( GoogleSitemapService.class );

    private static final String SITEMAP_XML_GZ = "sitemap.xml.gz";

    public static void createSitemap(Long siteId, String virtualHostUrl, String portalContext, String applicationPath) {
        if (log.isDebugEnabled()) {
            log.debug("Start GoogleSitemapService.createSitemap()");
            log.debug("siteId: "+ siteId);
            log.debug("virtualHostUrl: "+ virtualHostUrl);
            log.debug("portalContext: "+ portalContext);
            log.debug("applicationPath: "+ applicationPath);
        }
        List<SiteLanguage> siteLanguages = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);
        if (log.isDebugEnabled()) {
            log.debug("siteLanguages: " +siteLanguages.size());
        }
        List<Url> urls = new ArrayList<Url>();
        for (SiteLanguage siteLanguage : siteLanguages) {
            String localeName = StringTools.getLocale(siteLanguage.getCustomLanguage()).toString();

            List<CatalogLanguageItem> items = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItemList(siteLanguage.getSiteLanguageId());
            if (log.isDebugEnabled()) {
                log.debug("CatalogLanguageItem: " +items.size());
            }
            for (CatalogLanguageItem item : items) {
                List<CatalogItem> catalogItems = InternalDaoFactory.getInternalCatalogDao().getCatalogItemList(item.getCatalogLanguageId());
                if (log.isDebugEnabled()) {
                    log.debug("catalogItems: " +catalogItems.size());
                }
                processMenuItem(virtualHostUrl, localeName, portalContext, catalogItems, urls);
            }

        }
        if (log.isDebugEnabled()) {
            log.debug("Urls: " +urls);
        }
        writeToFile(urls,  applicationPath+File.separatorChar+"WEB-INF"+File.separatorChar+"sitemap"+File.separatorChar+siteId);
    }

    private static void writeToFile(List<Url> urlset, String applicationPath) {
        log.debug("Start writeToFile()");

        File applicationPathFile = new File(applicationPath);
        File sitemap = new File(applicationPathFile, SITEMAP_XML_GZ);
        try {
            if (!applicationPathFile.exists()) {
                applicationPathFile.mkdirs();
            }
            if (sitemap.exists()) {
                sitemap.delete();
            }

            if (log.isDebugEnabled()) {
                log.debug("applicationPathFile: " + applicationPathFile);
                log.debug("sitemap: " + sitemap);
            }

            FileOutputStream fileOutputStream = new FileOutputStream(sitemap);
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
            marshall(urlset, gzipOutputStream);
//            marshall(urlset, fileOutputStream);
            gzipOutputStream.flush();
            gzipOutputStream.close();
            gzipOutputStream=null;
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream=null;

        }
        catch (Exception e) {
            String es = "Erorr marshal sitemap to file";
            log.error(es, e);
            if (sitemap.exists()) {
                sitemap.delete();
            }
        }

    }

    static void marshall(List<Url> urlset, OutputStream os) throws JAXBException {
        log.debug("Start marshall()");
        JAXBContext jc = JAXBContext.newInstance( "org.riverock.webmill.google.sitemap.schema.sitemap" );
        Marshaller m = jc.createMarshaller();
        // create an element for marshalling
        Urlset set = (new ObjectFactory()).createUrlset();
        set.setUrl(urlset);

        // create a Marshaller and marshal to System.out
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE );
        m.marshal( set, os );
    }

    private static void processMenuItem(String virtualHostUrl, String localeName, String portalContext, List<CatalogItem> menuItemList, List<Url> urls) {
        for (CatalogItem catalogItem : menuItemList) {
            Url url = new Url();

            if (catalogItem.getUrl() == null)
                url.setLoc(virtualHostUrl+PortletService.pageid(portalContext) + '/' + localeName + '/' + catalogItem.getId());
            else
                url.setLoc(virtualHostUrl+PortletService.page(portalContext) + '/' + localeName + '/' + catalogItem.getUrl());
            
            urls.add(url);
            
            if (catalogItem.getSubCatalogItemList()!=null) {
                processMenuItem(virtualHostUrl, localeName, portalContext, catalogItem.getSubCatalogItemList(), urls);
            }
        }
    }
}
