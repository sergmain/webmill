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
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.common.utils.PortletUtils;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 1:32:02 PM
 * <p/>
 * $Id: ImageUploadPortlet.java 1229 2007-06-28 11:25:40Z serg_main $
 */
public final class ImageUploadPortlet implements Portlet {

    private final static Logger log = Logger.getLogger( ImageUploadPortlet.class );

    public ImageUploadPortlet() {
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

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.upload_image" ) ) {
                throw new PortletSecurityException( "You have not enough right" );
            }

            if ( log.isDebugEnabled() )
                log.debug( "Start commit new image from file" );

            String index_page = PortletUtils.url( "mill.image.index", renderRequest, renderResponse );

            if ( log.isDebugEnabled() )
                log.debug( "right to commit image - " + auth_.isUserInRole( "webmill.upload_image" ) );


            PortletSession sess = renderRequest.getPortletSession( true );
            if ( ( sess.getAttribute( "MILL.IMAGE.ID_MAIN" ) == null ) ||
                ( sess.getAttribute( "MILL.IMAGE.DESC_IMAGE" ) == null ) ) {
                out.write("Not all parametrs initialized");
//                renderResponse.sendRedirect( index_page );
                return;
            }

            Long id_main = (Long)sess.getAttribute( "MILL.IMAGE.ID_MAIN" );
            String desc = ( (String)sess.getAttribute( "MILL.IMAGE.DESC_IMAGE" ) );

            if ( log.isDebugEnabled() )
                log.debug( "image description " + desc );

            // Todo this sequence not work
            // 'cos this sequence was used only for recieve unique file name
//            CustomSequence seq = new CustomSequence();
//            seq.setSequenceName( "seq_image_number_file" );
//            seq.setTableName( "MAIN_FORUM_THREADS" );
//            seq.setColumnName( "ID_THREAD" );
            Long currID=null;
//            currID = dbDyn.getSequenceNextValue( seq );

            // Todo xxx work around with hacked URL - "../../.."
            String storage_ = portletConfig.getPortletContext().getRealPath("/") + File.separatorChar + "image";
            String fileName =
                storage_ + File.separator + StringTools.appendString( "" + currID, '0', 7, true ) + "-";

            if ( log.isDebugEnabled() )
                log.debug( "image fileName " + fileName );

            String newFileName = "";
            String supportExtension[] = {".jpg", ".jpeg", ".gif", ".png"};
            try {
                // Todo need fix
                if ( true ) throw new Exception( "Todo need fix" );
//                    newFileName =
//                            UploadFile.save(request, 1024 * 128, fileName, true,
//                                    supportExtension);
            }
            catch( Exception e ) {
                log.error( "Error save image to disk", e );
                out.write( "<html><head></head<body>" +
                    "Error while processing this page:<br>" +
                    ExceptionTools.getStackTrace( e, 20, "<br>" ) + "<br>" +
                    "<p><a href=\"" + index_page + "\">continue</a></p>" +
                    "</body></html>" );
                return;
            }

            if ( log.isDebugEnabled() )
                log.debug( "newFileName " + newFileName );


            User userInfo = auth_.getUser();

/*
            CustomSequence seqImageDir = new CustomSequence();
            seqImageDir.setSequenceName( "seq_WM_image_dir" );
            seqImageDir.setTableName( "WM_IMAGE_DIR" );
            seqImageDir.setColumnName( "ID_IMAGE_DIR" );
            Long seqValue = dbDyn.getSequenceNextValue( seqImageDir );

            ps = dbDyn.prepareStatement( "insert into WM_IMAGE_DIR " +
                "( ID_IMAGE_DIR, ID_FIRM, is_group, id, id_main, name_file, description )" +
                "(?, ?, 0, ?, ?, ?, ?" );

            RsetTools.setLong( ps, 1, seqValue );
            RsetTools.setLong( ps, 2, userInfo.getCompanyId() );
            RsetTools.setLong( ps, 3, currID );
            RsetTools.setLong( ps, 4, id_main );
            ps.setString( 5, "/image/" + newFileName );
            ps.setString( 6, desc );

            ps.executeUpdate();

            dbDyn.commit();
*/

            if ( log.isDebugEnabled() )
                log.debug( "redirect to indexPage - " + index_page );

            out.write( "Image successful uploaded" );
//            renderResponse.sendRedirect( index_page );
            return;

        }
        catch( Exception e ) {
            final String es = "Error upload image";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }
}

