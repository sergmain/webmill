<%--
  ~ org.riverock.commerce - Commerce application
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

<f:loadBundle basename="org.riverock.commerce.resource.StandardCurrency" var="msg"/>
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
    <h:form id="standard_currency_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="currency-list-action" action="currency" value="#{manager.currency_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="shop-list-action" action="shop" value="#{manager.shop_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:commandButton id="currency-precision-action" action="currency-precision" value="#{manager.currency_precision_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}"/>

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager']}">

            <f:subview id="subviewStandardCurrencyList">
                <jsp:include page="standard-currency-list.jsp"/>
            </f:subview>

            <h:panelGrid columns="1" rendered="#{!empty standardCurrencySessionBean.standardCurrency.standardCurrencyName}">

                <f:subview id="subviewStandardCurrencyInfo">
                    <jsp:include page="standard-currency-info.jsp"/>
                </f:subview>

                <h:panelGroup id="editDeleteControls">
                    <h:commandButton value="#{msg.edit_standard_currency_action}" action="standard-currency-edit" styleClass="sub-top-button-action"/>
                    <h:commandButton value="#{msg.delete_standard_currency_action}" action="standard-currency-delete" styleClass="sub-top-button-action"/>
                    <h:commandButton value="#{msg.add_curs_action}" action="#{standardCurrencyAction.addCurs}" styleClass="sub-top-button-action"/>
                </h:panelGroup>

                <f:subview id="subviewStandardCurrencyCursInfo">
                    <jsp:include page="standard-currency-curs-info.jsp"/>
                </f:subview>

            </h:panelGrid>

        </h:panelGrid>


    </h:form>
</f:view>
