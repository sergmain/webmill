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

    .company-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="edit_company_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company and isUserInRole['webmill.portal-manager']}">

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
