<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .company-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<h:panelGrid columns="7">

<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}">
	    <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                     styleClass="top-button-action" />
	    <h:outputText value="#{manager.auth_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
	    <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                     styleClass="top-button-action"/>
	    <h:outputText value="#{manager.role_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
	    <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                     styleClass="top-button-action"/>
	    <h:outputText value="#{manager.company_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
	    <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                     styleClass="top-button-action"/>
	    <h:outputText value="#{manager.holding_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
	    <h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                     styleClass="top-button-action" />
	    <h:outputText value="#{manager.portlet_name_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.user-manager']}">
	    <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                     styleClass="top-button-action"/>
	    <h:outputText value="#{manager.portal_user_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>
<h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.template,webmill.css,webmill.xslt']}">
	    <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}" styleClass="top-button-action"/>
	    <h:outputText value="#{manager.site_info}" style="font-size:10px"/>
        </h:panelGrid>
</h:panelGroup>


</h:panelGrid>

