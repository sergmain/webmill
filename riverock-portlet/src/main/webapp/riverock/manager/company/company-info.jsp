<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>	

 <h:outputText value="Company info"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.company_id}"/>
 		<h:outputText value="#{companySessionBean.company.id}"/>

 		<h:outputText value="#{msg.company_name}"/>
 		<h:outputText value="#{companySessionBean.company.name}"/>

 		<h:outputText value="#{msg.company_short_name}"/>
 		<h:outputText value="#{companySessionBean.company.shortName}"/>

 		<h:outputText value="#{msg.company_address}"/>
 		<h:outputText value="#{companySessionBean.company.address}"/>

 		<h:outputText value="#{msg.company_ceo}"/>
 		<h:outputText value="#{companySessionBean.company.ceo}"/>

 		<h:outputText value="#{msg.company_cfo}"/>
 		<h:outputText value="#{companySessionBean.company.cfo}"/>

 		<h:outputText value="#{msg.company_website}"/>
 		<h:outputText value="#{companySessionBean.company.website}"/>
 </h:panelGrid>
 	
