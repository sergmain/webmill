<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<f:subview id="site-css-view-desc-subview" rendered="#{newsSessionBean.objectType == newsSessionBean.newsType}">
    <jsp:include page="news-item-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-css-panel">
    <h:commandButton id="edit-news-action" action="#{newsAction.editNewsAction}"
                     value="#{msg['edit_news_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-news-action" action="news-delete"
                     value="#{msg['delete_news_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
