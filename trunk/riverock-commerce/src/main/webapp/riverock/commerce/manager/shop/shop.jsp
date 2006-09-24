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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.commerce.resource.Shop" var="msg"/>
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

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="shop_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="standard-currency-list-action" action="standard-currency" value="#{manager.standard_currency_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="currency-list-action" action="currency" value="#{manager.currency_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="currency-precision-action" action="currency-precision" value="#{manager.currency_precision_button}"
                         styleClass="top-button-action"/>

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager']}">

            <f:subview id="subviewShopList">
                <jsp:include page="shop-list.jsp"/>
            </f:subview>

            <h:panelGrid columns="1" rendered="#{!empty shopSessionBean.shopExtendedBean}">

                <f:subview id="subviewShopInfo">
                    <jsp:include page="shop-info.jsp"/>
                </f:subview>

                <h:panelGroup id="editDeleteControls">
                    <h:commandButton value="#{msg.edit_shop_action}" action="#{shopAction.editShop}" styleClass="sub-top-button-action"/>
                    <h:commandButton value="#{msg.delete_shop_action}" action="shop-delete" styleClass="sub-top-button-action"/>
                </h:panelGroup>

            </h:panelGrid>

        </h:panelGrid>


    </h:form>
</f:view>
