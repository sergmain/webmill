<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>

<h:panelGrid columns="2">

    <h:outputText value="Name:"/>
    <h:outputText value="#{siteDataProvider.siteExtended.site.siteName}"/>

    <h:outputText value="Company"/>
    <h:outputText value="#{siteDataProvider.siteExtended.company.name}"/>

    <h:outputText value="Locale"/>
    <h:outputText value="#{siteDataProvider.siteExtended.siteDefaultLocale}"/>

    <h:outputText value="Admin E-Mail"/>
    <h:outputText value="#{siteDataProvider.siteExtended.site.adminEmail}"/>

    <h:outputText value="User can register"/>
    <h:outputText value="#{siteDataProvider.siteExtended.site.registerAllowed}"/>

    <h:outputText value="CSS is dynamic"/>
    <h:outputText value="#{siteDataProvider.siteExtended.site.cssDynamic}"/>

    <h:outputText value="CSS file"/>
    <h:outputText value="#{siteDataProvider.siteExtended.site.cssFile}"/>

</h:panelGrid>

<h:panelGrid columns="1">

    <h:outputText value="#{msg['virtual_host_list']}" styleClass="standard_bold"/>
    <t:dataList id="virtual-host-list"
                styleClass="standardList"
                var="host"
                value="#{siteDataProvider.siteExtended.virtualHosts}"
                layout="orderedList" forceId="true">
        <h:outputText value="#{host.host}"/>
    </t:dataList>

</h:panelGrid>

<h:panelGroup id="operation-site-panel">
    <h:commandButton id="edit-site-action" action="site-edit"
                     value="#{msg['edit_site_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-site-action" action="site-delete"
                     value="#{msg['delete_site_action']}"
                     styleClass="site-button-action"
        >
    </h:commandButton>
</h:panelGroup>
