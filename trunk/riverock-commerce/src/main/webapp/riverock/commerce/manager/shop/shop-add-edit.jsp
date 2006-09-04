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
 	
