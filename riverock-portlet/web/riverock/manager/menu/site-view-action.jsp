<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>

<f:subview id="site-site-extend-desc-subview" rendered="#{menuSessionBean.objectType == menuSessionBean.siteType}">

    <h:panelGrid columns="2">

        <h:outputText value="#{msg.site_name}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.siteName}"/>

        <h:outputText value="#{msg.site_company}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.company.name}"/>

        <h:outputText value="#{msg.site_locale}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.siteDefaultLocale}"/>

        <h:outputText value="#{msg.site_admin_email}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.adminEmail}"/>

        <h:outputText value="#{msg.site_user_can_register}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.registerAllowed}"/>

        <h:outputText value="#{msg.site_css_is_dynamic}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.cssDynamic}"/>

        <h:outputText value="#{msg.site_css_file}"/>
        <h:outputText value="#{menuSessionBean.siteExtended.site.cssFile}"/>

    </h:panelGrid>

</f:subview>

