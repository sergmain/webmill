<%--
  ~ org.riverock.portlet - Portlet Library
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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

    <h:panelGrid columns="2">
	<h:outputText value="#{msg.holding_short_name}"/>
	<h:outputText value="#{holdingDataProvider.currentHolding.shortName}" />

	<h:outputText value="#{msg.holding_name}"/>
	<h:outputText value="#{holdingDataProvider.currentHolding.name}" />

    </h:panelGrid>

    <h:panelGrid columns="1">
        <h:outputText value="#{msg.company_list}" styleClass="standard_bold" />
	<t:dataList id="company-list"         
        	styleClass="standardList"
        	var="company"
        	value="#{holdingDataProvider.currentHolding.companies}"
        	layout="orderedList" forceId="true">
        	<h:outputText value="#{company.name}" />
    	</t:dataList>
    </h:panelGrid>

