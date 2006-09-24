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

<f:loadBundle basename="org.riverock.webmill.admin.resource.PortalUser" var="msg"/>
<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<style type="text/css">
    td {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24px;
    }

    .portal-user-button-action {
        width: 150px;
        height: 22px;
    }
</style>

<f:view>

    <h:form id="portal_user_form">

        <f:subview id="portal-user-top-actions-subview">
            <jsp:include page="_portal-user-top-action.jsp"/>
        </f:subview>

        <h:panelGrid rendered="#{empty portalUserService.companyList}">
            <h:outputText id="empty-company-list" value="Before of creating of user, you must create a company"/>
        </h:panelGrid>

        <h:panelGrid columns="2" rendered="#{!empty portalUserService.companyList}">

            <f:subview id="subviewPortalUserList">
                <jsp:include page="portal-user-list.jsp"/>
            </f:subview>

            <h:panelGroup>
                <h:panelGrid columns="1" rendered="#{!empty portalUserSessionBean.portalUser}">

                    <f:subview id="subviewPortalUserInfo">
                        <jsp:include page="portal-user-info.jsp"/>
                    </f:subview>

                    <h:panelGroup id="editDeleteControls">
                    </h:panelGroup>

                </h:panelGrid>
            </h:panelGroup>

        </h:panelGrid>
    </h:form>

</f:view>
