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
    <h:form id="foo" rendered="#{holdingSessionBean.add and isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager']}">

            <f:subview id="select-user-subview">
                <jsp:include page="holding-list.jsp"/>
            </f:subview>


            <h:panelGroup id="add-user-panel" rendered="#{!empty holdingDataProvider.currentHolding and holdingSessionBean.add}">


                <t:messages id="messages" styleClass="error" summaryFormat="{0}"/>

                <h:panelGrid columns="2">

                    <h:outputText value="#{msg.holding_short_name}"/>
                    <h:inputText id="input_holding_short_name" value="#{holdingDataProvider.currentHolding.shortName}" maxlength="20" size="20"
                                 required="false">
                    </h:inputText>

                    <h:outputText value="#{msg.holding_name}"/>
                    <h:inputText id="input_holding_name" value="#{holdingDataProvider.currentHolding.name}" maxlength="20" size="20"
                                 required="false">
                    </h:inputText>

                </h:panelGrid>


                <h:panelGrid columns="1">

                    <f:subview id="add-company-list-subview">
                        <jsp:include page="holding-company-list.jsp"/>
                    </f:subview>


                    <h:panelGroup id="select-action-group">
                        <h:commandButton id="process-add-holding-action" action="#{holdingAction.processAddHoldingAction}"
                                         value="#{msg.process_add_holding_action}" styleClass="holding-button-action"
                            >
                        </h:commandButton>
                        <h:commandButton id="cancel-add-holding-action" action="#{holdingAction.cancelAddHoldingAction}"
                                         value="#{msg.cancel_add_holding_action}" immediate="true" styleClass="holding-button-action"
                            >
                        </h:commandButton>
                    </h:panelGroup>

                </h:panelGrid>

            </h:panelGroup>


        </h:panelGrid>


    </h:form>
</f:view>




