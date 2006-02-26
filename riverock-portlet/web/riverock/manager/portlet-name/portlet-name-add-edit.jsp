<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

 <h:outputText value="Portlet name info"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.portlet_name_name}"/>
 		<h:inputText id="portlet--name-name-field" value="#{portletNameSessionBean.portletName.name}"/>
 </h:panelGrid>
 	
