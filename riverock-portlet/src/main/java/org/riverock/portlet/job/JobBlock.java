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

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.XmlTools;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.job.schema.JobBlockType;
import org.riverock.portlet.job.schema.JobItemType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;
import org.riverock.interfaces.ContainerConstants;

/**
 * $Id: JobBlock.java 1400 2007-09-04 20:25:29Z serg_main $
 */
public class JobBlock implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( JobBlock.class );

    private List<JobItem> v = new ArrayList<JobItem>();
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
    }

    public PortletResultContent getInstance( Long id ) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( String portletCode_ ) throws PortletException {
        return getInstance();
    }

    public List<JobItem> getJobItem() {
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
        try {
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

            RsetTools.setLong( ps, 1, siteId );
            rs = ps.executeQuery();

            while( rs.next() ) {
                if( log.isDebugEnabled() )
                    log.debug( "#10.01.04 " + RsetTools.getLong( rs, "ID_JOB_POSITION" ) );

                JobItem item = JobItem.getInstance(
                    RsetTools.getLong( rs, "ID_JOB_POSITION" ), siteId
                );
                v.add( item );
            }
            if( log.isDebugEnabled() )
                log.debug( "#10.01.05 " );

        }
        catch( Exception e ) {
            log.error( "Error get job block ", e );
            throw new PortletException( e.toString() );
        }
        return this;
    }

    public byte[] getPlainHTML() {
        return null;
    }

    private static final Object syncDebug = new Object();

    public byte[] getXml( String rootElement ) throws Exception {
        JobBlockType block = new JobBlockType();
        for (JobItem ji : v) {
            JobItemType job = new JobItemType();

            job.setAgeFrom(ji.ageFrom);
            job.setAgeFromString(ji.getAgeFromString());
            job.setAgeString(ji.getAgeString());
            job.setAgeTill(ji.ageTill);
            job.setAgeTillString(ji.getAgeTillString());
            job.setCity(ji.cityPosition);
            job.setCityString(ji.getCityString());
            job.setContactPerson(ji.contactPerson);
            job.setContactPersonString(ji.getContactPersonString());

            job.setDatePost(DateTools.getStringDate(ji.getJobDatePost(), "dd.MMM.yyyy",
                renderRequest.getLocale()));
            job.setDateEnd(DateTools.getStringDate(ji.getJobDateEnd(), "dd.MMM.yyyy",
                renderRequest.getLocale()));

            job.setEducationString(ji.getEducationString());
            job.setGender(ji.gender);
            job.setGenderString(ji.getGenderString());
            job.setDateEndString(ji.getDateEndString());
            job.setDatePostString(ji.getDatePostString());
            job.setJobName(ji.jobName);
            job.setJobNameString(ji.getJobNameString());
            job.setEducation(ji.nameEducation);
            job.setSalary(ji.salaryComment);
            job.setSalaryString(ji.getSalaryString());
            job.setTestPeriod(ji.testPeriod);
            job.setTestPeriodString(ji.getTestPeriodString());
            job.setTextJob(ji.textJob);
            job.setTextJobString(ji.getTextJobString());
            job.setUrl(ji.getUrlToJob(renderRequest, renderResponse));

            block.getJobItem().add(job);
        }

        if( log.isDebugEnabled() ) {
            synchronized( syncDebug ) {
                try {
                    XmlTools.writeToFile( block, SiteUtils.getTempDir() + File.separatorChar + "test-job-block.xml", "utf-8" );
                    byte bDebug[] = XmlTools.getXml( block, rootElement );
                    String sDebug = new String( bDebug );

                    MainTools.writeToFile( SiteUtils.getTempDir() + File.separatorChar + "test-job-block-1.xml", sDebug.getBytes() );
                    MainTools.writeToFile( SiteUtils.getTempDir() + File.separatorChar + "test-job-block-2.xml", sDebug.getBytes( "utf-8" ) );
                }
                catch( Throwable exception ) {
                    log.error( "Exception in ", exception );
                }
            }
        }

        return XmlTools.getXml( block, rootElement );
    }

    public byte[] getXml() throws Exception {
        return getXml( "JobBlock" );
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        return null;
    }

    public void setPortalDaoProvider(PortalSpiProvider provider) {
    }
}