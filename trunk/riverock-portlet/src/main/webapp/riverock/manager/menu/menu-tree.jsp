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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="menu-tree-group">

    <h:panelGroup id="menu-tree-site-change-group">
        <h:selectOneMenu id="select-one-site" value="#{menuSessionBean.currentSiteId}" styleClass="selectOneSite" required="true">
            <f:selectItems value="#{menuService.siteList}"/>
        </h:selectOneMenu>
        <h:commandButton id="menu-change-site-action" action="#{menuSiteAction.changeSite}"
                         value="Ok"
                         styleClass="menu-button-action"
            >
        </h:commandButton>
    </h:panelGroup>

    <t:tree2 id="serverTree" value="#{menuTree.menuTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false">
        <f:facet name="tree-root">
            <h:panelGroup id="menu-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="menu-catalog-list">
            <h:panelGroup id="menu-tree-menu-catalog-group">
                <t:graphicImage id="menu-tree-menu-catalog-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="menu-tree-menu-catalog-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="menu-tree-menu-catalog-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="menu-tree-menu-catalog-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-menu-catalog-action-id" action="#{menuCatalogAction.addMenuCatalogAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_menu_catalog_button_alt}">

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.menuCatalogType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup id="menu-tree-site-group">
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{menuSiteAction.selectSite}"
                    >

                    <t:graphicImage id="menu-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="menu-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="menu-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup id="menu-tree-site-language-group">
                <t:graphicImage id="menu-tree-sile-language-image" value="/images/user.png" border="0"/>
                <h:outputText id="menu-tree-site-language-name" value="#{node.description}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="menu-catalog">
            <h:panelGroup id="menu-tree-menu-catalog-group">
                <h:commandLink id="select-menu-catalog-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{menuCatalogAction.selectMenuCatalog}"
                    >

                    <t:graphicImage id="menu-tree-menu-catalog-image" value="/images/user.png" border="0"/>
                    <h:outputText id="menu-tree-menu-catalog-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.menuCatalogType}"/>
                </h:commandLink>
                <h:commandButton id="add-menu-item-action-id" action="#{menuAction.addMenuItemAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_menu_item_button_alt}">

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.menuItemType}"/>
                    <t:updateActionListener property="#{menuSessionBean.currentMenuCatalogId}" value="#{node.identifier}" />
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="menu-item">
            <h:panelGroup id="menu-tree-menu-item-group">
                <h:commandLink id="select-menu-item-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{menuAction.selectMenuItem}"
                    >

                    <t:graphicImage id="menu-tree-menu-item-image" value="/images/user.png" border="0"/>
                    <h:outputText id="menu-tree-menu-item-name" value="#{node.description}"/>
                    <h:outputText id="menu-tree-menu-item-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.menuItemType}"/>
                </h:commandLink>
                <h:commandButton id="add-menu-item-action-id" action="#{menuAction.addMenuItemAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_menu_item_button_alt}">

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.menuItemType}"/>
                    <t:updateActionListener property="#{menuSessionBean.currentMenuItemId}" value="#{node.identifier}" />
                </h:commandButton>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
