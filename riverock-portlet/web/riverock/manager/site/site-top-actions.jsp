<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}"/>
<h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.user-manager']}"/>
