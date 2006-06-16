<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="site-tree-group">

    <t:tree2 id="serverTree" value="#{menuTree.menuTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false" binding="#{menuTree.tree}">
        <f:facet name="tree-root">
            <h:panelGroup id="site-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="menu-catalog-list">
            <h:panelGroup id="site-tree-css-group">
                <t:graphicImage id="site-tree-css-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-css-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-css-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-css-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-css-action-id" action="#{cssAction.addCssAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_css_button_alt}">

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.cssType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup id="site-tree-site-group">
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{menuSiteAction.selectSite}"
                    >

                    <t:graphicImage id="site-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="site-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="site-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup id="site-tree-site-language-group">
                <t:graphicImage id="site-tree-sile-language-image" value="/images/user.png" border="0"/>
                <h:outputText id="site-tree-site-language-name" value="#{node.description}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="menu-item">
            <h:panelGroup id="site-tree-css-group">
                <h:commandLink id="select-css-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{cssAction.selectCss}"
                    >

                    <t:graphicImage id="site-tree-css-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-css-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{menuSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{menuSessionBean.objectType}" value="#{menuSessionBean.cssType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
