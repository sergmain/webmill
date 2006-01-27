<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<f:view>
<h:form id="delete-role-form">
<f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>	
 <h:panelGroup id="panelGroup2">
 <h:panelGrid columns="1" rendered="#{!empty roleSessionBean.role}">

<f:subview id="subviewRoleInfo">
            <jsp:include page="role-info.jsp"/>
</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.action_role_delete_confirm}" action="#{roleAction.processDeleteRole}"/>
		<h:commandButton value="#{msg.action_role_delete_cancel}" action="role"/>	
	</h:panelGroup>
 	 
 </h:panelGrid>	 
 	
</h:panelGroup>
 	
	 
	
</h:form>
</f:view>
