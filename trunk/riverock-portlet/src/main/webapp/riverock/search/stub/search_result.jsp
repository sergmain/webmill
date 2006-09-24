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


<table border="0">
    <tr>
            <fmt:message key="search_result_empty" >
                <fmt:param value="${searchWord}"/>
            </fmt:message>
        </td>
    </tr>
    <portlet:actionURL var="portletUrl"/>
    <form method="POST" action="<c:out value='${portletUrl}'/>">
        <tr>
            <td width="50%"><input type="text" name="searchWord" size="50" value="<c:out value='${searchWord}'/>"></td>
            <td><input style="width:150px;" type="submit" name="search" value="<fmt:message key='search_button'/>" ></td>
        </tr>
    </form>
</table>



