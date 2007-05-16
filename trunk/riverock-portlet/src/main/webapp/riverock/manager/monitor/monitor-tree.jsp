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

<h:panelGroup id="monitor-tree-group">

<h:panelGrid columns="1">

<%--
    <h:panelGroup>
    <h:commandButton value="Expand All" action="#{menuTree.expandAll}" styleClass="menu-button-action" rendered="#{!empty menuTree.tree}"/>
    <h:commandButton value="Collapse All" action="#{menuTree.collapseAll}" styleClass="menu-button-action" rendered="#{!empty menuTree.tree}"/>
    </h:panelGroup>
--%>

    <t:tree2 id="serverTree" value="#{monitorTree.serverTree}" var="node"
             varNodeToggler="t" clientSideToggle="true"
             binding="#{monitorTree.tree}"
             showRootNode="true" >
        <f:facet name="tree-root">
            <h:panelGroup id="menu-tree-tree-root-group">
                <h:outputText value="Root element"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="directory">
            <h:panelGroup id="monitor-tree-directory-group">
                <h:outputText id="monitor-tree-directory-name" value="#{node.description}"/>
            </h:panelGroup>
        </f:facet>
    </t:tree2>

</h:panelGrid>
</h:panelGroup>
