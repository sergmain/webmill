<%@ page contentType="text/html; charset=utf-8" language="java" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request"/>

<table width="95%" border="0" cellspacing="1" cellpadding="5">

    <tr>
        <td>
            <c:out value="${actionMessage.value}"/>
        </td>
    </tr>
    <tr>
        <td class="forum-th" align="center">
            <a href="javascript:history.go(-1)"><font color="#000000">Go back</font></a>
        </td>
    </tr>
</table>				
				
