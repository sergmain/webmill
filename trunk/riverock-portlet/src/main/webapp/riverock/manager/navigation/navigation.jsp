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
</style>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="site-top-actions-subview">
            <jsp:include page="top-action.jsp"/>
        </f:subview>

        <h:panelGrid columns="1">

            <h:panelGroup id="site-tree-site-change-group">
                <h:selectOneMenu id="select-one-site" value="#{navSessionBean.currentSiteId}" styleClass="selectOneMenu" required="true">
                    <f:selectItems value="#{navService.siteList}"/>
                </h:selectOneMenu>
                <h:commandButton id="site-change-site-action" action="#{navAction.changeSite}"
                                 value="Ok"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
            </h:panelGroup>

            <h:outputText value="#{msg.temlate_types}" />
            <h:panelGrid columns="2">
                <t:dataTable id="siteLanguageDataTable"
                             var="siteLanguageBean"
                             value="#{navDataProvider.siteLanguageList}"
                             preserveDataModel="true" >
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="#{msg.header_table_portlet_name_name}" />
                        </f:facet>
                        <t:commandLink action="nav" immediate="true" >
                            <h:outputText value="#{siteLanguageBean.customLanguage}, #{siteLanguageBean.nameCustomLanguage}" />
                            <t:updateActionListener property="#{navSessionBean.currentSiteLanguageId}" value="#{siteLanguageBean.siteLanguageId}" />
                        </t:commandLink>
                    </h:column>

                </t:dataTable>

                <h:panelGroup id="site-tree-site-change-group">
                </h:panelGroup>

            </h:panelGrid>

            <h:outputText value="#{msg.portlet_aliases}" />
            <h:panelGrid columns="2">
                <t:dataTable id="siteLanguageDataTable"
                             var="siteLanguageBean"
                             value="#{navDataProvider.siteLanguageList}"
                             preserveDataModel="true" >
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="#{msg.header_table_portlet_name_name}" />
                        </f:facet>
                        <t:commandLink action="nav" immediate="true" >
                            <h:outputText value="#{siteLanguageBean.customLanguage}, #{siteLanguageBean.nameCustomLanguage}" />
                            <t:updateActionListener property="#{navSessionBean.currentSiteLanguageId}" value="#{siteLanguageBean.siteLanguageId}" />
                        </t:commandLink>
                    </h:column>

                </t:dataTable>

                <h:panelGroup id="site-tree-site-change-group">
                </h:panelGroup>

            </h:panelGrid>

            <h:outputText value="#{msg.url_aliases}" />
            <h:panelGrid columns="2">
                <t:dataTable id="siteLanguageDataTable"
                             var="siteLanguageBean"
                             value="#{navDataProvider.siteLanguageList}"
                             preserveDataModel="true" >
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="#{msg.header_table_portlet_name_name}" />
                        </f:facet>
                        <t:commandLink action="nav" immediate="true" >
                            <h:outputText value="#{siteLanguageBean.customLanguage}, #{siteLanguageBean.nameCustomLanguage}" />
                            <t:updateActionListener property="#{navSessionBean.currentSiteLanguageId}" value="#{siteLanguageBean.siteLanguageId}" />
                        </t:commandLink>
                    </h:column>

                </t:dataTable>

                <h:panelGroup id="site-tree-site-change-group">
                </h:panelGroup>

            </h:panelGrid>


        </h:panelGrid>


    </h:form>
</f:view>
