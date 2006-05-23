<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 150px;
        height: 22px;
    }

    .company-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>

<f:view>
    <h:form id="edit-company-form">

        <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

            <f:subview id="subviewCompanyInfo">
                <jsp:include page="company-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.action_edit_save}" action="#{action.processEditCompany}"
                                 styleClass="company-button-action"/>
                <h:commandButton value="#{msg.action_edit_cancel}" action="#{action.cancelEditCompany}"
                                 styleClass="company-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
