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

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
    <tr>
        <td valign="middle">
            <b><img src="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER="0" alt="banner">
                &nbsp;
                <a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>
                &nbsp;&gt;&nbsp;
                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=forum&f_id=<c:out value='${messageBean.forumId}'/>"><c:out value='${messageBean.forumName}'/></A>
                &nbsp;&gt;&nbsp;
                <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=topic&t_id=<c:out value='${messageBean.topicId}'/>"><c:out value='${messageBean.topicName}'/></A>
                &nbsp;&gt;&nbsp;Edit the topic
            </b>
        </td>
    </tr>
</table>
<BR/>

<script language="JavaScript" type="text/javascript">
    <!--
    var submitFlag = false;

    function sCheck() {
        if (document.FormPost.content.value.length < 2) {
            alert("Content length must be great than 1 chars");
            return false;
        }
        if (submitFlag) {
            alert('Please wait...');
            return false;
        } else {
            submitFlag = true;
        }
        FormPost.submit();
    }
    //-->
</script>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
    <tr class="forum-th">
        <td align="center" colspan="2" class="forum-th">Edit message</td>
    </tr>

    <form name="FormPost" method="POST" action="<c:out value='${genericBean.actionUrl}'/>">
        <input type="hidden" name="action" value="commit-edit-message">
        <input type="hidden" name="forum_id" value="<c:out value='${messageBean.forumId}'/>">
        <input type="hidden" name="t_id" value="<c:out value='${messageBean.topicId}'/>">
        <input type="hidden" name="m_id" value="<c:out value='${messageBean.messageId}'/>">
        <tr class="forum-td">
            <td>Icon<br>
                <c:if test="${messageBean.iconId==0}">
                    <c:set var="iconChecked1" value="checked"/>
                </c:if>
                <INPUT name="tm_iconid" type="radio" value="0"
                <c:out value='${iconChecked1}'/>>None
            </td>
            <td>
                <c:forEach var="index" begin="1" end="14">
                    <c:set var="iconChecked" value=""/>
                    <c:if test="${messageBean.iconId == index}">
                        <c:set var="iconChecked" value="checked"/>
                    </c:if>

                    <input name="tm_iconid" type="radio" value="<c:out value='${index}'/>"
                        <c:out value='${iconChecked}'/>>
                    <img border="0" height="15" width="15" src="<%= application.getRealPath("/") %>/riverock/forum/img/icons/<c:out value='${index}'/>.gif" alt="icon">&nbsp;
                    <c:if test="${ index == 7}">
                        <br>
                    </c:if>
                </c:forEach>
            </td>
        </tr>

        <tr class="forum-td">
            <td>Content</td>
            <td><textarea name="content" cols="80" rows="10"><c:out value="${messageBean.content}"/></textarea></td>
        </tr>
        <tr class="forum-td">
            <td align="center" colspan="2">
                <input type="button" value="OK" onClick="sCheck();">
                &nbsp;&nbsp;
                <%--             <input type="reset" value="Reset">--%>
            </td>
        </tr>
    </form>
</table>

<%@ include file="inc/footer.jsp" %>