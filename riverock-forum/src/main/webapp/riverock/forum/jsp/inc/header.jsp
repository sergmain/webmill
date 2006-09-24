<%--
  ~ org.riverock.generic - Database connectivity classes, part of Webmill portal
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
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


