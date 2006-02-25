<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
TD { vertical-align: top; }
</style>

<f:view>
<h:form id="foo">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"/>
        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>

    <h:panelGrid columns="2">

        <f:subview id="holding-subview" rendered="#{!userSessionBean.edit && !holdingSessionBean.delete && !holdingSessionBean.add}">
            <jsp:include page="holding-list.jsp"/>
        </f:subview>


        <f:subview id="select-holding-subview" rendered="#{!empty holdingDataProvider.currentHolding && !holdingSessionBean.edit && !holdingSessionBean.delete && !holdingSessionBean.add}">
            <jsp:include page="holding-select.jsp"/>
        </f:subview>

    </h:panelGrid>

</h:form>
</f:view>

