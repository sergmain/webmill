<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Company" var="msg"/>

<t:dataTable id="companyDataTable"
             var="company"
             value="#{companyService.companyList}"
             preserveDataModel="true">
    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg.header_table_company_name}"/>
        </f:facet>
        <t:commandLink action="#{action.selectCompany}" immediate="true">
            <h:outputText value="#{company.name}"/>
            <!-- for convenience: MyFaces extension. sets id of current row in countryForm -->
            <!-- you don't have to implement a custom action! -->
            <t:updateActionListener property="#{companySessionBean.currentCompanyId}" value="#{company.id}"/>
        </t:commandLink>
    </h:column>

</t:dataTable>
