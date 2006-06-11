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
    <h:form id="role-form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
        <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.user-manager']}"/>
        <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                         styleClass="top-button-action"
 rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.template,webmill.css,webmill.xslt']}"/>

        <h:panelGrid columns="1" rendered="#{isUserInRole['webmill.portal-manager']}">
            <h:commandButton value="#{msg.action_role_add_role}" action="#{roleAction.addRole}"
                             styleClass="role-button-action"/>

            <h:panelGrid columns="2">

                <f:subview id="subviewRoleList">
                    <jsp:include page="role-list.jsp"/>
                </f:subview>

                <h:panelGrid columns="1" rendered="#{!empty roleSessionBean.role}">

                    <f:subview id="subviewRoleInfo">
                        <jsp:include page="role-info.jsp"/>
                    </f:subview>

                    <h:panelGroup id="editDeleteControls">
                        <h:commandButton value="#{msg.role_action_role_edit_role}" action="role-edit" styleClass="role-button-action"/>
                        <h:commandButton value="#{msg.role_action_role_delete_role}" action="role-delete" styleClass="role-button-action"/>
                    </h:panelGroup>

                </h:panelGrid>
            </h:panelGrid>

        </h:panelGrid>

    </h:form>
</f:view>
