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
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 1:31:13 PM
 *
 * $Id$
 */

package org.riverock.portlet.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.sql.PreparedStatement;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.main.Constants;
import org.riverock.sso.a3.AuthSession;
import org.riverock.webmill.main.UploadFileException;
import org.riverock.webmill.portlet.PortletTools;

import org.apache.log4j.Logger;

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
        DatabaseAdapter dbDyn = null;

        try {
            out = renderResponse.getWriter();
            if ( log.isDebugEnabled() )
                log.debug( "Start commit new image" );

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.upload_image" ) ) {
                throw new PortletSecurityException( "You have not enough right" );
            }

            dbDyn = DatabaseAdapter.getInstance( true );

            if ( log.isDebugEnabled() )
                log.debug( "urlString - " + renderRequest.getParameter( "url_download" ) );

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
                throw new UploadFileException( "Unsupported file extension. Error #20.03" );


            if ( log.isDebugEnabled() )
                log.debug( "id_main - " + PortletTools.getLong( renderRequest, "id_main" ) );

            Long id_main = PortletTools.getLong( renderRequest, "id_main" );
            if ( id_main == null )
                throw new IllegalArgumentException( "id_firm not initialized" );


            String desc = PortletTools.getString( renderRequest, "d" );


            // Todo ���� ������� ������ ��������, ������ �� ��������.
            // �.�. ������� ������ ������������� ����� �������� ���������� ��� �����
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_image_number_file" );
            seq.setTableName( "MAIN_FORUM_THREADS" );
            seq.setColumnName( "ID_THREAD" );
            Long currID = new Long( dbDyn.getSequenceNextValue( seq ) );

            // Todo check was need PropertiesProvider.getApplicationPath(), not PropertiesProvider.getConfigPath()
            String storage_ = PropertiesProvider.getApplicationPath() + File.separatorChar + "image";
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

            out.write( DateUtils.getCurrentDate( "dd-MMMM-yyyy HH:mm:ss:SS", MainTools.RUlocale() ) + "<br>" );

            ps = dbDyn.prepareStatement( "insert into image_dir " +
                "( id_image_dir, ID_FIRM, is_group, id, id_main, name_file, description )" +
                "(select seq_image_dir.nextval, ID_FIRM, 0, ?, ?, ?, ? " +
                " from auth_user where user_login = ? )" );

            RsetTools.setLong( ps, 1, currID );
            RsetTools.setLong( ps, 2, id_main );
            ps.setString( 3, "/image/" + newFileName );
            ps.setString( 4, desc );
            ps.setString( 5, auth_.getUserLogin() );

            ps.executeUpdate();
            dbDyn.commit();

            out.write( "�������� ������ ������ ��� ������<br>" +
                "�������� ���� " + newFileName + "<br>" +
                DateUtils.getCurrentDate( "dd-MMMM-yyyy HH:mm:ss:SS", MainTools.RUlocale() ) + "<br>" +
                "<br>" +
                "<p><a href=\"" + PortletTools.url( "mill.image.index", renderRequest, renderResponse ) +
                "\">��������� ������ ��������</a></p><br>" +
                "<p><a href=\"" + PortletTools.url( Constants.CTX_TYPE_INDEX, renderRequest, renderResponse ) +
                "\">�� ������� ��������</a></p>" );

        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e1 ) {
            }

            final String es = "Error upload image from url";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }
}
