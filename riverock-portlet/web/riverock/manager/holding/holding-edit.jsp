<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

<f:view>
<h:form id="foo" rendered="#{holdingSessionBean.edit}">

    <h:panelGrid columns="2">

        <f:subview id="select-user-subview">
            <jsp:include page="holding-list.jsp"/>
        </f:subview>


<h:panelGroup id="edit-holding-panel" rendered="#{holdingSessionBean.edit}">
   <h:panelGrid columns="2">

	<h:outputText value="Holding short name:"/>
            <h:inputText id="input_holding_short_name" value="#{holdingDataProvider.currentHolding.shortName}" maxlength="20" size="20" 
		required="false">
            </h:inputText>

	<h:outputText value="Holding name:"/>
            <h:inputText id="input_holding_name" value="#{holdingDataProvider.currentHolding.name}" maxlength="20" size="20" 
		required="false">
            </h:inputText>

</h:panelGrid>	 


<h:panelGrid columns="1">
        <f:subview id="edit-company-list-subview">
            <jsp:include page="holding-company-list.jsp"/>
        </f:subview>


    <h:panelGroup id="edit-delete-actions">
        <h:commandButton id="save-holding-action" action="#{holdingAction.saveHoldingAction}"
		value="#{msg['save_holding_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-edit-holding-action" action="#{holdingAction.cancelEditHoldingAction}"
		value="#{msg['cancel_edit_holding_action']}"
	>
        </h:commandButton>
    </h:panelGroup>


</h:panelGrid>	 
</h:panelGroup>

    </h:panelGrid>


</h:form>
</f:view>





