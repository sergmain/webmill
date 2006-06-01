<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

 <h:outputText value="#{msg.site_language_info}"/>
 <h:panelGrid columns="2">
     <h:outputText value="#{msg.site_language_name}"/>
     <h:inputText id="site-language-name-field" value="#{siteSessionBean.siteLanguage.nameCustomLanguage}"/>

     <h:outputText value="#{msg.site_language_locale}"/>
     <h:inputText id="site-language-locale-field" value="#{siteSessionBean.siteLanguage.customLanguage}"/>

 </h:panelGrid>
