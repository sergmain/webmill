<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
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

<f:loadBundle basename="org.riverock.webmill.admin.resource.Company" var="msg"/>

 <h:outputText value="Company info"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.company_id}"/>
 		<h:outputText value="#{companySessionBean.company.id}"/>

 		<h:outputText value="#{msg.company_name}"/>
 		<h:outputText value="#{companySessionBean.company.name}"/>

 		<h:outputText value="#{msg.company_short_name}"/>
 		<h:outputText value="#{companySessionBean.company.shortName}"/>

 		<h:outputText value="#{msg.company_address}"/>
 		<h:outputText value="#{companySessionBean.company.address}"/>

 		<h:outputText value="#{msg.company_ceo}"/>
 		<h:outputText value="#{companySessionBean.company.ceo}"/>

 		<h:outputText value="#{msg.company_cfo}"/>
 		<h:outputText value="#{companySessionBean.company.cfo}"/>

 		<h:outputText value="#{msg.company_website}"/>
 		<h:outputText value="#{companySessionBean.company.website}"/>
 </h:panelGrid>
 	
