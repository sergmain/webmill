<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>

<style type="text/css">
td { vertical-align: top; }
</style>

<f:view>
<h:form id="delete-portal-name-form">

    <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

	<f:subview id="subviewPortalUserInfo">
            <jsp:include page="portal-user-info.jsp"/>
	</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.portal_user_delete_confirm_action}" action="#{portalUserAction.processDeletePortalUser}"/>
		<h:commandButton value="#{msg.portal_user_delete_cancel_action}" action="portal-user"/>
	</h:panelGroup>
 	 
    </h:panelGrid>	 
	
</h:form>
</f:view>
