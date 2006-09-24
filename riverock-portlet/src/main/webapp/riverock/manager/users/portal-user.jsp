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

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

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

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="portal_user_form" rendered="#{isUserInRole['webmill.authentic']}">

                <f:subview id="portal-user-top-actions-subview">
                    <jsp:include page="_portal-user-top-action.jsp"/>
                </f:subview>

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.user-manager']}">

                <f:subview id="subviewPortalUserList">
                    <jsp:include page="portal-user-list.jsp"/>
                </f:subview>

            <h:panelGroup>
                <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

                    <f:subview id="subviewPortalUserInfo">
                        <jsp:include page="portal-user-info.jsp"/>
                    </f:subview>

                    <h:panelGroup id="editDeleteControls">
                        <h:commandButton value="#{msg.portal_user_action_edit_portal_user_action}"
                                         action="portal-user-edit" styleClass="portal-user-button-action"/>
                        <h:commandButton value="#{msg.portal_user_action_delete_portal_user_action}"
                                         action="portal-user-delete" styleClass="portal-user-button-action"/>
                    </h:panelGroup>

                </h:panelGrid>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>
