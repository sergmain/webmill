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

import java.util.ArrayList;



import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.member.ClassQueryItem;

import org.riverock.webmill.portlet.PortletGetList;



import org.apache.log4j.Logger;



public class LanguagePerSite implements PortletGetList

{

    private static Logger cat = Logger.getLogger("org.riverock.member.context.LanguagePerSite");



    public LanguagePerSite(){}



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        if (cat.isDebugEnabled())

            cat.debug("Get list of Language. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);



        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        List v = new ArrayList();

        try {

            db_ = DatabaseAdapter.getInstance( false );

/*

                    "SELECT b.ID_LANGUAGE, b.SHORT_NAME_LANGUAGE, b.NAME_LANGUAGE "+

                    "FROM site_ctx_lang_catalog a, MAIN_LANGUAGE b, " +

                    "site_support_language c, site_support_language d "+

                    "where a.ID_SITE_CTX_LANG_CATALOG=? and "+

                    "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+

                    "c.ID_SITE=d.ID_SITE and d.ID_LANGUAGE=b.ID_LANGUAGE"

*/

            ps = db_.prepareStatement(

                "SELECT d.ID_SITE_SUPPORT_LANGUAGE, d.CUSTOM_LANGUAGE, d.NAME_CUSTOM_LANGUAGE "+

                "FROM SITE_CTX_LANG_CATALOG a, " +

                "SITE_SUPPORT_LANGUAGE c, SITE_SUPPORT_LANGUAGE d "+

                "where a.ID_SITE_CTX_LANG_CATALOG=? and " +

                "a.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and "+

                "c.ID_SITE=d.ID_SITE"

            );

            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );



            rs = ps.executeQuery();

            while (rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE");

                String name = "" + id + ", " +

                        RsetTools.getString(rs, "CUSTOM_LANGUAGE") + ", " +

                        RsetTools.getString(rs, "NAME_CUSTOM_LANGUAGE");



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

            cat.error("Get list of Language. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);

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

