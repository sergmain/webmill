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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.webmill.admin.resource.PortalUser" var="msg"/>

    <h:commandButton value="#{msg.portal_user_add_portal_user_action}" action="#{portalUserAction.addPortalUser}" style=" width : 106px; height : 22px;"/>

                <t:dataTable id="portalUserDataTable"
                        var="portalUserBean"
                        value="#{portalUserService.portalUserList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_portal_user_name}" />
                       </f:facet>
                       <t:commandLink action="#{portalUserAction.selectPortalUser}" immediate="true" >
                            <h:outputText value="#{portalUserBean.name}" />
                            <t:updateActionListener property="#{portalUserSessionBean.currentPortalUserId}" value="#{portalUserBean.userId}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
