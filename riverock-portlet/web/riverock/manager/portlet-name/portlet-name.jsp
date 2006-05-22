<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 150px;
        height: 22px;
    }

    .portlet-name-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:view>
<h:form id="portlet-name-form">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}" styleClass="top-button-action"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}" styleClass="top-button-action"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}" styleClass="top-button-action"/>
        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}" styleClass="top-button-action"/>
        <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}" styleClass="top-button-action"/>
        <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}" styleClass="top-button-action"/>

    <h:panelGrid columns="2">

	<f:subview id="subviewPortletNameList">
            <jsp:include page="portlet-name-list.jsp"/>
	</f:subview>

 
    <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName}">

	<f:subview id="subviewPortletNameInfo">
            <jsp:include page="portlet-name-info.jsp"/>
	</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.edit_portlet_name_action}" action="portlet-name-edit" styleClass="portlet-name-button-action"/>
		<h:commandButton value="#{msg.delete_portlet_name_action}" action="portlet-name-delete" styleClass="portlet-name-button-action"/>
	</h:panelGroup>
 
   </h:panelGrid>	 
	
</h:panelGrid>


</h:form>
</f:view>
