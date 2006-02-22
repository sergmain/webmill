<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

<f:view>
<h:form id="foo" rendered="#{holdingSessionBean.edit}">

    <t:panelTabbedPane bgcolor="#FFFFCC" >

        <t:panelTab id="tab1" label="#{msg['tabbed_tab1']}">
    
<f:verbatim><table borser="0" width="100%"><tr><td width="300" valign="top"></f:verbatim>


        <f:subview id="select-user-subview">
            <jsp:include page="auth-tree.jsp"/>
        </f:subview>


<f:verbatim></td><td valign="top"></f:verbatim>


<h:panelGroup id="edit-user-panel" rendered="#{holdingSessionBean.edit}">
   <h:panelGrid columns="2">

	<h:outputText value="User name:"/>
	<h:outputText value="#{holdingDataProvider.currentUser.userName}" />

	<h:outputText value="User login:"/>
	<h:outputText value="#{holdingDataProvider.currentUser.authInfo.userLogin}" />

	<h:outputText value="Company:"/>


    <h:panelGroup id="select-company-group">
	<h:selectBooleanCheckbox id="select_company_checkbox" 
         value="#{holdingDataProvider.currentUser.authInfo.company}"/>

        <h:selectOneMenu id="select-one-company" value="#{holdingDataProvider.currentUser.authInfo.companyId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{holdingDataProvider.companyList}" />
        </h:selectOneMenu>
    </h:panelGroup>

	<h:outputText value="Holding:"/>

    <h:panelGroup id="select-holding-group">
	<h:selectBooleanCheckbox id="select_holding_checkbox" 
         value="#{holdingDataProvider.currentUser.authInfo.holding}"/>

        <h:selectOneMenu id="select-one-holding" value="#{holdingDataProvider.currentUser.authInfo.holdingId}"
	styleClass="selectOneMenu" required="false" 
	>
            <f:selectItems value="#{holdingDataProvider.holdingList}" />
        </h:selectOneMenu>
    </h:panelGroup>

</h:panelGrid>	 


<h:panelGrid columns="1">
        <f:subview id="edit-role-list-subview">
            <jsp:include page="auth-role-list.jsp"/>
        </f:subview>


    <h:panelGroup id="edit-delete-actions">
        <h:commandButton id="save-user-action" action="#{holdingAction.saveUserAction}"
		value="#{msg['save_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-edit-user-action" action="#{holdingAction.cancelEditUserAction}"
		value="#{msg['cancel_edit_user_action']}"
	>
        </h:commandButton>
    </h:panelGroup>


</h:panelGrid>	 
</h:panelGroup>


<f:verbatim></td></tr></table></f:verbatim>


        </t:panelTab>

        <t:panelTab id="tab2" label="#{msg['tabbed_tab2']}">
        <f:subview id="role-subview">
            <jsp:include page="role/role.jsp"/>
        </f:subview>
        </t:panelTab>

    </t:panelTabbedPane>
</h:form>


</f:view>





