/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.TimeZone;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.log4j.Logger;
import org.apache.commons.lang.time.DateFormatUtils;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 1:31:13 PM
 *
 * $Id: ImageUploadFromUrlPortlet.java 1334 2007-08-24 14:47:45Z serg_main $
 */
public final class ImageUploadFromUrlPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( ImageUploadFromUrlPortlet.class );

    public ImageUploadFromUrlPortlet() {
    }

    protected PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
        portletConfig = null;
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws PortletException {

        Writer out = null;
        PreparedStatement ps = null;

        try {
            out = renderResponse.getWriter();
            if ( log.isDebugEnabled() )
                log.debug( "Start commit new image" );

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.upload_image" ) ) {
                throw new PortletSecurityException( "You have not enough right" );
            }

            PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );

            if ( log.isDebugEnabled() ) {
                log.debug( "urlString - " + renderRequest.getParameter( "url_download" ) );
            }

            String urlString = renderRequest.getParameter( "url_download" ).trim();
            if ( urlString == null )
                throw new IllegalArgumentException( "id_firm not initialized" );


            if ( log.isDebugEnabled() )
                log.debug( "result url_download " + urlString );

            String ext[] = {".jpg", ".jpeg", ".gif", ".png"};
            int i;
            for( i = 0; i<ext.length; i++ ) {
                if ( ( ext[i] != null ) && urlString.toLowerCase().endsWith( ext[i].toLowerCase() ) )
                    break;
            }
            if ( i == ext.length )
                throw new Exception( "Unsupported file extension. Error #20.03" );


            if ( log.isDebugEnabled() )
                log.debug( "id_main - " + PortletUtils.getLong( renderRequest, "id_main" ) );

            Long id_main = PortletUtils.getLong( renderRequest, "id_main" );
            if ( id_main == null )
                throw new IllegalArgumentException( "id_firm not initialized" );


            String desc = PortletUtils.getString(renderRequest, "d", null);


            // Todo этот сиквенс просто заглушка, сейчас не работает.
            // т.к. сиквенс просто использовался чтобы получить уникальное имя файла
//            CustomSequence seq = new CustomSequence();
//            seq.setSequenceName( "seq_image_number_file" );
//            seq.setTableName( "MAIN_FORUM_THREADS" );
//            seq.setColumnName( "ID_THREAD" );
            Long currID=null;
//            currID = dbDyn.getSequenceNextValue( seq );

            String storage_ = portletConfig.getPortletContext().getRealPath("/") + File.separatorChar + "image";
            String fileName = storage_ + File.separatorChar;

            if ( log.isDebugEnabled() )
                log.debug( "filename - " + fileName );

            URL url = new URL( urlString );
            File fileUrl = new File( url.getFile() );

            if ( log.isDebugEnabled() )
                log.debug( "fileUrl - " + fileUrl );

            String newFileName = StringTools.appendString( "" + currID,
                '0', 7, true ) + "-" + fileUrl.getName();

            if ( log.isDebugEnabled() )
                log.debug( "newFileName " + newFileName );

            fileName += newFileName;

            if ( log.isDebugEnabled() )
                log.debug( "file to write " + fileName );

            InputStream is = url.openStream();
            FileOutputStream fos = new FileOutputStream( new File( fileName ) );
            byte bytes[] = new byte[1000];
            int count = 0;
            while( ( count = is.read( bytes ) ) != -1 ) {
                fos.write( bytes, 0, count );
            }

            fos.close();
            fos = null;
            is.close();
            is = null;
            url = null;

            out.write( DateFormatUtils.format(System.currentTimeMillis(), "dd-MMMM-yyyy HH:mm:ss:SS",
                TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone()), 
                renderRequest.getLocale() ) + "<br>" );

//            ps = dbDyn.prepareStatement( "insert into WM_IMAGE_DIR " +
//                "( id_image_dir, ID_FIRM, is_group, id, id_main, name_file, description )" +
//                "(select seq_WM_IMAGE_DIR.nextval, ID_FIRM, 0, ?, ?, ?, ? " +
//                " from WM_AUTH_USER where user_login = ? )" );

            RsetTools.setLong( ps, 1, currID );
            RsetTools.setLong( ps, 2, id_main );
            ps.setString( 3, "/image/" + newFileName );
            ps.setString( 4, desc );
            ps.setString( 5, auth_.getUserLogin() );

            ps.executeUpdate();

            out.write( "Загрузка данных прошла без ошибок<br>" +
                "Загружен файл " + newFileName + "<br>" +
                DateFormatUtils.format(System.currentTimeMillis(), "dd-MMMM-yyyy HH:mm:ss:SS",
                    TimeZone.getTimeZone(portalInfo.getSite().getServerTimeZone()),
                    renderRequest.getLocale() ) + "<br>" +
                "<br>" +
                "<p><a href=\"" + PortletUtils.url( "mill.image.index", renderRequest, renderResponse ) +
                "\">Загрузить данные повторно</a></p><br>" +
                "<p><a href=\"" + PortletUtils.url( ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse ) +
                "\">На главную страницу</a></p>" );

        }
        catch( Exception e ) {

            final String es = "Error upload image from url";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }
}
