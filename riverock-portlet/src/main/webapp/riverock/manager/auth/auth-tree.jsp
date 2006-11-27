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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>


<h:panelGroup id="user-tree-group">

    <h:panelGrid columns="1">

    <h:panelGroup>
    <h:commandButton value="Expand All" action="#{userTree.expandAll}" styleClass="auth-button-action" rendered="#{!empty userTree.tree}"/>
    <h:commandButton value="Collapse All" action="#{userTree.collapseAll}" styleClass="auth-button-action" rendered="#{!empty userTree.tree}"/>
    </h:panelGroup>

    <t:tree2 id="serverTree" value="#{userTree.userTree}" var="node" varNodeToggler="t" clientSideToggle="false"
             binding="#{userTree.tree}"
             showRootNode="false">
        <f:facet name="company">
            <h:panelGroup id="auth-tree-company-group">
                <t:graphicImage id="auth-tree-company-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="auth-tree-company-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="auth-tree-company-name" value="#{node.description}" styleClass="nodeFolder"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="foo-folder">
            <h:panelGroup id="auth-tree-foo-folder-group">
                <t:graphicImage id="auth-tree-foo-folder-image-open" value="/images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="auth-tree-foo-folder-image-close" value="/images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="auth-tree-foo-folder-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="auth-tree-foo-folder-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="user">
            <h:panelGroup id="auth-tree-company-user">
                <h:commandLink id="select-user-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{authUserAction.selectUserAction}">

                    <t:graphicImage id="auth-tree-user-image" value="/images/user.png" border="0"/>
                    <h:outputText id="auth-tree-company-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{userSessionBean.currentAuthUserId}" value="#{node.identifier}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
    </t:tree2>
</h:panelGrid>
</h:panelGroup>



