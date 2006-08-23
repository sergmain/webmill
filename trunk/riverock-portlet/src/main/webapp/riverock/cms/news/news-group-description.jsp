<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">
    <h:outputText value="Name:"/>
    <h:outputText value="#{newsDataProvider.newsGroup.newsGroupName}"/>
    <h:outputText value="Code:"/>
    <h:outputText value="#{newsDataProvider.newsGroup.newsGroupCode}"/>
    <h:outputText value="Max news:"/>
    <h:outputText value="#{newsDataProvider.newsGroup.maxNews}"/>
    <h:outputText value="Order value:"/>
    <h:outputText value="#{newsDataProvider.newsGroup.orderValue}"/>
</h:panelGrid>

