<%@page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />
<fmt:setBundle basename="org.riverock.forum.i18n.ForumResources"/>

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td>
			<b><IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER="0" valign="middle">
                 &nbsp;
                 <a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>
                 &nbsp;&gt;&nbsp;
                 <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${postBean.f_id}'/>"><c:out value='${postBean.f_name}'/></A>
                 &nbsp;&gt;&nbsp;
                 <c:choose>
                    <c:when test="${postBean.reply}">
                       <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${postBean.t_id}'/>"><c:out value='${postBean.t_name}'/></A>
                       &nbsp;&gt;&nbsp;Reply the topic
                    </c:when>
                    <c:otherwise>
                       Post a new topic
                    </c:otherwise>
                 </c:choose>
			</b>
		</td>
	</tr>
</table>
<BR/>

<SCRIPT LANGUAGE="JavaScript">
<!--
var submitFlag=false;

function sCheck(){
	if(document.FormPost.subject.value.length<5){
		alert("Subject length must be great than 4 chars");
		return false;
	}
	if(document.FormPost.content.value.length<2){
		alert("Content length must be great than 1 chars");
		return false;
	}
	if(submitFlag){
		alert('Please wait...');
		return false;
	}else{
		submitFlag=true;
	}
	FormPost.submit();
}
//-->
</SCRIPT>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
	<tr class="forum-th">
		<td align="center" colspan="2" class="forum-th">Post & Reply</td>
	</tr>
	<form name="FormPost" method="POST" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	<input type="hidden" name="action" value="postp">
	<input type="hidden" name="forum_id" value="<c:out value='${postBean.forumId}'/>">
	<input type="hidden" name="reply" value="<c:out value='${postBean.reply}'/>">
    <c:choose>
        <c:when test="${postBean.reply}">
            <input type="hidden" name="t_id" value="<c:out value='${postBean.t_id}'/>">
        </c:when>
        <c:otherwise>
            <input type="hidden" name="f_id" value="<c:out value='${postBean.f_id}'/>">
        </c:otherwise>
    </c:choose>
	<tr class="forum-td">
		<td>Subject</td>
		<td>
        <c:if test="${postBean.reply}">
           <c:set var="topicName" value="${postBean.t_name}"/>
        </c:if>
		<input type="text" name="subject" size="60" maxlength="100" value="<c:out value='${topicName}'/>">
		</td>
	</tr>
	<tr class="forum-td">
		<td>Icon<br>
		<INPUT name="tm_iconid" type="radio" value="0" checked>None
		</td>
		<td>		
			 <c:forEach begin="1" end="14" var="index">
				<INPUT  name="tm_iconid" type="radio" value="<c:out value='${index}'/>">
				<IMG border="0" height="15" width="15" src="<%= application.getRealPath("/") %>/riverock/forum/img/icons/<c:out value='${index}'/>.gif">&nbsp;
                <c:if test="${ index == 7}">
                    <br>
                </c:if>
            </c:forEach>
		</td>
	</tr>


	<tr class="forum-td">
		<td>Content</td>
		<td><textarea name="content" cols="80" rows="10"></textarea></td>
	</tr>
	<tr class="forum-td">
		<td align="center" colspan="2">
          <input type="button" value="OK" onClick="sCheck();">
          &nbsp;&nbsp;
<%--          <input type="reset" value="Reset"></td>--%>
	</tr>

</table>

<%@include file="inc/footer.jsp"%>