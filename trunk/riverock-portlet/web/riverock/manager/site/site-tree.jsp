<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="site-tree-group">
    <t:tree2 id="serverTree" value="#{siteTree.siteTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="true" binding="#{siteTree.tree}">
        <f:facet name="site">
            <h:panelGroup>
                <h:commandLink id="select-template-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template">
            <h:panelGroup>
                <h:commandLink id="select-template-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.templateType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup>
                <h:commandLink id="select-site-language-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
