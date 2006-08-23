<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:panelGroup rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">
    <h:panelGrid columns="2">

        <h:outputText value="#{msg.news_portlet_name}"/>
        <h:outputText value="#{newsSessionBean.news.portletName.portletName}"/>

        <h:outputText value="#{msg.news_template_name}"/>
        <h:outputText value="#{newsSessionBean.news.template.templateName}"/>

        <h:outputText value="#{msg.news_order_field}"/>
        <h:outputText value="#{newsSessionBean.news.orderField}"/>

        <h:outputText value="#{msg.news_name}"/>
        <h:outputText value="#{newsSessionBean.news.keyMessage}"/>

        <h:outputText value="#{msg.news_url}"/>
        <h:outputText value="#{newsSessionBean.news.url}"/>

    </h:panelGrid>

    <h:panelGrid columns="1">
        <h:outputText value="#{msg.news_role}"/>
        <h:outputText value="#{newsSessionBean.news.portletRole}"/>
    </h:panelGrid>
</h:panelGroup>

