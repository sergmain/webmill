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
            <b><img src="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16"
                    BORDER=0 alt="banner">&nbsp;
                <a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>
                &nbsp;&gt;&nbsp;
                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${topicBean.f_id}'/>">
                    <c:out value='${topicBean.f_name}'/></A>
                &nbsp;&gt;&nbsp;
                <c:out value="${topicBean.t_name}"/>
            </b>
        </td>
    </tr>
</table>

<!--post bar -->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td>
            Moderator:
            <%--<A HREF="user.do?u_id=<%=topicBean.getF_u_id()%>">--%>
            <c:out value="${topicBean.moderatorName}"/>
            <%--</A>--%>
        </td>
        <td align="right" valign="top">
            <%--				<A HREF="<c:out value='${topicBean.postTopicUrl}'/>"><img src="<%= application.getRealPath("/") %>/riverock/forum/img/post.gif" WIDTH="110" HEIGHT="26" BORDER=0 ALT="post"></A>--%>
            <%--				&nbsp;&nbsp;--%>
            <A HREF="<c:out value='${topicBean.replyTopicUrl}'/>&reply=true"><img
                src="<%= application.getRealPath("/") %>/riverock/forum/img/reply.gif" WIDTH="98" HEIGHT="22" BORDER="0"
                ALT="reply"></A>
        </td>
    </tr>
</table>

<!--messages-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
<tr class="forum-th">
    <td width="20%" class="forum-th">&nbsp;Author:</td>

    <td class="forum-th">
        <c:choose>
            <c:when test="${topicBean.t_order > 0 && topicBean.t_locked > 0}">
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/lockedSticky.gif" alt="lockedSticky"
                     WIDTH="16" HEIGHT="16" BORDER="0"/>
            </c:when>
            <c:when test="${topicBean.t_order > 0}">
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/sticky.gif" alt="sticky" WIDTH="16"
                     HEIGHT="16" BORDER="0"/>
            </c:when>
            <c:when test="${topicBean.t_locked > 0}">
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/locked.gif" alt="locked" WIDTH="16"
                     HEIGHT="16" BORDER="0"/>
            </c:when>
            <c:when test="${topicBean.t_replies > 20}">
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/hotTopic.gif" alt="hotTopic" WIDTH="16"
                     HEIGHT="16" BORDER="0"/>
            </c:when>
            <c:otherwise>
                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/topic.gif" alt="topic" WIDTH="16"
                     HEIGHT="16" BORDER="0"/>
            </c:otherwise>
        </c:choose>
        Subject:&nbsp;<c:out value='${topicBean.t_name}'/> (Replies:<c:out value='${topicBean.t_replies}'/>)
    </td>
</tr>

<!--loop begin-->
<c:set var="indexColor" value="1"/>
<c:forEach var="message" items="${topicBean.messages}">
    <c:set var="indexColor" value="${ 1 - indexColor }"/>
    <c:choose>
        <c:when test="${indexColor == 1}">
            <c:set var="backgroundStyle" value="forum-td"/>
        </c:when>
        <c:otherwise>
            <c:set var="backgroundStyle" value="forum-td2"/>
        </c:otherwise>
    </c:choose>

    <tr>

        <td class="<c:out value='${backgroundStyle}'/>" valign="top" cellpadding="5">
            &nbsp;&nbsp;<c:out value='${message.u_name}'/><BR>
            &nbsp;&nbsp;<c:out value='${message.r_name}'/><BR>
            <c:if test="${!empty message.u_avatar_id}">
                &nbsp;&nbsp;<img name="avatar" src="<%= application.getRealPath("/") %>/riverock/forum/img/avatars/<c:out value='${message.u_avatar_id}'/>.gif" width="36" height="36" border="0" alt="avatar">
                <BR>
            </c:if>

            &nbsp;&nbsp;Posts: <c:out value="${message.u_post}"/><BR>
            &nbsp;&nbsp;Location: <c:out value="${message.u_address}"/><BR>
            &nbsp;&nbsp;Registered:<BR>
            &nbsp;&nbsp;<fmt:formatDate value="${message.u_regtime}" pattern="dd/MM/yyyy HH:mm"/>
        </td>
        <td class="<c:out value='${backgroundStyle}'/>">

            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        &nbsp;&nbsp;
                        <c:if test="${!empty message.m_iconid && message.m_iconid > 0}">
                            <img
                                src="<%= application.getRealPath("/") %>/riverock/forum/img/icons/<c:out value='${message.m_iconid}'/>.gif"
                                width="15" height="15" border="0" alt="icon">
                        </c:if>
                        <fmt:formatDate value="${message.m_time}" pattern="dd/MM/yyyy HH:mm"/>
                    </td>
                    <td align="right">
                        <c:choose>
                            <c:when test="${message.edited}">
                                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=edit-message&t_id=<c:out value='${topicBean.t_id}'/>&f_id=<c:out value='${topicBean.f_id}'/>&m_id=<c:out value='${message.m_id}'/>">
                                    <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_edit.gif"
                                         WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
                                </A>
                            </c:when>
                            <c:otherwise>
                                <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_edit.gif" WIDTH="55"
                                     HEIGHT="17" BORDER="0" ALT="">
                            </c:otherwise>
                        </c:choose>

                            <%--					<img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_quote.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">--%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" height="1" bgcolor="#999999">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" valign="top">
                        <BR>

                        <p><c:out value="${message.m_content}" escapeXml="false"/></p>
                        ------------------------------------------------------------------<BR>
                        <c:out value="${message.u_sign}"/><BR><BR>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" height="1" bgcolor="#999999">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" valign="bottom">
                            <%--						<A HREF="user.do?u_id=<%=message.getM_u_id()%>">--%>
<%--
                        <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_profile.gif" WIDTH="55"
                             HEIGHT="17" BORDER="0" ALT="">
--%>
                            <%--                            </A>--%>
<%--
                        <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_pm.gif" WIDTH="55"
                             HEIGHT="17" BORDER="0" ALT="">
                        <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_email.gif" WIDTH="55"
                             HEIGHT="17" BORDER="0" ALT="">
                        <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_www.gif" WIDTH="55"
                             HEIGHT="17" BORDER="0" ALT="">
                        <img src="<%= application.getRealPath("/") %>/riverock/forum/img/m_search.gif" WIDTH="55"
                             HEIGHT="17" BORDER="0" ALT="">
--%>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</c:forEach>
<!--loop end-->

<TR>
    <TD colspan="2" class="forum-td">

        <TABLE width="100%" border="0" cellpadding="0" cellspacing="0">
            <TR>
                <TD align="left">
                    <%--				Topcis:&nbsp;<c:out value="${forumBean.count}"/>&nbsp;&nbsp;RPP:&nbsp;<c:out value="${forumBean.range}"/>&nbsp;&nbsp;Start:&nbsp;<c:out value="${forumBean.start}"/>--%>
                    &nbsp;
                </TD>
                <TD width="20%" ALIGN="right">
                    <c:if test="${topicBean.countPages > 1}">
                        [ <img src="<%= application.getRealPath("/") %>/riverock/forum/img/multipage.gif" WIDTH="10"
                               HEIGHT="12" BORDER="0" alt="multipage">
                        <fmt:message var="msg" key="to_page"/>
                        <c:forEach begin="1" end="${topicBean.countPages}" var="index" varStatus="varStatus">
                            <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${topicBean.t_id}'/>&start=<c:out value='${topicBean.messagesPerPage * (index - 1) }'/>"
                                ><c:out value="${index}"/></a><c:if test="${not varStatus.last}">, </c:if>
                        </c:forEach>
                        ]
                    </c:if>
                </TD>
            </TR>
        </table>

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
                Search:
                <INPUT TYPE="text" NAME="keyword" SIZE="10" MAXLENGTH="10"
                       VALUE="<c:out value='${forumBean.keyword}'/>">
                <INPUT TYPE="submit" VALUE="GO">
            </td>
            <td align="right" valign="top">
                Forum jump:
                <select name="f_id">
                    <c:forEach var="forum" items="${topicBean.forums}">
                        <c:set var="selectedForum" value=""/>
                        <c:if test="${forumBean.f_id == forum.forumId }">
                            <c:set var="selectedForum" value="selected"/>
                        </c:if>

                        <option value="<c:out value='${forum.forumId}'/>" <c:out value="${selectedForum}"/> >
                            <c:out value="${forum.forumName}"/></option>
                    </c:forEach>
                </select>
                <input type="submit" value="GO">
            </td>
        </tr>
    </form>
</table>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td>
            <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/topic.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="topic">&nbsp;
            <fmt:message key="topic_image"/>&nbsp;&nbsp;
            <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/hotTopic.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="hotTopic">&nbsp;
            <fmt:message key="hot_topic_image"/><BR>
            <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/sticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="sticky">&nbsp;
            <fmt:message key="sticky_image"/>&nbsp;&nbsp;
            <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/locked.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="locked">&nbsp;
            <fmt:message key="locked_image"/><BR>
            <IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lockedSticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 alt="lockedSticky">&nbsp;
            <fmt:message key="locked_sticky_image"/>
        </td>
    </tr>
</table>

<%@ include file="inc/footer.jsp" %>