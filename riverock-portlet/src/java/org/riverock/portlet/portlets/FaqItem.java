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

 * $Id$

 */

package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Calendar;

import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.StringTools;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.portlet.portlets.utils.RsetUtils;



import org.apache.log4j.Logger;



public class FaqItem implements PortletGetList

{

    private static Logger log = Logger.getLogger( FaqGroup.class );



    public String question = null;

    public String answer = null;

    public Calendar datePost = null;



    public void terminate(java.lang.Long id_){}



    protected void finalize() throws Throwable

    {

        question = null;

        answer = null;

        datePost = null;

//        param = null;



        super.finalize();

    }



    public FaqItem(String q, String a)

    {

        question = q;

        answer = a;

    }



//    public void setParameter(PortletParameter param_)

//    {

//        this.param = param_;

//    }



    public FaqItem(){}



/*

    public String getFaqItemDate()

    {

        return DateTools.getStringDate(datePost, "dd.MMM.yyyy", param.ctxInstance.page.currentLocale);

    }



    public String getFaqItemTime()

    {

        return DateTools.getStringDate(datePost, "HH:mm", param.ctxInstance.page.currentLocale);

    }

*/



    public String getFaqItemQuestion()

    {

        return StringTools.prepareToParsing( question);

    }



    public String getFaqItemAnswer()

    {

        return StringTools.prepareToParsing( answer );

    }



    public static FaqItem getInstance(DatabaseAdapter db_, Long id)

    {

        try

        {

            return new FaqItem(db_, id);

        }

        catch (Exception e)

        {

        }

        return null;

    }



    public FaqItem(DatabaseAdapter db_, Long id)

        throws PortletException

    {

        if ( id==null)

            return;



        String sql_ =

            "select DATE_POST " +

            "from SITE_PORTLET_FAQ_LIST " +

            "where ID_SITE_PORTLET_FAQ_LIST=?";



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id);



            rs = ps.executeQuery();

            if (rs.next())

            {

                datePost = RsetTools.getCalendar(rs, "DATE_POST");

            }

            answer = RsetUtils.getBigTextField(db_, id, "ANSWER", "SITE_PORTLET_FAQ_ANSWER", "ID_SITE_PORTLET_FAQ_LIST", "ID_SITE_PORTLET_FAQ_ANSWER");

            question = RsetUtils.getBigTextField(db_, id, "QUESTION", "SITE_PORTLET_FAQ_QUESTION", "ID_SITE_PORTLET_FAQ_LIST", "ID_SITE_PORTLET_FAQ_QUESTION");

        }

        catch(SQLException exc)

        {

            log.error("Error create Faq item", exc);

            throw new PortletException(exc.getMessage());

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

}