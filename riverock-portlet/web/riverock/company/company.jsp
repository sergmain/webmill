<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<f:view>
<h:form id="company-form">

    <f:loadBundle basename="org.riverock.portlet.company.resource.Company" var="msg"/>	
    <h:panelGrid columns="2" rendered="#{not empty action.authSessionBean.authSession }">
	<h:panelGroup id="panelGroup1">
	    <h:commandButton value="#{msg.action_add_company}" 
	    action="#{action.addCompany}" 
	    style=" width : 106px; height : 22px;"
	    />	

	    <f:subview id="subviewCompanyList">
            	<jsp:include page="company-list.jsp"/>
	    </f:subview>

    	</h:panelGroup>
    	<h:panelGroup id="panelGroup2">
    	    <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">
            	
    	    	<f:subview id="subviewCompanyInfo">
            	    <jsp:include page="company-info.jsp"/>
	    	</f:subview>
 	
	    	<h:panelGroup id="editDeleteControls">
		    <h:commandButton value="#{msg.edit_company}" action="company-edit"/>
		    <h:commandButton value="#{msg.delete_company}" action="company-delete"/>	
	    	</h:panelGroup>
    	    </h:panelGrid>	 
    	</h:panelGroup>
    </h:panelGrid>

</h:form>
</f:view>
