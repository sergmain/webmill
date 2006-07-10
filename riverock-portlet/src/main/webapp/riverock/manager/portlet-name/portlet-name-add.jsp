<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .portlet-name-button-action {
        width: 150px;
        height: 22px;
    }
</style>


<f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="add-portlet-name-form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName and isUserInRole['webmill.portal-manager']}">

            <f:subview id="subviewPortletNameInfo">
                <jsp:include page="portlet-name-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup>
                <h:commandButton value="#{msg.add_process_action}" action="#{portletNameAction.processAddPortletName}"
                                 styleClass="portlet-name-button-action"/>
                <h:commandButton value="#{msg.add_cancel_action}" action="#{portletNameAction.cancelAddPortletName}"
                                 styleClass="portlet-name-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
