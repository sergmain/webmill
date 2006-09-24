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
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .holding-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="foo" rendered="#{holdingSessionBean.edit and isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager']}">

            <f:subview id="select-user-subview">
                <jsp:include page="holding-list.jsp"/>
            </f:subview>


            <h:panelGroup id="edit-holding-panel" rendered="#{holdingSessionBean.edit}">
                <h:panelGrid columns="2">

                    <h:outputText value="#{msg.holding_short_name}"/>
                    <h:inputText id="input_holding_short_name" value="#{holdingDataProvider.currentHolding.shortName}"
                                 maxlength="20" size="20"
                                 required="false">
                    </h:inputText>

                    <h:outputText value="#{msg.holding_name}"/>
                    <h:inputText id="input_holding_name" value="#{holdingDataProvider.currentHolding.name}"
                                 maxlength="20" size="20"
                                 required="false">
                    </h:inputText>

                </h:panelGrid>


                <h:panelGrid columns="1">
                    <f:subview id="edit-company-list-subview">
                        <jsp:include page="holding-company-list.jsp"/>
                    </f:subview>


                    <h:panelGroup id="edit-delete-actions">
                        <h:commandButton id="save-holding-action" action="#{holdingAction.saveHoldingAction}"
                                         value="#{msg['save_holding_action']}" styleClass="holding-button-action"
                            >
                        </h:commandButton>
                        <h:commandButton id="cancel-edit-holding-action"
                                         action="#{holdingAction.cancelEditHoldingAction}"
                                         value="#{msg['cancel_edit_holding_action']}" styleClass="holding-button-action"
                            >
                        </h:commandButton>
                    </h:panelGroup>


                </h:panelGrid>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>





