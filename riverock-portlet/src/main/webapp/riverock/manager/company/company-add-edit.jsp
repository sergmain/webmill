<%--
  ~ org.riverock.portlet - Portlet Library
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

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>	

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
 	
