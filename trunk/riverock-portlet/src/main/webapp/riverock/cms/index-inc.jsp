<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .company-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<h:panelGrid columns="2">

    <h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">
            <h:commandButton id="news-list-action" action="news" value="#{manager.news_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.news_info}" style="font-size:10px"/>
        </h:panelGrid>
    </h:panelGroup>
    <h:panelGroup>
        <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.article-manager']}">
            <h:commandButton id="article-list-action" action="article" value="#{manager.article_button}"
                             styleClass="top-button-action"/>
            <h:outputText value="#{manager.article_info}" style="font-size:10px"/>
        </h:panelGrid>
    </h:panelGroup>


</h:panelGrid>

