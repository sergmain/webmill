<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<h:form id="foo" rendered="#{userSessionBean.add}">

    <t:panelTabbedPane bgcolor="#FFFFCC" >

        <t:panelTab id="tab1" label="#{msg['tabbed_tab1']}">
    
<f:verbatim><table borser="0" width="100%"><tr><td width="300" valign="top"></f:verbatim>


        <f:subview id="select-user-subview">
            <jsp:include page="auth-tree.jsp"/>
        </f:subview>


<f:verbatim></td><td valign="top"></f:verbatim>


<h:panelGroup id="add-user-panel" rendered="#{!empty dataProvider.currentUser and userSessionBean.add}">


   <t:messages id="messages" styleClass="error" summaryFormat="{0}" />

   <h:panelGrid columns="2">

	<h:outputText value="User name:"/>

    <h:panelGroup id="select-user-group">
        <h:selectOneMenu id="select-one-user" 
	styleClass="selectOneMenu" required="true" value="#{dataProvider.currentUser.authInfo.userId}"
	>
            <f:selectItems value="#{dataProvider.userList}" />
        </h:selectOneMenu>
    </h:panelGroup>

	<h:outputText value="User login:"/>

            <h:inputText id="input_user_login" value="#{dataProvider.currentUser.authInfo.userLogin}" maxlength="20" size="20" 
		required="false">
            </h:inputText>

	<h:outputText value="User password:"/>
            <h:inputText id="input_user_password" value="#{dataProvider.currentUser.userPassword}" maxlength="20" size="20" 
		required="false">
            </h:inputText>

	<h:outputText value="Repeat user password:"/>
            <h:inputText id="input_user_password2" value="#{dataProvider.currentUser.userPassword2}" maxlength="20" size="20" 
		required="false">
            </h:inputText>

	<h:outputText value="Company:"/>

    <h:panelGroup id="select-companyy-group">
	<h:selectBooleanCheckbox id="select_company_checkbox" value="#{dataProvider.currentUser.authInfo.company}"/>

        <h:selectOneMenu id="select-one-company" value="#{dataProvider.currentUser.authInfo.companyId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{dataProvider.companyList}" />
        </h:selectOneMenu>
    </h:panelGroup>

	<h:outputText value="Holding:"/>

    <h:panelGroup id="select-holding-group">
	<h:selectBooleanCheckbox id="select_holding_checkbox" value="#{dataProvider.currentUser.authInfo.holding}"/>

        <h:selectOneMenu id="select-one-holding" value="#{dataProvider.currentUser.authInfo.holdingId}"
	styleClass="selectOneMenu" required="false" 
	>
            <f:selectItems value="#{dataProvider.holdingList}" />
        </h:selectOneMenu>
    </h:panelGroup>


</h:panelGrid>	 


<h:panelGrid columns="1">

        <f:subview id="add-role-list-subview">
            <jsp:include page="auth-role-list.jsp"/>
        </f:subview>


    <h:panelGroup id="select-action-group">
        <h:commandButton id="process-add-user-action" action="#{authUserAction.processAddUserAction}"
		value="#{msg['process_add_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-add-user-action" action="#{authUserAction.cancelAddUserAction}"
		value="#{msg['cancel_add_user_action']}" immediate="true"
	>
        </h:commandButton>
    </h:panelGroup>

</h:panelGrid>	 

</h:panelGroup>


<f:verbatim></td></tr></table></f:verbatim>


        </t:panelTab>

        <t:panelTab id="tab2" label="#{msg['tabbed_tab2']}">
        <f:subview id="role-subview">
        </f:subview>
        </t:panelTab>

    </t:panelTabbedPane>
</h:form>


</f:view>




