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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.commerce.resource.Shop" var="msg"/>

 <h:outputText value="Shop info"/>
 <h:panelGrid columns="2">
     <h:outputText value="#{msg.shop_name}"/>
     <h:inputText id="shop-name-field" value="#{shopSessionBean.shopBean.shopName}" size="30"/>

     <h:outputText value="#{msg.shop_code}"/>
     <h:inputText id="shop-code-field" value="#{shopSessionBean.shopBean.shopCode}" size="30"/>

     <h:outputText value="#{msg.is_opened}"/>
     <h:selectBooleanCheckbox id="select_is_used_checkbox" value="#{shopSessionBean.shopBean.opened}"/>

     <h:outputText value="#{msg.digit_after_comma}"/>
     <h:inputText id="digit-after-comma-field" value="#{shopSessionBean.shopBean.digitsAfterComma}" size="10"/>

     <h:outputText value="#{msg.shop_discount}"/>
     <h:inputText id="shop-discount-field" value="#{shopSessionBean.shopBean.discount}" size="10"/>

     <h:outputText value="#{msg.is_need_recalc}"/>
     <h:selectBooleanCheckbox id="select_is_need_recalc" value="#{shopSessionBean.shopBean.needRecalc}"/>

     <h:outputText value="#{msg.default_currency}"/>
     <h:selectOneMenu id="select-one-default-currency" value="#{shopSessionBean.shopBean.defaultCurrencyId}"
                      styleClass="selectOneMenu" required="true"
         >
         <f:selectItems value="#{shopService.currencyList}"/>
     </h:selectOneMenu>

     <h:outputText value="#{msg.invoice_currency}"/>
     <h:selectOneMenu id="select-one-invoice-currency" value="#{shopSessionBean.shopBean.invoiceCurrencyId}"
                      styleClass="selectOneMenu" required="true"
         >
         <f:selectItems value="#{shopService.currencyList}"/>
     </h:selectOneMenu>


 </h:panelGrid>
 	
