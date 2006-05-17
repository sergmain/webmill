<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


    <f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>

<h:panelGrid columns="2">

	<h:outputText value="Name:"/>
	<h:outputText value="#{siteDataProvider.siteExtended.site.siteName}" />

	<h:outputText value="Locale"/>
	<h:outputText value="#{siteDataProvider.siteExtended.siteDefaultLocale}" />

</h:panelGrid>

<h:panelGrid columns="1">

    <h:outputText value="#{msg['role_list']}" styleClass="standard_bold" />
    <t:dataList id="virtual-host-list"
        styleClass="standardList"
        var="host"
        value="#{siteDataProvider.siteExtended.virtualHosts}"
        layout="orderedList" forceId="true">
        <h:outputText value="#{host.host}" />
    </t:dataList>

</h:panelGrid>