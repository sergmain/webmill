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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.webmill.init.resource.Site" var="msg"/>

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

    <h:outputText value="#{msg.site_locale}"/>
    <h:inputText id="site-locale-field" value="#{siteSessionBean.siteExtended.site.siteDefaultLocale}"/>

    <h:outputText value="#{msg.site_admin_email}"/>
    <h:inputText id="site-admin-email-field" value="#{siteSessionBean.siteExtended.site.adminEmail}"/>

    <h:outputText value="#{msg.site_user_can_register}"/>
    <h:selectBooleanCheckbox id="site-register-allowed-field" value="#{siteSessionBean.siteExtended.site.registerAllowed}"/>

    <h:outputText value="#{msg.site_css_is_dynamic}"/>
    <h:selectBooleanCheckbox id="site-css-dynamic-field" value="#{siteSessionBean.siteExtended.site.cssDynamic}"/>

    <h:outputText value="#{msg.site_css_file}"/>
    <h:inputText id="site-css-file-field" value="#{siteSessionBean.siteExtended.site.cssFile}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.site_properties}"/>
    <h:inputTextarea id="site-properties-field" value="#{siteSessionBean.siteExtended.site.properties}"
                     rows="10" cols="50"
        />
</h:panelGrid>

<f:subview id="site-virtual-host-edit-subview">
    <jsp:include page="site-virtual-host-edit-list.jsp"/>
</f:subview>

