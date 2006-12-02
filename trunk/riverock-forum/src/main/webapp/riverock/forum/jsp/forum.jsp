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

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td valign="middle">
            <b><img src="<%= request.getContextPath() %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16"
                    BORDER="0" alt="banner">&nbsp;
                <a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>
                &nbsp;&gt;&nbsp;<c:out value="${forumBean.f_name}"/></b>
        </td>
    </tr>
</table>

<!--post bar -->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td>
            <fmt:message key="moderator"/> <A HREF="<c:out value='${forumBean.urlToModeratorInfo}'/>">
            <c:out value="${forumBean.moderatorName}"/></A>
        </td>
        <td align="right">
            <A HREF="<c:out value="${forumBean.urlToPostThread}"/>">
                <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/post.gif" WIDTH="110" HEIGHT="26"
                     BORDER=0 ALT="post">
            </A>
        </td>
    </tr>
</table>

<!--topics-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
    <tr class="forum-th">
        <td nowrap class="forum-th">::</td>
        <td nowrap class="forum-th"><fmt:message key="topic_forum_table_header"/></td>
        <td nowrap class="forum-th"><fmt:message key="poster_forum_table_header"/></td>
        <td nowrap class="forum-th"><fmt:message key="replies_forum_table_header"/></td>
        <td nowrap class="forum-th"><fmt:message key="views_forum_table_header"/></td>
        <td nowrap class="forum-th"><fmt:message key="last_post_forum_table_header"/></td>
    </tr>

    <!--loop begin-->
    <c:forEach var="topic" items="${forumBean.topics}">
        <tr>
            <td class="forum-td">
                <c:choose>
                    <c:when test="${topic.t_order > 0 && topic.t_locked > 0}">
                        <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/lockedSticky.gif" WIDTH="16"
                             HEIGHT="16" BORDER="0" alt="lockedSticky"/>
                    </c:when>
                    <c:when test="${topic.t_order > 0}">
                        <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/sticky.gif" WIDTH="16"
                             HEIGHT="16" BORDER="0" alt="sticky"/>
                    </c:when>
                    <c:when test="${topic.t_locked > 0}">
                        <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/locked.gif" WIDTH="16"
                             HEIGHT="16" BORDER="0" alt="locked"/>
                    </c:when>
                    <c:when test="${topic.t_replies > 20}">
                        <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/hotTopic.gif" WIDTH="16"
                             HEIGHT="16" BORDER="0" alt="hotTopic"/>
                    </c:when>
                    <c:otherwise>
                        <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/topic.gif" WIDTH="16"
                             HEIGHT="16" BORDER="0" alt="topic"/>
                    </c:otherwise>
                </c:choose>
            </td>

            <td class="forum-td" width="70%">
                <c:if test="${topic.t_iconid > 0}">
                    <img src="<%= request.getContextPath() %>/riverock/forum/img/icons/<c:out value='${topic.t_iconid}'/>.gif"
                         width="15" height="15" border="0" alt="icon">
                </c:if>
                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${topic.t_id}'/>">
                    <c:out value="${topic.t_name}"/></A>
                <c:if test="${topic.countPages > 1}">
                    <br>
                    [ <img SRC="<%= request.getContextPath() %>/riverock/forum/img/multipage.gif" WIDTH="10"
                           HEIGHT="12" BORDER="0" alt="multipage topic">
                    <fmt:message key="to_page"/>
                    <c:forEach begin="1" end="${topic.countPages}" var="index" varStatus="varStatus">
                        <a href="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${topic.t_id}'/>&start=<c:out value='${forumBean.messagesPerPage * (index - 1) }'/>"
                            ><c:out value="${index}"/></a><c:if test="${not varStatus.last}">, </c:if>
                    </c:forEach>
                    ]
                </c:if>
            </td>
            <td class="forum-td" width="30%">
                <A HREF="<c:out value='${topic.t_iconid}'/>"><c:out value="${topic.u_name}"/></A>
            </td>
            <td class="forum-td2"><c:out value="${topic.t_replies}"/></td>
            <td class="forum-td"><c:out value="${topic.t_views}"/></td>
            <td nowrap class="forum-td2" align="right">
                <fmt:formatDate value="${topic.t_lasttime}" pattern="dd/MM/yyyy HH:mm"/>
                <BR>
                by <A HREF="<c:out value='${topic.urlToLastPosterInfo}'/>">
                <c:out value="${topic.u_name2}"/>
            </a>
            </td>
        </tr>
    </c:forEach>
    <!--loop end-->

    <TR>
        <TD colspan="6" class="forum-td">
            <!--split page-->
            <TABLE width="100%" border="0" cellpadding="0" cellspacing="0">
                <TR>
                    <TD align="left">
                        <fmt:message key="topics_stat"/>&nbsp;<c:out value="${forumBean.count}"/>&nbsp;&nbsp;<fmt:message key="rpp_stats"/>&nbsp;<c:out
                        value="${forumBean.topicsPerPage}"/>&nbsp;&nbsp;<fmt:message key="start_stat"/>&nbsp;<c:out value="${forumBean.start}"/>
                    </TD>
                    <TD width="20%" ALIGN="right">
                        <c:if test="${forumBean.countPages > 1}">
                            <br>
                            [ <img SRC="<%= request.getContextPath() %>/riverock/forum/img/multipage.gif" WIDTH="10"
                                   HEIGHT="12" BORDER="0" alt="pager">
                            <fmt:message var="msg" key="to_page"/>
                            <c:forEach begin="1" end="${forumBean.countPages}" var="index" varStatus="varStatus">
                                <a href="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${forumBean.f_id}'/>&start=<c:out value='${forumBean.topicsPerPage * (index - 1) }'/>"
                                    ><c:out value="${index}"/></a><c:if test="${not varStatus.last}">, </c:if>
                            </c:forEach>
                            ]
                        </c:if>
                    </TD>
                </TR>
            </TABLE>
        </TD>
    </TR>

</table>

<BR>
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <form method="GET" action="<c:out value='${genericBean.forumHomeUrl}' escapeXml="false" />">
        <input type="hidden" name="action" value="forum"/>
        <input type="hidden" name="forum_id" value="<c:out value="${genericBean.forumId}"/>"/>
        <tr>
            <td>
                <fmt:message key="search_form"/>
                <INPUT TYPE="text" NAME="keyword" SIZE="10" MAXLENGTH="10" VALUE="<c:out value='${forumBean.keyword}'/>">
                <INPUT TYPE="submit" VALUE="<fmt:message key="submit_search_form"/>">
            </td>
            <td align="right" valign="top">
                <fmt:message key="forum_jump"/>
                <select name="f_id">
                    <c:forEach var="forum" items="${forumBean.forums}">
                        <c:set var="selectedForum" value=""/>
                        <c:if test="${forumBean.f_id == forum.forumId }">
                            <c:set var="selectedForum" value="selected"/>
                        </c:if>

                        <option value="<c:out value='${forum.forumId}'/>" <c:out value="${selectedForum}"/> >
                            <c:out value="${forum.forumName}"/></option>
                    </c:forEach>
                </select>
                <input type="submit" value="<fmt:message key="submit_search_form"/>">
            </td>
        </tr>
    </form>
</table>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td>
            <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/topic.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="topic">&nbsp;
            <fmt:message key="topic_image"/>&nbsp;&nbsp;
            <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/hotTopic.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="hotTopic">&nbsp;
            <fmt:message key="hot_topic_image"/><BR>
            <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/sticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="sticky">&nbsp;
            <fmt:message key="sticky_image"/>&nbsp;&nbsp;
            <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/locked.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="locked">&nbsp;
            <fmt:message key="locked_image"/><BR>
            <IMG SRC="<%= request.getContextPath() %>/riverock/forum/img/lockedSticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="lockedSticky">&nbsp;
            <fmt:message key="locked_sticky_image"/>
        </td>
    </tr>
</table>

<%@ include file="inc/footer.jsp" %>