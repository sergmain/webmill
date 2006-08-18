<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}"/>
<h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="menu-list-action" action="menu" value="#{manager.menu_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}"/>
<h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
<h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.template,webmill.css,webmill.xslt']}"/>

