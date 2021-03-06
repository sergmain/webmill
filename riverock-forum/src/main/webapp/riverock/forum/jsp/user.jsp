<%--
  ~ org.riverock.forum - Forum portlet
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
  --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         import="org.riverock.forum.util.*,org.riverock.forum.bean.*, org.riverock.common.tools.StringTools" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@ include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.forum.i18n.ForumResources"/>

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td>
            <b><IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16"
                    BORDER=0 valign="middle">&nbsp;<a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out
                value="${genericBean.forumName}"/></A>&nbsp;&gt;&nbsp;View Profile</b>
        </td>
    </tr>
</table>
<BR>
<!--user-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
    <tr class="forum-th">
        <td align="center" colspan="2"><font color="<%=TH_TEXT_COLOR%>">View Profile</font></td>
    </tr>

    <tr class="forum-td">
        <td>
            <BR>
            <table align="center" border="0">
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">User Name</td>
                    <td bgcolor="#F3F3F3">
                        <%=StringTools.encodeXml(userBean.getU_name())%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Avatar</td>
                    <td bgcolor="#F3F3F3"><img
                        src="<%= application.getRealPath("/") %>/riverock/forum/img/avatars/<%=userBean.getU_avatar_id()%>.gif"
                        width=36 height=36 border="0"></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Email</td>
                    <td bgcolor="#F3F3F3">
                        <%=StringTools.encodeXml(userBean.getU_email())%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Register Time</td>
                    <td bgcolor="#F3F3F3"><%=DateTimeUtil.shortFmt(userBean.getU_regtime())%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Address</td>
                    <td bgcolor="#F3F3F3">
                        <%=StringTools.encodeXml(userBean.getU_address())%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">sign</td>
                    <td bgcolor="#F3F3F3"><%=ForumStringUtils.displayHtml(userBean.getU_sign())%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Post</td>
                    <td bgcolor="#F3F3F3"><%=userBean.getU_post()%></td>
                </tr>
                <tr>
                    <td width="50%" bgcolor="#F3F3F3">Last Post Time</td>
                    <td bgcolor="#F3F3F3"><%=DateTimeUtil.shortFmt(userBean.getU_lasttime())%></td>
                </tr>
            </table>
            <BR>
        </td>
    </tr>
</table>

<%@ include file="inc/footer.jsp" %>
