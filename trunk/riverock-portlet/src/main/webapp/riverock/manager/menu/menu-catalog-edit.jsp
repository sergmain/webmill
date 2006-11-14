<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Menu" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .menu-button-action {
        width: 150px;
        height: 22px;
    }

    .menu-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="menu-top-actions-subview">
            <jsp:include page="menu-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="menu-tree-subview">
                <jsp:include page="menu-tree.jsp"/>
            </f:subview>


            <h:panelGrid columns="1" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

                <f:subview id="subview-menu-catalog-info">
                    <jsp:include page="menu-catalog-add-edit.jsp"/>
                </f:subview>

                <h:panelGroup id="operation-menu-catalog-edit-panel">
                    <h:commandButton id="menu-catalog-edit-process-action" action="#{menuCatalogAction.processEditMenuCatalogAction}"
                                     value="#{msg['process_edit_menu_catalog_action']}"
                                     styleClass="menu-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="menu-catalog-edit-cancel-action" action="#{menuCatalogAction.cancelEditMenuCatalogAction}"
                                     value="#{msg['cancel_edit_menu_catalog_action']}"
                                     styleClass="menu-button-action"
                                     immediate="true"
                        >
                    </h:commandButton>
                </h:panelGroup>

            </h:panelGrid>
        </h:panelGrid>

    </h:form>
</f:view>
