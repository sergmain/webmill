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
  Date: 18.09.2006
  Time: 0:25:20
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
<fmt:setBundle basename="org.riverock.portlet.resource.Search" scope="request"/>

<div id="query-form">
    <table border="0">
        <portlet:renderURL var="portletUrl"/>
        <form method="POST" action="<c:out value='${portletUrl}'/>">
            <tr>
                <td width="50%"><input type="text" name="query" size="50" value="<c:out value='${query}'/>"></td>
                <td><input style="width:150px;" type="submit" name="search" value="<fmt:message key='search_button'/>" ></td>
            </tr>
        </form>
    </table>
</div>

<c:if test="${not empty result.resultItems}">
    <div id="query-result">
        <c:forEach items="${result.resultItems}" var="item">
            <div id="result-item">
                <div id="result-item-title">
                    <b><a href='<c:out value="${item.url}"/>'><c:out value="${item.title}"/></a></b>
                </div>
                <div id="result-item-desc">
                    <c:out value="${item.description}"/>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>


