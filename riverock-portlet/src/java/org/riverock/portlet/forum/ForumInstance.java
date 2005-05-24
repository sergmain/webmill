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

package org.riverock.portlet.forum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.net.URLEncoder;

import javax.portlet.PortletRequest;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

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

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.CookieManager;
import org.riverock.webmill.portal.PortalConstants;

/**
 * $Id$
 */
public final class ForumInstance {
    private final static Logger log = Logger.getLogger( ForumInstance.class );

    public String forumURI = "";
    public int month = 0;
    public int year = 0;
    public Long currYear = new Long(GregorianCalendar.getInstance().get(Calendar.YEAR));
    public Long id_forum = null;
    public Long id = null;

    public static final String SEQ_FORUM_THREADS = "SEQ_MAIN_FORUM_THREADS";
    public static final String SEQ_FORUM_MESSAGE = "SEQ_MAIN_FORUM_MESSAGE";
    public static final String FORUM_THREADS_TABLE = "MAIN_FORUM_THREADS";

    private PortletRequest renderRequest = null;

    public ForumMessage getForumMessage(DatabaseAdapter db__, Long id__)
        throws ForumException {

        return ForumMessage.getInstance(db__, id__);
    }

    public ForumInstance(){}

    public ForumInstance(PortletRequest renderRequest) {

        this.renderRequest = renderRequest;
        this.id = PortletTools.getLong(renderRequest, ForumPortlet.NAME_ID_MESSAGE_FORUM_PARAM );

        this.id_forum = PortletTools.getLong(renderRequest, ForumPortlet.NAME_ID_FORUM_PARAM);
        this.year = PortletTools.getInt(
            renderRequest, ForumPortlet.NAME_YEAR_PARAM,
            new Integer(Calendar.getInstance().get(Calendar.YEAR))
        ).intValue();
    }


    static void setCookie( PortletRequest request ) throws ForumException {
        try {
            Cookie[] cookies = (Cookie[])request.getAttribute( PortalConstants.PORTAL_COOKIES_ATTRIBUTE );
            CookieManager cookieManager = (CookieManager)request.getAttribute( PortalConstants.PORTAL_COOKIE_MANAGER_ATTRIBUTE );

            if (PortletTools.getString(request, "action").equals("add"))
            {
                String email = PortletTools.getString(request, "e");
                String name = PortletTools.getString(request, "n");
                String name_req = "";
                String email_req = "";

                for (int i = 0; i < cookies.length; i++)
                {
                    Cookie c = cookies[i];
                    String name_cookie = c.getName();
                    if (name_cookie.equals("_name"))
                        name_req = c.getValue();

                    if (name_cookie.equals("_email"))
                        email_req = c.getValue();
                }

                if ( log.isDebugEnabled() ) {
                    log.debug( "cookie name: " + name + ", email; " + email );
                    log.debug( "cookie name: " + StringTools.convertString( name, "Cp1251", "utf8" ) );
                    log.debug( "cookie name: " + StringTools.convertString( name, "utf8", "Cp1251" ) );
                }


                if ((name != null) && !name.equals(name_req)) {

//                    name = StringTools.convertString( name, "Cp1251", "utf8" );
                    name = URLEncoder.encode( name, "utf8" );
                    Cookie cookie = new Cookie("_name", name);
                    cookie.setVersion( 1 );
                    cookie.setMaxAge(1 * 365 * 24 * 3600);
                    cookieManager.addCookie(cookie);
                }

                if ((email != null) && !email.equals(email_req)) {
                    Cookie cookie = new Cookie("_email", email);
                    cookie.setVersion( 1 );
                    cookie.setMaxAge(1 * 365 * 24 * 3600);
                    cookieManager.addCookie(cookie);
                }
            }
        }
        catch( Throwable e ) {
            String es = "Error set forum cookie";
            log.error( es, e );
            throw new ForumException( es, e );
        }
    }

    public Long getIdThread(Long id_main__) throws ForumException {

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
        catch (Throwable e){
            String es = "Error in getIdThread()";
            log.error(es, e);
            throw new ForumException( es, e );
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


    public String getThreads() throws ForumException {
        String sql_ = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String s = "";

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance(false);
            if (id!=null) {
                sql_ = "";

                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        // Todo check for duplicate of ID_THREAD
                        String mysqlSql =
                            "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID=? and  " +
                            "year(z.DATE_POST)=? and month(z.DATE_POST)=?";

                        if (log.isDebugEnabled()) log.debug("year:"+year+", month: "+month+", sql: "+mysqlSql);

                        sql_ +=
                            "select a.ID_THREAD " +
                            "from "  + FORUM_THREADS_TABLE + " a " +
                            "where a.ID_THREAD in ( " +
                            StringTools.getIdByString(
                                DatabaseManager.getIdByList(db_,
                                    mysqlSql,
                                    new Object[]{id, new Integer(year), new Integer(month)}
                                ), "NULL"
                            )+
                            ") order by a.DATE_POST DESC ";

                        break;
                    default:
                        sql_ =
                            "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID = ? and  " +
                            "to_number(to_char(z.DATE_POST, 'yyyy')) = ? and " +
                            "to_number(to_char(z.DATE_POST, 'mm')) = ? ";
                        break;
                }
//                sql_ += ") order by a.DATE_POST DESC ";

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
                    "where a.ID_THREAD in (";

                switch(db_.getFamaly()){
                    case DatabaseManager.MYSQL_FAMALY:
                        String mysqlSql =
                            "select distinct ID_THREAD from " + FORUM_THREADS_TABLE + ' ' +
                            "where  ID_FORUM=? and year(DATE_POST)=? and month(DATE_POST)=?";

                        if (log.isDebugEnabled()) log.debug("year:"+year+", month: "+month+", sql: "+mysqlSql);

                        sql_ += StringTools.getIdByString(
                            DatabaseManager.getIdByList(db_,
                                mysqlSql,
                                new Object[]{id_forum, new Integer(year), new Integer(month)}
                            ), "NULL"
                        );
                        break;
                    default:
                        sql_ +=
                            "select distinct z.ID_THREAD " +
                            "from " + FORUM_THREADS_TABLE + " z " +
                            "where   z.ID_FORUM = ? and " +
                            "to_number(to_char(DATE_POST, 'yyyy')) = ? and " +
                            "to_number(to_char(DATE_POST, 'mm')) = ? ";
                            break;
                }
                sql_ += ") order by a.DATE_POST DESC ";

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
        catch (Throwable e)
        {
            log.error("this.class: "+this.getClass().getName());
            log.error("sql: "+sql_);

            String es = "Error in getThreads(String nameTemplate)";
            log.error(es, e);
            throw new ForumException(es, e);
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

    public Integer getFirstMonthInYear()throws ForumException{
        String sql_ = "";
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try{
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
            if (rs.next()){
                Object obj = rs.getObject("month");
                return (obj == null ? null : new Integer(rs.getInt("month")));
            }
        }
        catch (Throwable e){
            String es = "Error in getFirstMonthInYear()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
        finally{
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }

        return null;
    }

    public String getListYears()throws ForumException{
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

                    if (listYear.indexOf(currYear)==-1)
                        listYear.add(currYear);
                    break;
                default:
                    listYear = DatabaseManager.getIdByList(db_,
                        "select year year " +
                        "from ( select distinct to_number(to_char(date_post,'yyyy')) year " +
                        "        from " + FORUM_THREADS_TABLE + " where id_forum = ? " +
                        "        union " +
                        "        select to_number(to_char(sysdate,'yyyy')) year from dual ) " +
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
                    s += ("<a href=\"" + PortletTools.ctx( renderRequest ) + '?' +
                        Constants.NAME_LANG_PARAM + '=' + renderRequest.getLocale().toString() + '&' +
                        ForumPortlet.NAME_YEAR_PARAM + '=' + yearValue + '&' +
                        ForumPortlet.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + ForumPortlet.CTX_TYPE_FORUM + '&' +
                        "\">" + yearValue + "</a>&nbsp;");
            }
        }
        catch (Throwable e){
            String es = "Error in getListYears()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
        finally{
            DatabaseManager.close(db_);
            db_ = null;
        }
        return s;
    }

    public String getListMonths()throws ForumException{
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
                        "select  distinct to_number(to_char(month,'mm')) month " +
                        "from ( " +
                        "        select distinct trunc(date_post,'dd') month " +
                        "        from " + FORUM_THREADS_TABLE + " " +
                        "        where   id_forum = ? and to_number(to_char(date_post,'yyyy'))=? " +
                        "        union " +
                        "        select trunc(sysdate,'dd') month from dual " +
                        "	     where to_number(to_char(sysdate,'yyyy'))=? " +
                        ") " +
                        "order by month desc",
                        new Object[]{id_forum, new Integer(year), new Integer(year)}
                    );
                    break;
            }
            Collections.sort(listMonth);

            int effectiveMonth = 0;
            if (listMonth.indexOf(new Long(month))==-1 && listMonth.size()>0)
                effectiveMonth=((Long)listMonth.get(0)).intValue();
            else
                effectiveMonth = month;

            for (int i=0; i<listMonth.size(); i++) {
                Long monthLong = (Long)listMonth.get(i);
                int monthValue = monthLong.intValue();
                if (log.isDebugEnabled())
                    log.debug("monthLong: "+monthLong+", monthValue: "+monthValue+
                        ", monthDate: "+DateTools.getDateWithMask(monthLong.toString(), "MM")+", monthStr: "+
                        DateUtils.getStringDate(
                            DateTools.getDateWithMask(monthLong.toString(), "MM"),
                            "MMMM",
                            renderRequest.getLocale()
                        )
                    );

                String monthString = DateUtils.getStringDate(
                    DateTools.getDateWithMask(monthLong.toString(), "MM"),
                    "MMMM",
                    renderRequest.getLocale());


                if (monthValue==effectiveMonth)
                    s += ("<b>" + monthString + "</b>&nbsp;");
                else
                    s += ("<a href=\"" + PortletTools.ctx( renderRequest ) + '?' +
                        Constants.NAME_LANG_PARAM + '=' + renderRequest.getLocale().toString() + '&' +
                        ForumPortlet.NAME_YEAR_PARAM + '=' + year + '&' +
                        ForumPortlet.NAME_MONTH_PARAM + '=' + monthValue + '&' +
                        ForumPortlet.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + ForumPortlet.CTX_TYPE_FORUM + '&' +
                        "\">" + monthString + "</a>&nbsp;");
            }
        }
        catch (Throwable e){
            String es = "Error in getListMonths()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
        finally{
            DatabaseManager.close(db_);
            db_ = null;
        }
        return s;
    }


    public List getMessagesInThread()throws ForumException{
        if (id==null) return null;

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
        catch (Throwable e){
            String es = "Error in getMessagesInThread()";
            log.error(es, e);
            throw new ForumException(es, e);
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


    public String getStartMessages() throws ForumException{
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
                "<th align=\"center\" height=\"25\" class=\"thCornerL\" nowrap=\"nowrap\">&nbsp;����&nbsp;</th>"+
                "<th width=\"50\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;�������&nbsp;</th>"+
                "<th width=\"100\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;�����&nbsp;</th>"+
//                "<th width=\"50\" align=\"center\" class=\"thTop\" nowrap=\"nowrap\">&nbsp;����������&nbsp;</th>"+
                "<th align=\"center\" class=\"thCornerR\" nowrap=\"nowrap\">&nbsp;��������� ���������&nbsp;</th>"+
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
                    "<a class=\"topictitle\" href=\"" + PortletTools.ctx( renderRequest ) + '?' +
                    Constants.NAME_LANG_PARAM + '=' + renderRequest.getLocale().toString() + '&' +
                    ForumPortlet.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                    ForumPortlet.NAME_ID_MESSAGE_FORUM_PARAM + '=' +
                    RsetTools.getLong(rs, "ID") + '&' +

                    ForumPortlet.NAME_YEAR_PARAM + '=' + cal.get(Calendar.YEAR) + '&' +
                    ForumPortlet.NAME_MONTH_PARAM + '=' + (cal.get(Calendar.MONTH) + 1) + '&' +

                    Constants.NAME_TYPE_CONTEXT_PARAM + '=' + ForumPortlet.CTX_TYPE_FORUM + '&' +
//                    Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + nameTemplate +
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
        catch (Throwable e){
            String es = "Error in getStartMessages()";
            log.error(es, e);
            throw new ForumException(es, e);
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
        try{
            return getForumMessage(DatabaseAdapter.getInstance(false), id);
        }
        catch (Throwable e){
            String es = "Error in getCurrentMessage()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
    }

    List getMessages(DatabaseAdapter db_, Long threadId)
        throws ForumException {

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
        catch (Throwable e){
            log.error("ForumInstance.getMessages(), sql: "+sql_);

            String es = "Error in getMessages()";
            log.error(es, e);
            throw new ForumException(es, e);
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

        if (messages==null)
            return "";

        String r_ = "";
        try {

            ListIterator it = messages.listIterator();
            while (it.hasNext()) {
                ForumMessage message = (ForumMessage)it.next();

                r_ += StringTools.getMultypleString("&nbsp;&nbsp;&nbsp;&nbsp;", currentLevel);

                Long idValue = message.getId();
                Calendar cal = message.getDatePost();
                String dat = DateTools.getStringDate(cal, "dd-MM-yyyy HH:mm:ss", renderRequest.getLocale());

                r_ += "<b>";
                if (!message.getId().equals(currentMessageId)) {
                    r_ += ("<a href=\"" + PortletTools.ctx( renderRequest ) + '?' +
                        Constants.NAME_LANG_PARAM + '=' + renderRequest.getLocale().toString() + '&' +
                        ForumPortlet.NAME_ID_FORUM_PARAM + '=' + id_forum + '&' +
                        ForumPortlet.NAME_ID_MESSAGE_FORUM_PARAM + '='+ message.getId() + '&' +
                        ForumPortlet.NAME_YEAR_PARAM + '=' + cal.get(Calendar.YEAR) + '&' +
                        ForumPortlet.NAME_MONTH_PARAM + '=' + (cal.get(Calendar.MONTH) + 1) + '&' +
                        Constants.NAME_TYPE_CONTEXT_PARAM + '=' + ForumPortlet.CTX_TYPE_FORUM + '&' +
//                        Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' + ctxInstance.getNameTemplate() +
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
        catch (Throwable e){
            String es = "Error in getMessages()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
        finally {}
        return r_;
    }

    /**
     * @return id of added message
     */
    public Long addMessage(Long id_thread__, Long id_main__)
        throws ForumException {

        PortletRequest portletRequest = renderRequest;
        PreparedStatement st = null;
        Long currentId = null;

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance(true);

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName(SEQ_FORUM_MESSAGE);
            seq.setTableName(FORUM_THREADS_TABLE);
            seq.setColumnName("ID");
            currentId = new Long(dbDyn.getSequenceNextValue(seq));


            st = dbDyn.prepareStatement("insert into " + FORUM_THREADS_TABLE + " " +
                "(ID, id_main, id_forum, id_thread, date_post, header, fio, email, ip) " +
                "values " +
                "(?, ?, ?, ?, " + dbDyn.getNameDateBind() + ", ?, ?, ?, ?) ");
            RsetTools.setLong(st, 1, currentId);
            RsetTools.setLong(st, 2, id_main__);
            RsetTools.setLong(st, 3, id_forum);
            RsetTools.setLong(st, 4, id_thread__);
            dbDyn.bindDate(st, 5, DateTools.getCurrentTime());
            st.setString(6, PortletTools.getString(portletRequest, "h")); // header
            st.setString(7, PortletTools.getString(portletRequest, "n")); // name
            st.setString(8, PortletTools.getString(portletRequest, "e")); // email
            st.setString(9, (String)portletRequest.getAttribute( PortalConstants.PORTAL_REMOTE_ADDRESS_ATTRIBUTE ) );

            st.executeUpdate();

            DatabaseManager.insertBigText(dbDyn,
                currentId, "ID",
                PrimaryKeyTypeTypeType.NUMBER,
                "MAIN_FORUM_TEXT", "ID_MAIN_FORUM_TEXT",
                "MESSAGE_TEXT",
                PortletTools.getString(portletRequest, "b"),
                false);

            dbDyn.commit();
        }
        catch (Throwable e) {
            try {
                dbDyn.rollback();
            }
            catch (Exception ee) {
            }

            String es = "Error in addMessage()";
            log.error(es, e);
            throw new ForumException(es, e);
        }
        finally {
            DatabaseManager.close(dbDyn, st);
            st = null;
            dbDyn = null;
        }
        return currentId;
    }
}