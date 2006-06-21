<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <h:outputText value="#{msg.menu_item_portlet_name}"/>
    <h:outputText value="#{menuSessionBean.menuItem.portletName.portletName}"/>

    <h:outputText value="#{msg.menu_item_template_name}"/>
    <h:outputText value="#{menuSessionBean.menuItem.template.templateName}"/>

    <h:outputText value="#{msg.menu_item_order_field}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.orderField}"/>

    <h:outputText value="#{msg.menu_item_name}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.keyMessage}"/>

    <h:outputText value="#{msg.menu_item_url}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.url}"/>

    <h:outputText value="#{msg.menu_item_title}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.title}"/>

    <h:outputText value="#{msg.menu_item_author}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.author}"/>

    <h:outputText value="#{msg.menu_item_keyword}"/>
    <h:outputText value="#{menuSessionBean.menuItem.menuItem.keyword}"/>

</h:panelGrid>


