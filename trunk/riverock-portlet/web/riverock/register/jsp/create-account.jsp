<%@ page language="java" contentType="text/html; charset=utf-8"
         import="javax.portlet.PortletRequest,org.riverock.webmill.container.*,java.util.*,org.riverock.webmill.container.*"
    %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>

<c:if test="${empty locale}">
    <c:set var="locale" value="en"/>
</c:if>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.register.i18n.Register" scope="request"/>


admin email: <%= ((PortletRequest) request).getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_ADMIN_EMAIL) %><br>

<%
    PortletRequest portletRequest = (PortletRequest) request;
    Enumeration e = portletRequest.getPortalContext().getPropertyNames();

    for (; e.hasMoreElements();) {
        String key = (String) e.nextElement();
        String value = portletRequest.getPortalContext().getProperty(key);
        out.println("key: " + key + ", value: " + value + "<BR>");
    }


    Map<String, String> p = (Map<String, String>) portletRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE);
    if (p == null) {
        out.println("portlet metadata is null");
    } else {
        for (Map.Entry<String, String> entry : p.entrySet()) {
            out.println("key: " + entry.getKey() + ", value: " + entry.getValue() + "<BR>");
        }
    }

%>

<br>
<fmt:message key="reg.need_register"/>

<table border="0">
    <form name="FormPost" method="POST" action="<c:out value='${registerBean.baseModuleUrl}'/>">
        <input type="hidden" name="action" value="create-account">
        <tr>
            <td witdh="25%"><fmt:message key="reg.login"/>:</td>
            <td><input type="text" name="username" size="20" maxlength="20"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.password"/>:</td>
            <td>
                <input type="password" name="password1" size="20" maxlength="20">
            </td>
        </tr>
        <tr>
            <td><fmt:message key="reg.password_repeat"/>:</td>
            <td><input type="password" name="password2" size="20" maxlength="20"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.first_name"/>:</td>
            <td><input type="text" name="first_name" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.last_name"/>:</td>
            <td><input type="text" name="last_name" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.telephone"/>:</td>
            <td><input type="text" name="phone" size="25" maxlength="25"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.address"/>:</td>
            <td><input type="text" name="addr" size="50" maxlength="50"></td>
        </tr>
        <tr>
            <td><fmt:message key="reg.email"/>:</td>
            <td><input type="text" name="email" size="30" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="<fmt:message key='reg.register'/>"></td>
        </tr>
    </form>
</table>


<!--

Registration Requirements
Username:
Display Name:
E-mail Address:
Password:
Retype Password:

I agree to be bound by this website's Terms of Use and Privacy Policy.

-->