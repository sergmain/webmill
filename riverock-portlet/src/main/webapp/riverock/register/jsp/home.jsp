<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>

<c:if test="${empty locale}">
    <c:set var="locale" value="en"/>
</c:if>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.register.i18n.Register" scope="request"/>


<table border="0">
    <tr>
        <td width="25%"></td><td></td>
    </tr>
    <td colspan="2"><fmt:message key="reg.forgot_password"/></td>

    <form method="POST" action="<c:out value='${registerBean.baseModuleUrl}'/>">
        <input type="hidden" name="action" value="send-password">
        <tr>
            <td><fmt:message key="reg.email"/></td>
            <td><input type="text" name="email" size="12"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="<fmt:message key='reg.send_password'/>"></td>
        </tr>
    </form>
    <tr>
        <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2"><fmt:message key="reg.need_register"/></td>
    </tr>

    <form name="FormPost" method="POST" action="<c:out value='${registerBean.baseModuleUrl}'/>">
        <input type="hidden" name="action" value="create-account">
        <input type="hidden" name="captchaId" value="<c:out value='${captchaId}'/>">
        <tr>
            <td><span style="color:red">*</span><fmt:message key="reg.login"/></td>
            <td><input type="text" name="username" size="20" maxlength="20"></td>
        </tr>
        <tr>
            <td><span style="color:red">*</span><fmt:message key="reg.password"/></td>
            <td>
                <input type="password" name="password1" size="20" maxlength="20">
            </td>
        </tr>
        <tr>
            <td><span style="color:red">*</span><fmt:message key="reg.password_repeat"/></td>
            <td><input type="password" name="password2" size="20" maxlength="20"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.first_name"/></td>
            <td><input type="text" name="first_name" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.last_name"/></td>
            <td><input type="text" name="last_name" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.telephone"/></td>
            <td><input type="text" name="phone" size="25" maxlength="25"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.address"/></td>
            <td><input type="text" name="addr" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><span style="color:red">*</span><fmt:message key="reg.email"/></td>
            <td><input type="text" name="email" size="30" maxlength="30"></td>
        </tr>
        <tr>
            <td><span style="color:red">*</span><fmt:message key="reg.captcha"/></td>
            <td>
                <img src="<%= request.getContextPath() %>/jcaptcha?id=<c:out value='${captchaId}'/>" alt="captcha"><br/>
                <input type="text" name="j_captcha_response" value="">
            </td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="<fmt:message key='reg.register'/>"></td>
        </tr>
    </form>
</table>




