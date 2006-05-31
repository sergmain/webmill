<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>

<f:verbatim><br/></f:verbatim>
<h:outputText value="#{msg['company_list']}" styleClass="standard_bold"/>
<f:verbatim><br/><br/></f:verbatim>

<h:selectOneMenu value="#{holdingDataProvider.currentHolding.newCompanyId}"
                 styleClass="selectOneMenu" required="true"
    >
    <f:selectItems value="#{holdingDataProvider.companyList}"/>
</h:selectOneMenu>

<t:commandButton value="#{msg.holding_add_company_action}" action="#{holdingAction.addCompanyAction}"/>

<f:verbatim><br/></f:verbatim>

<t:dataTable id="company-edit-list"
             styleClass="standardTable"
             headerClass="standardTable_Header"
             rowClasses="standardTable_Row1,standardTable_Row2"
             columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
             var="company"
             value="#{holdingDataProvider.currentHolding.companies}"
             preserveDataModel="true">

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg.company_name}"/>
        </f:facet>
        <h:outputText id="company-name" value="#{company.name}"/>
    </h:column>

    <h:column>
        <t:commandButton value="#{msg.delete_company_action}" actionListener="#{holdingAction.deleteCompanyActionListener}">
            <t:updateActionListener property="#{holdingSessionBean.currentCompanyId}" value="#{company.id}"/>
        </t:commandButton>
    </h:column>
</t:dataTable>
