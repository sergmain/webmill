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
<%--
<%@ taglib uri="http://sourceforge.net/projects/facestrace" prefix="ft"%>
--%>

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

    <h:form id="siteManagerForm">

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
                    <t:commandButton id="siteProcessActionButton" action="#{siteAction.processAddSiteAction}"
                                     value="#{msg['process_add_site_action']}"
                                     styleClass="site-button-action"
                                     forceId="true"
                        >
                    </t:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="site-add-cancel-action" action="#{siteAction.cancelAddSiteAction}"
                                     value="#{msg['cancel_add_site_action']}"
                                     styleClass="site-button-action"
                        immediate="true"
                        >
                    </h:commandButton>
                </h:panelGroup>
            </h:panelGrid>

        </h:panelGrid>

    </h:form>

<!--
    <ft:trace />
-->

</f:view>
