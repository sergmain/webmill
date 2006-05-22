<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGrid columns="2">

    <h:outputText value="Name:"/>
    <h:outputText value="#{siteDataProvider.css.cssComment}"/>

    <h:outputText value="Date create:"/>
    <h:outputText value="#{siteDataProvider.css.date}"/>

    <h:outputText value="Is current:"/>
    <h:outputText value="#{siteDataProvider.css.current}"/>
</h:panelGrid>

<h:panelGroup id="operation-css-panel">
    <h:commandButton id="edit-css-action" action="css-edit"
                     value="#{msg['edit_css_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-css-action" action="css-delete"
                     value="#{msg['delete_css_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
