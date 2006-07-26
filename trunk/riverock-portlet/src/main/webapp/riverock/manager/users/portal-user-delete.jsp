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
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="delete_portal_name_form" rendered="#{isUserInRole['webmill.authentic']}">

                <f:subview id="portal-user-top-actions-subview">
                    <jsp:include page="_portal-user-top-action.jsp"/>
                </f:subview>

        <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser and isUserInRole['webmill.user-manager']}">

            <f:subview id="subviewPortalUserInfo">
                <jsp:include page="portal-user-info.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.portal_user_delete_confirm_action}"
                                 action="#{portalUserAction.processDeletePortalUser}"
                                 styleClass="portal-user-button-action"/>
                <h:commandButton value="#{msg.portal_user_delete_cancel_action}" action="portal-user"
                                 styleClass="portal-user-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
