<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="site-tree-group">

    <t:tree2 id="serverTree" value="#{siteTree.siteTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="true" binding="#{siteTree.tree}">
        <f:facet name="site-list">
            <h:panelGroup id="site-tree-site-list-group">
                <t:graphicImage id="site-tree-sile-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-sile-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-site-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-site-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-action-id" action="#{siteAction.addSiteAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_button_alt}">

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language-list">
            <h:panelGroup id="site-tree-site-language-list-group">
                <t:graphicImage id="site-tree-sile-language-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-sile-language-list-image-open" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-site-language-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-site-language-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-language-action-id" action="#{siteLanguageAction.addSiteLanguageAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_langage_button_alt}">

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template-list">
            <h:panelGroup id="site-tree-template-group">
                <t:graphicImage id="site-tree-template-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-template-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-template-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-template-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-template-action-id" action="#{templateAction.addTemplateAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_template_button_alt}">

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.templateType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xslt-list">
            <h:panelGroup id="site-tree-xslt-group">
                <t:graphicImage id="site-tree-xslt-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-xslt-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-xslt-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-xslt-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-xslt-action-id" action="#{xsltAction.addXsltAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_xslt_button_alt}">

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.xsltType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="css-list">
            <h:panelGroup id="site-tree-css-group">
                <t:graphicImage id="site-tree-css-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-css-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-css-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-css-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-css-action-id" action="#{cssAction.addCssAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_css_button_alt}">

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.cssType}"/>
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
        <f:facet name="site-language">
            <h:panelGroup id="site-tree-site-language-group">
                <h:commandLink id="select-site-language-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{siteLanguageAction.selectSiteLanguage}"
                    >

                    <t:graphicImage id="site-tree-sile-language-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-site-language-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template">
            <h:panelGroup id="site-tree-template-group">
                <h:commandLink id="select-template-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}" action="site"
                               actionListener="#{templateAction.selectTemplate}"
                    >

                    <t:graphicImage id="site-tree-template-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-template-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.templateType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xslt">
            <h:panelGroup id="site-tree-xslt-group">
                <h:commandLink id="select-xslt-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{xsltAction.selectXslt}"
                    >

                    <t:graphicImage id="site-tree-xslt-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-xslt-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.xsltType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="css">
            <h:panelGroup id="site-tree-css-group">
                <h:commandLink id="select-css-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               actionListener="#{cssAction.selectCss}"
                    >

                    <t:graphicImage id="site-tree-css-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-css-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.cssType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
