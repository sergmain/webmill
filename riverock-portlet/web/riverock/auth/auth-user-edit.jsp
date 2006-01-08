<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<h:panelGroup id="edit-user-panel" rendered="#{treeBacker.currentUser.edit}">

<f:verbatim><table borser="0" width="100%" valign="top"><tr><td width="250"></f:verbatim>
	<h:outputText value="User name:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<h:outputText value="#{treeBacker.currentUser.userName}" />
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="User login:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<h:outputText value="#{treeBacker.currentUser.userLogin}" />
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Company:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_company_checkbox" value="#{treeBacker.currentUser.company}"/>

        <h:selectOneMenu id="select-one-company" value="#{treeBacker.currentUser.companyId}" 
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{treeBacker.companyList}" />
        </h:selectOneMenu>

<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Group of company:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_group_company_checkbox" value="#{treeBacker.currentUser.groupCompany}"/>

        <h:selectOneMenu id="select-one-group-company" value="#{treeBacker.currentUser.groupCompanyId}" 
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{treeBacker.groupCompanyList}" />
        </h:selectOneMenu>

<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Holding:"/>
<f:verbatim></td><td valign="top"></f:verbatim>

	<h:selectBooleanCheckbox id="select_holding_checkbox" value="#{treeBacker.currentUser.holding}"/>

        <h:selectOneMenu id="select-one-holding" value="#{treeBacker.currentUser.holdingId}" 
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{treeBacker.holdingList}" />
        </h:selectOneMenu>


<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>

    <h:outputText value="#{msg['role_list']}" styleClass="standard_bold" />

<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>

        <h:commandButton id="save-user-action" action="#{treeBacker.saveUserAction}" 
		value="#{msg['save_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-edit-user-action" action="#{treeBacker.cancelEditUserAction}" 
		value="#{msg['cancel_edit_user_action']}"
	>
        </h:commandButton>

<f:verbatim></td></tr></table></f:verbatim>
</h:panelGroup>

