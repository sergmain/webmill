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
