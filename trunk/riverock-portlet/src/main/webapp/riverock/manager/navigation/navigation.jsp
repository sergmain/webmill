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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Navigation" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .site-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
    .first-column-table {
        width:40%;
    }
    .second-column-table {
        width:60%;
    }
</style>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="1">

            <f:subview id="site-top-actions-subview">
                <jsp:include page="top-action.jsp"/>
            </f:subview>

            <h:panelGroup>
                <h:selectOneMenu value="#{navSessionBean.currentSiteId}" styleClass="selectOneMenu" required="true">
                    <f:selectItems value="#{navService.siteList}"/>
                </h:selectOneMenu>
                <h:commandButton action="#{navAction.changeSite}"
                                 value="Ok"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
            </h:panelGroup>

            <h:panelGrid columns="1" rendered="#{!empty navSessionBean.currentSiteId}">
                <h:outputText value="#{msg.type_of_template}" />
                <h:panelGrid columns="2" columnClasses="first-column-table, second-column-table">
                    <t:dataTable var="siteLanguageBean" value="#{navDataProvider.siteLanguageList}" preserveDataModel="true" >
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="#{msg.site_locale_list}" />
                            </f:facet>
                            <t:commandLink action="nav" immediate="true" >
                                <h:outputText value="#{siteLanguageBean.customLanguage}, #{siteLanguageBean.nameCustomLanguage}" />
                                <t:updateActionListener property="#{navSessionBean.currentSiteLanguageId}" value="#{siteLanguageBean.siteLanguageId}" />
                            </t:commandLink>
                        </h:column>

                    </t:dataTable>

                    <h:panelGroup>
                        <h:panelGroup rendered="#{!empty navSessionBean.currentSiteLanguageId}">
                            <h:panelGrid columns="2">
                                <h:outputText value="#{msg.dynamic_template}" />
                                <h:selectOneMenu value="#{navSessionBean.dynamicTemplateId}" styleClass="selectOneMenu" required="true">
                                    <f:selectItems value="#{navDataProvider.templateList}"/>
                                </h:selectOneMenu>

                                <h:outputText value="#{msg.popup_template}" />
                                <h:selectOneMenu value="#{navSessionBean.popupTemplateId}" styleClass="selectOneMenu" required="true">
                                    <f:selectItems value="#{navDataProvider.templateList}"/>
                                </h:selectOneMenu>

                                <h:outputText value="#{msg.maximazed_template}" />
                                <h:selectOneMenu value="#{navSessionBean.maximazedTemplateId}" styleClass="selectOneMenu" required="true">
                                    <f:selectItems value="#{navDataProvider.templateList}"/>
                                </h:selectOneMenu>
                            </h:panelGrid>

                            <h:commandButton action="#{navAction.applyTemplateChanges}"
                                             value="#{msg.apply_template_changes}"
                                             styleClass="site-button-action"
                                >
                            </h:commandButton>
                        </h:panelGroup>
                    </h:panelGroup>
                </h:panelGrid>

                
                <h:panelGroup>
                    <h:outputText value="#{msg.portlet_aliases}" />
                    <h:commandButton action="#{navAction.addPortletAlias}" image="/images/add.gif" style="border:0" alt="#{msg.add_new_portlet_alias_alt}"/>
                </h:panelGroup>
                
                <h:panelGrid columns="2" columnClasses="first-column-table, second-column-table">
                    <t:dataTable var="portletAlias" value="#{navDataProvider.portletAliases}">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="#{msg.portlet_aliases_list}" />
                            </f:facet>
                            <t:commandLink action="#{navAction.loadPortletAlias}" >
                                <h:outputText value="#{portletAlias.shortUrl}, #{portletAlias.portletName}, #{portletAlias.templateName}" />
                                <t:updateActionListener property="#{navSessionBean.currentPortletAliasId}" value="#{portletAlias.portletAliasId}" />
                            </t:commandLink>
                        </h:column>
                    </t:dataTable>


                    <h:panelGroup>
                        <f:subview id="nav-portlet-alias-add-subview" rendered="#{navDataProvider.addPortletAliasState}">
                            <jsp:include page="portlet-alias-add.jsp"/>
                        </f:subview>
                        <h:panelGroup rendered="#{!empty navSessionBean.currentPortletAliasId}">
                            <f:subview id="nav-portlet-alias-view-subview" rendered="#{navDataProvider.viewPortletAliasState}">
                                <jsp:include page="portlet-alias-view.jsp"/>
                            </f:subview>
                            <f:subview id="nav-portlet-alias-edit-subview" rendered="#{navDataProvider.editPortletAliasState}">
                                <jsp:include page="portlet-alias-edit.jsp"/>
                            </f:subview>
                            <f:subview id="nav-portlet-alias-delete-subview" rendered="#{navDataProvider.deletePortletAliasState}">
                                <jsp:include page="portlet-alias-delete.jsp"/>
                            </f:subview>
                        </h:panelGroup>
                    </h:panelGroup>
                </h:panelGrid>


                <h:panelGroup>
                    <h:outputText value="#{msg.url_aliases}" />
                    <h:commandButton action="#{navAction.addUrlAlias}" image="/images/add.gif" style="border : 0" alt="#{msg.add_new_url_alias_alt}"/>
                </h:panelGroup>
                <h:panelGrid columns="2" columnClasses="first-column-table, second-column-table">
                    <t:dataTable var="urlAlias" value="#{navDataProvider.urlAliases}">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="#{msg.url_aliases_list}" />
                            </f:facet>
                            <t:commandLink action="#{navAction.loadUrlAlias}" >
                                <h:outputText value="#{urlAlias.url} -> #{urlAlias.alias}" />
                                <t:updateActionListener property="#{navSessionBean.currentUrlAliasId}" value="#{urlAlias.urlAliasId}" />
                            </t:commandLink>
                        </h:column>

                    </t:dataTable>

                    <h:panelGroup>
                        <f:subview id="nav-url-alias-add-subview" rendered="#{navDataProvider.addUrlAliasState}">
                            <jsp:include page="url-alias-add.jsp"/>
                        </f:subview>
                        <h:panelGroup rendered="#{!empty navSessionBean.currentUrlAliasId}">
                            <f:subview id="nav-url-alias-view-subview" rendered="#{navDataProvider.viewUrlAliasState}">
                                <jsp:include page="url-alias-view.jsp"/>
                            </f:subview>
                            <f:subview id="nav-url-alias-edit-subview" rendered="#{navDataProvider.editUrlAliasState}">
                                <jsp:include page="url-alias-edit.jsp"/>
                            </f:subview>
                            <f:subview id="nav-url-alias-delete-subview" rendered="#{navDataProvider.deleteUrlAliasState}">
                                <jsp:include page="url-alias-delete.jsp"/>
                            </f:subview>
                        </h:panelGroup>
                    </h:panelGroup>

                </h:panelGrid>


            </h:panelGrid>
        </h:panelGrid>
    </h:form>
</f:view>
