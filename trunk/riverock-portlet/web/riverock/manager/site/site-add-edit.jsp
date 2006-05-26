<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

 <h:outputText value="Site info"/>
 <h:panelGrid columns="2">
     <h:outputText value="#{msg.site_name}"/>
     <h:inputText id="site-name-field" value="#{siteSessionBean.siteExtended.site.siteName}"/>

     <h:outputText value="#{msg.site_company}"/>
     <h:panelGroup id="select-company-group">
         <h:selectOneMenu id="select-one-company" value="#{siteSessionBean.siteExtended.site.companyId}"
                          styleClass="selectOneMenu" required="true"
             >
             <f:selectItems value="#{siteService.companyList}"/>
         </h:selectOneMenu>
     </h:panelGroup>

     <h:outputText value="#{msg.site_locale}"/>
     <h:inputText id="site-locale-field" value="#{siteSessionBean.siteExtended.site.siteDefaultLocale}"/>

     <h:outputText value="#{msg.site_admin_email}"/>
     <h:inputText id="site-admin-email-field" value="#{siteSessionBean.siteExtended.site.adminEmail}"/>

     <h:outputText value="#{msg.site_user_can_register}"/>
     <h:selectBooleanCheckbox id="site-register-allowed-field" value="#{siteSessionBean.siteExtended.site.registerAllowed}"/>

     <h:outputText value="#{msg.site_css_is_dynamic}"/>
     <h:selectBooleanCheckbox id="site-css-dynamic-field" value="#{siteSessionBean.siteExtended.site.cssDynamic}"/>

     <h:outputText value="#{msg.site_css_file}"/>
     <h:inputText id="site-css-file-field" value="#{siteSessionBean.siteExtended.site.cssFile}"/>
 </h:panelGrid>

