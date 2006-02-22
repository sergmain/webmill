<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	
    <f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
<h:form id="foo">

        <h:commandButton id="auth-list-action" action="auth" value="#{manager.auth_button}"/>
        <h:commandButton id="role-list-action" action="role" value="#{manager.role_button}"/>
        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"/>

<f:verbatim><table borser="0" width="100%"><tr><td width="300" valign="top"></f:verbatim>


        <f:subview id="auth-tree-subview" rendered="#{!userSessionBean.edit && !holdingSessionBean.delete && !holdingSessionBean.add}">
            <jsp:include page="holding-list.jsp"/>
        </f:subview>


<f:verbatim></td><td valign="top"></f:verbatim>


<h:panelGroup rendered="#{!empty holdingDataProvider.currentUser}">


        <f:subview id="select-user-subview" rendered="#{!holdingSessionBean.edit && !holdingSessionBean.delete && !holdingSessionBean.add}">
            <jsp:include page="auth-user-select.jsp"/>
        </f:subview>

</h:panelGroup>


<f:verbatim></td></tr></table></f:verbatim>



</h:form>


</f:view>

