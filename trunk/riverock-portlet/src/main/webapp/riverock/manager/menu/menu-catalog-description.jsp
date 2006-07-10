<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <h:outputText value="Code:"/>
    <h:outputText value="#{menuDataProvider.menuCatalog.catalogCode}"/>
</h:panelGrid>

