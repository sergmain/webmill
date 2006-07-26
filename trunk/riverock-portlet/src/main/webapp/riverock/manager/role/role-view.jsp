<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:view>
<h:form id="role_form">

	<f:subview id="subviewRoleList">
            <jsp:include page="role.jsp"/>
	</f:subview>

</h:form>
</f:view>
