<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~ Riverock - The Open-source Java Development Community,
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
