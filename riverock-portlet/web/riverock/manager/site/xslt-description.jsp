<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGrid columns="2">

    <h:outputText value="Name:"/>
    <h:outputText value="#{siteDataProvider.xslt.name}"/>

    <h:outputText value="Is current:"/>
    <h:outputText value="#{siteDataProvider.xslt.current}"/>

</h:panelGrid>

<h:panelGroup id="operation-xslt-panel">
    <h:commandButton id="edit-xslt-action" action="xslt-edit"
                     value="#{msg['edit_xslt_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-xslt-action" action="xslt-delete"
                     value="#{msg['delete_xslt_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
