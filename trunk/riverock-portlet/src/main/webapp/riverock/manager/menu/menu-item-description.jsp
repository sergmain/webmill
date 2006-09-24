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


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:panelGroup rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">
    <h:panelGrid columns="2">

        <h:outputText value="#{msg.menu_item_portlet_name}"/>
        <h:outputText value="#{menuSessionBean.menuItem.portletName.portletName}"/>

        <h:outputText value="#{msg.menu_item_template_name}"/>
        <h:outputText value="#{menuSessionBean.menuItem.template.templateName}"/>

        <h:outputText value="#{msg.menu_item_order_field}"/>
        <h:outputText value="#{menuSessionBean.menuItem.menuItem.orderField}"/>

        <h:outputText value="#{msg.menu_item_name}"/>
        <h:outputText value="#{menuSessionBean.menuItem.menuItem.keyMessage}"/>

        <h:outputText value="#{msg.menu_item_url}"/>
        <h:outputText value="#{menuSessionBean.menuItem.menuItem.url}"/>

    </h:panelGrid>

    <h:panelGrid columns="1">
        <h:outputText value="#{msg.menu_item_role}"/>
        <h:outputText value="#{menuSessionBean.menuItem.menuItem.portletRole}"/>
    </h:panelGrid>
</h:panelGroup>

