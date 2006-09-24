<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.riverock.org
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community,
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

<f:loadBundle basename="org.riverock.webmill.admin.resource.Company" var="msg"/>

<h:outputText value="Company info"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg['company_name']}"/>
    <h:inputText id="company-name-field" value="#{companySessionBean.company.name}"/>

    <h:outputText value="#{msg.company_short_name}"/>
    <h:inputText id="company-short-name-field" value="#{companySessionBean.company.shortName}"/>

    <h:outputText value="#{msg.company_address}"/>
    <h:inputText id="company-address-field" value="#{companySessionBean.company.address}"/>

    <h:outputText value="#{msg.company_ceo}"/>
    <h:inputText id="company-ceo-field" value="#{companySessionBean.company.ceo}"/>

    <h:outputText value="#{msg.company_cfo}"/>
    <h:inputText id="company-cfo-field" value="#{companySessionBean.company.cfo}"/>

    <h:outputText value="#{msg.company_website}"/>
    <h:inputText id="company-website-field" value="#{companySessionBean.company.website}"/>
</h:panelGrid>
 	
