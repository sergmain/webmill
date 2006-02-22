<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>	

<f:view>
<h:form id="edit-company-form">
 <h:panelGroup id="panelGroup2">
 <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

<f:subview id="subviewCompanyInfo">
            <jsp:include page="company-add-edit.jsp"/>
</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.action_edit_save}" action="#{action.processEditCompany}"/>
		<h:commandButton value="#{msg.action_edit_cancel}" action="#{action.cancelEditCompany}"/>	
	</h:panelGroup>
 	 
 </h:panelGrid>	 
 	
</h:panelGroup>
 	
	 
	
</h:form>
</f:view>
