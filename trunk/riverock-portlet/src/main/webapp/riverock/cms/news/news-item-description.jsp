<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:panelGroup rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">
    <h:panelGrid columns="1">

        <h:outputText value="#{msg.news_header}"/>
        <h:outputText value="#{newsSessionBean.news.newsHeader}"/>

        <h:outputText value="#{msg.news_anons}"/>
        <h:outputText value="#{newsSessionBean.news.newsAnons}"/>

        <h:outputText value="#{msg.news_text}"/>
        <h:outputText value="#{newsSessionBean.news.newsText}"/>

    </h:panelGrid>
</h:panelGroup>

