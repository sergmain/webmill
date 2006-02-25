<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

<f:view>
<h:form id="foo" rendered="#{holdingSessionBean.delete}">

    <h:panelGrid columns="2">

        <f:subview id="select-user-subview">
            <jsp:include page="holding-list.jsp"/>
        </f:subview>


<h:panelGroup id="delete-user-panel" rendered="#{holdingSessionBean.delete}">

        <f:subview id="delete-user-info-subview" >
            <jsp:include page="holding-info.jsp"/>
        </f:subview>

	<h:outputText value="#{msg['confirm_delete_action']}"/>
	<f:verbatim><br/></f:verbatim>

        <h:commandButton id="process-delete-user-action" action="#{holdingAction.processDeleteHoldingAction}"
		value="#{msg['process_delete_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-delete-user-action" action="#{holdingAction.cancelDeleteHoldingAction}"
		value="#{msg['cancel_delete_user_action']}"
	>
        </h:commandButton>

</h:panelGroup>

    </h:panelGrid>

</h:form>
</f:view>

