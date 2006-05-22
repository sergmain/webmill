<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="site-tree-group">

    <t:saveState id="ss-site-tree" value="#{siteTree.siteTree}" />

    <t:tree2 id="serverTree" value="#{siteTree.siteTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="true" binding="#{siteTree.tree}">
        <f:facet name="site-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-action-id" action="#{siteAction.addSiteAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_button_alt}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-language-action-id" action="#{siteLanguageAction.addSiteLanguageAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_langage_button_alt}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-template-action-id" action="#{templateAction.addTemplateAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_template_button_alt}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xslt-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-xslt-action-id" action="#{xsltAction.addXsltAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_xslt_button_alt}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="css-list">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-css-action-id" action="#{cssAction.addCssAction}"
                    image="/images/add.gif" style="border : 0" alt="#{msg.add_new_css_button_alt}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site">
            <h:panelGroup>
                <h:commandLink id="select-site-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template">
            <h:panelGroup>
                <h:commandLink id="select-template-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}" action="site">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.templateType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup>
                <h:commandLink id="select-site-language-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="css">
            <h:panelGroup>
                <h:commandLink id="select-css-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.cssType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xslt">
            <h:panelGroup>
                <h:commandLink id="select-xslt-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.xsltType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
