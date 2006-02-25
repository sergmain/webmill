<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>	

<style type="text/css">
TD { vertical-align: top; }
</style>

<f:view>
<h:form id="add-company-form">

    <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

	<f:subview id="subviewCompanyInfo">
            <jsp:include page="company-add-edit.jsp"/>
	</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.action_add_process}" action="#{action.processAddCompany}"/>
		<h:commandButton value="#{msg.action_add_cancel}" action="#{action.cancelAddCompany}"/>	
	</h:panelGroup>
 	 
    </h:panelGrid>	 
	
</h:form>
</f:view>
