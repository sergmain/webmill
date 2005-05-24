<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<fmt:setBundle basename="bindBean.bundle"/>

<table border="0" cellspacing="0" cellpadding="2">
<tr>
<td valign="top">

<b>Авторизационные данные пользователей</b>
<b><fmt:message key="index.jsp.title" /></b>

<p><a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-bind-add"><c:out value="${bindBean.addButton}"/></a></p>
<table border="0" class="l">
<tr>
<th class="memberArea">Предприятие</th>
<th class="memberArea">ФИО</th>
<th class="memberArea">Логин</th>
<c:if test="${bindBean.company}">
   <th class="memberArea" width="5%">Уровень компании</th>
</c:if>
<c:if test="${bindBean.groupCompany}">
   <th class="memberArea" width="5%">Уровень группы компаний</th>
</c:if>
<c:if test="${bindBean.holding}">
   <th class="memberArea" width="5%">Уровень холдинга</th>
</c:if>
<th class="memberArea">Действие</th>
</tr>

<c:forEach var="user" items="${bindBean.users}">

<tr>
<td class="memberArea"><c:out value="${user.companyName}"/></td>
<td class="memberArea"><c:out value="${user.userName}"/></td>
<td class="memberArea"><c:out value="${user.userLogin}"/></td>
<c:if test="${bindBean.company}">
   <td class="memberArea"><c:out value="${user.companyLevel}"/></td>
</c:if>
<c:if test="${bindBean.groupCompany}">
   <td class="memberArea"><c:out value="${user.groupCompanyLevel}"/></td>
</c:if>
<c:if test="${bindBean.holding}">
   <td class="memberArea"><c:out value="${user.holdingLevel}"/></td>
</c:if>
<td class="memberAreaAction">
    <input type="button" value="<c:out value="${bindBean.changeButton}"/>" onclick="location.href='<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-bind-change&id_auth_user=<c:out value="${user.authUserId}"/>';">
    <input type="button" value="<c:out value="${bindBean.deleteButton}"/>" onclick="location.href='<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-bind-delete&id_auth_user=<c:out value="${user.authUserId}"/>';">
</td>
</tr>

</c:forEach>

</table>
<p><a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-bind-add"><c:out value="${bindBean.addButton}"/></a></p>
<p><a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=home">На начальную страницу</a></p>
</td>
</tr>
</table>


<%@include file="inc/footer.jsp"%>