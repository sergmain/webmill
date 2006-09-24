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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.commerce.resource.Shop" var="msg"/>

 <h:outputText value="#{msg.shop_info}"/>
 <h:panelGrid columns="2" border="0">
     <h:outputText value="#{msg.shop_id}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.shopBean.shopId}"/>

     <h:outputText value="#{msg.shop_name}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.shopBean.shopName}"/>

     <h:outputText value="#{msg.shop_code}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.shopBean.shopCode}"/>

     <h:outputText value="#{msg.is_opened}"/>
     <h:panelGroup>
         <t:graphicImage value="/images/yes.gif" rendered="#{shopSessionBean.shopExtendedBean.shopBean.opened}" border="0"/>
         <t:graphicImage value="/images/no.gif" rendered="#{!shopSessionBean.shopExtendedBean.shopBean.opened}" border="0"/>
     </h:panelGroup>

     <h:outputText value="#{msg.shop_name_for_price_list}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.shopBean.shopNameForPriceList}"/>

     <h:outputText value="#{msg.default_currency}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.defaultCurrencyName}"/>

     <h:outputText value="#{msg.invoice_currency}"/>
     <h:outputText value="#{shopSessionBean.shopExtendedBean.invoiceCurrencyName}"/>

 </h:panelGrid>

 	
