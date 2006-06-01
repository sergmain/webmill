<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:outputText value="#{msg.template_info}"/>
<h:panelGrid columns="2">

    <h:outputText value="#{msg.template_name}"/>
    <h:inputText id="template-name-field" value="#{siteSessionBean.template.templateName}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.template_data}"/>
    <h:inputTextarea id="template-data-field" value="#{siteSessionBean.template.templateData}"
                     rows="20" cols="70"
        />
</h:panelGrid>





