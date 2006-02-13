<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<f:view>
<h:form id="delete-company-form">
<f:loadBundle basename="org.riverock.portlet.company.resource.Company" var="msg"/>	
 <h:panelGroup id="panelGroup2">
 <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

<f:subview id="subviewCompanyInfo">
            <jsp:include page="company-info.jsp"/>
</f:subview>
 	
	<h:panelGroup id="editDeleteControls">
		<h:commandButton value="#{msg.action_delete_confirm}" action="#{action.processDeleteCompany}"/>
		<h:commandButton value="#{msg.action_delete_cancel}" action="company"/>	
	</h:panelGroup>
 	 
 </h:panelGrid>	 
 	
</h:panelGroup>
 	
	 
	
</h:form>
</f:view>
