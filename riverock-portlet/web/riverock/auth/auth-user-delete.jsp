<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<f:view>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<h:form id="foo" rendered="#{userSessionBean.delete}">

    <t:panelTabbedPane bgcolor="#FFFFCC" >

        <t:panelTab id="tab1" label="#{msg['tabbed_tab1']}">
    
<f:verbatim><table borser="0" width="100%"><tr><td width="300" valign="top"></f:verbatim>


        <f:subview id="select-user-subview">
            <jsp:include page="auth-tree.jsp"/>
        </f:subview>


<f:verbatim></td><td valign="top"></f:verbatim>

<h:panelGroup id="delete-user-panel" rendered="#{userSessionBean.delete}">

        <f:subview id="delete-user-info-subview" >
            <jsp:include page="auth-user-info.jsp"/>
        </f:subview>

	<h:outputText value="#{msg['confirm_delete_action']}"/>
	<f:verbatim><br/></f:verbatim>

        <h:commandButton id="process-delete-user-action" action="#{authUserAction.processDeleteUserAction}"
		value="#{msg['process_delete_user_action']}"
	>
        </h:commandButton>
        <h:commandButton id="cancel-delete-user-action" action="#{authUserAction.cancelDeleteUserAction}"
		value="#{msg['cancel_delete_user_action']}"
	>
        </h:commandButton>

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

