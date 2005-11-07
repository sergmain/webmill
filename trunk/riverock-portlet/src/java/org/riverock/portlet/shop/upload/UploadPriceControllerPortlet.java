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
package org.riverock.portlet.shop.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.config.ConfigException;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portlet.menu.MenuInterface;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.price.ImportPriceList;
import org.riverock.portlet.price.Shop;
import org.riverock.portlet.price.ShopPortlet;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.portal.PortalInfo;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 10:16:24 PM
 *
 * $Id$
 */
public final class UploadPriceControllerPortlet implements Portlet {
    private final static Log log = LogFactory.getLog( UploadPriceControllerPortlet.class );

    public UploadPriceControllerPortlet() {
    }

    protected PortletConfig portletConfig = null;
    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String ERROR_URL = "ERROR_URL";
    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws IOException {

        if ( renderRequest.getAttribute( ERROR_TEXT )!=null ) {
            Writer out = renderResponse.getWriter();
            String srcURL = PortletService.url(UploadPrice.CTX_TYPE_UPLOAD_PRICE, renderRequest, renderResponse );
            WebmillErrorPage.processPortletError(out, null, (String)renderRequest.getAttribute( ERROR_TEXT ), srcURL, (String)renderRequest.getAttribute( ERROR_TEXT ));

            out.flush();
            out.close();
        }
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {

        DatabaseAdapter db_ = null;
        InputStream priceData = null;

        PortalInfo portalInfo = (PortalInfo)actionRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);

        try  {
            if ( log.isDebugEnabled() ) {
                log.debug( "#55.01.06 " );
                try {
                    for ( Enumeration e = actionRequest.getParameterNames(); e.hasMoreElements(); )
                    {
                        String s = (String)e.nextElement();
                        log.debug( "Request attr - "+s+", value - "+RequestTools.getString( actionRequest, s ) );
                    }
                }
                catch (ConfigException configException) {
                    log.error("Exception in ", configException);
                }
            }

            Object obj = actionRequest.getParameterMap().get( UploadPrice.UPLOAD_FILE_PARM_NAME );
            if (obj==null){
                actionResponse.setRenderParameter(
                    ERROR_TEXT, "Parameter '"+UploadPrice.UPLOAD_FILE_PARM_NAME+"' not found");
                actionResponse.setRenderParameter(
                    ERROR_URL, "загрузить повторно");
                return;
            }

            if (!(obj instanceof InputStream)){
                actionResponse.setRenderParameter(
                    ERROR_TEXT, "Parameter '"+UploadPrice.UPLOAD_FILE_PARM_NAME+"' must be type 'FILE'" );
                actionResponse.setRenderParameter(
                    ERROR_URL, "загрузить повторно");
                return;
            }

            priceData = (InputStream)obj;

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.11 " );

            AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( "webmill.upload_price_list" ) ){
                throw new PortletSecurityException( "You have not right to upload price on this site" );
            }

            db_ = DatabaseAdapter.getInstance();
            boolean isSiteWithShop = isSiteWithShop( db_, portalInfo, actionResponse );
            if (!isSiteWithShop) {
                return;
            }

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.15 idSite -  "+portalInfo.getSiteId() );

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.15 start import price in db " );

            PricesType prices = null;
            try{
                InputSource inSrc = new InputSource( priceData );
                prices = (PricesType)Unmarshaller.unmarshal( PricesType.class, inSrc );
            }
            catch (Exception e){
                log.error( "Exception parse of uploaded file with data", e );
                actionResponse.setRenderParameter(
                    ERROR_TEXT,
                    "Exception parse uploaded file with data " +
                    WebmillErrorPage.getErrorMessage(e)
                );
                actionResponse.setRenderParameter( ERROR_URL, "загрузить повторно" );

                return;
            }

            try{
                ImportPriceList.process( prices, portalInfo.getSiteId(), db_);
                // reinit Shop in cache. need for correct output date/time of upload price
                Shop.reinit();
            }
            catch (Exception e){
                log.error( "Exception store price data in DB", e );

                actionResponse.setRenderParameter(
                    ERROR_TEXT,
                    "Exception store price data in DB " + WebmillErrorPage.getErrorMessage(e)
                );
                actionResponse.setRenderParameter( ERROR_URL, "загрузить повторно" );
                return;
            }

            String srcURL = PortletService.url( UploadPrice.CTX_TYPE_UPLOAD_PRICE, actionRequest, actionResponse );

            if ( log.isDebugEnabled() ){
                log.debug( "#55.01.15  finish import price in db" );
                log.debug( "url to redirect "+srcURL );
            }

            actionResponse.sendRedirect( srcURL );
            return;

        }
        catch (Exception e){
            log.error( "General exception import price-list", e );
            actionResponse.setRenderParameter(
                ERROR_TEXT,
                "General exception inport price-list " + WebmillErrorPage.getErrorMessage(e)
            );
            actionResponse.setRenderParameter( ERROR_URL, "загрузить повторно" );
            return;
        }
        finally{
            DatabaseAdapter.close( db_ );
            db_ = null;
        }
    }

    private boolean isSiteWithShop( DatabaseAdapter db_, PortalInfo portalInfo, ActionResponse actionResponse ) {

        return true;

        // Todo need implement
/*
        SiteMenu siteMenu = SiteMenu.getInstance( db_, portalInfo.getSiteId() );
        boolean isSiteWithShop = false;

        if (siteMenu!=null) {
            for (int i = 0; i < siteMenu.getMenuLanguageCount(); i++) {
                MenuLanguageInterface c = siteMenu.getMenuLanguage(i);

                for (int j = 0; j < c.getMenuCount(); j++) {
                    MenuInterface item = c.getMenu( j );

                    if (checkShopContext(item)) {
                        isSiteWithShop =  true;
                        break;
                    }
                }
            }
        }

        if ( !isSiteWithShop ) {
            actionResponse.setRenderParameter(
                ERROR_TEXT, "ƒанный сайт не поддерживает ни одного прайса.  од ошибки #10.04");
            actionResponse.setRenderParameter(
                ERROR_URL, "загрузить повторно");
        }
        return isSiteWithShop;
*/
    }

    private boolean checkShopContext(MenuInterface menu) {
        return checkShopContext(menu.getMenuItem());
    }

    private boolean checkShopContext(List v)
    {
        if (v == null)
            return false;

        for (int j = 0; j < v.size(); j++) {
            MenuItemInterface ctxItem =
                (MenuItemInterface) v.get(j);

            if (ShopPortlet.CTX_TYPE_SHOP.equals(ctxItem.getType()))
                return true;

            if (checkShopContext(ctxItem.getCatalogItems()))
                return true;
        }
        return false;
    }
}
