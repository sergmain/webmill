<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="news-news-view-action-subview"
 rendered="#{newsSessionBean.objectType == newsSessionBean.newsType and !empty newsSessionBean.news and
	isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">

    <jsp:include page="news-item-view-action.jsp"/>
</f:subview>

<f:subview id="news-news-group-view-action-subview"
 rendered="#{newsSessionBean.objectType == newsSessionBean.newsGroupType and !empty newsSessionBean.newsGroup and
	isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">

    <jsp:include page="news-group-view-action.jsp"/>
</f:subview>

<f:subview id="news-site-view-action-subview"
 rendered="#{newsSessionBean.objectType == newsSessionBean.siteType and !empty newsSessionBean.siteExtended and
	isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">

    <jsp:include page="site-view-action.jsp"/>
</f:subview>


