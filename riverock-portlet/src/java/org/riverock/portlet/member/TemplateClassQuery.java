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

 * Date: Nov 24, 2002

 * Time: 3:58:34 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import org.apache.log4j.Logger;



import java.util.List;

import java.util.ArrayList;

import java.sql.PreparedStatement;

import java.sql.ResultSet;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;



import javax.servlet.http.HttpServletRequest;



public class TemplateClassQuery extends BaseClassQuery

{

    private static Logger cat = Logger.getLogger( "org.riverock.member.TemplateClassQuery" );



    private Long idSiteCtxLangCatalog = null;



    private Long idSiteTemplate = null;



    public void setIdSiteCtxLangCatalog(Long param)

    {

        idSiteCtxLangCatalog = param;



        if (cat.isDebugEnabled())

            cat.debug("idSiteCtxLangCatalog - "+idSiteCtxLangCatalog.longValue());

    }



    public void setIdSiteTemplate(Long param)

    {

        idSiteTemplate = param;



        if (cat.isDebugEnabled())

            cat.debug("idSiteTemplate - "+idSiteTemplate.longValue());

    }



    /**

     * ¬озвращает текущее значение дл€ отображени€ на веб-странице

     * @return String

     */

    public String getCurrentValue(HttpServletRequest request)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        try {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement(

                "select NAME_SITE_TEMPLATE from SITE_TEMPLATE where ID_SITE_TEMPLATE = ?"

            );



            RsetTools.setLong(ps, 1, idSiteTemplate );



            rs = ps.executeQuery();

            if (rs.next())

                return RsetTools.getString(rs, "NAME_SITE_TEMPLATE");

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }



        return "";

    }



    /**

     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента

     * @return Vector of org.riverock.member.ClassQueryItem

     */

    public List getSelectList(HttpServletRequest request)

        throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;



        List v = new ArrayList();

        DatabaseAdapter db_ = null;

        try {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement(

                "select a.ID_SITE_TEMPLATE, a.NAME_SITE_TEMPLATE "+

                "from SITE_TEMPLATE a, SITE_CTX_LANG_CATALOG b "+

                "where a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and "+

                "b.ID_SITE_CTX_LANG_CATALOG = ?"

            );



            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );



            rs = ps.executeQuery();

            while (rs.next())

            {

                ClassQueryItem item = new ClassQueryItem(

                    RsetTools.getLong(rs, "ID_SITE_TEMPLATE"),

                    RsetTools.getString(rs, "NAME_SITE_TEMPLATE")

                );



                if (item.index.equals( idSiteTemplate ))

                    item.isSelected = true;



                v.add( item );

            }

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }



        return v;

    }



    MemberQueryParameter param = null;



    public void setQueryParameter(MemberQueryParameter parameter) throws Exception

    {

        this.param = parameter;

    }

}

