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

<h:panelGroup id="article-tree-group">

    <h:panelGroup id="article-tree-site-change-group">
        <h:selectOneMenu id="select-one-site" value="#{articleSessionBean.currentSiteId}" styleClass="selectOneSite" required="true">
            <f:selectItems value="#{articleService.siteList}"/>
        </h:selectOneMenu>
        <h:commandButton id="article-change-site-action" action="#{articleSiteAction.changeSite}"
                         value="Ok"
                         styleClass="article-button-action"
            >
        </h:commandButton>
    </h:panelGroup>

    <t:tree2 id="serverTree" value="#{articleTree.articleTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false">
        <f:facet name="tree-root">
            <h:panelGroup id="article-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="plain-article-list">
            <h:panelGroup id="article-tree-plain-article-list-group">
                <t:graphicImage id="article-tree-plain-article-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="article-tree-plain-article-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="article-tree-plain-article-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="article-tree-plain-article-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-article-group-action-id" action="#{articleAction.addArticleAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_plain_article_button_alt}">

                    <t:updateActionListener property="#{articleSessionBean.xml}" value="false" />
                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.articleType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xml-article-list">
            <h:panelGroup id="article-tree-xml-article-list-group">
                <t:graphicImage id="article-tree-xml-article-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="article-tree-xml-article-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="article-tree-xml-article-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="article-tree-xml-article-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-article-group-action-id" action="#{articleAction.addArticleAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_xml_article_button_alt}">

                    <t:updateActionListener property="#{articleSessionBean.xml}" value="true" />
                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.articleType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup id="article-tree-site-group">
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{articleSiteAction.selectSite}"
                    >

                    <t:graphicImage id="article-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="article-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="article-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup id="article-tree-site-language-group">
                <t:graphicImage id="article-tree-sile-language-image" value="/images/user.png" border="0"/>
                <h:outputText id="article-tree-site-language-name" value="#{node.description}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="plain-article">
            <h:panelGroup id="article-tree-plain-article-group">
                <h:commandLink id="select-article-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{articleAction.selectArticle}"
                    >

                    <t:graphicImage id="article-tree-plain-article-image" value="/images/user.png" border="0"/>
                    <h:outputText id="article-tree-plain-article-name" value="#{node.description}"/>
                    <h:outputText id="article-tree-plain-article-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.articleType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xml-article">
            <h:panelGroup id="article-tree-xml-article-group">
                <h:commandLink id="select-xml-article-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{articleAction.selectArticle}"
                    >

                    <t:graphicImage id="article-tree-xml-article-image" value="/images/user.png" border="0"/>
                    <h:outputText id="article-tree-xml-article-name" value="#{node.description}"/>
                    <h:outputText id="article-tree-xml-article-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.articleType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
