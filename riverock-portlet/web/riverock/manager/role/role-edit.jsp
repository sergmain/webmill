<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Role" var="msg"/>

<style type="text/css">
    td {
        vertical-align: top;
    }
</style>

<f:view>
    <h:form id="edit-role-form">

        <h:panelGrid columns="1" rendered="#{!empty roleSessionBean.role}">

            <f:subview id="subviewRoleInfo">
                <jsp:include page="role-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.action_role_edit_save}" action="#{roleAction.processEditRole}"/>
                <h:commandButton value="#{msg.action_role_edit_cancel}" action="#{roleAction.cancelEditRole}"/>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>
