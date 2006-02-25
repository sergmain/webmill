<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
 
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
TD { vertical-align: top; }
</style>

<f:view>
<h:form id="foo">

        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>
        <h:commandButton id="holding-list-action" action="holding" value="#{manager.holding_button}"/>

   <h:panelGrid columns="2"> 

        <f:subview id="auth-tree-subview" rendered="#{!userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
            <jsp:include page="auth-tree.jsp"/>
        </f:subview>

        <f:subview id="select-user-subview" rendered="#{!empty dataProvider.currentUser && !userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
            <jsp:include page="auth-user-select.jsp"/>
        </f:subview>

   </h:panelGrid>

</h:form>
</f:view>

