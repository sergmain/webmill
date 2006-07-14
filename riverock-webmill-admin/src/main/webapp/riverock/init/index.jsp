<%--
  ~ org.riverock.webmill.init - Webmill portal initializer web application
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


<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }
</style>

<f:loadBundle basename="org.riverock.webmill.init.resource.Manager" var="manager"/>

<f:view>
    <h:form id="foo">

        <h:panelGrid columns="3">

            <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                             styleClass="top-button-action"/>
            <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                             styleClass="top-button-action"/>
            <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                             styleClass="top-button-action"/>

        </h:panelGrid>
    </h:form>
</f:view>