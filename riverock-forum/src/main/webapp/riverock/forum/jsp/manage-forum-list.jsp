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
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@ include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.forum.i18n.ForumResources"/>

<request:isUserInRole role="wm.forum-admin, webmill.root">


    <!--forums-->
    <table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
        <tr>
            <td colspan="5" class="forum-td2">List all forums</td>
        </tr>
        <tr class="forum-th" align="center">
            <td nowrap class="forum-th">::</td>
            <td width="80%" class="forum-th">Forum name</td>
            <td class="forum-th"></td>
        </tr>
        <c:forEach var="forum" items="${forums.forums}">
            <tr>
                <td class="forum-td">:.</td>
                <td class="forum-td2">

                    <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                        <input type="hidden" name="action" value="manage-forum-list"/>
                        <input type="hidden" name="sub-action" value="update-forum"/>
                        <input type="hidden" name="forumId" value="<c:out value='${forum.forumId}'/>"/>
                        <input type="text" name="forum-name" value="<c:out value='${forum.forumName}'/>" size="30">
                        &nbsp;
                        <input type="submit" value="Update name"/>
                    </form>
                </td>
                <td class="forum-td">
                    <c:choose>
                        <c:when test="${forum.deleted}">
                            <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                                <input type="hidden" name="action" value="manage-forum-list"/>
                                <input type="hidden" name="sub-action" value="permanent-delete-forum"/>
                                <input type="hidden" name="forumId" value="<c:out value='${forum.forumId}'/>"/>
                                Forum deleted virtually
                                <input type="checkbox" value="1" name="confirm-delete"/>
                                <input type="submit" value="Permanet delete forum"/>
                            </form>
                            <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                                <input type="hidden" name="action" value="manage-forum-list"/>
                                <input type="hidden" name="sub-action" value="restore-forum"/>
                                <input type="hidden" name="forumId" value="<c:out value='${forum.forumId}'/>"/>
                                <input type="submit" value="Restore forum"/>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                                <input type="hidden" name="action" value="manage-forum-list"/>
                                <input type="hidden" name="sub-action" value="delete-forum"/>
                                <input type="hidden" name="forumId" value="<c:out value='${forum.forumId}'/>"/>
                                <input type="checkbox" value="1" name="confirm-delete"/>
                                <input type="submit" value="Delete forum"/>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>

        </c:forEach>

        <tr>
            <td class="forum-td" colspan="5">
                Count of lost user: <c:out value='${integrity.countLostUser}'/>
                <form name="FormPost" method="POST" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                    <input type="hidden" name="action" value="manage-forum-list"/>
                    <input type="hidden" name="sub-action" value="create-lost-user"/>
                    <input type="submit" value="Create lost user">
                </form>
            </td>
        </tr>

        <tr>
            <td class="forum-td" colspan="5">
                <form name="FormPost" method="POST" action="<c:out value='${genericBean.forumHomeUrl}'/>">
                    <input type="hidden" name="action" value="manage-forum-list"/>
                    <input type="hidden" name="sub-action" value="add-forum"/>
                    New top level forum forum
                    <input name="forum-name" type="text" value=""/>&nbsp;
                    <input type="submit" value="Add new top level forum">
                </form>
            </td>
        </tr>
    </table>


</request:isUserInRole>

<%@ include file="inc/footer.jsp" %>