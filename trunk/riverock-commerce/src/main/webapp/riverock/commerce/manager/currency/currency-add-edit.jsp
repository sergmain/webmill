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
 	
