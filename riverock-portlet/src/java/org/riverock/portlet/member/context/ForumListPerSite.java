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

 * Date: Jan 12, 2003

 * Time: 8:11:48 PM

 *

 * $Id$

 */

package org.riverock.portlet.member.context;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.List;

import java.util.Vector;



import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.member.ClassQueryItem;

import org.riverock.webmill.portlet.PortletGetList;



import org.apache.log4j.Logger;



public class ForumListPerSite implements PortletGetList

{

    private static Logger log = Logger.getLogger("org.riverock.portlet.member.context.ForumListPerSite");



    public ForumListPerSite(){}



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        if (log.isDebugEnabled())

            log.debug("Get list of Forum. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);





        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        Vector v = new Vector();

        try {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement(

                    "SELECT b.ID_FORUM, b.NAME_FORUM, b.ID_SITE "+

                    "FROM site_ctx_lang_catalog a, MAIN_FORUM b, site_support_language c "+

                    "where a.ID_SITE_CTX_LANG_CATALOG=? and "+

                    "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+

                    "c.ID_SITE=b.ID_SITE"

            );



            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );



            rs = ps.executeQuery();

            while (rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_FORUM");

                String name = "" + id + ", " + RsetTools.getString(rs, "NAME_FORUM");



                ClassQueryItem item =

                        new ClassQueryItem(id, StringTools.truncateString(name, 60) );



                if (item.index.equals(idContext))

                    item.isSelected = true;



                v.add( item );

            }

            return v;



        }

        catch(Exception e)

        {

            log.error("Get list of Forum. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);

            return null;

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

    }

}

