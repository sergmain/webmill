<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<style type="text/css">
td { vertical-align: top; }
</style>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

 <h:outputText value="#{msg.portlet_name_info}"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.portlet_name_id}"/>
 		<h:outputText value="#{portletNameSessionBean.portletName.id}"/>

 		<h:outputText value="#{msg.portlet_name_name}"/>
 		<h:outputText value="#{portletNameSessionBean.portletName.name}"/>
 </h:panelGrid>
 	
