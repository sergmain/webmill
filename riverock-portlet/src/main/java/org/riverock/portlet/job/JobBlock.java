/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.job;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.portlet.schema.portlet.job.JobBlockType;
import org.riverock.portlet.schema.portlet.job.JobItemType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.ContainerConstants;

/**
 * $Id$
 */
public class JobBlock implements PortletResultObject, PortletGetList, PortletResultContent {
    private static Logger log = Logger.getLogger( JobBlock.class );

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
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, siteId );
            rs = ps.executeQuery();

            while( rs.next() ) {
                if( log.isDebugEnabled() )
                    log.debug( "#10.01.04 " + RsetTools.getLong( rs, "ID_JOB_POSITION" ) );

                JobItem item = JobItem.getInstance(
                    db_, RsetTools.getLong( rs, "ID_JOB_POSITION" ), siteId
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
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
        return this;
    }

    public byte[] getPlainHTML() {
        return null;
    }

    private static Object syncDebug = new Object();

    public byte[] getXml( String rootElement ) throws Exception {
        JobBlockType block = new JobBlockType();
        Iterator<JobItem> iterator = v.iterator();
        while( iterator.hasNext() ) {
            JobItem ji = iterator.next();
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

            job.setDatePost( DateTools.getStringDate( ji.getJobDatePost(), "dd.MMM.yyyy",
                renderRequest.getLocale() ) );
            job.setDateEnd( DateTools.getStringDate( ji.getJobDateEnd(), "dd.MMM.yyyy",
                renderRequest.getLocale() ) );

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

        if( log.isDebugEnabled() ) {
            synchronized( syncDebug ) {
                try {
                    XmlTools.writeToFile( block, SiteUtils.getTempDir() + File.separatorChar + "test-job-block.xml", "utf-8", rootElement );
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
}