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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.commerce.resource.CurrencyPrecision" var="msg"/>

 <h:outputText value="#{msg.shop_info}"/>
 <h:panelGrid columns="2" border="0">
     
     <h:outputText value="#{msg.shop_id}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.shop.shopId}"/>

     <h:outputText value="#{msg.shop_name}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.shop.shopName}"/>

     <h:outputText value="#{msg.shop_code}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.shop.shopCode}"/>

     <h:outputText value="#{msg.is_opened}"/>
     <h:panelGroup>
         <t:graphicImage value="/images/yes.gif" rendered="#{currencyPrecisionSessionBean.shopExtendedBean.shop.closed}" border="0"/>
         <t:graphicImage value="/images/no.gif" rendered="#{!currencyPrecisionSessionBean.shopExtendedBean.shop.closed}" border="0"/>
     </h:panelGroup>

     <h:outputText value="#{msg.shop_name_for_price_list}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.shop.shopNameForPriceList}"/>

     <h:outputText value="#{msg.default_currency}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.defaultCurrencyName}"/>

     <h:outputText value="#{msg.invoice_currency}"/>
     <h:outputText value="#{currencyPrecisionSessionBean.shopExtendedBean.invoiceCurrencyName}"/>

 </h:panelGrid>


