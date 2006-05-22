<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>

<h:panelGroup id="select-holding-panel"  rendered="#{!holdingSessionBean.edit && !holdingSessionBean.delete && !holdingSessionBean.add}">

        <f:subview id="select-holding-info-subview" >
            <jsp:include page="holding-info.jsp"/>
        </f:subview>

        <h:commandButton id="edit-holding-action" action="#{holdingAction.editHoldingAction}" value="#{msg.edit_holding_action}"
                         styleClass="holding-button-action">
        </h:commandButton>

        <h:commandButton id="delete-holding-action" action="#{holdingAction.deleteHoldingAction}" value="#{msg.delete_holding_action}"
                         styleClass="holding-button-action">
        </h:commandButton>

</h:panelGroup>


