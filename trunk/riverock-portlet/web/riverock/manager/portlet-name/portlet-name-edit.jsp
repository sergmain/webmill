<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

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


<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="edit-portlet-name-form" rendered="#{isUserInRole['webmill.portal-manager']}">

        <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName}">

            <f:subview id="subviewPortletNameInfo">
                <jsp:include page="portlet-name-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.edit_save_action}" action="#{portletNameAction.processEditPortletName}"
                                 styleClass="portlet-name-button-action"/>
                <h:commandButton value="#{msg.edit_cancel_action}" action="#{portletNameAction.cancelEditPortletName}"
                                 styleClass="portlet-name-button-action"/>
            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>
