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

<f:loadBundle basename="org.riverock.commerce.resource.Currency" var="msg"/>
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
    <h:form id="currency_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="standard-currency-list-action" action="standard-currency" value="#{manager.standard_currency_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="shop-list-action" action="shop" value="#{manager.shop_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="currency-precision-action" action="currency-precision" value="#{manager.currency_precision_button}"
                         styleClass="top-button-action"/>

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager']}">

            <f:subview id="subviewCurrencyList">
                <jsp:include page="currency-list.jsp"/>
            </f:subview>

            <h:panelGrid columns="1" rendered="#{!empty currencySessionBean.currencyExtendedBean}">

                <f:subview id="subviewCurrencyInfo">
                    <jsp:include page="currency-info.jsp"/>
                </f:subview>

                <h:panelGroup id="editDeleteControls">
                    <h:commandButton value="#{msg.edit_currency_action}" action="#{currencyAction.editCurrency}" styleClass="sub-top-button-action"/>
                    <h:commandButton value="#{msg.delete_currency_action}" action="currency-delete" styleClass="sub-top-button-action"/>
                    <h:commandButton value="#{msg.add_curs_action}" action="#{currencyAction.addCurs}" styleClass="sub-top-button-action"
                                     rendered="#{!currencySessionBean.currencyExtendedBean.currencyBean.useStandard}"
                        />
                </h:panelGroup>

                <f:subview id="subviewCurrencyCursInfo" rendered="#{!currencySessionBean.currencyExtendedBean.currencyBean.useStandard}">
                    <jsp:include page="currency-curs-info.jsp"/>
                </f:subview>

            </h:panelGrid>

        </h:panelGrid>


    </h:form>
</f:view>
