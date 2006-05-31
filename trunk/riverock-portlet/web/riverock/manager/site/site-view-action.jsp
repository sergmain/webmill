<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<f:subview id="site-site-extend-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteType}">
    <jsp:include page="site-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-site-panel">
    <h:commandButton id="edit-site-action" action="site-edit"
                     value="#{msg['edit_site_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-site-action" action="site-delete"
                     value="#{msg['delete_site_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
