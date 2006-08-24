<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:outputText value="#{msg.news_group_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.news_group_name}"/>
    <h:inputText id="news_group_name_field" value="#{newsSessionBean.newsGroup.newsGroupName}"/>

    <h:outputText value="#{msg.news_group_code}"/>
    <h:inputText id="news_group_code_field" value="#{newsSessionBean.newsGroup.newsGroupCode}"/>

    <h:outputText value="#{msg.news_group_max_news}"/>
    <h:inputText id="news_group_max_news_field" value="#{newsSessionBean.newsGroup.maxNews}"/>

    <h:outputText value="#{msg.news_group_order_value}"/>
    <h:inputText id="news_group_order_field" value="#{newsSessionBean.newsGroup.orderValue}"/>

</h:panelGrid>




