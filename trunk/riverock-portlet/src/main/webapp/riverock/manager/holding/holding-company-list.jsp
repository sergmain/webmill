<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>

<f:verbatim><br/></f:verbatim>
<h:outputText value="#{msg['company_list']}" styleClass="standard_bold"/>
<f:verbatim><br/><br/></f:verbatim>

<h:selectOneMenu value="#{holdingDataProvider.currentHolding.newCompanyId}"
                 styleClass="selectOneMenu" required="false"
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
             var="companyBean"
             value="#{holdingDataProvider.currentHolding.companies}"
             preserveDataModel="true">

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg.company_name}"/>
        </f:facet>
        <h:outputText id="company-name" value="#{companyBean.name}"/>
    </h:column>

    <h:column>
        <t:commandButton value="#{msg.delete_company_action}" action="#{holdingAction.deleteCompanyAction}">
            <t:updateActionListener property="#{holdingSessionBean.currentCompanyId}" value="#{companyBean.id}"/>
        </t:commandButton>
    </h:column>
</t:dataTable>
