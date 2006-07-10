<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<f:subview id="site-menu-catalog-view-desc-subview" rendered="#{menuSessionBean.objectType == menuSessionBean.menuCatalogType}">
    <jsp:include page="menu-catalog-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-menu-catalog-panel">
    <h:commandButton id="edit-menu-catalog-action" action="menu-catalog-edit"
                     value="#{msg['edit_menu_catalog_action']}"
                     styleClass="menu-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-menu-catalog-action" action="menu-catalog-delete"
                     value="#{msg['delete_menu_catalog_action']}"
                     styleClass="menu-button-action"
        >
    </h:commandButton>
</h:panelGroup>
