<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGrid columns="2" 
>

    <h:outputText value="#{msg.site_name}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.siteName}"/>

    <h:outputText value="#{msg.site_company}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.company.name}"/>

    <h:outputText value="#{msg.site_locale}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.siteDefaultLocale}"/>

    <h:outputText value="#{msg.site_admin_email}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.adminEmail}"/>

    <h:outputText value="#{msg.site_user_can_register}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.registerAllowed}"/>

    <h:outputText value="#{msg.site_css_is_dynamic}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.cssDynamic}"/>

    <h:outputText value="#{msg.site_css_file}"/>
    <h:outputText value="#{siteSessionBean.siteExtended.site.cssFile}"/>

</h:panelGrid>

<h:panelGrid columns="1">

    <h:outputText value="#{msg['virtual_host_list']}" styleClass="standard_bold"/>
    <t:dataList id="virtual-host-list"
                styleClass="standardList"
                var="host"
                value="#{siteSessionBean.siteExtended.virtualHosts}"
                layout="orderedList" forceId="true">
        <h:outputText value="#{host}"/>
    </t:dataList>

</h:panelGrid>

