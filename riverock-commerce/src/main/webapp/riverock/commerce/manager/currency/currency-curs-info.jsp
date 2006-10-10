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

<f:loadBundle basename="org.riverock.commerce.resource.Currency" var="msg"/>

<t:dataTable id="currencyCursDataTable"
        var="currencyCurs"
        value="#{currencySessionBean.currencyExtendedBean.currencyBean.curses}"
        rows="15"
    >

   <h:column>
       <f:facet name="header">
          <h:outputText value="#{msg.table_curs_created}" />
       </f:facet>
       <h:outputText value="#{currencyCurs.created}">
           <f:convertDateTime type="both" dateStyle="short" timeStyle="short"/>
       </h:outputText>
   </h:column>
   <h:column>
       <f:facet name="header">
          <h:outputText value="#{msg.table_curs_value}" />
       </f:facet>
       <h:outputText value="#{currencyCurs.curs}">
           <f:convertNumber type="number" minIntegerDigits="1" minFractionDigits="2" maxFractionDigits="5"/>
       </h:outputText>
   </h:column>

</t:dataTable>

