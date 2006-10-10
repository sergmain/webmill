<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.riverock.org
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community,
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

<f:loadBundle basename="org.riverock.webmill.admin.resource.Company" var="msg"/>
<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24px;
    }

    .company-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:view>
    <h:form id="add_company_form">

        <h:panelGrid columns="1" rendered="#{!empty companySessionBean.company}">

            <f:subview id="subviewCompanyInfo">
                <jsp:include page="company-add-edit.jsp"/>
            </f:subview>

            <h:panelGroup id="editDeleteControls">
                <h:commandButton value="#{msg.action_add_process}" action="#{companyAction.processAddCompany}"
                                 styleClass="company-button-action"/>
                <h:commandButton value="#{msg.action_add_cancel}" action="#{companyAction.cancelAddCompany}"
                                 styleClass="company-button-action"/>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>