<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

 <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>	

 <h:outputText value="Role info"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.role_id}"/>
 		<h:outputText value="#{roleSessionBean.role.id}"/>
 		<h:outputText value="#{msg.role_name}"/>
 		<h:outputText value="#{roleSessionBean.role.name}"/>
 </h:panelGrid>
 	
