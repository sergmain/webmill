<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>

<%@include file="inc/header.jsp" %>

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td>
			<b><IMG SRC="/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">&nbsp;
               <a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>
               &nbsp;&gt;&nbsp;
               <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${topicBean.f_id}'/>"><c:out value='${topicBean.f_name}'/></A>
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
<%--				<A HREF="<c:out value='${topicBean.postTopicUrl}'/>"><IMG SRC="/riverock/forum/img/post.gif" WIDTH="110" HEIGHT="26" BORDER=0 ALT="post"></A>--%>
<%--				&nbsp;&nbsp;--%>
                <A HREF="<c:out value='${topicBean.replyTopicUrl}'/>&reply=true"><IMG SRC="/riverock/forum/img/reply.gif" WIDTH="98" HEIGHT="22" BORDER="0" ALT="reply"></A>
		</td>
	</tr>
</table>

<!--messages-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
  <tr class="forum-th">
		<td  width="20%" class="forum-th">&nbsp;Author:</td>

		<td class="forum-th">
            <c:choose>
                 <c:when test="${topicBean.t_order > 0 && topicBean.t_locked > 0}">
                    <IMG SRC="/riverock/forum/img/lockedSticky.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topicBean.t_order > 0}">
                    <IMG SRC="/riverock/forum/img/sticky.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topicBean.t_locked > 0}">
                    <IMG SRC="/riverock/forum/img/locked.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topicBean.t_replies > 20}">
                    <IMG SRC="/riverock/forum/img/hotTopic.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:otherwise>
                    <IMG SRC="/riverock/forum/img/topic.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:otherwise>
            </c:choose>
			Subject:&nbsp;<c:out value='${topicBean.t_name}'/>  (Replies:<c:out value='${topicBean.t_replies}'/>)
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
			&nbsp;&nbsp;<img name="avatar" src="/riverock/forum/img/avatars/<c:out value='${message.u_avatar_id}'/>.gif" width="36" height="36" border="0"><BR>
            </c:if>

			&nbsp;&nbsp;Posts: <c:out value="${message.u_post}"/><BR>
			&nbsp;&nbsp;Location: <c:out value="${message.u_address}"/><BR>
			&nbsp;&nbsp;Registered:<BR>
            &nbsp;&nbsp;<fmt:formatDate value="${message.u_regtime}" pattern="dd/MM/yyyy HH:mm" />
		</td>
		<td class="<c:out value='${backgroundStyle}'/>">

				<table border="0" width="100%" cellpadding="0" cellspacing="0">
				<tr>
				<td>
					&nbsp;&nbsp;
                    <c:if test="${!empty message.m_iconid && message.m_iconid > 0}">
                        <img src="/riverock/forum/img/icons/<c:out value='${message.m_iconid}'/>.gif" width="15" height="15" border="0">
                    </c:if>
                    <fmt:formatDate value="${message.m_time}" pattern="dd/MM/yyyy HH:mm" />
					</td>
				<td  align="right">
                <c:choose>
                <c:when test="${message.edited}">
                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=edit-message&t_id=<c:out value='${topicBean.t_id}'/>&f_id=<c:out value='${topicBean.f_id}'/>&m_id=<c:out value='${message.m_id}'/>">
                    <IMG SRC="/riverock/forum/img/m_edit.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
                </A>
                </c:when>
                <c:otherwise>
                    <IMG SRC="/riverock/forum/img/m_edit.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
                </c:otherwise>
                </c:choose>

<%--					<IMG SRC="/riverock/forum/img/m_quote.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">--%>
				</td>
				</tr>
				<tr>
				<td  colspan="2" height="1" bgcolor="#999999">  
				</td>
				</tr>
					<tr>
						<td colspan="2"  valign="top"> 
						<BR>
						<p><c:out value="${message.m_content}"/></p>
						------------------------------------------------------------------<BR>
                        <c:out value="${message.u_sign}"/><BR><BR>
						</td>
					</tr>
					<tr>
						<td  colspan="2" height="1" bgcolor="#999999">  
						</td>
					</tr>
					<tr>
						<td colspan="2" valign="bottom" > 
<%--						<A HREF="user.do?u_id=<%=message.getM_u_id()%>">--%>
                            <IMG SRC="/riverock/forum/img/m_profile.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
<%--                            </A>--%>
							<IMG SRC="/riverock/forum/img/m_pm.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
							<IMG SRC="/riverock/forum/img/m_email.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
							<IMG SRC="/riverock/forum/img/m_www.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
							<IMG SRC="/riverock/forum/img/m_search.gif" WIDTH="55" HEIGHT="17" BORDER="0" ALT="">
						</td>
					</tr>
				</table>
		</td>
	</tr>
    </c:forEach>
<!--loop end-->

	<TR>
		<TD colspan="2" class="forum-td">

			<TABLE width="100%" border="0"  cellpadding="0" cellspacing="0">
			<TR>
				<TD align="left">
<%--				Topcis:&nbsp;<c:out value="${forumBean.count}"/>&nbsp;&nbsp;RPP:&nbsp;<c:out value="${forumBean.range}"/>&nbsp;&nbsp;Start:&nbsp;<c:out value="${forumBean.start}"/>--%>
                    &nbsp;
				</TD>
				<TD width="20%" ALIGN="right">
                    <c:if test="${topicBean.countPages > 1}">
                        [ <IMG SRC="/riverock/forum/img/multipage.gif" WIDTH="10" HEIGHT="12" BORDER="0"> to page:
                        <c:set var="isNotFirst" value="false"/>
                        <c:forEach begin="1" end="${topicBean.countPages}" var="index" >
                            <c:if test="${isNotFirst}">,</c:if>
                            <c:if test="${!isNotFirst}"><c:set var="isNotFirst" value="true"/></c:if>
                            <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&t_id=<c:out value='${topicBean.t_id}'/>&start=<c:out value='${topicBean.messagesPerPage * (index - 1) }'/>">
                            <c:out value="${index}"/>
                            </a>
                        </c:forEach>
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
	<tr>
		<td>
			Search:
			<INPUT TYPE="text" NAME="keyword" SIZE="10" MAXLENGTH="10" VALUE="<c:out value='${forumBean.keyword}'/>">
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
		 <IMG SRC="/riverock/forum/img/topic.gif" WIDTH="16" HEIGHT="16" BORDER=0 >&nbsp;Topic&nbsp;&nbsp;<IMG SRC="/riverock/forum/img/hotTopic.gif" WIDTH="16" HEIGHT="16" BORDER=0 >&nbsp;Hot Topic (More than 20 replies)<BR>
      <IMG SRC="/riverock/forum/img/sticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 >&nbsp;Sticky Topic&nbsp;&nbsp;<IMG SRC="/riverock/forum/img/locked.gif" WIDTH="16" HEIGHT="16" BORDER=0 >&nbsp;Locked Topic<BR>
	  <IMG SRC="/riverock/forum/img/lockedSticky.gif" WIDTH="16" HEIGHT="16" BORDER=0 >&nbsp;Locked Sticky Topic
		</td>
	</tr>
</table>

<%@include file="inc/footer.jsp"%>