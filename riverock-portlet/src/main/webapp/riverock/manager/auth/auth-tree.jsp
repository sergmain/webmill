<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>


<h:panelGroup id="user-tree-group">

    <t:tree2 id="serverTree" value="#{userTree.userTree}" var="node" varNodeToggler="t" clientSideToggle="false"
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

</h:panelGroup>



