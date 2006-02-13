<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

 <f:loadBundle basename="org.riverock.portlet.company.resource.Company" var="msg"/>	

 <h:outputText value="Company info"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg['company_name']}"/>
 		<h:inputText id="company-name-field" value="#{companySessionBean.company.name}"/>
 		<h:outputText value="#{msg.company_short_name}"/>
 		<h:inputText id="company-short-name-field" value="#{companySessionBean.company.shortName}"/>
 		<h:outputText value="#{msg.company_address}"/>
 		<h:inputText id="company-address-field" value="#{companySessionBean.company.address}"/>
 		<h:outputText value="#{msg.company_ceo}"/>
 		<h:inputText id="company-ceo-field" value="#{companySessionBean.company.ceo}"/>
 		<h:outputText value="#{msg.company_cfo}"/>
 		<h:inputText id="company-cfo-field" value="#{companySessionBean.company.cfo}"/>
 		<h:outputText value="#{msg.company_website}"/>
 		<h:inputText id="company-website-field" value="#{companySessionBean.company.website}"/>
 </h:panelGrid>
 	
