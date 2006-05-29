<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .site-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
	<h:form>

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="portlet-name-list-action" action="portlet-name" value="#{manager.portlet_name_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                         styleClass="top-button-action"/>

    <h:panelGrid columns="1">
	 	 
	 	<h:panelGrid columns="2">
	 	
             <f:subview id="site-tree-subview" rendered="#{!siteSessionBean.edit}">
                 <jsp:include page="site-tree.jsp"/>
             </f:subview>

             <f:subview id="site-right-subview">
                 <jsp:include page="site-right-panel.jsp"/>
             </f:subview>

         </h:panelGrid>
	 	
	 	
	 </h:panelGrid>
	 
	 
	
</h:form>
</f:view>
