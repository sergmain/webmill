<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 150px;
        height: 22px;
    }

    .portlet-name-button-action {
        width: 150px;
        height: 22px;
    }
</style>


<f:view>
    <h:form id="delete-portlet-name-form">

        <h:panelGrid columns="1" rendered="#{!empty portletNameSessionBean.portletName}">

            <f:subview id="subviewPortletNameInfo">
                <jsp:include page="portlet-name-info.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.delete_confirm_action}"
                                 action="#{portletNameAction.processDeletePortletName}"
                                 styleClass="portlet-name-button-action"/>
                <h:commandButton value="#{msg.delete_cancel_action}" action="portlet-name"
                                 styleClass="portlet-name-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
