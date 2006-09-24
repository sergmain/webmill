<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
<%@ page language="java" contentType="text/html; charset=utf-8"
 import="org.apache.log4j.Logger"
%>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td>
			<b><IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">&nbsp;<a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>&nbsp;&gt;&nbsp;Error</b>
		</td>
	</tr>
</table>

<BR />
<%!
            Logger log = Logger.getLogger("actionMessage.jsp");
%>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">

	<tr class="forum-th">
		<td align="center" class="forum-th">Errror</td>
	</tr>
	<tr >	
		<td class="forum-th" align="center" >
        <%
            final Object attribute = request.getAttribute("actionMessage");
            log.error("bean value: " + attribute );
            if (attribute!=null) {
                log.error("bean class: " + attribute.getClass().getName() );
            }
        %>
		<font color="#FF0000"><b><c:out value="${actionMessage.value}"/></b></font>
		</td>
	</tr>
	<tr >
		<td class="forum-th" align="center" >
		<a href="javascript:history.go(-1)"><font color="#000000">Go back</font></a>
		</td>
	</tr>		
</table>				
				
<%@include file="inc/footer.jsp"%>

