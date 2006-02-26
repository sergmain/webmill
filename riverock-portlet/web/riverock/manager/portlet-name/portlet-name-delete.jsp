<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

<style type="text/css">
td { vertical-align: top; }
</style>

<f:view>
<h:form id="delete-portlet-name-form">

    <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName}">

	<f:subview id="subviewPortletNameInfo">
            <jsp:include page="portlet-name-info.jsp"/>
	</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.action_portlet_name_delete_confirm}" action="#{portletNameAction.processDeletePortletName}"/>
		<h:commandButton value="#{msg.action_portlet_name_delete_cancel}" action="portlet-name"/>
	</h:panelGroup>
 	 
    </h:panelGrid>	 
	
</h:form>
</f:view>
