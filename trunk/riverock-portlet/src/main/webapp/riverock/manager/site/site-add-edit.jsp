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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

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

