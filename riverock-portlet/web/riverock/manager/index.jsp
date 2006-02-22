<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
<h:form id="manager">

   <h:panelGrid columns="2">
        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"/>
	<h:outputText value="#{manager.auth_info}"/>

        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"/>
	<h:outputText value="#{manager.role_info}"/>


        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>
	<h:outputText value="#{manager.company_info}"/>

        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"/>
	<h:outputText value="#{manager.holding_info}"/>
   </h:panelGrid>

</h:form>
</f:view>

