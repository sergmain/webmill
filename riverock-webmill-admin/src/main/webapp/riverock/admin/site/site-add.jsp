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

<f:loadBundle basename="org.riverock.webmill.admin.resource.Site" var="msg"/>
<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24;
    }

    .site-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>

    <h:form>

        <f:subview id="site-top-actions-subview">
            <jsp:include page="site-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="site-tree-subview">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGrid columns="1">

                <f:subview id="subview-site-info">
                    <jsp:include page="site-add-edit.jsp"/>
                </f:subview>

                <h:panelGroup id="operation-site-add-panel">
                    <h:commandButton id="site-add-process-action" action="#{siteAction.processAddSiteAction}"
                                     value="#{msg['process_add_site_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="site-add-cancel-action" action="#{siteAction.cancelAddSiteAction}"
                                     value="#{msg['cancel_add_site_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                </h:panelGroup>
            </h:panelGrid>

        </h:panelGrid>

    </h:form>
</f:view>