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
<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />
<fmt:setBundle basename="org.riverock.forum.i18n.ForumResources"/>

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" >
	<tr>
		<td valign="middle">
			<b>
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="banner">
                <c:out value="${genericBean.forumName}"/>
            </b>
		</td>
	</tr>
</table>

<!--login bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" >
	<tr>
		<td align="left">
             <c:choose>
                 <c:when test="${empty principal}">
                     Welcome, Guest.
                     <c:choose>
                         <c:when test="${!empty genericBean.loginUrl && !empty genericBean.registerUrl}">
                              Please <A HREF="<c:out value='${genericBean.loginUrl}'/>">Login</A> or
                              <A HREF="<c:out value='${genericBean.registerUrl}'/>">Register</A>.
                         </c:when>
                         <c:when test="${!empty genericBean.loginUrl}">
                              Please <A HREF="<c:out value='${genericBean.loginUrl}'/>">Login</A>.
                         </c:when>
                         <c:when test="${!empty genericBean.registerUrl}">
                              Please <A HREF="<c:out value='${genericBean.registerUrl}'/>">Register</A>.
                         </c:when>
                     </c:choose>
                 </c:when>
                 <c:otherwise>
                     Welcome, <c:out value="${principal.name}"/>.
<%--<A HREF="userEdit.do">Edit Profile</A> /--%>
                            <c:if test="${!empty genericBean.logoutUrl}">
                               <A HREF="<c:out value='${genericBean.logoutUrl}'/>">Logout</A>
                            </c:if>
                 </c:otherwise>
             </c:choose>
		</td>
		<td align="right">
			Members: <c:out value="${forumBean.userSum}"/><BR>
			Topics: <c:out value="${forumBean.topicSum}"/>  |  Messages: <c:out value="${forumBean.messageSum}"/>
		</td>
	</tr>
</table>

<!--forums-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
<c:forEach var="category" items="${forumBean.forumCategories}">
<tr>
    <td colspan="5" class="forum-td2"><c:out value="${category.categoryName}"/></td>
</tr>
<tr class="forum-th">
	<td nowrap  class="forum-th">::</td>
	<td width="80%" class="forum-th">Forum</td>
	<td nowrap class="forum-th">Topics</td>
	<td nowrap class="forum-th">Messsages</td>
	<td nowrap class="forum-th">Last Post</td>
</tr>
<c:forEach var="forum" items="${category.forums}">
<tr>
	<td class="forum-td">:.</td>
	<td class="forum-td2">
		<A HREF="<c:out value='${forum.forumUrl}'/>"><c:out value='${forum.f_name}'/></A><BR>
		<c:out value='${forum.f_info}'/>
	</td>
	<td class="forum-td"><c:out value='${forum.f_topics}'/></td>
	<td class="forum-td2"><c:out value='${forum.f_messages}'/></td>
	<td nowrap class="forum-td" align="right">
    <c:if test="${!empty forum.f_lasttime}">
        <fmt:formatDate value="${forum.f_lasttime}" pattern="dd/MM/yyyy HH:mm" />
		by <A HREF="user.do?u_id=<c:out value='${forum.lastPorterId}'/>"><c:out value='${forum.lastPosterName}'/>
    </c:if>
    <c:if test="${empty forum.f_lasttime}">
        none
    </c:if>
	</td>
</tr>

</c:forEach>
</c:forEach>
</table>


<request:isUserInRole role="wm.forum-admin, webmill.root">
    You are in role "admin".
    <p><A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=admin-forum">Admin this forum</A></p>
</request:isUserInRole>


<!--personal info-->
<BR>
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
	 <tr class="forum-th">
		<td>Personal infomation:</td>
	</tr>

	<tr>
		<td class="forum-th">
        <c:out value="${genericBean.remoteAddr}"/>&nbsp;<c:out value="${genericBean.userAgent}"/>
		</td>
	</tr>
</table>
<!--Links-->
<BR>
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
	 <tr class="forum-th">
		<td>
			Links:
		</td>
	</tr>

	<tr  class="forum-td">
		<td>
		<a href="#" ></a>
		</td>
	</tr>

</table>
<%@include file="inc/footer.jsp"%>