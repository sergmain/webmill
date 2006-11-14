<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
  --%>
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

<h:panelGrid columns="8">

    <h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}">
            <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                             styleClass="top-button-action"/>
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
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">
            <h:commandButton id="menu-list-action" action="menu" value="#{manager.menu_button}" styleClass="top-button-action"/>
            <h:outputText value="#{manager.menu_info}" style="font-size:10px"/>
        </h:panelGrid>
    </h:panelGroup>
    <h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
            <h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                             styleClass="top-button-action"/>
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

<h:panelGrid columns="1">

    <h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager']}">
            <h:commandButton id="create-google-sitemap-action" action="#{portalActionExecutor.createGoogleSitemap}" value="Create Google sitemap" styleClass="top-button-action"/>
            <h:outputText value="Create Google sitemap" style="font-size:10px"/>
        </h:panelGrid>
    </h:panelGroup>

</h:panelGrid>

