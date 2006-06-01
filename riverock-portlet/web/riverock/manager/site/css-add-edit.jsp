<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:outputText value="#{msg.css_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.css_is_current}"/>
    <h:selectBooleanCheckbox id="select_css_checkbox" value="#{siteSessionBean.css.current}"/>

    <h:outputText value="#{msg.css_name}"/>
    <h:inputText id="css-name-field" value="#{siteSessionBean.css.cssComment}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.css_data}"/>
    <h:inputTextarea id="css-data-field" value="#{siteSessionBean.css.css}"
                     rows="20" cols="70"
        />
</h:panelGrid>



