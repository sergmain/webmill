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
          &nbsp;&gt;&nbsp;<c:out value="${forumBean.f_name}"/></b>
		</td>
	</tr>
</table>

<!--post bar -->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td>
			Moderator: <A HREF="<c:out value='${forumBean.urlToModeratorInfo}'/>">
                       <c:out value="${forumBean.moderatorName}"/></A>
		</td>
		<td align="right">
			<A HREF="<c:out value="${forumBean.urlToPostThread}"/>">
              <IMG SRC="/riverock/forum/img/post.gif" WIDTH="110" HEIGHT="26" BORDER=0 ALT="post">
           </A>
		</td>
	</tr>
</table>

<!--topics-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
  <tr class="forum-th">
	<td nowrap class="forum-th">::</td>
	<td nowrap class="forum-th">Topic</td>
	<td nowrap class="forum-th">Poster</td>
	<td nowrap class="forum-th">Replies</td>
	<td nowrap class="forum-th">Views</td>
	<td nowrap class="forum-th">Last Post</td>
  </tr>

<!--loop begin-->
    <c:forEach var="topic" items="${forumBean.topics}">
	<tr>
		<td class="forum-td">
            <c:choose>
                 <c:when test="${topic.t_order > 0 && topic.t_locked > 0}">
                    <IMG SRC="/riverock/forum/img/lockedSticky.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topic.t_order > 0}">
                    <IMG SRC="/riverock/forum/img/sticky.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topic.t_locked > 0}">
                    <IMG SRC="/riverock/forum/img/locked.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:when test="${topic.t_replies > 20}">
                    <IMG SRC="/riverock/forum/img/hotTopic.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:when>
                 <c:otherwise>
                    <IMG SRC="/riverock/forum/img/topic.gif" WIDTH="16" HEIGHT="16" BORDER="0" />
                 </c:otherwise>
            </c:choose>
		</td>

		<td class="forum-td" width="70%">
            <c:if test="${topic.t_iconid > 0}">
                <img src="/riverock/forum/img/icons/<c:out value='${topic.t_iconid}'/>.gif" width="15" height="15" border="0">
            </c:if>
			<A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${topic.t_id}'/>"><c:out value="${topic.t_name}"/></A>
            <c:if test="${topic.countPages > 1}">
                <br>
                [ <IMG SRC="/riverock/forum/img/multipage.gif" WIDTH="10" HEIGHT="12" BORDER="0"> to page:
                <c:set var="isNotFirst" value="false"/>
                <c:forEach begin="1" end="${topic.countPages}" var="index" >
                    <c:if test="${isNotFirst}">,</c:if>
                    <c:if test="${!isNotFirst}"><c:set var="isNotFirst" value="true"/></c:if>
                    <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${topic.t_id}'/>&start=<c:out value='${forumBean.messagesPerPage * (index - 1) }'/>">
                    <c:out value="${index}"/>
                    </a>
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
            <fmt:formatDate value="${topic.t_lasttime}" pattern="dd/MM/yyyy HH:mm" />
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
			<TABLE width="100%" border="0"  cellpadding="0" cellspacing="0">
			<TR>
				<TD align="left">
				Topcis:&nbsp;<c:out value="${forumBean.count}"/>&nbsp;&nbsp;RPP:&nbsp;<c:out value="${forumBean.topicsPerPage}"/>&nbsp;&nbsp;Start:&nbsp;<c:out value="${forumBean.start}"/>
				</TD>
				<TD width="20%" ALIGN="right">
                    <c:if test="${forumBean.countPages > 1}">
                        <br>
                        [ <IMG SRC="/riverock/forum/img/multipage.gif" WIDTH="10" HEIGHT="12" BORDER="0"> to page:
                        <c:set var="isNotFirst" value="false"/>
                        <c:forEach begin="1" end="${forumBean.countPages}" var="index" >
                            <c:if test="${isNotFirst}">,</c:if>
                            <c:if test="${!isNotFirst}"><c:set var="isNotFirst" value="true"/></c:if>
                            <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${forumBean.f_id}'/>&start=<c:out value='${forumBean.topicsPerPage * (index - 1) }'/>">
                            <c:out value="${index}"/>
                            </a>
                        </c:forEach>
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
			Search:
			<INPUT TYPE="text" NAME="keyword" SIZE="10" MAXLENGTH="10" VALUE="<c:out value='${forumBean.keyword}'/>">
			<INPUT TYPE="submit" VALUE="GO">
		</td>
		<td align="right" valign="top">
		Forum jump:
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