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

import java.util.List;

import java.util.ArrayList;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.schema.portlet.job.JobBlockType;

import org.riverock.portlet.schema.portlet.job.JobItemType;

import org.riverock.common.tools.MainTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.tools.XmlTools;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.generic.site.SiteListSite;



public class JobBlock implements Portlet, PortletResultObject, PortletGetList, PortletParameterSetter

{

    private static Logger cat = Logger.getLogger(JobBlock.class);



    private List v = new ArrayList();

    private PortletParameter param = null;



    private static Object syncDebug = new Object();



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



    public boolean isXml()

    {

        return true;

    }



    public boolean isHtml()

    {

        return false;

    }



    public List getJobItem()

    {

        return v;

    }



    public JobBlock()

    {

    }



    public PortletResultObject getInstance(DatabaseAdapter db_)

        throws PortletException

    {



        final String sql_ =

            "select	a.ID_JOB_POSITION " +

            "from JOB_POSITION a, SITE_LIST_SITE c " +

            "where a.ID_FIRM=c.ID_FIRM and c.ID_SITE=? " +

            "order by a.DATE_POST desc";



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            Long idSite = SiteListSite.getIdSite( param.getRequest().getServerName());



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite);

            rs = ps.executeQuery();



            while (rs.next())

            {

                if (cat.isDebugEnabled())

                    cat.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_JOB_POSITION"));



                JobItem item = JobItem.getInstance(

                    db_, RsetTools.getLong(rs, "ID_JOB_POSITION"),

                    param.getRequest().getServerName()

                );

                item.setParameter( param );

                v.add( item );

            }

            if (cat.isDebugEnabled())

                cat.debug("#10.01.05 ");



        }

        catch (Exception e)

        {

            cat.error("Error get job block ", e);

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

        return null;

    }



    public byte[] getXml(String rootElement) throws Exception

    {

        JobBlockType block = new JobBlockType();

        for (int i = 0; i < v.size(); i++)

        {

            JobItem ji = (JobItem) v.get(i);

            JobItemType job = new JobItemType();



            job.setAgeFrom( ji.ageFrom );

            job.setAgeFromString( ji.getAgeFromString() );

            job.setAgeString( ji.getAgeString() );

            job.setAgeTill( ji.ageTill );

            job.setAgeTillString( ji.getAgeTillString() );

            job.setCity( ji.cityPosition );

            job.setCityString( ji.getCityString() );

            job.setContactPerson( ji.contactPerson );

            job.setContactPersonString( ji.getContactPersonString() );

            job.setDateEnd( ji.getJobDateEnd() );

            job.setDatePost( ji.getJobDatePost() );

            job.setEducationString( ji.getEducationString() );

            job.setGender( ji.gender );

            job.setGenderString( ji.getGenderString() );

            job.setDateEndString( ji.getDateEndString() );

            job.setDatePostString( ji.getDatePostString() );

            job.setJobName( ji.jobName );

            job.setJobNameString( ji.getJobNameString() );

            job.setEducation( ji.nameEducation );

            job.setSalary( ji.salaryComment );

            job.setSalaryString( ji.getSalaryString() );

            job.setTestPeriod( ji.testPeriod );

            job.setTestPeriodString( ji.getTestPeriodString() );

            job.setTextJob( ji.textJob );

            job.setTextJobString( ji.getTextJobString() );

            job.setUrl( ji.getUrlToJob() );



            block.addJobItem( job );

        }



        if (cat.isDebugEnabled())

        {

            synchronized(syncDebug)

            {

                XmlTools.writeToFile(block, WebmillConfig.getWebmillDebugDir()+"test-job-block.xml", "utf-8", rootElement);

                byte bDebug[] = XmlTools.getXml( block, rootElement );

                String sDebug = new String(bDebug);



                MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"test-job-block-1.xml", sDebug.getBytes());

                MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"test-job-block-2.xml", sDebug.getBytes("utf-8"));

            }

        }



        return XmlTools.getXml( block, rootElement );

    }



    public byte[] getXml()

        throws Exception

    {

        return getXml("JobBlock");

    }



    public List getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

}