<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<f:subview id="site-template-view-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.templateType}">
    <jsp:include page="template-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-template-panel">
    <h:commandButton id="edit-template-action" action="template-edit"
                     value="#{msg['edit_template_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-template-action" action="template-delete"
                     value="#{msg['delete_template_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
