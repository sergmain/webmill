<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<f:subview id="site-site-language-view-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteLanguageType}">
    <jsp:include page="site-language-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-site-language-panel">
    <h:commandButton id="edit-site-language-action" action="site-language-edit"
                     value="#{msg['edit_site_language_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-site-language-action" action="site-language-delete"
                     value="#{msg['delete_site_language_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
