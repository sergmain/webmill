<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    td {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .portal-user-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:view>
    <h:form id="portal-user-form">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"/>
        <h:commandButton id="portlet-name-action" action="portlet-name" value="#{manager.portal_user_button}"/>

        <h:panelGrid columns="2">

            <h:panelGroup>
                <f:subview id="subviewPortalUserList">
                    <jsp:include page="portal-user-list.jsp"/>
                </f:subview>
            </h:panelGroup>


            <h:panelGroup>
                <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

                    <f:subview id="subviewPortalUserInfo">
                        <jsp:include page="portal-user-info.jsp"/>
                    </f:subview>

                    <h:panelGroup id="editDeleteControls">
                        <h:commandButton value="#{msg.portal_user_action_edit_portal_user_action}"
                                         action="portal-user-edit" styleClass="portal-user-button-action"/>
                        <h:commandButton value="#{msg.portal_user_action_delete_portal_user_action}"
                                         action="portal-user-delete" styleClass="portal-user-button-action"/>
                    </h:panelGroup>

                </h:panelGrid>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>
