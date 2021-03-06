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

<f:loadBundle basename="org.riverock.commerce.resource.Currency" var="msg"/>

 <h:outputText value="#{msg.currency_info}"/>
 <h:panelGrid columns="2" border="0">
     <h:outputText value="#{msg.currency_id}"/>
     <h:outputText value="#{currencySessionBean.currencyExtendedBean.currencyBean.currencyId}"/>

     <h:outputText value="#{msg.currency_name}"/>
     <h:outputText value="#{currencySessionBean.currencyExtendedBean.currencyBean.currencyName}"/>

     <h:outputText value="#{msg.currency_code}"/>
     <h:outputText value="#{currencySessionBean.currencyExtendedBean.currencyBean.currencyCode}"/>

     <h:outputText value="#{msg.currency_real_curs}"/>
     <h:outputText value="#{currencySessionBean.currencyExtendedBean.realCurs}"/>

     <h:outputText value="#{msg.is_used}"/>
     <h:panelGroup id="select-currency-used-group">
         <t:graphicImage value="/images/yes.gif" rendered="#{currencySessionBean.currencyExtendedBean.currencyBean.used}" border="0"/>
         <t:graphicImage value="/images/no.gif" rendered="#{!currencySessionBean.currencyExtendedBean.currencyBean.used}" border="0"/>
     </h:panelGroup>

     <h:outputText value="#{msg.use_standard_currency}"/>
     <h:panelGroup id="select-standard-currency-group">
         <t:graphicImage value="/images/yes.gif" rendered="#{currencySessionBean.currencyExtendedBean.currencyBean.useStandard}" border="0"/>
         <t:graphicImage value="/images/no.gif" rendered="#{!currencySessionBean.currencyExtendedBean.currencyBean.useStandard}" border="0"/>
         <h:outputText value="#{currencySessionBean.currencyExtendedBean.standardCurrencyName}"/>
     </h:panelGroup>

 </h:panelGrid>

 	
