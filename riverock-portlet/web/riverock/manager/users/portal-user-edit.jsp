<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>

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
    <h:form id="edit-portal-user-form">

        <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

            <h:panelGroup>
                <h:outputText value="Portal user info"/>
                <h:panelGrid columns="2">

                    <h:outputText value="#{msg.portal_user_company_name}"/>
                    <h:outputText value="#{portalUserSessionBean.portalUser.companyName}"/>

                    <h:outputText value="#{msg.portal_user_first_name}"/>
                    <h:inputText id="portal-user-first-name-field"
                                 value="#{portalUserSessionBean.portalUser.firstName}"/>

                    <h:outputText value="#{msg.portal_user_middle_name}"/>
                    <h:inputText id="portal-user-middle-name-field"
                                 value="#{portalUserSessionBean.portalUser.middleName}"/>

                    <h:outputText value="#{msg.portal_user_last_name}"/>
                    <h:inputText id="portal-user-last-name-field" value="#{portalUserSessionBean.portalUser.lastName}"/>

                    <h:outputText value="#{msg.portal_user_address}"/>
                    <h:inputText id="portal-user-address-field" value="#{portalUserSessionBean.portalUser.address}"/>

                    <h:outputText value="#{msg.portal_user_phone}"/>
                    <h:inputText id="portal-user-phone-field" value="#{portalUserSessionBean.portalUser.phone}"/>

                    <h:outputText value="#{msg.portal_user_email}"/>
                    <h:inputText id="portal-user-email-field" value="#{portalUserSessionBean.portalUser.email}"/>
                </h:panelGrid>
            </h:panelGroup>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.portal_name_edit_save_action}"
                                 action="#{portalUserAction.processEditPortalUser}"
                                 styleClass="portal-user-button-action"/>
                <h:commandButton value="#{msg.portal_name_edit_cancel_action}"
                                 action="#{portalUserAction.cancelEditPortalUser}"
                                 styleClass="portal-user-button-action"/>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>
