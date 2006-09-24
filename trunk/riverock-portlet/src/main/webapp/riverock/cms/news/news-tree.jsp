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

<h:panelGroup id="news-tree-group">

    <h:panelGroup id="news-tree-site-change-group">
        <h:selectOneMenu id="select-one-site" value="#{newsSessionBean.currentSiteId}" styleClass="selectOneSite" required="true">
            <f:selectItems value="#{newsService.siteList}"/>
        </h:selectOneMenu>
        <h:commandButton id="news-change-site-action" action="#{newsSiteAction.changeSite}"
                         value="Ok"
                         styleClass="news-button-action"
            >
        </h:commandButton>
    </h:panelGroup>

    <t:tree2 id="serverTree" value="#{newsTree.newsTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false">
        <f:facet name="tree-root">
            <h:panelGroup id="news-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="news-group-list">
            <h:panelGroup id="news-tree-news-group-group">
                <t:graphicImage id="news-tree-news-group-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="news-tree-news-group-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="news-tree-news-group-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="news-tree-news-group-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-news-group-action-id" action="#{newsGroupAction.addNewsGroupAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_news_group_button_alt}">

                    <t:updateActionListener property="#{newsSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{newsSessionBean.objectType}" value="#{newsSessionBean.newsGroupType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup id="news-tree-site-group">
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{newsSiteAction.selectSite}"
                    >

                    <t:graphicImage id="news-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="news-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="news-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{newsSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{newsSessionBean.objectType}" value="#{newsSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup id="news-tree-site-language-group">
                <t:graphicImage id="news-tree-sile-language-image" value="/images/user.png" border="0"/>
                <h:outputText id="news-tree-site-language-name" value="#{node.description}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="news-group">
            <h:panelGroup id="news-tree-news-group-group">
                <h:commandLink id="select-news-group-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{newsGroupAction.selectNewsGroup}"
                    >

                    <t:graphicImage id="news-tree-news-group-image" value="/images/user.png" border="0"/>
                    <h:outputText id="news-tree-news-group-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{newsSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{newsSessionBean.objectType}" value="#{newsSessionBean.newsGroupType}"/>
                </h:commandLink>
                <h:commandButton id="add-news-action-id" action="#{newsAction.addNewsAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_news_button_alt}">

                    <t:updateActionListener property="#{newsSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{newsSessionBean.objectType}" value="#{newsSessionBean.newsType}"/>
                    <t:updateActionListener property="#{newsSessionBean.currentNewsGroupId}" value="#{node.identifier}" />
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="news">
            <h:panelGroup id="news-tree-news-group">
                <h:commandLink id="select-news-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{newsAction.selectNews}"
                    >

                    <t:graphicImage id="news-tree-news-image" value="/images/user.png" border="0"/>
                    <h:outputText id="news-tree-news-name" value="#{node.description}"/>
                    <h:outputText id="news-tree-news-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{newsSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{newsSessionBean.objectType}" value="#{newsSessionBean.newsType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
