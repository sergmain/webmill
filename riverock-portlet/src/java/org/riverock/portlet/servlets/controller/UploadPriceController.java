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
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 10:16:24 PM
 *
 * $Id$
 */
package org.riverock.portlet.servlets.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.riverock.common.config.ConfigException;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.price.ImportPriceList;
import org.riverock.portlet.price.Shop;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.portlet.servlets.view.UploadPrice;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.interfaces.portlet.menu.MenuInterface;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.interfaces.portlet.menu.MenuLanguageInterface;
import org.xml.sax.InputSource;

public class UploadPriceController extends HttpServlet
{
    private static Logger log = Logger.getLogger(UploadPriceController.class);

    public UploadPriceController()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    private boolean checkShopContext(MenuInterface menu)
    {
        return checkShopContext(menu.getMenuItem());
    }

    private boolean checkShopContext(List v)
    {
        if (v == null)
            return false;

        for (int j = 0; j < v.size(); j++)
        {
            MenuItemInterface ctxItem =
                (MenuItemInterface) v.get(j);

            if (Constants.CTX_TYPE_SHOP.equals(ctxItem.getType()))
                return true;

            if (checkShopContext(ctxItem.getCatalogItems()))
                return true;
        }
        return false;
    }

    private boolean isShopContext(SiteMenu siteMenu)
    {
        if (siteMenu==null)
            return false;

        for (int i = 0; i < siteMenu.getMenuLanguageCount(); i++)
        {
            MenuLanguageInterface c = siteMenu.getMenuLanguage(i);

            for (int j = 0; j < c.getMenuCount(); j++)
            {
                MenuInterface item = c.getMenu( j );

                if (checkShopContext(item))
                    return true;

            }
        }
        return false;
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Writer out = null;
        String srcURL = "/";
        DatabaseAdapter db_ = null;
        InputStream priceData = null;

//        CtxInstance ctxInstance =
//            (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
        RenderRequest renderRequest = null;
        PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(PortalConstants.PORTAL_INFO_ATTRIBUTE);

        try
        {
            out = response.getWriter();

            if ( log.isDebugEnabled() )
            {
                log.debug( "#55.01.06 " );
                try
                {
                    for ( Enumeration e = renderRequest.getParameterNames(); e.hasMoreElements(); )
                    {
                        String s = (String)e.nextElement();
                        log.debug( "Request attr - "+s+", value - "+PortletTools.getString( renderRequest, s ) );
                    }
                }
                catch (ConfigException configException)
                {
                    log.error("Exception in ", configException);
                }
            }
            srcURL = CtxInstance.url(Constants.CTX_TYPE_UPLOAD_PRICE);

            Object obj = renderRequest.getParameterMap().get( UploadPrice.UPLOAD_FILE_PARM_NAME );
            if (obj==null){
                WebmillErrorPage.process(out, null, "Parameter '"+UploadPrice.UPLOAD_FILE_PARM_NAME+"' not found", srcURL, "загрузить повторно");
                return;
            }

            if (!(obj instanceof InputStream)){
                WebmillErrorPage.process(out, null, "Parameter '"+UploadPrice.UPLOAD_FILE_PARM_NAME+"' must be type 'FILE'", srcURL, "загрузить повторно");
                return;
            }

            priceData = (InputStream)obj;

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.11 " );

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null || !auth_.isUserInRole( "webmill.upload_price_list" ) ){
                WebmillErrorPage.process(out, null, "You have not right to upload price on this site", srcURL, "загрузить повторно");
                return;
            }

            db_ = DatabaseAdapter.getInstance( true );
            SiteMenu siteMenu = SiteMenu.getInstance( db_, portalInfo.getSites().getIdSite() );

            if ( !isShopContext(siteMenu) )
            {
                if ( log.isDebugEnabled() )
                    log.debug( "url to redirect: "+srcURL );

                WebmillErrorPage.process(out, null, "Данный сайт не поддерживает ни одного прайса. Код ошибки #10.04", srcURL, "загрузить повторно");
                return;
            }

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.15 idSite -  "+portalInfo.getSites().getIdSite() );

            if ( log.isDebugEnabled() )
                log.debug( "#55.01.15 start import price in db " );

            PricesType prices = null;
            try{
                InputSource inSrc = new InputSource( priceData );
                prices = (PricesType)Unmarshaller.unmarshal( PricesType.class, inSrc );
            }
            catch (Exception e){
                log.error( "Exception parse of uploaded file with data", e );
                if ( out!=null )
                {
                    WebmillErrorPage.process(out, e, "Exception parse uploaded file with data", srcURL, "загрузить повторно");
                }
                else
                    throw new ServletException( e );

                out.flush();
                out.close();
                return;
            }

            try{
                ImportPriceList.process( prices, portalInfo.getSites().getIdSite(), db_);
                // reinit Shop in cache. need for correct output date/time of upload price
                Shop.reinit();
            }
            catch (Exception e){
                log.error( "Exception store price data in DB", e );
                if ( out!=null )
                {
                    WebmillErrorPage.process(out, e, "Exception store price data in DB", srcURL, "загрузить повторно");
                }
                else
                    throw new ServletException( e );

                out.flush();
                out.close();
                return;
            }

            if ( log.isDebugEnabled() ){
                log.debug( "#55.01.15  finish import price in db" );
                log.debug( "url to redirect "+srcURL );
            }

            response.sendRedirect( srcURL );
            return;

        }
        catch (Exception e){
            log.error( "General exception import price-list", e );
            if ( out!=null )
            {
                WebmillErrorPage.process(out, e, "General exception inport price-list", srcURL, "загрузить повторно");
            }
            else
                throw new ServletException( e );

            out.flush();
            out.close();
            return;
        }
        finally{
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
