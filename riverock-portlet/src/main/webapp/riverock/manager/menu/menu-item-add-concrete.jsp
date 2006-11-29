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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:outputText value="#{msg.menu_item_info}"/>
<h:panelGrid columns="1">
    <h:outputText value="#{msg.menu_item_portlet_name}"/>
    <h:panelGroup id="select-portlet-name-group">
        <h:selectOneMenu id="select-one-portlet-name" value="#{menuSessionBean.menuItem.menuItem.portletId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{menuService.portletList}"/>
        </h:selectOneMenu>
    </h:panelGroup>
</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.menu_item_template_name}"/>
    <h:panelGroup id="select-template-group">
        <h:selectOneMenu id="select-one-template" value="#{menuSessionBean.menuItem.menuItem.templateId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{menuDataProvider.templateList}"/>
        </h:selectOneMenu>
    </h:panelGroup>
</h:panelGrid>

<h:panelGrid columns="2">

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



