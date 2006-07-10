<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<f:subview id="site-css-view-desc-subview" rendered="#{menuSessionBean.objectType == menuSessionBean.menuItemType}">
    <jsp:include page="menu-item-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-css-panel">
    <h:commandButton id="edit-menu-item-action" action="#{menuAction.editMenuItemAction}"
                     value="#{msg['edit_menu_item_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-menu-item-action" action="menu-delete"
                     value="#{msg['delete_menu_item_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
