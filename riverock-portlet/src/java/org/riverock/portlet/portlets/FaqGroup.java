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

 *

 * Author: Serg Malyukov

 * Date: Aug 24, 2002

 * Time: 2:41:03 AM

 *

 * $Id$

 *

 */

package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.List;

import java.util.ArrayList;



import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.main.CacheFactory;

import org.riverock.portlet.portlets.FaqItem;

import org.riverock.webmill.portlet.PortletGetList;



import org.apache.log4j.Logger;



public class FaqGroup implements PortletGetList

{

    private static Logger log = Logger.getLogger( FaqGroup.class );



    public String faqGroupName= "";

    public Long id = null;

    public String faqCode = "";

    public List v = new ArrayList(); // List of FaqItem



    protected void finalize() throws Throwable

    {

        faqGroupName = null;

        faqCode = null;



        if (v != null)

        {

            v.clear();

            v = null;

        }



//        param = null;



        super.finalize();

    }



/*

    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

        for (int i=0;i<v.size(); i++)

        {

            PortletParameterSetter obj = (PortletParameterSetter)v.elementAt(i);

            obj.setParameter( param_ );

        }

    }

*/



    public String getFaqGroupName()

    {

        return faqGroupName;

    }



    private static CacheFactory cache = new CacheFactory( FaqGroup.class.getName() );



    public List getFaqItem()

    {

        return v;

    }



    public FaqGroup()

    {

    }



    public static FaqGroup getInstance(DatabaseAdapter db__, long id__)

            throws Exception

    {

        return getInstance(db__, new Long(id__) );

    }



    public static FaqGroup getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        return (FaqGroup) cache.getInstanceNew(db__, id__);

    }



/*

    public static FaqGroup getFaqGroup(DatabaseAdapter db__, String faqCode_, String serverName)

            throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("#10.01.01 " + faqCode_);



        int cacheIdx = dummy.getIndex();



        if (log.isDebugEnabled())

            log.debug("#10.01.02 " + cacheIdx);



        dummy.checkFullInitArray(cacheIdx);



        for (int i = 0; i < dummy.maxCountItems(); i++)

        {



            if (cache[cacheIdx][i] != null &&

                    ((FaqGroup) cache[cacheIdx][i]).faqCode.equals(faqCode_))

            {

                if (log.isDebugEnabled())

                    log.debug("#10.01.03");



                return (FaqGroup) cache[cacheIdx][i];

            }

        }



        String sql_ =

            "select a.ID_SITE_PORTLET_FAQ " +

            "from SITE_PORTLET_FAQ a, SITE_SUPPORT_LANGUAGE b " +

            "where a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and " +

            "b.ID_SITE=? and b.CUSTOM_LANGUAGE=? and a.FAQ_CODE=?";



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            long idSite = SiteListSite.getIdSite(serverName);



            ps = db__.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite);

            ps.setString(2, param.ctxInstance.getPortletRequest().getLocale().toString());

            ps.setString(3, faqCode_);



            rs = ps.executeQuery();

            if (rs.next())

            {

                if (log.isDebugEnabled())

                    log.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ"));



                return getInstance(db__, RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ"));

            }



            if (log.isDebugEnabled())

                log.debug("#10.01.05 ");



            // return dummy FaqGroup

            return getInstance(db__, -1);

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



    }

*/



    static String sql_ = null;

    static

    {

        sql_ =

            "select FAQ_CODE, NAME_FAQ from SITE_PORTLET_FAQ where ID_SITE_PORTLET_FAQ=?";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new FaqGroup().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    public FaqGroup(DatabaseAdapter db_, Long id_)

            throws Exception

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        id = id_;

        try

        {

            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id);



            rs = ps.executeQuery();

            if (rs.next())

            {

                faqGroupName = RsetTools.getString(rs, "NAME_FAQ");

                faqCode = RsetTools.getString(rs, "FAQ_CODE");



                initList();

            }

        }

        catch(Exception e)

        {

            log.error("Exception in FaqGroup()", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in FaqGroup()", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    static String sql1_ = null;

    static

    {

        sql1_ =

            "select * from SITE_PORTLET_FAQ_LIST where ID_SITE_PORTLET_FAQ=? " +

            "ORDER BY ORDER_FIELD asc, DATE_POST DESC";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql1_, new FaqGroup().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql1_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql1_, e);

        }

    }



    public void initList()

            throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("#10.07.01 " + id);



        if (id == null)

            return;



        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        if (log.isDebugEnabled())

            log.debug("#10.07.02 ");



        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement(sql1_);

            RsetTools.setLong(ps, 1, id);



            rs = ps.executeQuery();

            while (rs.next())

            {

                Long idFaqGroupItem = RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ_LIST");



                if (log.isDebugEnabled())

                    log.debug("#10.07.03 " + idFaqGroupItem);



                FaqItem fi = new FaqItem(db_, idFaqGroupItem);

                fi.datePost = RsetTools.getCalendar(rs, "DATE_POST");

                v.add(fi);

            }

        }

        finally

        {

            DatabaseManager.close( db_, rs, ps );

            rs = null;

            ps = null;

            db_ = null;

        }

    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

}