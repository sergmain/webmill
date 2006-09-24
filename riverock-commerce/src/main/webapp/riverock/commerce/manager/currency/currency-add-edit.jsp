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

    <f:loadBundle basename="org.riverock.commerce.resource.Currency" var="msg"/>

 <h:outputText value="Currency info"/>
 <h:panelGrid columns="2">
     <h:outputText value="#{msg.currency_name}"/>
     <h:inputText id="currency-name-field" value="#{currencySessionBean.currencyBean.currencyName}" size="30"/>

     <h:outputText value="#{msg.currency_code}"/>
     <h:inputText id="currency-code-field" value="#{currencySessionBean.currencyBean.currencyCode}" size="30"/>

     <h:outputText value="#{msg.is_used}"/>
     <h:selectBooleanCheckbox id="select_is_used_checkbox" value="#{currencySessionBean.currencyBean.used}"/>

     <h:outputText value="#{msg.use_standard_currency}"/>
     <h:panelGroup id="select-standard-currency-group">
         <h:selectBooleanCheckbox id="select_standard_currency_checkbox"
                                  value="#{currencySessionBean.currencyBean.useStandard}"/>

         <h:selectOneMenu id="select-one-standard-currency" value="#{currencySessionBean.currencyBean.standardCurrencyId}"
                          styleClass="selectOneMenu" required="true"
             >
             <f:selectItems value="#{currencyService.standardCurrencyList}"/>
         </h:selectOneMenu>
     </h:panelGroup>


 </h:panelGrid>
 	
