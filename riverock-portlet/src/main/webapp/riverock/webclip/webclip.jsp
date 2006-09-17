<%--
~ org.riverock.portlet - Portlet Library
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
<%--
  User: SergeMaslyukov
  Date: 10.09.2006
  Time: 19:17:21
  $Id$
--%>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<portlet:defineObjects/>

<%
    Object locale = request.getAttribute("request-locale");

    if (!(locale instanceof String) || StringUtils.isBlank((String)locale)) {
        locale = "en";
    }
    pageContext.setAttribute("locale", locale);
%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.resource.Webclip" scope="request"/>


<table border="0">
    <%
        if (request.isUserInRole("webmill.webclip-manager") ||
            request.isUserInRole("webmill.portal-manager")) {
    %>
    <portlet:actionURL var="portletUrl"/>
    <form method="POST" action="<c:out value='${portletUrl}'/>">
        <tr>
            <td width="25%"><fmt:message key="webclip_url"/></td>
            <td colspan="3" width="50%"><input type="text" name="sourceUrl" size="100" value="<c:out value='${sourceUrl}'/>"></td>
        </tr>
        <tr>                              
            <td width="25%"><fmt:message key="webclip_href_start_page"/></td>
            <td width="25%"><input type="text" name="hrefStartPart" size="30" value="<c:out value='${hrefStartPart}'/>"></td>
            <td width="25%"><fmt:message key="webclip_new_href_prefix"/></td>
            <td width="25%"><input type="text" name="newHrefPrefix" size="30" value="<c:out value='${newHrefPrefix}'/>"></td>
        </tr>
        <tr>
            <td colspan="4"><input style="width:150px;" type="submit" name="save" value="<fmt:message key='save_webclip_action'/>"></td>
        </tr>
        <tr>
            <td colspan="4">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="4"><input style="width:150px;" type="submit" name="refresh" value="<fmt:message key='refresh_webclip_data_action'/>" ></td>
        </tr>
    </form>
    <%
        }
    %>
</table>

<c:out value="${webclipBean.webclipData}" escapeXml="false"/>



