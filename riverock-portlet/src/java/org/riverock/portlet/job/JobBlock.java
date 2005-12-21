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
package org.riverock.portlet.job;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.portlet.job.JobBlockType;
import org.riverock.portlet.schema.portlet.job.JobItemType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;

/**
 * $Id$
 */
public class JobBlock implements PortletResultObject, PortletGetList, PortletResultContent {
    private static Log log = LogFactory.getLog( JobBlock.class );

    private List v = new ArrayList();
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;

    protected void finalize() throws Throwable {
        if (v != null) {
            v.clear();
            v = null;
        }
        super.finalize();
    }

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
    }

    public PortletResultContent getInstance( Long id) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( String portletCode_) throws PortletException {
        return getInstance();
    }

    public List getJobItem() {
        return v;
    }

    public JobBlock() {
    }

    public PortletResultContent getInstance() throws PortletException {

        final String sql_ =
            "select a.ID_JOB_POSITION " +
            "from   WM_JOB_POSITION a, WM_PORTAL_LIST_SITE c " +
            "where  a.ID_FIRM=c.ID_FIRM and c.ID_SITE=? " +
            "order by a.DATE_POST desc";

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            Long idSite = SiteListSite.getIdSite( renderRequest.getServerName());

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (log.isDebugEnabled())
                    log.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_JOB_POSITION"));

                JobItem item = JobItem.getInstance(
                    db_, RsetTools.getLong(rs, "ID_JOB_POSITION"),
                    renderRequest.getServerName()
                );
                v.add( item );
            }
            if (log.isDebugEnabled())
                log.debug("#10.01.05 ");

        }
        catch (Exception e)
        {
            log.error("Error get job block ", e);
            throw new PortletException(e.toString());
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
        return this;
    }

    public byte[] getPlainHTML()
    {
        return null;
    }

    private static Object syncDebug = new Object();
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

            job.setDatePost(
                DateTools.getStringDate(ji.getJobDatePost(), "dd.MMM.yyyy",
                    renderRequest.getLocale()
                )
            );
            job.setDateEnd(
                DateTools.getStringDate(ji.getJobDateEnd(), "dd.MMM.yyyy",
                    renderRequest.getLocale()
                )
            );

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
            job.setUrl( ji.getUrlToJob( renderRequest, renderResponse ) );

            block.addJobItem( job );
        }

        if (log.isDebugEnabled()) {
            synchronized(syncDebug) {
                try {
                    XmlTools.writeToFile(block, SiteUtils.getTempDir()+File.separatorChar+"test-job-block.xml", "utf-8", rootElement);
                    byte bDebug[] = XmlTools.getXml( block, rootElement );
                    String sDebug = new String(bDebug);

                    MainTools.writeToFile(SiteUtils.getTempDir()+File.separatorChar+"test-job-block-1.xml", sDebug.getBytes());
                    MainTools.writeToFile(SiteUtils.getTempDir()+File.separatorChar+"test-job-block-2.xml", sDebug.getBytes("utf-8"));
                }
                catch (Throwable exception) {
                    log.error("Exception in ", exception);
                }
            }
        }

        return XmlTools.getXml( block, rootElement );
    }

    public byte[] getXml() throws Exception {
        return getXml("JobBlock");
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext) {
        return null;
    }
}