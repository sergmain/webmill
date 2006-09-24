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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<f:subview id="news-site-extend-desc-subview" rendered="#{newsSessionBean.objectType == newsSessionBean.siteType}">

    <h:panelGrid columns="2">

        <h:outputText value="#{msg.site_name}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.siteName}"/>

        <h:outputText value="#{msg.site_company}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.company.name}"/>

        <h:outputText value="#{msg.site_locale}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.siteDefaultLocale}"/>

        <h:outputText value="#{msg.site_admin_email}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.adminEmail}"/>

        <h:outputText value="#{msg.site_user_can_register}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.registerAllowed}"/>

        <h:outputText value="#{msg.site_css_is_dynamic}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.cssDynamic}"/>

        <h:outputText value="#{msg.site_css_file}"/>
        <h:outputText value="#{newsSessionBean.siteExtended.site.cssFile}"/>

    </h:panelGrid>

</f:subview>
