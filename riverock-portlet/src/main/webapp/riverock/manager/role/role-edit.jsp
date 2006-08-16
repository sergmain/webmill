<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Role" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    td {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .role-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="edit_role_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="1" rendered="#{!empty roleSessionBean.role and isUserInRole['webmill.portal-manager']}">

            <f:subview id="subviewRoleInfo">
                <jsp:include page="role-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.action_role_edit_save}" action="#{roleAction.processEditRole}"
                                 styleClass="role-button-action"/>
                <h:commandButton value="#{msg.action_role_edit_cancel}" action="#{roleAction.cancelEditRole}"
                                 styleClass="role-button-action"/>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>