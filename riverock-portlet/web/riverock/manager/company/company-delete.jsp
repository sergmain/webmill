<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>

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

<f:view>
    <h:form id="delete-company-form">

        <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

            <f:subview id="subviewCompanyInfo">
                <jsp:include page="company-info.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.action_delete_confirm}" action="#{action.processDeleteCompany}"
                                 styleClass="company-button-action"/>
                <h:commandButton value="#{msg.action_delete_cancel}" action="company"
                                 styleClass="company-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
