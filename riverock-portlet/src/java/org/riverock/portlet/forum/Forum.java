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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.portlet.PortletRequest;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.common.collections.TreeUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.core.GetMainForumThreadsItem;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;

abstract public class Forum
{
    private static Logger log = Logger.getLogger(Forum.class);

    public String forumURI = "";
    public int month = 0;
    public int year = 0;
    public Long id_forum = null;
    public Long id = null;
    public static final String SEQ_FORUM_THREADS = "SEQ_MAIN_FORUM_THREADS";
    public static final String SEQ_FORUM_MESSAGE = "SEQ_MAIN_FORUM_MESSAGE";
    public static final String FORUM_THREADS_TABLE = "MAIN_FORUM_THREADS";
    private PortletRequest portletRequest = null;
    private CtxInstance ctxInstance = null;

    abstract public ForumMessage getForumMessage(DatabaseAdapter db__, Long id__) throws ForumException;

    public Forum(){}

    public Forum(PortletRequest portletRequest)
    {
        this.portletRequest = portletRequest;
        this.ctxInstance = (CtxInstance)portletRequest.getPortletSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
        this.id = PortletTools.getLong(portletRequest, Constants.NAME_ID_MESSAGE_FORUM_PARAM );

        this.id_forum = PortletTools.getLong(portletRequest, Constants.NAME_ID_FORUM_PARAM);
        this.year = PortletTools.getInt(
            portletRequest, Constants.NAME_YEAR_PARAM,
            new Integer(Calendar.getInstance().get(Calendar.YEAR))
        ).intValue();
    }


    public static void setCookie(Cookie[] cookies_req, PortletRequest request,
        javax.servlet.http.HttpServletResponse response)
        throws ConfigException
    {
        if (PortletTools.getString(request, "action").equals("add"))
        {
            String email = PortletTools.getString(request, "e");
            String name = PortletTools.getString(request, "n");
            String name_req = "";
            String email_req = "";

            for (int i = 0; i < cookies_req.length; i++)
            {
                Cookie c = cookies_req[i];
                String name_cookie = c.getName();
                if (name_cookie.equals("_name"))
                    name_req = c.getValue();

                if (name_cookie.equals("_email"))
                    email_req = c.getValue();
            }


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
            RsetTools.setLong(ps, 1, id_main__);

            rs = ps.executeQuery();
            if (rs.next())
                retValue = RsetTools.getLong(rs, "id_thread");
        }
        catch (Exception e)
        {
            log.error("exception", e);
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
            if (id!=null) {
                sql_ =
                    "select a.ID_THREAD " +
                    "from "  + FORUM_THREADS_TABLE + " a " +
                    "where a.ID_THREAD in ( " ;

                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        sql_ += StringTools.getIdByString(
                            DatabaseManager.getIdByList(db_,
                                "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID=? and  " +
                            "year(z.DATE_POST)=? and month(z.DATE_POST)=?",
                                new Object[]{id, new Integer(year), new Integer(month)}
                            ), "NULL"
                        );
                        break;
                    default:
                        sql_ +=
                            "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID = ? and  " +
                            "to_number(to_char(z.DATE_POST, 'yyyy')) = ? and " +
                            "to_number(to_char(z.DATE_POST, 'mm')) = ? ";
                        break;
                }
                sql_ += ") order by a.DATE_POST DESC ";

                if (log.isDebugEnabled()) log.debug("sql: "+sql_);

                ps = db_.prepareStatement(sql_);
                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        break;
                    default:
                        RsetTools.setLong(ps, 1, id );
                        ps.setLong(2, year);
                        ps.setLong(3, month);
                        break;
                }
            }
            else
            {
                sql_ =
                    "select a.ID_THREAD " +
                    "from " + FORUM_THREADS_TABLE + " a  "+
                    "where a.ID_THREAD in (  ";

                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        sql_ += StringTools.getIdByString(
                            DatabaseManager.getIdByList(db_,
                                "select distinct ID_THREAD from " + FORUM_THREADS_TABLE + ' ' +
                            "where  ID_FORUM=? and year(DATE_POST)=? and month(DATE_POST)=?",
                                new Object[]{id_forum, new Integer(year), new Integer(month)}
                            ), "NULL"
                        );
                        break;
                    default:
                        sql_ =
                            "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID_FORUM = ? and " +
                            "to_number(to_char(DATE_POST, 'yyyy')) = ? and " +
                            "to_number(to_char(DATE_POST, 'mm')) = ? ";
                            break;
                }
                sql_ = "order by a.DATE_POST DESC ";

                ps = db_.prepareStatement(sql_);
                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        break;
                    default:
                        RsetTools.setLong(ps, 1, id_forum );
                        ps.setLong(2, year);
                        ps.setLong(3, month);
                        break;
                }
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                s += getMessages(
                    db_, getMessages(db_, RsetTools.getLong(rs, "ID_THREAD")),
                    0, new Long(0));
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

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);
            switch(db_.getFamaly()){
                case DatabaseManager.MYSQL_FAMALY:
                    sql_ =
                        "select max(month(date_post)) month " +
                        "from " + FORUM_THREADS_TABLE + " " +
                        "where id_forum=? and year(date_post)=?";
                    break;
                default:
                    sql_ =
                        "select max(to_number(to_char(date_post,'mm'))) month " +
                        "from " + FORUM_THREADS_TABLE + " " +
                        "where id_forum=? and to_number(to_char(date_post,'yyyy'))=?";
                    break;
            }

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, id_forum );
            ps.setLong(2, year);

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

    public String getListYears()
        throws ForumException
    {
        DatabaseAdapter db_ = null;
        String s = "";
        try
        {
            db_ = DatabaseAdapter.getInstance(false);
            List listYear = null;
            switch(db_.getFamaly()){
                case DatabaseManager.MYSQL_FAMALY:
                    listYear = DatabaseManager.getIdByList(db_,
                        "select distinct year(date_post) year " +
                        "from " + FORUM_THREADS_TABLE + " where id_forum = ? ",
                        new Object[]{id_forum}
                    );
                    Long currYear = new Long(GregorianCalendar.getInstance().get(Calendar.YEAR));
                    if (listYear.indexOf(currYear)==-1)
                        listYear.add(currYear);
                    break;
                default:
                    listYear = DatabaseManager.getIdByList(db_,
                        "select year year " +
                        "from ( select distinct to_number(to_char(date_post,'yyyy')) year " +
                        "        from " + FORUM_THREADS_TABLE + " where id_forum = ? " +
                        "        union " +
                        "        select to_numebr(to_char(sysdate,'yyyy')) year from dual ) " +
                        "order by year desc ",
                        new Object[]{id_forum}
                    );
                    break;
            }
            Collections.sort(listYear);

            for (int i=0; i<listYear.size(); i++)
            {

                int yearValue = ((Long)listYear.get(i)).intValue();
                if (year == yearValue)
                    s += ("<b>" + yearValue + "</b>&nbsp;");
                else
                    s += ("<a href=\"" + CtxInstance.ctx() + '?' +
                        Constants.NAME_LANG_PARAM + '=' + portletRequest.getLocale().toString() + '&' +
                        Constants.NAME_YEAR_PARAM + '=' + yearValue + '&' +
                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +
                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + ctxInstance.getNameTemplate() +
                        "\">" + yearValue + "</a>&nbsp;");
            }
        }
        catch (Exception e)
        {
            log.error("exception",e);
            throw new ForumException(e.toString());
        }
        finally
        {
            DatabaseManager.close(db_);
            db_ = null;
        }
        return s;
    }

    public String getListMonths()
        throws ForumException
    {
        String s = "";
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);

            List listMonth = null;
            switch(db_.getFamaly()){
                case DatabaseManager.MYSQL_FAMALY:

                    listMonth = DatabaseManager.getIdByList(db_,
                        "select distinct month(date_post) month " +
                        "from " + FORUM_THREADS_TABLE + " " +
                        "where  id_forum=? and year(date_post)=? ",
                        new Object[]{id_forum, new Integer(year)}
                    );

                    Long currMonth = new Long(GregorianCalendar.getInstance().get(Calendar.MONTH));
                    if (listMonth.indexOf(currMonth)==-1)
                        listMonth.add(currMonth);
                    break;
                default:
                    listMonth = DatabaseManager.getIdByList(db_,
                        "select  distinct trunc(month, 'mm') dat, " +
                        "        to_number(to_char(month,'mm')) month " +
                        "         " +
                        "from ( " +
                        "        select distinct trunc(date_post,'dd') month " +
                        "        from " + FORUM_THREADS_TABLE + " " +
                        "        where   id_forum = ? and to_number(to_char(date_post,'yyyy'))=? " +
                        "        union " +
                        "        select trunc(sysdate,'dd') month from dual " +
                        "	 where to_numner(to_char(sysdate,'yyyy'))=? " +
                        ") " +
                        "order by month desc",
                        new Object[]{id_forum, new Integer(year), new Integer(year)}
                    );
                    break;
            }
            Collections.sort(listMonth);

            for (int i=0; i<listMonth.size(); i++)
            {
                Long monthLong = (Long)listMonth.get(i);
                int monthValue = monthLong.intValue();
                String monthString = DateUtils.getStringDate(
                    DateTools.getDateWithMask(monthLong.toString(), "mm"),
                    "MMMM",
                    portletRequest.getLocale());

                if (month == monthValue)
                    s += ("<b>" + monthString + "</b>&nbsp;");
                else
                    s += ("<a href=\"" + CtxInstance.ctx() + '?' +
                        Constants.NAME_LANG_PARAM + '=' + portletRequest.getLocale().toString() + '&' +
                        Constants.NAME_YEAR_PARAM + '=' + year + '&' +
                        Constants.NAME_MONTH_PARAM + '=' + monthValue + '&' +
                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +
                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + ctxInstance.getNameTemplate() +
                        "\">" + monthString + "</a>&nbsp;");
            }

        }
        catch (Exception e)
        {
            log.error("exception", e);
            throw new ForumException(e.toString());
        }
        finally
        {
            DatabaseManager.close(db_);
            db_ = null;
        }
        return s;
    }


    public List getMessagesInThread()
        throws ForumException
    {
        if (id==null)
            return null;

        PreparedStatement st = null;
        ResultSet rs = null;
        List v = new LinkedList();;

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
        PreparedStatement ps = null;
        ResultSet rs = null;
        String s = "";

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);

            String sql_ =
                "select a.* ";

            switch(db_.getFamaly()){
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    sql_ +=
                        ",(select count(*) COUNT_MESSAGES from "+FORUM_THREADS_TABLE+" z " +
                        "where z.ID_THREAD=a.ID_THREAD) COUNT_MESSAGES ";
                    break;
            }

            sql_ +=
                "from " + FORUM_THREADS_TABLE + " a " +
                "where a.ID_MAIN=0 and a.id_forum=? " +
                "order by a.DATE_POST DESC";

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
                Long countMessages = null;
                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        Long threadId = RsetTools.getLong(rs, "ID_THREAD");
                        countMessages = DatabaseManager.getLongValue(db_,
                            "select count(*) COUNT_MESSAGES from "+FORUM_THREADS_TABLE+" where ID_THREAD=?",
                            new Object[]{threadId}
                        );
                        break;
                    default:
                        countMessages = RsetTools.getLong(rs, "COUNT_MESSAGES");
                        break;
                }
                if (countMessages==null)
                    countMessages = new Long(0);

                s += (
                    "<tr><td>" +
                    "<span class=\"topictitle\">" +
                    "<a class=\"topictitle\" href=\"" + CtxInstance.ctx() + '?' +
                    Constants.NAME_LANG_PARAM + '=' + portletRequest.getLocale().toString() + '&' +
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
                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">" + countMessages.toString() + "</td>" +
                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">" + RsetTools.getString(rs, "fio") + "</td>"+
                    "<td align=\"center\" valign=\"middle\" class=\"postdetails\">&nbsp;</td></tr>\n"
                    );
            }
            s += "</table>";
        }
        catch (Exception e)
        {
            log.error("exception: ", e);
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

        try {
            return getForumMessage(DatabaseAdapter.getInstance(false), id);
        }
        catch(Exception e) {
            throw new ForumException(e.toString());
        }
    }

    List getMessages(DatabaseAdapter db_, Long threadId)
        throws ForumException {
//        MainForumThreadsListType thread = GetMainForumThreadsList.getInstance(db_, threadsId).item;
//        thread.setMainForumThreadsItems(  TreeUtils.rebuildTree() );

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql_ =
            "select * "+
            "from " + FORUM_THREADS_TABLE + ' ' +
            "where  id_thread=? " +
            "order by id asc ";

        LinkedList list = new LinkedList();
        try {
            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong(ps, 1, threadId );
            rs = ps.executeQuery();

            while (rs.next())
                list.add( new ForumMessage(db_, GetMainForumThreadsItem.fillBean(rs)) );

            return TreeUtils.rebuildTree(list);
        }
        catch(Exception e) {
            log.error("Forum.getMessages(), sql: "+sql_);
            log.error("Forum.getMessages() ", e);
            throw new ForumException(e.toString());
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }


    // xxx need optimize for decrease DB request
    public String getMessages(DatabaseAdapter db_, List messages, int currentLevel, Long currentMessageId)
        throws ForumException {

        String r_ = "";

        try {

            for (int i=0; i<messages.size(); i++)
            {
                ForumMessage message = (ForumMessage)messages.get(i);

                for (int v_i = 1; v_i <= currentLevel; v_i++)
                    r_ += "&nbsp;&nbsp;&nbsp;&nbsp;";

                Long idValue = message.getId();
                Calendar cal = message.getDatePost();
                String dat = DateTools.getStringDate(cal, "dd-MM-yyyy HH:mm:ss", portletRequest.getLocale());

                r_ += "<b>";
                if (!message.getId().equals(currentMessageId))
                {
                    r_ += ("<a href=\"" + CtxInstance.ctx() + '?' +
                        Constants.NAME_LANG_PARAM + '=' + portletRequest.getLocale().toString() + '&' +
                        Constants.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        Constants.NAME_ID_MESSAGE_FORUM_PARAM + '='+ message.getId() + '&' +
                        Constants.NAME_YEAR_PARAM + '=' + cal.get(Calendar.YEAR) + '&' +
                        Constants.NAME_MONTH_PARAM + '=' + (cal.get(Calendar.MONTH) + 1) + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_FORUM + '&' +
                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + ctxInstance.getNameTemplate() +
                        "\">");
                }

                r_ += message.getHeader();;
                if (!idValue.equals(currentMessageId))
                    r_ += "</a>";

                r_ += 
                    " <a href=\"mailto:" + message.getEmail() + "\">" +
                    message.getFio()+ "</a></b>"+
                    "&nbsp;" + dat + "<BR>"+
                    getMessages(db_, message.getSubMessageTree(), currentLevel + 1, currentMessageId);

            }
        }
        catch(Exception e) {
            log.error("Forum.getMessages() ", e);
            throw new ForumException(e.toString());
        }
        finally {}
        return r_;
    }

    /**
     * @return id of added message
     */
    public Long addMessage(CtxInstance ctxInstance, Long id_thread__, Long id_main__)
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
            st.setString(6, PortletTools.getString(ctxInstance.getPortletRequest(), "h") ); // header
            st.setString(7, PortletTools.getString(ctxInstance.getPortletRequest(), "n") ); // name
            st.setString(8, PortletTools.getString(ctxInstance.getPortletRequest(), "e") ); // email
            st.setString(9, ctxInstance.getRemoteAddr());

            st.executeUpdate();

            DatabaseManager.insertBigText(
                dbDyn,
                currentId, "ID",
                PrimaryKeyTypeTypeType.NUMBER,
                "MAIN_FORUM_TEXT", "ID_MAIN_FORUM_TEXT",
                "MESSAGE_TEXT",
                PortletTools.getString(ctxInstance.getPortletRequest(), "b"),
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