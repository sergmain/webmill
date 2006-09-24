<%--
  ~ org.riverock.commerce - Commerce application
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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<f:view>
<h:form id="foo" rendered="#{isUserInRole['webmill.authentic']}">

    <f:loadBundle basename="org.riverock.commerce.resource.Manager" var="manager"/>

    <style type="text/css">
        TD {
            vertical-align: top;
        }

        .top-button-action {
            width: 120px;
            height: 20px;
        }

        .sub-top-button-action {
            width: 150px;
            height: 22px;
        }
    </style>

    <h:panelGrid columns="4">

        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="statdard-currency-action" action="standard-currency" value="#{manager.standard_currency_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.standard_currency_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>
        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="currency-action" action="currency" value="#{manager.currency_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.currency_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>
        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="shop-action" action="shop" value="#{manager.shop_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.shop_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>
        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="currency-precision-action" action="currency-precision" value="#{manager.currency_precision_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.currency_precision_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>

    </h:panelGrid>


</h:form>
<h:outputText value="not logged" style="font-size:10px" rendered="#{!isUserInRole['webmill.authentic']}"/>
</f:view>