<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>

<h:panelGroup>
 <h:outputText value="#{msg.portal_user_info}"/>
 <h:panelGrid columns="2">
 		<h:outputText value="#{msg.portal_user_id}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.userId}"/>

 		<h:outputText value="#{msg.portal_user_company_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.companyName}"/>

 		<h:outputText value="#{msg.portal_user_created_date}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.createdDate}"/>

 		<h:outputText value="#{msg.portal_user_first_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.firstName}"/>

 		<h:outputText value="#{msg.portal_user_middle_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.middleName}"/>

 		<h:outputText value="#{msg.portal_user_last_name}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.lastName}"/>

 		<h:outputText value="#{msg.portal_user_address}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.address}"/>

 		<h:outputText value="#{msg.portal_user_phone}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.phone}"/>

 		<h:outputText value="#{msg.portal_user_email}"/>
 		<h:outputText value="#{portalUserSessionBean.portalUser.email}"/>
 </h:panelGrid>
 	
</h:panelGroup>
