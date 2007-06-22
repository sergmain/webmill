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


<f:loadBundle basename="org.riverock.portlet.manager.resource.Indexer" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .top-info-text {
        font-size:10px;
    }

    .indexer-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<h:panelGrid columns="3" rendered="#{isUserInRole['webmill.portal-manager,webmill.webclip-manager']}">

    <h:outputText value="&nbsp;" escape="false"/>
    <h:outputText value="&nbsp;" escape="false"/>
    <h:panelGroup>
        <h:panelGrid>
            <h:commandButton action="#{indexerAction.statisticAction}" value="#{manager.statistic_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.statistic_info}" styleClass="top-info-text"/>
        </h:panelGrid>
    </h:panelGroup>


    <h:panelGroup>
        <h:panelGrid>
            <h:commandButton action="#{indexerAction.markAllForIndexingAction}" value="#{manager.mark_all_for_reindex_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.mark_all_for_reload_info}" styleClass="top-info-text"/>
        </h:panelGrid>
    </h:panelGroup>
    <h:outputText value="&nbsp;" escape="false"/>
    <h:outputText value="&nbsp;" escape="false"/>


    <h:panelGroup>
        <h:panelGrid>
            <h:commandButton id="reload-all-content-action" action="#{indexerAction.reindexAction}" value="#{manager.reindex_all_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.reindex_all_info}" styleClass="top-info-text"/>
        </h:panelGrid>
    </h:panelGroup>
    <h:outputText value="&nbsp;" escape="false"/>
    <h:outputText value="&nbsp;" escape="false"/>

</h:panelGrid>

<h:panelGrid rendered="#{!empty indexerAction.result}">
    <h:panelGroup>
        <h:panelGrid>
            <h:commandButton id="clear-result-action" action="#{indexerAction.clearResult}" value="#{manager.clear_result_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.clear_result_info}" styleClass="top-info-text"/>
        </h:panelGrid>
    </h:panelGroup>

    <t:dataTable id="resultOfAction"
                 var="resultOfInvoke"
                 value="#{indexerAction.result}"
                 preserveDataModel="true">
        <h:column>
            <f:facet name="header">
                <h:outputText value="#{manager.result_of_invoke_of_action}" />
            </f:facet>
            <h:outputText value="#{resultOfInvoke}" />
        </h:column>

    </t:dataTable>
</h:panelGrid>


