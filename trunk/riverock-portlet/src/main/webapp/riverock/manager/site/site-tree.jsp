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

<h:panelGroup id="site-tree-group">

<h:panelGrid columns="1">
    <h:panelGroup id="site-tree-site-change-group">
        <h:selectOneMenu id="select-one-site" value="#{siteSessionBean.currentSiteId}" styleClass="selectOneMenu" required="true">
            <f:selectItems value="#{siteService.siteList}"/>
        </h:selectOneMenu>
        <h:commandButton id="site-change-site-action" action="#{siteAction.changeSite}"
                         value="Ok"
                         styleClass="site-button-action"
            >
        </h:commandButton>
    </h:panelGroup>

    <h:panelGroup>
    <h:commandButton value="Expand All" action="#{siteTree.expandAll}" styleClass="site-button-action" rendered="#{!empty siteTree.tree}"/>
    <h:commandButton value="Collapse All" action="#{siteTree.collapseAll}" styleClass="site-button-action" rendered="#{!empty siteTree.tree}"/>
    </h:panelGroup>

    <t:tree2 id="serverTree" value="#{siteTree.siteTree}" var="node" varNodeToggler="t" clientSideToggle="false"
             binding="#{siteTree.tree}"
            showRootNode="false">
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
                                 rendered="#{isUserInRole['webmill.portal-manager']}"
                    >

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language-list">
            <h:panelGroup id="site-tree-site-language-list-group" rendered="#{isUserInRole['webmill.site-manager,webmill.site,webmill.template,webmill.xslt']}">
                <t:graphicImage id="site-tree-site-language-list-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage id="site-tree-site-language-list-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText id="site-tree-site-language-list-name" value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText id="site-tree-site-language-list-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
                <h:commandButton id="add-site-language-action-id" action="#{siteLanguageAction.addSiteLanguageAction}"
                                 image="/images/add.gif" style="border : 0" alt="#{msg.add_new_site_langage_button_alt}"
                                 rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site']}"
                    >

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandButton>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template-list">
            <h:panelGroup id="site-tree-template-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.template']}">
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
            <h:panelGroup id="site-tree-xslt-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.xslt']}">
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
            <h:panelGroup id="site-tree-css-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.css']}">
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
                               action="#{siteAction.selectSite}"
                    >

                    <t:graphicImage id="site-tree-sile-image-open" value="/images/user.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="site-tree-sile-image-close" value="/images/user.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="site-tree-site-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="site-language">
            <h:panelGroup id="site-tree-site-language-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.template,webmill.xslt']}">
                <h:commandLink id="select-site-language-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{siteLanguageAction.selectSiteLanguage}"
                    >

                    <t:graphicImage id="site-tree-sile-language-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-site-language-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.siteLanguageType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="template">
            <h:panelGroup id="site-tree-template-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.template']}">
                <h:commandLink id="select-template-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{templateAction.selectTemplate}"
                    >

                    <t:graphicImage id="site-tree-template-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-template-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.templateType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="xslt">
            <h:panelGroup id="site-tree-xslt-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.xslt']}">
                <h:commandLink id="select-xslt-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{xsltAction.selectXslt}"
                    >

                    <t:graphicImage id="site-tree-xslt-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-xslt-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.xsltType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="css">
            <h:panelGroup id="site-tree-css-group" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.css']}">
                <h:commandLink id="select-css-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{cssAction.selectCss}"
                    >

                    <t:graphicImage id="site-tree-css-image" value="/images/user.png" border="0"/>
                    <h:outputText id="site-tree-css-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{siteSessionBean.id}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{siteSessionBean.objectType}" value="#{siteSessionBean.cssType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGrid>

</h:panelGroup>
