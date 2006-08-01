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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;



import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.container.tools.PortletService;

/**
 * $Id$
 */
public class JobItem {
    private static Logger log = Logger.getLogger( JobItem.class );

    public Integer periodActivity;

    public JobItem() {
    }

    public String getUrlToJob( RenderRequest renderRequest, RenderResponse renderResponse ) throws ConfigException {
        return PortletService.url(JobBlockPortlet.CTX_TYPE_JOB, renderRequest, renderResponse )+ '&' +
            JobBlockPortlet.NAME_ID_JOB_PARAM + '=' + idPosition;
    }

    public Calendar getJobDatePost() throws Exception {
        return datePost;
    }

    public Calendar getJobDateEnd() {
        return dateEnd;
    }

    public String getTextJob()
    {
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


    public boolean isFound = false;

    public Long idPosition = null;

// Вакансия
    public String jobName = "";

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

    private static String sqlJobPosition =
        "select a.id_job_position, a.date_post, a.date_post, a.period_activity, " +
        "       a.age_from, a.age_to, " +
        "       b.sex_name, c.name_education, " +
        "       a.salary, a.salary_comment, a.city_position, " +
        "       a.id_contact_person, a.test_period, " +
        "       a.name_position, a.contact_person " +
        "from	WM_JOB_POSITION a, WM_JOB_GENDER_LIST b, WM_JOB_TYPE_EDUCATION c, " +
        "       WM_PORTAL_LIST_SITE e  " +
        "where	a.id_job_position = ? and " +
        "	a.id_job_sex_list = b.id_job_sex_list and " +
        "	a.id_job_type_education=c.id_job_type_education and " +
        "	a.ID_FIRM=e.ID_FIRM and e.ID_SITE=? ";

    private static String sqlJobPositionData =
            "select TEXT_POSITION " +
            "from   WM_JOB_POSITION_DATA " +
            "where  ID_JOB_POSITION=? " +
            "order by ID_JOB_POSITION_DATA asc";

    public static JobItem getInstance(DatabaseAdapter db_, Long id_, Long siteId)
            throws Exception
    {
        if (log.isDebugEnabled())
            log.debug("Create job item");

        JobItem item = new JobItem();

        item.idPosition = id_;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            if (log.isDebugEnabled())
                log.debug("query db for job item");

            ps = db_.prepareStatement(sqlJobPosition);
            RsetTools.setLong(ps, 1, item.idPosition);
            RsetTools.setLong(ps, 2, siteId );
            rs = ps.executeQuery();

            if (log.isDebugEnabled())
                log.debug("query processed");

            if (rs.next())
            {
                if (log.isDebugEnabled())
                    log.debug("#1.1");

                item.jobName = RsetTools.getString(rs, "name_position");
                item.datePost = RsetTools.getCalendar(rs, "date_post");
                item.periodActivity = RsetTools.getInt(rs, "period_activity");
                item.dateEnd = item.datePost;
                item.dateEnd.add(
                    Calendar.HOUR_OF_DAY,
                    (item.periodActivity!=null?item.periodActivity:30)*24
                );
                item.ageFrom = RsetTools.getInt(rs, "age_from", 16);
                item.ageTill = RsetTools.getInt(rs, "age_to", 65);
                item.gender = RsetTools.getString(rs, "sex_name");
                item.nameEducation = RsetTools.getString(rs, "name_education");
                item.salary = RsetTools.getFloat(rs, "salary");
                item.salaryComment = RsetTools.getString(rs, "salary_comment");
                item.cityPosition = RsetTools.getString(rs, "city_position");
                item.testPeriod = RsetTools.getString(rs, "test_period");
                item.contactPerson = RsetTools.getString(rs, "contact_person");

                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;

                if (log.isDebugEnabled())
                    log.debug("#1.2");

                ps = db_.prepareStatement(sqlJobPositionData);
                RsetTools.setLong(ps, 1, item.idPosition);
                rs = ps.executeQuery();
                while (rs.next())
                {
                    item.textJob +=
                            RsetTools.getString(rs, "text_position");
                }

                if (log.isDebugEnabled())
                    log.debug("#1.3");

                return item;
            } // if (rs.next())
            else
            {
                if (log.isDebugEnabled())
                    log.debug("Item not found");

                return null;
            }

        }
        catch (Exception e)
        {
            log.error("Error get position", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
}