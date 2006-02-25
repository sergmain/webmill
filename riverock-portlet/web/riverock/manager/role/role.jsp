<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Role" var="msg"/>	
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
td { vertical-align: top; }
</style>

<f:view>
<h:form id="role-form">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"/>

    <h:panelGrid columns="2">

	<f:subview id="subviewRoleList">
            <jsp:include page="role-list.jsp"/>
	</f:subview>

 
    <h:panelGrid columns="1" rendered="#{!empty roleSessionBean.role}">

	<f:subview id="subviewRoleInfo">
            <jsp:include page="role-info.jsp"/>
	</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.role_action_role_edit_role}" action="role-edit"/>
		<h:commandButton value="#{msg.role_action_role_delete_role}" action="role-delete"/>	
	</h:panelGroup>
 
   </h:panelGrid>	 
	
</h:panelGrid>


</h:form>
</f:view>
