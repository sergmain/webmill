<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<h:panelGroup id="add-user-panel" rendered="#{dataProvider.currentUser.add}">

        <t:messages id="messages" styleClass="error" summaryFormat="{0}" />

<f:verbatim><table borser="0" width="100%" valign="top"><tr><td width="250"></f:verbatim>

	<h:outputText value="User name:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
        <h:selectOneMenu id="select-one-user" 
	styleClass="selectOneMenu" required="true" value="#{dataProvider.currentUser.userId}"
	>
            <f:selectItems value="#{dataProvider.userList}" />
        </h:selectOneMenu>

<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="User login:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
            <h:inputText id="input_user_login" value="#{dataProvider.currentUser.userLogin}" maxlength="20" size="20" required="true">
            </h:inputText>
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="User password:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
            <h:inputText id="input_user_password" value="#{dataProvider.currentUser.userPassword}" maxlength="20" size="20" required="true">
            </h:inputText>
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Repeat user password:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
            <h:inputText id="input_user_password2" value="#{dataProvider.currentUser.userPassword2}" maxlength="20" size="20" required="true">
            </h:inputText>
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Company:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_company_checkbox" value="#{dataProvider.currentUser.company}"/>

        <h:selectOneMenu id="select-one-company" value="#{dataProvider.currentUser.companyId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{dataProvider.companyList}" />
        </h:selectOneMenu>

<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Group of company:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_group_company_checkbox" value="#{dataProvider.currentUser.groupCompany}"/>

        <h:selectOneMenu id="select-one-group-company" value="#{dataProvider.currentUser.groupCompanyId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{dataProvider.groupCompanyList}" />
        </h:selectOneMenu>

<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Holding:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_holding_checkbox" value="#{dataProvider.currentUser.holding}"/>

        <h:selectOneMenu id="select-one-holding" value="#{dataProvider.currentUser.holdingId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{dataProvider.holdingList}" />
        </h:selectOneMenu>


<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>

        <f:subview id="add-role-list-subview">
            <jsp:include page="auth-role-list.jsp"/>
        </f:subview>

<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>

        <h:commandButton id="process-add-user-action" action="#{authUserAction.processAddUserAction}"
		value="#{msg['process_add_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-add-user-action" action="#{authUserAction.cancelAddUserAction}"
		value="#{msg['cancel_add_user_action']}" immediate="true"
	>
        </h:commandButton>

<f:verbatim></td></tr></table></f:verbatim>
</h:panelGroup>

