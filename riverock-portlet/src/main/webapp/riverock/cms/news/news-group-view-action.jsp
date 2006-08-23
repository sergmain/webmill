<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<f:subview id="site-news-group-view-desc-subview" rendered="#{newsSessionBean.objectType == newsSessionBean.newsGroupType}">
    <jsp:include page="news-group-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-news-group-panel">
    <h:commandButton id="edit-news-group-action" action="news-group-edit"
                     value="#{msg['edit_news_group_action']}"
                     styleClass="news-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-news-group-action" action="news-group-delete"
                     value="#{msg['delete_news_group_action']}"
                     styleClass="news-button-action"
        >
    </h:commandButton>
</h:panelGroup>
