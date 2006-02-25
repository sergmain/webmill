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

        <t:dataTable id="holdingDataTable" var="holding"
		value="#{holdingDataProvider.holdingBeans}" preserveDataModel="true" 
		rendered="#{ !empty holdingDataProvider.holdingBeans}">
	     <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_holding_name}" />
                       </f:facet>
                       <t:commandLink action="#{holdingAction.selectHolding}" immediate="true" >
                            <h:outputText value="#{holding.shortName}" />
                            <t:updateActionListener property="#{holdingSessionBean.currentHoldingId}" value="#{holding.id}" />
                       </t:commandLink>
             </h:column>
        </t:dataTable>
    </h:panelGroup>

    </h:panelGrid>
                