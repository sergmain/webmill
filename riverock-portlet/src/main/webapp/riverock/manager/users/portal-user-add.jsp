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

<style type="text/css">
    td {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .portal-user-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="add_portlet_name_form" rendered="#{isUserInRole['webmill.authentic']}">

                <f:subview id="portal-user-top-actions-subview">
                    <jsp:include page="_portal-user-top-action.jsp"/>
                </f:subview>

        <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser and isUserInRole['webmill.user-manager']}">

            <h:panelGroup>
                <h:outputText value="Portal user info"/>
                <h:panelGrid columns="2" border="0">
                    <h:outputText value="#{msg.portal_user_company_name}"/>
                    <h:selectOneMenu id="select-one-company" value="#{portalUserSessionBean.portalUser.companyId}"
                                     styleClass="selectOneMenu" required="true"
                        >
                        <f:selectItems value="#{portalUserService.companyList}"/>
                    </h:selectOneMenu>

                    <h:outputText value="#{msg.portal_user_first_name}"/>
                    <h:inputText id="portal-user-first-name-field"
                                 value="#{portalUserSessionBean.portalUser.firstName}"/>

                    <h:outputText value="#{msg.portal_user_middle_name}"/>
                    <h:inputText id="portal-user-middle-name-field"
                                 value="#{portalUserSessionBean.portalUser.middleName}"/>

                    <h:outputText value="#{msg.portal_user_last_name}"/>
                    <h:inputText id="portal-user-last-name-field" value="#{portalUserSessionBean.portalUser.lastName}"/>

                    <h:outputText value="#{msg.portal_user_address}"/>
                    <h:inputText id="portal-user-address-field" value="#{portalUserSessionBean.portalUser.address}"/>

                    <h:outputText value="#{msg.portal_user_phone}"/>
                    <h:inputText id="portal-user-phone-field" value="#{portalUserSessionBean.portalUser.phone}"/>

                    <h:outputText value="#{msg.portal_user_email}"/>
                    <h:inputText id="portal-user-email-field" value="#{portalUserSessionBean.portalUser.email}"/>
                </h:panelGrid>
            </h:panelGroup>

            <h:panelGroup>
                <h:commandButton value="#{msg.portal_user_add_process_action}"
                                 action="#{portalUserAction.processAddPortalUser}"
                                 styleClass="portal-user-button-action"/>
                <h:commandButton value="#{msg.portal_user_add_cancel_action}"
                                 action="#{portalUserAction.cancelAddPortalUser}"
                                 styleClass="portal-user-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
