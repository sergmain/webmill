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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

    <h:panelGrid columns="1">
        <h:commandButton id="add-holding-action" action="#{holdingAction.addHoldingAction}"
		value="#{msg.add_holding_action}" 
		rendered="#{!holdingSessionBean.add}"
	>
        </h:commandButton>


    <h:panelGroup id="holding-list-group">
	<h:outputText value="List of holding is empty" rendered="#{ empty holdingDataProvider.holdingBeans}"/>

        <t:dataTable id="holdingDataTable" var="holdingBean"
		value="#{holdingDataProvider.holdingBeans}" preserveDataModel="true" 
		rendered="#{ !empty holdingDataProvider.holdingBeans}">
	     <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_holding_name}" />
                       </f:facet>
                       <t:commandLink action="#{holdingAction.selectHolding}" immediate="true" >
                            <h:outputText value="#{holdingBean.shortName}" />
                            <t:updateActionListener property="#{holdingSessionBean.currentHoldingId}" value="#{holdingBean.id}" />
                       </t:commandLink>
             </h:column>
        </t:dataTable>
    </h:panelGroup>

    </h:panelGrid>
                