<%@ page language="java" contentType="text/html; charset=utf-8" import="org.apache.log4j.Logger" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>

<c:if test="${empty locale}">
    <c:set var="locale" value="en"/>
</c:if>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.register.i18n.Register" scope="request"/>

<fmt:message key="reg.forgot_password"/>
<table border="0">
    <form method="POST" action="">
        <input type="hidden" name="action" value="send-password">
        <tr>
            <td witdh="25%"><fmt:message key="reg.member_email"/>: </td>
            <td><input type="text" name="email" size="12"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="<fmt:message key='reg.send_password'/>"></td>
        </tr>
    </form>
</table>






 