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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.webmill.admin.resource.Site" var="msg"/>

<h:outputText value="#{msg.site_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.site_name}"/>
    <h:inputText id="site-name-field" value="#{siteSessionBean.siteExtended.site.siteName}"/>

    <h:outputText value="#{msg.site_company}"/>
    <h:panelGroup id="select-company-group">
        <h:selectOneMenu id="select-one-company" value="#{siteSessionBean.siteExtended.site.companyId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{siteService.companyList}"/>
        </h:selectOneMenu>
    </h:panelGroup>
    <h:outputText value="#{msg.site_timezone}"/>
    <h:panelGroup id="select-timezones-group">
        <h:selectOneMenu id="select-one-timezone" value="#{siteSessionBean.siteExtended.site.serverTimeZone}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{siteService.timeZones}"/>
        </h:selectOneMenu>
    </h:panelGroup>

    <h:outputText value="#{msg.site_locale}"/>
    <h:inputText id="site-locale-field" value="#{siteSessionBean.siteExtended.locales}"/>

</h:panelGrid>

<f:subview id="site-virtual-host-edit-subview">
    <jsp:include page="site-virtual-host-edit-list.jsp"/>
</f:subview>

