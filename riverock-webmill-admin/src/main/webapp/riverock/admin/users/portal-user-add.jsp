<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~ Riverock - The Open-source Java Development Community,
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

<style type="text/css">
    td {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24px;
    }

    .portal-user-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.webmill.admin.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<f:view>

    <h:form id="add-portlet-name-form">

                <f:subview id="portal-user-top-actions-subview">
                    <jsp:include page="_portal-user-top-action.jsp"/>
                </f:subview>

        <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

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
