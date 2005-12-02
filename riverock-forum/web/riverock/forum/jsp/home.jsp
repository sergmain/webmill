<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" >
	<tr>
		<td>
			<b>
                <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">
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