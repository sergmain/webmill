<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGrid columns="2">

    <h:outputText value="Name:"/>
    <h:outputText value="#{siteDataProvider.siteLanguage.nameCustomLanguage}"/>

    <h:outputText value="Locale:"/>
    <h:outputText value="#{siteDataProvider.siteLanguage.customLanguage}"/>

</h:panelGrid>

