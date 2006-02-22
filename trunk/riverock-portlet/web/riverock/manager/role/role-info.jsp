<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Role" var="msg"/>	

 <h:outputText value="#{msg.role_role_info}"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.role_role_id}"/>
 		<h:outputText value="#{roleSessionBean.role.roleId}"/>
 		<h:outputText value="#{msg.role_name}"/>
 		<h:outputText value="#{roleSessionBean.role.name}"/>
 </h:panelGrid>
 	
