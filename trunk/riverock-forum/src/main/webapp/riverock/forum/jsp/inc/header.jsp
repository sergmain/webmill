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
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="session" uri="/tld/jakarta-session" %>


<table width="758" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#999999">
  <tr>
	<td bgcolor="#e2e3e4">
		<TABLE width="95%" border="0" align="center" height="80">
			<TR>
				<TD>
<%--                          <a href="<c:out value='${genericBean.forumHomeUrl}'/>">--%>
<%--                             <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_logo.gif" WIDTH="253" HEIGHT="32" BORDER="0" ALT="">--%>
<%--                          </a>--%>
                </TD>
				<TD align="right">
                     <c:choose>
                         <c:when test="${empty principal}">
                            <c:if test="${!empty genericBean.loginUrl}">
                            <A HREF="<c:out value='${genericBean.loginUrl}'/>">Login</A>&nbsp;|&nbsp;
                            </c:if>
                            <c:if test="${!empty genericBean.registerUrl}">
                            <A HREF="<c:out value='${genericBean.registerUrl}'/>">Register</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:when>
                         <c:otherwise>
                            <c:if test="${!empty genericBean.logoutUrl}">
                               <A HREF="<c:out value='${genericBean.logoutUrl}'/>">Logout</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:otherwise>
                     </c:choose>

                     <c:if test="${!empty genericBean.membersUrl}">
                     <A HREF="<c:out value='${genericBean.membersUrl}'/>">Members</A>&nbsp;|&nbsp;
                     </c:if>

                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=help">Help</A>&nbsp;|&nbsp;
                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>">Home</A>
				</TD>
			</TR>
		</TABLE>


