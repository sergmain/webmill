<%--
  ~ org.riverock.webmill.init - Webmill portal initializer web application
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~ Riverock - The Open-source Java Development Community,
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

    <f:loadBundle basename="org.riverock.webmill.init.resource.PortalUser" var="msg"/>

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
