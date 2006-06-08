<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .holding-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="foo" rendered="#{holdingSessionBean.delete and isUserInRole['webmill.portal-manager']}">

        <h:panelGrid columns="2">

            <f:subview id="select-user-subview">
                <jsp:include page="holding-list.jsp"/>
            </f:subview>


            <h:panelGroup id="delete-user-panel" rendered="#{holdingSessionBean.delete}">

                <f:subview id="delete-user-info-subview">
                    <jsp:include page="holding-info.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:commandButton id="process-delete-user-action" action="#{holdingAction.processDeleteHoldingAction}"
                                 value="#{msg['process_delete_user_action']}" styleClass="holding-button-action"
                    >
                </h:commandButton>
                <h:commandButton id="cancel-delete-user-action" action="#{holdingAction.cancelDeleteHoldingAction}"
                                 value="#{msg['cancel_delete_user_action']}" styleClass="holding-button-action"
                    >
                </h:commandButton>

            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>

