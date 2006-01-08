<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<h:panelGroup id="delete-user-panel" rendered="#{treeBacker.currentUser.delete}">

        <f:subview id="delete-user-info-subview" >
            <jsp:include page="auth-user-info.jsp"/>
        </f:subview>

	<h:outputText value="#{msg['confirm_delete_action']}"/>
	<f:verbatim><br/></f:verbatim>

        <h:commandButton id="process-delete-user-action" action="#{treeBacker.processDeleteUserAction}" 
		value="#{msg['process_delete_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-delete-user-action" action="#{treeBacker.cancelDeleteUserAction}" 
		value="#{msg['cancel_delete_user_action']}"
	>
        </h:commandButton>

</h:panelGroup>

