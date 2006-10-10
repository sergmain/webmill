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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.webmill.admin.resource.PortalUser" var="msg"/>

<h:panelGroup>
 <h:outputText value="#{msg.portal_user_info}"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.portal_user_id}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.userId}"/>

 		<h:outputText value="#{msg.portal_user_company_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.companyName}"/>

 		<h:outputText value="#{msg.portal_user_created_date}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.createdDate}"/>

 		<h:outputText value="#{msg.portal_user_first_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.firstName}"/>

 		<h:outputText value="#{msg.portal_user_middle_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.middleName}"/>

 		<h:outputText value="#{msg.portal_user_last_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.lastName}"/>

 		<h:outputText value="#{msg.portal_user_address}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.address}"/>

 		<h:outputText value="#{msg.portal_user_phone}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.phone}"/>

 		<h:outputText value="#{msg.portal_user_email}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.email}"/>
 </h:panelGrid>
 	
</h:panelGroup>