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

package org.riverock.portlet.forum;



import java.io.File;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.Types;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.List;



import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;



import org.riverock.common.config.ConfigException;

import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



abstract public class Forum

{

    private static Logger log = Logger.getLogger(Forum.class);



    public String forumURI = "";

    public int month = 0;

    public int year = 0;

    public Long id_forum = null;

    public Long id = null;

    public InitPage jspPage = null;

    public static final String SEQ_FORUM_THREADS = "SEQ_MAIN_FORUM_THREADS";

    public static final String SEQ_FORUM_MESSAGE = "SEQ_MAIN_FORUM_MESSAGE";

    public static final String FORUM_THREADS_TABLE = "MAIN_FORUM_THREADS";



    abstract public ForumMessage getForumMessage(DatabaseAdapter db__, Long id__) throws ForumException;



    public Forum(){}



    public Forum(javax.servlet.http.HttpServletRequest request,

        javax.servlet.http.HttpServletResponse response,

        InitPage jspPage)

        throws ForumException

    {

        id = ServletTools.getLong(request, Constants.NAME_ID_MESSAGE_FORUM_PARAM );

        id_forum = ServletTools.getLong(request, Constants.NAME_ID_FORUM_PARAM);

        year = ServletTools.getInt(

            request, Constants.NAME_YEAR_PARAM,

            new Integer(Calendar.getInstance().get(Calendar.YEAR))

        ).intValue();





        forumURI = response.encodeURL(

            new File(request.getRequestURI()).getName());



        this.jspPage = jspPage;



    }



    public static void setCookie(javax.servlet.http.HttpServletRequest request,

        javax.servlet.http.HttpServletResponse response)

        throws ConfigException

    {

        if (ServletUtils.getString(request, "action").equals("add"))

        {

            String email = ServletUtils.getString(request, "e");

            String name = ServletUtils.getString(request, "n");

/*

cat.debug("#10.00.NAME1 "+StringTools.convertString(name,

"UTF-8",

"8859_1"));

cat.debug("#10.00.NAME "+name);

cat.debug("#10.00.EMAIL "+email);

*/

            String name_req = "";

            String email_req = "";



            Cookie[] cookies_req = request.getCookies();

            for (int i = 0; i < cookies_req.length; i++)

            {

                Cookie c = cookies_req[i];

                String name_cookie = c.getName();

                if (name_cookie.equals("_name"))

                {

/*			name_req = StringTools.convertString(c.getValue(),

"8859_1",

"UTF-8");

*/

                    name_req = c.getValue();

                }



                if (name_cookie.equals("_email"))

                    email_req = c.getValue();

            }

//cat.debug("COOKIE.NAME "+name_req);

//cat.debug("COOKIE.EMAIL "+email_req);



            if ((name != null) && !name.equals(name_req))

            {

                Cookie cookie = new Cookie("_name", name);

                cookie.setMaxAge(1 * 365 * 24 * 3600);

                response.addCookie(cookie);

            }



            if ((email != null) && !email.equals(email_req))

            {

                Cookie cookie = new Cookie("_email", email);

                cookie.setMaxAge(1 * 365 * 24 * 3600);

                response.addCookie(cookie);

            }

        }

    }



    public Long getIdThread(Long id_main__)

        throws ForumException

    {



        Long retValue = null;



        String sql_ = "select id_thread from " + FORUM_THREADS_TABLE + " where id=?";



        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;

        try

        {



            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement(sql_);



            if (id_main__!=null)

                RsetTools.setLong(ps, 1, id_main__);

            else

                ps.setNull(1, Types.NUMERIC);



            rs = ps.executeQuery();

            if (rs.next())

                retValue = RsetTools.getLong(rs, "id_thread");



        }

        catch (Exception e)

        {

            log.error(e);

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

        return retValue;

    }



    protected void finalize() throws Throwable

    {

        super.finalize();

    }





    public String getThreads(String nameTemplate)

        throws ForumException

    {

        String sql_ = "";

        PreparedStatement ps = null;

        ResultSet rs = null;

        String s = "";



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            if (id!=null)

            {

                sql_ =

                    "select a.ID_THREAD " +

                    "from "  + FORUM_THREADS_TABLE + " a, " +

                    "( " +

                    "select distinct z.ID_THREAD " +

                    "from " + FORUM_THREADS_TABLE + " z " +

                    "where   z.ID = ? and  " +

                    "to_number(to_char(z.DATE_POST, 'yyyy')) = ? and " +

                    "to_number(to_char(z.DATE_POST, 'mm')) = ? " +

                    ") b " +

                    "where a.ID_THREAD=b.ID_THREAD " +

                    "order by a.DATE_POST DESC ";



                ps = db_.prepareStatement(sql_);

                RsetTools.setLong(ps, 1, id );

                ps.setLong(2, year);

                ps.setLong(3, month);

            }

            else

            {

                sql_ =

                    "select a.ID_THREAD " +

                    "from " + FORUM_THREADS_TABLE + " a, " +

                    "( " +

                    "select distinct z.ID_THREAD " +

                    "from " + FORUM_THREADS_TABLE + " z " +

                    "where   ID_FORUM = ? and " +

                    "to_number(to_char(DATE_POST, 'yyyy')) = ? and " +

                    "to_number(to_char(DATE_POST, 'mm')) = ? " +

                    ") b " +

                    "where a.ID_THREAD=b.ID_THREAD " +

                    "order by a.DATE_POST DESC ";



                ps = db_.prepareStatement(sql_);

                RsetTools.setLong(ps, 1, id_forum );

                ps.setLong(2, year);

                ps.setLong(3, month);

            }



            rs = ps.executeQuery();



            while (rs.next())

            {

                s += getMessages(id,

                    RsetTools.getLong(rs, "ID_THREAD"), new Long(0), new Long(0), nameTemplate);

            }

        }

        catch (Exception e)

        {

            log.error(e);

            log.error("this.class: "+this.getClass().getName());

            log.error("sql: "+sql_);

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

        return s;

    }



    public Integer getFirstMonthInYear()

        throws ForumException

    {

        String sql_ = "";

        PreparedStatement ps = null;

        ResultSet rs = null;



//	int retValue		= 1;

//        Integer retValue = null;



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            sql_ =

                "select max(to_number(to_char(date_post,'mm'))) month " +

                "from " + FORUM_THREADS_TABLE + " " +

                "where id_forum=? and to_char(date_post,'yyyy')=?";



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_forum );

            ps.setString(2, "" + year);



            rs = ps.executeQuery();

            if (rs.next())

            {

                Object obj = rs.getObject("month");

                return (obj == null ? null : new Integer(rs.getInt("month")));

            }

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }



        return null;

    }



    public String getListYears(String nameTemplate)

        throws ForumException

    {

        String s = "";

        PreparedStatement st = null;

        ResultSet rs = null;



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            st = db_.prepareStatement(

                "select distinct to_number(year) year " +

                "from ( select distinct to_char(date_post,'yyyy') year " +

                "        from " + FORUM_THREADS_TABLE + " where id_forum = ? " +

                "        union " +

                "        select to_char(sysdate,'yyyy') year from dual ) " +

                "order by year desc "

            );



            st.setLong(1, id_forum.longValue());



            rs = st.executeQuery();



            while (rs.next())

            {

                int yearValue = RsetTools.getInt(rs, "year").intValue();

                if (year == yearValue)

                    s += ("<b>" + yearValue + "</b>&nbsp;");

                else

                    s += ("<a href=\"" + CtxURL.ctx() + '?' +

                        Constants.NAME_LANG_PARAM + '=' + jspPage.currentLocale.toString() + '&' +

                        Constants.NAME_YEAR_PARAM + '=' + yearValue + '&' +

                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +

                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +

                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate +

                        "\">" + yearValue + "</a>&nbsp;");

            }

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, st);

            rs = null;

            st = null;

            db_ = null;

        }

        return s;

    }



    public String getListMonths(String nameTemplate)

        throws ForumException

    {

        String s = "";

        PreparedStatement st = null;

        ResultSet rs = null;



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            st = db_.prepareStatement(

                "select  distinct trunc(month, 'mm') dat, " +

                "        to_number(to_char(month,'mm')) month " +

                "         " +

                "from ( " +

                "        select distinct trunc(date_post,'dd') month " +

                "        from " + FORUM_THREADS_TABLE + " " +

                "        where   id_forum = ? and to_char(date_post,'yyyy')=to_char(?) " +

                "        union " +

                "        select trunc(sysdate,'dd') month from dual " +

                "	 where to_char(sysdate,'yyyy')=to_char(?) " +

                ") " +

                "order by month desc"

            );



            st.setLong(1, id_forum.longValue());

            st.setInt(2, year);

            st.setInt(3, year);

            rs = st.executeQuery();



            while (rs.next())

            {

                int monthValue = RsetTools.getInt(rs, "month").intValue();



                String monthString = RsetTools.getStringDate(rs, "dat", "MMMM", "unknown", jspPage.currentLocale);

                if (month == monthValue)

                    s += ("<b>" + monthString + "</b>&nbsp;");

                else

                    s += ("<a href=\"" + forumURI + '?' +

                        Constants.NAME_LANG_PARAM + '=' + jspPage.currentLocale.toString() + '&' +

                        Constants.NAME_YEAR_PARAM + '=' + year + '&' +

                        Constants.NAME_MONTH_PARAM + '=' + monthValue + '&' +

                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +

                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +

                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate +

                        "\">" + monthString + "</a>&nbsp;");

            }



        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, st);

            rs = null;

            st = null;

            db_ = null;

        }

        return s;

    }





    public List getMessagesInThread()

        throws ForumException

    {

        if (id==null)

            return null;



//        String s = "";

        PreparedStatement st = null;

        ResultSet rs = null;

        List v = null;



        String sql_ =

            "select  a.id " +

            "from " + FORUM_THREADS_TABLE + " a, " + FORUM_THREADS_TABLE + " b " +

            "where b.id=? and a.id_thread=b.id_thread " +

            "order by a.date_post asc";



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            st = db_.prepareStatement(sql_);

            st.setLong(1, id.longValue());

            rs = st.executeQuery();



            v = new ArrayList(0);



            while (rs.next())

                v.add(getForumMessage(db_, RsetTools.getLong(rs, "id")) );

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, st);

            rs = null;

            st = null;

            db_ = null;

        }

        return v;

    }





    public String getStartMessages(String nameTemplate)

        throws ForumException

    {

        String sql_ =

            "select a.*, (select count(*) COUNT_MESSAGES from " + FORUM_THREADS_TABLE + " z " +

            "where z.ID_THREAD=a.ID_THREAD) COUNT_MESSAGES " +

            "from " + FORUM_THREADS_TABLE + " a " +

            "where a.ID_MAIN=0 and a.id_forum=? " +

            "order by a.DATE_POST DESC";



        PreparedStatement ps = null;

        ResultSet rs = null;

        String s = "";



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_forum );

            rs = ps.executeQuery();





            s +=

                "<STYLE>" +

                "<!--" +

                "/* titles for the topics: could specify viewed link colour too */ "+

                ".topictitle		  { font-weight: bold; font-size: 11px; color:#000000; } "+

                "a.topictitle:link    { text-decoration: none; color : #000000; } "+

                "a.topictitle:visited { text-decoration: none; color : #000000; } "+

                "a.topictitle:hover	  { text-decoration: NONE; color : #3D4743; } "+

                "/* Location, number of posts, post date etc */ "+

                ".postdetails		{ font-size : 10px; color : #000000; } "+

                "th.thHead,th.thSides,th.thTop,th.thLeft,th.thRight,th.thBottom,th.thCornerL,th.thCornerR { " +

                "    font-weight: bold; font-size: 11px; color:#000000; " +

                "height: 28px; } " +

                "-->" +

                "</style>"+

                "<table border=\"1\">\n" +

                "<tr>"+

                "<th align=\"center\" height=\"25\" class=\"thCornerL\" nowrap=\"nowrap\">&nbsp;Темы&nbsp;</th>"+

                "<th width=\"50\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;Ответов&nbsp;</th>"+

                "<th width=\"100\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;Автор&nbsp;</th>"+

//                "<th width=\"50\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;Просмотров&nbsp;</th>"+

                "<th align=\"center\" class=\"thCornerR\" nowrap=\"nowrap\">&nbsp;Последнее сообщение&nbsp;</th>"+

                "</tr>"+

                "<tr><td>";

            while (rs.next())

            {

                Calendar cal = RsetTools.getCalendar(rs, "DATE_POST");

                String dat = DateTools.getStringDate(cal, "dd-MM-yyyy HH:mm:ss", jspPage.currentLocale);



                s += (

                    "<tr><td>" +

                    "<span class=\"topictitle\">" +

                    "<a class=\"topictitle\" href=\"" + CtxURL.ctx() + '?' +

                    Constants.NAME_LANG_PARAM + '=' + jspPage.currentLocale.toString() + '&' +

                    Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +

                    Constants.NAME_ID_MESSAGE_FORUM_PARAM + '=' +

                    RsetTools.getLong(rs, "ID") + '&' +



                    Constants.NAME_YEAR_PARAM + '=' + cal.get(Calendar.YEAR) + '&' +

                    Constants.NAME_MONTH_PARAM + '=' + (cal.get(Calendar.MONTH) + 1) + '&' +



                    Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate +

                    "\">" + RsetTools.getString(rs, "header", "___") + "</a>" +

                    "</span>" +

                    "</td>" +

                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">" + RsetTools.getInt(rs, "COUNT_MESSAGES") + "</td>" +

                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">" + RsetTools.getString(rs, "fio") + "</td>"+

                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">&nbsp;</td></tr>\n"

                    );

            }

            s += "</table>";

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

        return s;

    }





    public ForumMessage getCurrentMessage()

        throws ForumException

    {

        if (id==null)

            return null;



        try

        {

            return getForumMessage(DatabaseAdapter.getInstance(false), id);

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

    }





// xxx need optimize for decrease DB request

    public String getMessages(

        Long id__,

        Long id_thread__,

        Long curr_level__,

        Long id_main__,

        String nameTemplate)

        throws ForumException

    {

        String r_ = "";



        PreparedStatement ps = null;

        ResultSet rs = null;

        String sql_ =

            "select  id, id_main, id_forum, header, fio, email, " +

            "        MESSAGE_TEXT, id_thread, ip, " +

            "        date_post " +

            "from " + FORUM_THREADS_TABLE + " " +

            "where   id_thread=? and level = ? and id_main=? " +

            "start with id_main=0 " +

            "CONNECT BY PRIOR id=id_main " +

            "order by id asc ";





        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);



            ps = db_.prepareStatement( sql_ );



            RsetTools.setLong(ps, 1, id_thread__ );

            ps.setLong(2, curr_level__.longValue() + 1);

            RsetTools.setLong(ps, 3, id_main__ );



            rs = ps.executeQuery();



            while (rs.next())

            {



                for (int v_i = 1; v_i <= curr_level__.longValue(); v_i++)

                    r_ += "&nbsp;&nbsp;&nbsp;&nbsp;";



                Long idValue = RsetTools.getLong(rs, "ID");



                Calendar cal = RsetTools.getCalendar(rs, "DATE_POST");

                String dat = DateTools.getStringDate(cal, "dd-MM-yyyy HH:mm:ss", jspPage.currentLocale);



                r_ += "<b>";

                if (!idValue.equals(id__))

                {

/*

                    r_ += "<a href=\"" + forumURI + '?' + webPage.addURL +

                            Constants.NAME_ID_FORUM_PARAM   + '=' + id_forum + '&' +

                            Constants.NAME_ID_MESSAGE_FORUM_PARAM     + '=' +

                            RsetTools.getLong(rs, "ID") + "\">";

*/

                    r_ += ("<a href=\"" + CtxURL.ctx() + '?' +

                        Constants.NAME_LANG_PARAM + '=' + jspPage.currentLocale.toString() + '&' +

//                        Constants.NAME_YEAR_PARAM + '=' + yearValue + '&' +

                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +

                        Constants.NAME_ID_MESSAGE_FORUM_PARAM + '=' +

                        RsetTools.getLong(rs, "ID") + '&' +



                        Constants.NAME_YEAR_PARAM + '=' + cal.get(Calendar.YEAR) + '&' +

                        Constants.NAME_MONTH_PARAM + '=' + (cal.get(Calendar.MONTH) + 1) + '&' +



                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +

                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate +

                        "\">");

                }





                String header_ = RsetTools.getString(rs, "header", "").trim();



                if (header_.length() == 0)

                    header_ = "___";



                r_ += header_;

                if (!idValue.equals(id__))

                    r_ += "</a>";



                r_ += " ";



                r_ += "<a href=\"mailto:" +

                    RsetTools.getString(rs, "email", "") + "\">" +

                    RsetTools.getString(rs, "fio", "") + "</a></b>";



                r_ += "&nbsp;" + dat + "<BR>";



                r_ += getMessages(id__, RsetTools.getLong(rs, "id_thread"),

                    new Long( curr_level__.longValue() + 1), idValue, nameTemplate);



            }

        }

        catch (Exception e)

        {

            log.error("Forum.getMessages(), sql: "+sql_);

            log.error("Forum.getMessages() ", e);

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

        return r_;

    }



    /**

     return id of added message

     */

    public Long addMessage(HttpServletRequest request, Long id_thread__, Long id_main__)

        throws ForumException

    {



        PreparedStatement st = null;

        Long currentId = null;



        DatabaseAdapter dbDyn = null;

        try

        {

            dbDyn = DatabaseAdapter.getInstance(true);



            CustomSequenceType seq = new CustomSequenceType();

            seq.setSequenceName(SEQ_FORUM_MESSAGE);

            seq.setTableName(FORUM_THREADS_TABLE);

            seq.setColumnName("ID");

            currentId = new Long(dbDyn.getSequenceNextValue(seq));





            st = dbDyn.prepareStatement(

                "insert into " + FORUM_THREADS_TABLE + " " +

                "(ID, id_main, id_forum, id_thread, date_post, header, fio, email, ip) " +

                "values " +

                "(?, ?, ?, ?, "+dbDyn.getNameDateBind()+", ?, ?, ?, ?) "

            );

            RsetTools.setLong(st, 1, currentId);

            RsetTools.setLong(st, 2, id_main__);

            RsetTools.setLong(st, 3, id_forum);

            RsetTools.setLong(st, 4, id_thread__);

            dbDyn.bindDate(st, 5, DateTools.getCurrentTime());

            st.setString(6, ServletUtils.getString(request, "h") ); // header

            st.setString(7, ServletUtils.getString(request, "n") ); // name

            st.setString(8, ServletUtils.getString(request, "e") ); // email

            st.setString(9, request.getRemoteAddr());



            st.executeUpdate();



            DatabaseManager.insertBigText(

                dbDyn,

                currentId, "ID",

                PrimaryKeyTypeTypeType.NUMBER,

                "MAIN_FORUM_TEXT", "ID_MAIN_FORUM_TEXT",

                "MESSAGE_TEXT",

                ServletUtils.getString(request, "b"),

                false

            );



            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch(Exception ee){}

            throw new ForumException(e.toString());

        }

        finally

        {

            DatabaseManager.close(dbDyn, st);

            st = null;

            dbDyn = null;

        }

        return currentId;

    }

}