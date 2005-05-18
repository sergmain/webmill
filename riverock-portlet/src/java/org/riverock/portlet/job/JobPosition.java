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
package org.riverock.portlet.job;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.site.SiteListSite;
import org.riverock.common.tools.RsetTools;

public final class JobPosition {
    private final static Logger log = Logger.getLogger( JobPosition.class );

    private static String sqlJobPosition =
        "select a.id_job_position, a.date_post, (a.date_post + a.period_activity) date_end, " +
        "a.age_from, a.age_to, b.sex_name, c.name_education, " +
        "a.salary, a.salary_comment, a.city_position, " +
        "a.id_contact_person, a.test_period, " +
        "a.name_position, a.contact_person " +
        "from	job_position a, job_sex_list b, job_type_education c, " +
        "	SITE_LIST_SITE e  " +
        "where	a.id_job_position = ? and " +
        "	a.id_job_sex_list = b.id_job_sex_list and " +
        "	a.id_job_type_education=c.id_job_type_education and " +
        "	a.ID_FIRM=e.ID_FIRM and e.ID_SITE=? ";

    private static String sqlJobPositionData =
            "select TEXT_POSITION " +
            "from  JOB_POSITION_DATA " +
            "where ID_JOB_POSITION=? " +
            "order by ID_JOB_POSITION_DATA asc";

    public boolean isFound = false;

    public Long idPosition = null;

// Вакансия
    public String namePosition = "";

// Дата размещения
    public Calendar datePost = null;

// Действительна до
    public Calendar dateEnd = null;

// Возраст от
    public int ageFrom = 0;

// Возраст до
    public int ageTill = 0;

// Пол</td>
    public String gender = "";

// Образование</td>
    public String nameEducation = "";

// Зарплата от</td>
    public float salary = 0;

// Зарплата, комментарий
    public String salaryComment = "";

// Город</td>
    public String cityPosition = "";

// Испытательный срок</td>
    public String testPeriod = "";

// Контакт по данной вакансии</td>
    public String contactPerson = "";

//Проффесиональные навыки:
    public String textPosition = "";

    public JobPosition(){}
/*
private static JobPosition dummy = new JobPosition();
public int maxCountItems(){ return 30; };
public long maxTimePeriod(){ return 1000000; };
public String getNameClass(){ return "org.riverock.job.JobPosition"; };

public void terminate(Long id)
{
cat.debug("#7.17.001  id "+id);
reinit();
}

public static JobPosition getInstance(DatabaseAdapter db__, Long id__)
throws Exception
{
return (JobPosition) dummy.getInstanceNew( db__, id__.longValue());

// return getInstance( db__, id__.longValue());
}

public static JobPosition getInstance(DatabaseAdapter db__, long id__)
throws Exception
{
return (JobPosition) dummy.getInstanceNew( db__, id__);
}
*/

    public JobPosition(DatabaseAdapter db_, Long id_, String serverName)
            throws Exception
    {
        if (id_ == null)
            return;

        idPosition = id_;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            Long idSite = SiteListSite.getIdSite(serverName );

            ps = db_.prepareStatement(sqlJobPosition);
            RsetTools.setLong(ps, 1, idPosition);
            RsetTools.setLong(ps, 2, idSite);
//            ps.setString(2, serverName);
            rs = ps.executeQuery();

            if (rs.next())
            {
                isFound = true;

                namePosition = RsetTools.getString(rs, "name_position", "&nbsp;");
                datePost = RsetTools.getCalendar(rs, "date_post");
                dateEnd = RsetTools.getCalendar(rs, "date_end");
                ageFrom = RsetTools.getInt(rs, "age_from", new Integer(16) ).intValue();
                ageTill = RsetTools.getInt(rs, "age_to", new Integer(65) ).intValue();
                gender = RsetTools.getString(rs, "sex_name");
                nameEducation = RsetTools.getString(rs, "name_education");
                salary = RsetTools.getFloat(rs, "salary", new Float(0)).floatValue();
                salaryComment = RsetTools.getString(rs, "salary_comment");
                cityPosition = RsetTools.getString(rs, "city_position");
                testPeriod = RsetTools.getString(rs, "test_period");
                contactPerson = RsetTools.getString(rs, "contact_person");

                rs.close();
                rs = null;
                ps.close();
                ps = null;

                ps = db_.prepareStatement(sqlJobPositionData);

                RsetTools.setLong(ps, 1, idPosition);
                rs = ps.executeQuery();
                while (rs.next())
                {
                    textPosition +=
                            RsetTools.getString(rs, "text_position");
                }

            } // if (rs.next())

        }
        catch (Exception e)
        {
            log.error("Error get position", e);
            throw new Exception(e.toString());
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

    }

}