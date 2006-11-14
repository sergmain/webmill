<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.webmill.admin.resource.Site" var="msg"/>

<h:panelGroup id="site-tree-group">

    <t:tree2 id="serverTree" value="#{siteTree.siteTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false" binding="#{siteTree.tree}">
        <f:facet name="tree-root">
            <h:panelGroup id="site-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-list">
            <h:panelGroup id="site-tree-site-list-group">
                <t:graphicImage id="site-tree-site-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-site-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-site-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-site-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-action-id" action="#{siteAction.addSiteAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_button_alt}"

>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup id="site-tree-site-group">
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{siteAction.selectSite}"
                    >

                    <t:graphicImage id="site-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="site-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="site-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
    </t:tree2>
</h:panelGroup>
