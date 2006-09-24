<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>
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

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="company_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}"/>
        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="menu-list-action" action="menu" value="#{manager.menu_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}"/>
        <h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.user-manager']}"/>
        <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                         styleClass="top-button-action"
 rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.template,webmill.css,webmill.xslt']}"/>

        <h:panelGrid columns="1" rendered="#{not empty action.authSessionBean.authSession and isUserInRole['webmill.portal-manager']}">

            <h:commandButton value="#{msg.action_add_company}"
                             action="#{action.addCompany}"
                             styleClass="company-button-action"
                />

            <h:panelGrid columns="2">

                <f:subview id="subviewCompanyList">
                    <jsp:include page="company-list.jsp"/>
                </f:subview>

                <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

                    <f:subview id="subviewCompanyInfo">
                        <jsp:include page="company-info.jsp"/>
                    </f:subview>

                    <h:panelGroup id="editDeleteControls">
                        <h:commandButton value="#{msg.edit_company}" action="company-edit"
                                         styleClass="company-button-action"/>
                        <h:commandButton value="#{msg.delete_company}" action="company-delete"
                                         styleClass="company-button-action"/>
                    </h:panelGroup>
                </h:panelGrid>

            </h:panelGrid>
        </h:panelGrid>

    </h:form>
</f:view>
