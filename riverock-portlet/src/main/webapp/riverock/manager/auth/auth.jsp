<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .auth-button-action {
        width: 150px;
        height: 22px;
    }

    .auth-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="foo" rendered="#{isUserInRole['webmill.authentic']}">

        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                         styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager']}"/>
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


        <h:panelGrid columns="1" id="main-grid" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}">
            <h:commandButton id="add-user-action" action="#{authUserAction.addUserAction}"
                             value="#{msg['add_user_action']}"
                             styleClass="auth-button-action">
            </h:commandButton>

            <h:panelGrid columns="2" id="tree-grid">

                <f:subview id="auth-tree-subview"
                           rendered="#{!userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
                    <jsp:include page="auth-tree.jsp"/>
                </f:subview>

                <f:subview id="select-user-subview"
                           rendered="#{!empty dataProvider.currentUser && !userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
                    <jsp:include page="auth-user-select.jsp"/>
                </f:subview>

            </h:panelGrid>
        </h:panelGrid>

    </h:form>
</f:view>

