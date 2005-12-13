<%@ page 
 contentType="text/html; charset=utf-8"
 language="java"
 import="org.apache.log4j.Logger"
 %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>


<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />

<%!
            Logger log = Logger.getLogger("actionMessage.jsp");
%>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">

	<tr class="forum-th">
		<td align="center" class="forum-th">Errror</td>
	</tr>
	<tr >	
		<td class="forum-th" align="center" >
        <%
            final Object attribute = request.getAttribute("actionMessage");
            log.error("bean value: " + attribute );
            if (attribute!=null) {
                log.error("bean class: " + attribute.getClass().getName() );
            }
        %>
		<font color="#FF0000"><b><c:out value="${actionMessage.value}"/></b></font>
		</td>
	</tr>
	<tr >
		<td class="forum-th" align="center" >
		<a href="javascript:history.go(-1)"><font color="#000000">Go back</font></a>
		</td>
	</tr>		
</table>				
				
