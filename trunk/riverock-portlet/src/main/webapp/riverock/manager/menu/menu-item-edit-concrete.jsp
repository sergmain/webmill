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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:outputText value="#{msg.menu_item_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.menu_item_portlet_name}"/>
    <h:outputText value="#{menuSessionBean.menuItem.portletName.portletName}"/>

    <h:outputText value="#{msg.menu_item_template_name}"/>
    <h:panelGroup id="select-template-group">
        <h:selectOneMenu id="select-one-template" value="#{menuSessionBean.menuItem.menuItem.templateId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{menuDataProvider.templateList}"/>
        </h:selectOneMenu>
    </h:panelGroup>

    <h:outputText value="#{msg.menu_item_context_name}" rendered="#{! empty menuDataProvider.contextList}"/>
    <h:panelGroup id="select-context-group" rendered="#{! empty menuDataProvider.contextList}">
        <h:selectOneMenu id="select-one-context" value="#{menuSessionBean.menuItem.menuItem.contextId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{menuDataProvider.contextList}"/>
        </h:selectOneMenu>
    </h:panelGroup>

    <h:outputText value="#{msg.menu_item_order_field}"/>
    <h:inputText id="menu-item-order-field" value="#{menuSessionBean.menuItem.menuItem.orderField}"/>

    <h:outputText value="#{msg.menu_item_name}"/>
    <h:inputText id="menu-name-field" value="#{menuSessionBean.menuItem.menuItem.keyMessage}"/>

    <h:outputText value="#{msg.menu_item_url}"/>
    <h:inputText id="menu-url-field" value="#{menuSessionBean.menuItem.menuItem.url}"/>

    <h:outputText value="#{msg.menu_item_title}"/>
    <h:inputText id="menu-title-field" value="#{menuSessionBean.menuItem.menuItem.title}"/>

    <h:outputText value="#{msg.menu_item_author}"/>
    <h:inputText id="menu-author-field" value="#{menuSessionBean.menuItem.menuItem.author}"/>

    <h:outputText value="#{msg.menu_item_keyword}"/>
    <h:inputText id="menu-keyword-field" value="#{menuSessionBean.menuItem.menuItem.keyword}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.menu_item_role}"/>
    <h:inputText id="menu-role-field" value="#{menuSessionBean.menuItem.menuItem.portletRole}"
        size="70"/>

    <h:outputText value="#{msg.menu_item_metadata}"/>
    <h:inputTextarea id="menu-metadata-field" value="#{menuSessionBean.menuItem.menuItem.metadata}"
                     rows="10" cols="70"
        />
</h:panelGrid>
