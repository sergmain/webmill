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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.portlet.job.JobItemType;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

/**
 * $Id$
 */
public final class JobItemSimple implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( JobItemSimple.class );

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
//    private ResourceBundle bundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
    }

    public PortletResultContent getInstance(DatabaseAdapter db__) {
        return null;
    }

    public JobItemSimple() {
    }

    private String getJobDatePost() throws Exception {
        return DateTools.getStringDate(datePost, "dd.MMM.yyyy", renderRequest.getLocale());
    }

    public String getJobDateEnd() throws Exception {
        return DateTools.getStringDate(dateEnd, "dd.MMM.yyyy", renderRequest.getLocale());
    }

    public String getTextJob() {
        return
            StringTools.replaceStringArray(
                textJob,
                new String[][]
                {{"<", "&lt"},
                 {">", "&gt"},
                 {"\t", "&nbsp;&nbsp;&nbsp;&nbsp;"},
                 {"\n", "<br>\n"}
                }
            );
    }

    public String getJobNameString()
    {
        return "Вакансия";
    }

    public String getDatePostString()
    {
        return "Дата размещения";
    }

    public String getDateEndString()
    {
        return "Действительна до";
    }

    public String getAgeString()
    {
        return "Возраст";
    }

    public String getAgeFromString()
    {
        return "от";
    }

    public String getAgeTillString()
    {
        return "до";
    }

    public String getGenderString()
    {
        return "Пол";
    }

    public String getEducationString()
    {
        return "Образование";
    }

    public String getSalaryString()
    {
        return "Зарплата от";
    }

    public String getCityString()
    {
        return "Город";
    }

    public String getTestPeriodString()
    {
        return "Испытательный срок";
    }

    public String getContactPersonString()
    {
        return "Контакт по данной вакансии";
    }

    public String getTextJobString()
    {
        return "Проффесиональные навыки:";
    }


    private static String sqlJobPosition =
            "select a.id_job_position, a.date_post, a.period_activity, " +
            "a.age_from, a.age_to, " +
            "b.sex_name, c.name_education, " +
            "a.salary, a.salary_comment, a.city_position, " +
            "a.id_contact_person, a.test_period, " +
            "a.name_position, a.contact_person " +

            "from	WM_JOB_POSITION a, WM_JOB_GENDER_LIST b, WM_JOB_TYPE_EDUCATION c  " +

            "where	a.id_job_position = ? and " +
            "	a.id_job_sex_list = b.id_job_sex_list and " +
            "	a.id_job_type_education=c.id_job_type_education " ;

    private static String sqlJobPositionData =
            "select TEXT_POSITION " +
            "from   WM_JOB_POSITION_DATA " +
            "where  ID_JOB_POSITION=? " +
            "order by ID_JOB_POSITION_DATA asc";

    public boolean isFound = false;

    public Long idPosition = null;

// Вакансия
    public String jobName = "";

    private Integer periodActivity = null;

// Дата размещения
    public Calendar datePost = null;


// Действительна до
    public Calendar dateEnd = null;

// Возраст от
    public Integer ageFrom = null;

// Возраст до
    public Integer ageTill = null;

// Пол</td>
    public String gender = "";

// Образование</td>
    public String nameEducation = "";

// Зарплата от</td>
    public Float salary = null;

// Зарплата, комментарий
    public String salaryComment = "";

// Город</td>
    public String cityPosition = "";

// Испытательный срок</td>
    public String testPeriod = "";

// Контакт по данной вакансии</td>
    public String contactPerson = "";

//Проффесиональные навыки:
    public String textJob = "";

    public PortletResultContent getInstance(Long id_) throws PortletException{
        if (log.isDebugEnabled()) log.debug("Create job item");

        this.idPosition = id_;

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            if (log.isDebugEnabled())
                log.debug("query db for job item");

            ps = db_.prepareStatement(sqlJobPosition);
            RsetTools.setLong(ps, 1, this.idPosition);
            rs = ps.executeQuery();

            if (log.isDebugEnabled())
                log.debug("query processed");

            if (rs.next())
            {
                if (log.isDebugEnabled())
                    log.debug("#1.1");

                this.jobName = RsetTools.getString(rs, "name_position");
                this.datePost = RsetTools.getCalendar(rs, "date_post");

                this.periodActivity = RsetTools.getInt(rs, "period_activity", 30);
                this.dateEnd = this.datePost;
                this.dateEnd.add( Calendar.HOUR_OF_DAY,  this.periodActivity *24 );

                this.ageFrom = RsetTools.getInt(rs, "age_from", 16);
                this.ageTill = RsetTools.getInt(rs, "age_to", 65);
                this.gender = RsetTools.getString(rs, "sex_name");
                this.nameEducation = RsetTools.getString(rs, "name_education");
                this.salary = RsetTools.getFloat(rs, "salary");
                this.salaryComment = RsetTools.getString(rs, "salary_comment");
                this.cityPosition = RsetTools.getString(rs, "city_position");
                this.testPeriod = RsetTools.getString(rs, "test_period");
                this.contactPerson = RsetTools.getString(rs, "contact_person");

                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;

                if (log.isDebugEnabled())
                    log.debug("#1.2");

                ps = db_.prepareStatement(sqlJobPositionData);
                RsetTools.setLong(ps, 1, this.idPosition);
                rs = ps.executeQuery();
                while (rs.next())
                {
                    this.textJob +=
                            RsetTools.getString(rs, "text_position");
                }

                if (log.isDebugEnabled())
                    log.debug("#1.3");

                return this;
            } // if (rs.next())
            else
            {
                if (log.isDebugEnabled())
                    log.debug("Item not found");

                return null;
            }

        }
        catch (Throwable e){
            String es = "Error get position";
            log.error(es, e);
            throw new PortletException(es,e);
        }
        finally{
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    public PortletResultContent getInstanceByCode( String portletCode_) {
        return null;
    }


    public byte[] getPlainHTML()
    {
        return null;
    }

    public byte[] getXml(String rootElement)
        throws Exception
    {
        JobItemType job = new JobItemType();

        job.setAgeFrom( this.ageFrom );
        job.setAgeFromString( this.getAgeFromString() );
        job.setAgeString( this.getAgeString() );
        job.setAgeTill( this.ageTill );
        job.setAgeTillString( this.getAgeTillString() );
        job.setCity( this.cityPosition );
        job.setCityString( this.getCityString() );
        job.setContactPerson( this.contactPerson );
        job.setContactPersonString( this.getContactPersonString() );
        job.setDateEnd( this.getJobDateEnd() );
        job.setDatePost( this.getJobDatePost() );
        job.setEducationString( this.getEducationString() );
        job.setGender( this.gender );
        job.setGenderString( this.getGenderString() );
        job.setDateEndString( this.getDateEndString() );
        job.setDatePostString( this.getDatePostString() );
        job.setJobName( this.jobName );
        job.setJobNameString( this.getJobNameString() );
        job.setEducation( this.nameEducation );
        job.setSalary( this.salaryComment );
        job.setSalaryString( this.getSalaryString() );
        job.setTestPeriod( this.testPeriod );
        job.setTestPeriodString( this.getTestPeriodString() );
        job.setTextJob( this.textJob );
        job.setTextJobString( this.getTextJobString() );

        return XmlTools.getXml( job, rootElement );
    }

    public byte[] getXml()
        throws Exception
    {
        return getXml("JobItemSimple");
    }

    public List getList(Long idSiteCtxLangCatalog, Long idContext)
    {
        return null;
    }

}