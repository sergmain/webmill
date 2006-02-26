<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<style type="text/css">
td { vertical-align: top; }
</style>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

<f:view>
<h:form id="add-portlet-name-form">

    <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName}">

	<f:subview id="subviewPortletNameInfo">
            <jsp:include page="portlet-name-add-edit.jsp"/>
	</f:subview>
 	
	<h:panelGroup>
		<h:commandButton value="#{msg.action_portlet_name_add_process}" action="#{portletNameAction.processAddPortletName}"/>
		<h:commandButton value="#{msg.action_portlet_name_add_cancel}" action="#{portletNameAction.cancelAddPortletName}"/>	
	</h:panelGroup>
 	 
    </h:panelGrid>	 
	
</h:form>
</f:view>
