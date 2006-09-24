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


<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24px;
    }
</style>

<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<f:view>
    <h:form id="foo">

        <h:panelGrid columns="4">

            <h:commandButton id="structure-list-action" action="structure" value="#{manager.structure_button}"
                             styleClass="top-button-action"/>
            <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                             styleClass="top-button-action"/>
            <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                             styleClass="top-button-action"/>
            <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                             styleClass="top-button-action"/>

        </h:panelGrid>
    </h:form>
</f:view>