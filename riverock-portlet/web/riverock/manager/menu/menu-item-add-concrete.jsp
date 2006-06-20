<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:outputText value="#{msg.menu_item_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{menu.menu_item_portlet_name}"/>
    <h:panelGroup id="select-portlet-name-group">
        <h:selectOneMenu id="select-one-portlet-name" value="#{menuSessionBean.menuItem.menuItem.portletId}"
                         styleClass="selectOneMenu" required="true"
            >
            <f:selectItems value="#{menuService.portletList}"/>
        </h:selectOneMenu>
    </h:panelGroup>

    <h:outputText value="#{menu.menu_item_order_field}"/>
    <h:inputText id="menu-item-order-field" value="#{menuSessionBean.menuItem.menuItem.orderField}"/>

    <h:outputText value="#{menu.menu_item_name}"/>
    <h:inputText id="css-name-field" value="#{menuSessionBean.menuItem.menuItem.keyMessage}"/>

    <h:outputText value="#{menu.menu_item_url}"/>
    <h:inputText id="css-name-field" value="#{menuSessionBean.menuItem.menuItem.url}"/>

    <h:outputText value="#{menu.menu_item_title}"/>
    <h:inputText id="css-name-field" value="#{menuSessionBean.menuItem.menuItem.title}"/>

    <h:outputText value="#{menu.menu_item_author}"/>
    <h:inputText id="css-name-field" value="#{menuSessionBean.menuItem.menuItem.author}"/>

    <h:outputText value="#{menu.menu_item_keywords}"/>
    <h:inputText id="css-name-field" value="#{menuSessionBean.menuItem.menuItem.keyword}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{menu.menu_item_metadata}"/>
    <h:inputTextarea id="css-data-field" value="#{menuSessionBean.menuItem.menuItem.metadata}"
                     rows="20" cols="70"
        />
</h:panelGrid>



