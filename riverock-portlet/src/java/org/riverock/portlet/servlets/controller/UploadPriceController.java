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

import java.io.Writer;

import java.util.Enumeration;

import java.util.List;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.main.UploadFileException;

import org.riverock.common.multipart.AbstractPart;

import org.riverock.common.multipart.MultipartHandler;

import org.riverock.common.multipart.MultipartRequestWrapper;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portal.menu.SiteMenu;

import org.riverock.webmill.portal.menu.MenuInterface;

import org.riverock.webmill.portal.menu.MenuLanguage;

import org.riverock.webmill.portal.menu.MenuItemInterface;

import org.riverock.webmill.portal.menu.MenuLanguageInterface;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.portlet.price.ImportPriceList;

import org.riverock.portlet.price.Shop;

import org.riverock.portlet.schema.import_price.PricesType;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Unmarshaller;

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



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        String srcURL = "/";

        boolean isGrantAccess = false;

        AbstractPart part = null;

        InitPage jspPage = null;

        try

        {

            out = response.getWriter();



            if ( log.isDebugEnabled() )

                log.debug( "#55.01.06 " );



            DatabaseAdapter db_ = DatabaseAdapter.getInstance( false );

            jspPage = new InitPage( db_, request,

                                    "mill.locale.auth"

            );



            srcURL = response.encodeURL( CtxURL.ctx() )+'?'+

                jspPage.getAsURL()+

                Constants.NAME_TEMPLATE_CONTEXT_PARAM+'='+

                ServletUtils.getString( request, Constants.NAME_TEMPLATE_CONTEXT_PARAM )+'&'+

                Constants.NAME_TYPE_CONTEXT_PARAM+'='+

                Constants.CTX_TYPE_UPLOAD_PRICE;



            boolean saveUploadedFilesToDisk = false;

            MultipartRequestWrapper reqWrapper =

                new MultipartRequestWrapper(

                    request,

                    saveUploadedFilesToDisk,

                    null,

                    false,

                    false,

                    1024*1024

                );



            MultipartHandler handler = reqWrapper.getMultipartHandler();

            if ( handler==null )

                throw new UploadFileException( UploadFileException.WRONG_REQUEST_HEADER_ERROR );



            Enumeration e = handler.getPartsHash().keys();

            if ( !e.hasMoreElements() )

            {

                response.sendRedirect( srcURL );

                return;

            }



            // in request must be only one file

            String key = (String)e.nextElement();

            part = (AbstractPart)handler.getPartsHash().get( key );



            if ( part==null )

            {

                response.sendRedirect( srcURL );

                return;

            }



            if ( part.getType()!=AbstractPart.FILE_TYPE )

                throw new UploadFileException( UploadFileException.WRONG_FORMAT_ERROR );



            if ( log.isDebugEnabled() )

                log.debug( "#55.01.11 " );



            AuthSession auth_ = AuthTools.check( request, response, "/" );

            if ( auth_==null )

                return;



            SiteMenu siteMenu = SiteMenu.getInstance( db_, jspPage.p.sites.getIdSite() );



            if ( !isShopContext(siteMenu) )

            {

                if ( log.isDebugEnabled() )

                    log.debug( "url to redirect: "+srcURL );



                WebmillErrorPage.process(out, null, "Данный сайт не поддерживает ни одного прайса. Код ошибки #10.04", srcURL, "загрузить повторно");

//                out.write( "<html><head></head<body>"+

//                    "<p>Данный сайт не поддерживает ни одного прайса. Код ошибки #10.04</p>"+

//                    "<p><a href=\""+srcURL+"\">загрузить повторно</a></p>"+

//                    "</body></html>" );



                return;

            }



            if ( log.isDebugEnabled() )

                log.debug( "#55.01.15 idSite -  "+jspPage.p.sites.getIdSite() );



            isGrantAccess = auth_.isUserInRole( "webmill.upload_price_list" );

        }

        catch (Exception e)

        {

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





        if ( isGrantAccess && part!=null)

        {



            if ( log.isDebugEnabled() )

                log.debug( "#55.01.15 start import price in db " );



            PricesType prices = null;

            try

            {

                InputSource inSrc = new InputSource( part.getInputStream() );

                prices = (PricesType)Unmarshaller.unmarshal( PricesType.class, inSrc );

            }

            catch (Exception e)

            {

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



            try

            {

                ImportPriceList.process( prices, jspPage.p.sites.getIdSite() );

                // reinit Shop in cache. need for correct output date/time of upload price

                Shop.reinit();

            }

            catch (Exception e)

            {

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



            if ( log.isDebugEnabled() )

            {

                log.debug( "#55.01.15  finish import price in db" );

                log.debug( "url to redirect "+srcURL );

            }



            response.sendRedirect( srcURL );

            return;



        } // check auth

    }



}

