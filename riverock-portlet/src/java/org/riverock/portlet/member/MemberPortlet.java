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
package org.riverock.portlet.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.io.Writer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.tools.RequestTools;



/**
 * User: Admin
 * Date: Nov 25, 2002
 * Time: 8:42:34 PM
 *
 * $Id$
 */
public final class MemberPortlet  implements Portlet {

    private final static Log log = LogFactory.getLog( MemberPortlet.class );

    public MemberPortlet() {
    }

    private PortletConfig portletConfig = null;
    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse ) throws PortletException {

        try {

            String applicationCode = RequestTools.getString(renderRequest, MemberConstants.MEMBER_NAME_APPL_PARAM);
            String moduleCode = RequestTools.getString(renderRequest, MemberConstants.MEMBER_NAME_MOD_PARAM);

            if (log.isDebugEnabled())
            {
                log.debug("applicationCode " + applicationCode);
                log.debug("moduleCode " + moduleCode);
            }

            PreparedStatement ps = null;
            ResultSet rs = null;
            DatabaseAdapter db_ = null;
            try
            {
                db_ = DatabaseAdapter.getInstance();
                ps = db_.prepareStatement(
                    "select a.is_new, a.url " +
                    "from auth_object_arm a, auth_arm b " +
                    "where a.id_arm = b.id_arm and b.CODE_ARM=? and a.CODE_OBJECT_ARM=? "
                );
                ps.setString(1, applicationCode);
                ps.setString(2, moduleCode);
                rs = ps.executeQuery();
                if (rs.next())
                {

                    String url = RsetTools.getString(rs, "URL");
                    int isNew = RsetTools.getInt( rs, "IS_NEW", 0);
                    String fullUrl = null;
//                    Map parameterMap = null;
                    if  (isNew==1)
                    {
                        fullUrl = "/member_view?" + url.substring( url.indexOf('?')+1)+ '&';
//                        parameterMap = ServletTools.getParameterMap(fullUrl);
                    }
                    else
                    {
                        fullUrl = url+'?';
//                        parameterMap = new HashMap();
                    }

/*
                    if (cat.isDebugEnabled() && isNew==0)
                    {
                        String nameFile = InitParam.millApplPath + fullUrl;


                        cat.debug("Full url - " + fullUrl);
                        cat.debug("millApplPath - " + InitParam.millApplPath);
                        cat.debug("nameFile - " + nameFile);

                        if (nameFile.indexOf('?') != -1)
                            nameFile = nameFile.substring(0, nameFile.indexOf('?'));

                        cat.debug("end nameFile - " + nameFile);
                    }
*/

                    PortletRequestDispatcher dispatcher = portletConfig.getPortletContext().getRequestDispatcher( fullUrl );

                    if (log.isDebugEnabled()) {
                        log.debug("RequestDispatcher - " + dispatcher);
                        log.debug("Method is 'include'. Url - " + fullUrl);
                    }

                    if (dispatcher == null) {
                        log.error("RequestDispatcher is null");
                        Writer out = renderResponse.getWriter();
                        out.write("Error get dispatcher for path " + fullUrl);
                    }
                    else {
                        if (log.isDebugEnabled()) {
                            log.debug("renderRequest session - "+renderRequest.getPortletSession());
                            for (Enumeration e = renderRequest.getParameterNames(); e.hasMoreElements();) {
                                String s = (String) e.nextElement();
                                log.debug("Request attr - " + s + ", value - " + RequestTools.getString(renderRequest, s));
                            }
                        }
                        dispatcher.include( renderRequest, renderResponse );
                    }
                    return;
                }
                else {
                    String errorSttring = " application '" + applicationCode + "'  module '" + moduleCode + "' not found";
                    log.info(errorSttring);
                    Writer out = renderResponse.getWriter();
                    out.write(errorSttring);
                }
            }
            finally {
                DatabaseManager.close( db_, rs, ps );
                rs = null;
                ps = null;
                db_ = null;
            }
        }
        catch (Exception e) {
            String es = "Error in MemberPortlet";
            log.error( es, e );
            throw new PortletException( es, e );
        }
    }
}
