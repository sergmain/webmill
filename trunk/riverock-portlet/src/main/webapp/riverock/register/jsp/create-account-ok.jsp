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
<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>

<c:if test="${empty locale}">
    <c:set var="locale" value="en"/>
</c:if>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.register.i18n.Register" scope="request"/>

<table width="95%" border="0" cellspacing="1" cellpadding="5">

	<tr >
		<td>
		<fmt:message key="reg.succes_register"/>
		</td>
	</tr>
</table>

