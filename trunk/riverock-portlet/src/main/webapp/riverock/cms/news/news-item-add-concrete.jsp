<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:panelGrid columns="1">

    <h:outputText value="#{msg.news_header}"/>
    <h:inputText id="news-header-field" value="#{newsSessionBean.news.newsHeader}" size="50"/>

    <h:outputText value="#{msg.news_anons}"/>
    <h:inputTextarea id="news-anons-field" value="#{newsSessionBean.news.newsAnons}"
                     rows="8" cols="70"
        />

    <h:outputText value="#{msg.news_text}"/>
    <h:inputTextarea id="news-text-field" value="#{newsSessionBean.news.newsText}"
                     rows="12" cols="70"
        />

</h:panelGrid>




