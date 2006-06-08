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
    <h:form id="foo" rendered="#{userSessionBean.edit and isUserInRole['webmill.auth']}">


        <h:panelGrid columns="2">

            <f:subview id="select-user-subview">
                <jsp:include page="auth-tree.jsp"/>
            </f:subview>


            <h:panelGroup id="edit-user-panel" rendered="#{userSessionBean.edit}">
                <h:panelGrid columns="2">

                    <h:outputText value="User name:"/>
                    <h:outputText value="#{dataProvider.currentUser.userName}"/>

                    <h:outputText value="User login:"/>
                    <h:outputText value="#{dataProvider.currentUser.authInfo.userLogin}"/>

                    <h:outputText value="Company:"/>


                    <h:panelGroup id="select-company-group">
                        <h:selectBooleanCheckbox id="select_company_checkbox"
                                                 value="#{dataProvider.currentUser.authInfo.company}"/>

                        <h:selectOneMenu id="select-one-company" value="#{dataProvider.currentUser.authInfo.companyId}"
                                         styleClass="selectOneMenu" required="true"
                            >
                            <f:selectItems value="#{dataProvider.companyList}"/>
                        </h:selectOneMenu>
                    </h:panelGroup>

                    <h:outputText value="Holding:"/>

                    <h:panelGroup id="select-holding-group">
                        <h:selectBooleanCheckbox id="select_holding_checkbox"
                                                 value="#{dataProvider.currentUser.authInfo.holding}"/>

                        <h:selectOneMenu id="select-one-holding" value="#{dataProvider.currentUser.authInfo.holdingId}"
                                         styleClass="selectOneMenu" required="false"
                            >
                            <f:selectItems value="#{dataProvider.holdingList}"/>
                        </h:selectOneMenu>
                    </h:panelGroup>

                </h:panelGrid>


                <h:panelGrid columns="1">
                    <f:subview id="edit-role-list-subview">
                        <jsp:include page="auth-role-list.jsp"/>
                    </f:subview>


                    <h:panelGroup id="edit-delete-actions">
                        <h:commandButton id="save-user-action" action="#{authUserAction.saveUserAction}"
                                         value="#{msg['save_user_action']}"
                                         styleClass="auth-button-action"
                            >
                        </h:commandButton>
                        <h:commandButton id="cancel-edit-user-action" action="#{authUserAction.cancelEditUserAction}"
                                         value="#{msg['cancel_edit_user_action']}"
                                         styleClass="auth-button-action"
                            >
                        </h:commandButton>
                    </h:panelGroup>


                </h:panelGrid>
            </h:panelGroup>


        </h:panelGrid>


    </h:form>
</f:view>





