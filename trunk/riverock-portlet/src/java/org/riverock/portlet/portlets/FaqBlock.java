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



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.schema.portlet.faq.FaqBlockType;

import org.riverock.portlet.schema.portlet.faq.FaqGroupType;

import org.riverock.portlet.schema.portlet.faq.FaqItemType;

import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.tools.XmlTools;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;



import org.apache.log4j.Logger;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;

import java.util.List;



public class FaqBlock implements Portlet, PortletResultObject, PortletGetList, PortletParameterSetter

{

    private static Logger cat = Logger.getLogger( FaqBlock.class );



    public List v = null;

    public PortletParameter param = null;



    protected void finalize() throws Throwable

    {

        if (v != null)

        {

            v.clear();

            v = null;

        }



        param = null;



        super.finalize();

    }



    public boolean isXml(){ return true; }

    public boolean isHtml(){ return true; }



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public PortletResultObject getInstance(DatabaseAdapter db__, Long id) throws Exception

    {

        return getInstance(db__);

    }



    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String portletCode_) throws Exception

    {

        return getInstance(db__);

    }



    public List getFaqGroup()

    {

        return v;

    }



    public FaqBlock()

    {

    }



    public PortletResultObject getInstance(DatabaseAdapter db_)

            throws PortletException

    {

        String sql_ =

/*

            "select a.ID_SITE_PORTLET_FAQ " +

            "from SITE_PORTLET_FAQ a, SITE_SUPPORT_LANGUAGE b " +

            "where a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and " +

            "b.ID_SITE=? and b.CUSTOM_LANGUAGE=?";

*/

            "select ID_SITE_PORTLET_FAQ from SITE_PORTLET_FAQ where ID_SITE_SUPPORT_LANGUAGE=?";



        v = new Vector();



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

//            long idSite = SiteListSite.getIdSite( param.ctxInstance.getPortletRequest().getServerName() );



            ps = db_.prepareStatement(sql_);



            RsetTools.setLong(ps, 1,

                param.getPage().p.getIdSupportLanguage(param.getPortletRequest().getLocale())

            );



//            RsetTools.setLong(ps, 1, idSite);

//            ps.setString(2, param.ctxInstance.getPortletRequest().getLocale().toString());



            rs = ps.executeQuery();

            while (rs.next())

            {

                cat.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ"));

                v.add(FaqGroup.getInstance(db_, RsetTools.getLong(rs, "ID_SITE_PORTLET_FAQ")));

            }



            cat.debug("#10.01.05 ");

        }

        catch (Exception e)

        {

            cat.error("Error get faq block ", e);

            throw new PortletException(e.toString());

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

        return this;

    }



    public byte[] getPlainHTML()

    {

        String s = "";

        for (int i = 0; i < v.size(); i++)

        {

            FaqGroup faqGroup = (FaqGroup) v.get(i);

            s += faqGroup.faqGroupName + "<br>";



            s += "<table border=\"0\">";



            for (int j = 0; j < faqGroup.v.size(); j++)

            {

                FaqItem fi = (FaqItem) faqGroup.v.get(j);



                s += "<tr><td>" + fi.question + "</td><td>" + fi.answer + "</td></tr>";

            }

            s += "</table>";

        }

        return s.getBytes();

    }



    public byte[] getXml(String rootElement) throws Exception

    {

        FaqBlockType faqBlock = new FaqBlockType();



        for (int i = 0; i < v.size(); i++)

        {

            FaqGroup faqGroup = (FaqGroup) v.get(i);

            FaqGroupType group_ = new FaqGroupType();

            group_.setFaqGroupName( faqGroup.faqGroupName );



            for (int j = 0; j < faqGroup.v.size(); j++)

            {

                FaqItem fi = (FaqItem) faqGroup.v.get(j);

                FaqItemType item_ = new FaqItemType();

                item_.setFaqItemAnswer( fi.answer );

                item_.setFaqItemQuestion( fi.question );

                item_.setFaqItemDate(

                    DateTools.getStringDate( fi.datePost, "dd.MMM.yyyy", param.getPortletRequest().getLocale())

                );

                item_.setFaqItemTime(

                    DateTools.getStringDate( fi.datePost, "HH:mm", param.getPortletRequest().getLocale())

                );



                group_.addFaqItem( item_ );

            }

            faqBlock.addFaqGroup( group_ );

        }



        return XmlTools.getXml( faqBlock, rootElement );

    }



    public byte[] getXml()

        throws Exception

    {

        return getXml("FaqBlock");

    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

}