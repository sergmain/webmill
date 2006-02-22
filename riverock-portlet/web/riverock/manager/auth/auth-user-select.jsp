<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>

<h:panelGroup id="select-user-panel"  rendered="#{!userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">

        <f:subview id="select-user-info-subview" >
            <jsp:include page="auth-user-info.jsp"/>
        </f:subview>

        <h:commandButton id="edit-user-action" action="#{authUserAction.editUserAction}"
		value="#{msg['edit_user_action']}" 
	>
        </h:commandButton>
	<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:commandButton id="delete-user-action" action="#{authUserAction.deleteUserAction}"
		value="#{msg['delete_user_action']}"
	>
        </h:commandButton>

</h:panelGroup>


