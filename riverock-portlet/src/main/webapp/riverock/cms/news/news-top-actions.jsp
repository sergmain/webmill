<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.cms.resource.Manager" var="manager"/>

<h:commandButton id="article-list-action" action="article" value="#{manager.article_button}"
                 styleClass="top-button-action" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.article-manager']}"/>
