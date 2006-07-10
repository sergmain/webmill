<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:outputText value="#{msg.menu_catalog_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.menu_catalog_code}"/>
    <h:inputText id="select_menu_catalog_input_field" value="#{menuSessionBean.menuCatalog.catalogCode}"/>

</h:panelGrid>




