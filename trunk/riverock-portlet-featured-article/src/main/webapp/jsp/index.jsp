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
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<c:out value="${errorMessage}" escapeXml="true"/>

<%
    if (request.isUserInRole("webmill.feature-article-manager") ||
        request.isUserInRole("webmill.portal-manager")) {

%>

<portlet:defineObjects/>
<table border="0">
    <portlet:actionURL var="portletUrl"/>
    <form method="POST" action="<c:out value='${portletUrl}'/>">
        <tr>
            <td><input style="width:150px;" type="submit" name="add-action" value="Add article"/></td>
        </tr>
        <tr>
            <td><input type="text" name="imageUrl" size="100"/></td>
        </tr>
        <tr>
            <td><textarea name="articleText" rows="5" cols="50" id="articleTextId"></textarea></td>
        </tr>
    </form>
    <script type="text/javascript">
        document.getElementById("articleTextId").focus();
    </script>
</table>

<table>
    <c:forEach items="${articles}" var="article">
        <portlet:actionURL var="portletUrl"/>
        <form method="POST" action="<c:out value='${portletUrl}'/>">
            <input type="hidden" name="articleId" value="<c:out value="${article.articleId}" escapeXml="false"/>"/>
            <tr>
                <td><textarea name="articleText" rows="5" cols="50"><c:out value="${article.articleText}" escapeXml="false"/></textarea></td>
                <td>
                    <input style="width:150px;" type="submit" name="save-action" value="Save"/>
                    <input style="width:150px;" type="submit" name="delete-action" value="Delete"/>
                </td>
            </tr>
        </form>
    </c:forEach>
</table>

    <%

        }
        else {
    %>


<table>
    <c:forEach items="${articles}" var="article">
        <tr>
            <td><c:out value="${article.articleText}" escapeXml="false"/></td>
        </tr>
    </c:forEach>
</table>

<%
    }
%>



