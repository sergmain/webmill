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
        <f:facet name="article-list">
            <h:panelGroup id="article-tree-article-group-group">
                <t:graphicImage id="article-tree-article-group-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="article-tree-article-group-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="article-tree-article-group-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="article-tree-article-group-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-article-group-action-id" action="#{articleAction.addArticleAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_article_list_button_alt}">

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
        <f:facet name="article">
            <h:panelGroup id="article-tree-article-group">
                <h:commandLink id="select-article-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{articleAction.selectArticle}"
                    >

                    <t:graphicImage id="article-tree-article-image" value="/images/user.png" border="0"/>
                    <h:outputText id="article-tree-article-name" value="#{node.description}"/>
                    <h:outputText id="article-tree-article-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{articleSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{articleSessionBean.objectType}" value="#{articleSessionBean.articleType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
