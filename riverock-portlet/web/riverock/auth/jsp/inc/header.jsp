<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="jstl/core" %>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="auth">
  <tr>
	<td class="auth">


<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" >
<tr>
	<td>
        <a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-bind-index">Bind right</a>
    </td>
    <td>
        <a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-right-index">Rights on module</a>
    </td>
    <td>
        <a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-role-index">Roles</a>
    </td>
    <td>
        <a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-role-index">Roles</a>
    </td>
    <td>
        <a href="<c:out value='${genericBean.baseModuleUrl}'/>&action=auth-role-index">Roles</a>
    </td>
</tr>
</table>
