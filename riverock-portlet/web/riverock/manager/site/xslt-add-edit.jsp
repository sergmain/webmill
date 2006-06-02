<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:outputText value="#{msg.xslt_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.xslt_is_current}"/>
    <h:selectBooleanCheckbox id="select_xslt_checkbox" value="#{siteSessionBean.xslt.current}"/>

    <h:outputText value="#{msg.xslt_name}"/>
    <h:inputText id="xslt-name-field" value="#{siteSessionBean.xslt.name}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.xslt_data}"/>
    <h:inputTextarea id="xslt-data-field" value="#{siteSessionBean.xslt.xsltData}"
                     rows="20" cols="70"
        />
</h:panelGrid>



