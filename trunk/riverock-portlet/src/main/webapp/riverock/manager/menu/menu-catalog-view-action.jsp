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

<f:subview id="site-menu-catalog-view-desc-subview" rendered="#{menuSessionBean.objectType == menuSessionBean.menuCatalogType}">
    <jsp:include page="menu-catalog-description.jsp"/>
</f:subview>

<h:panelGroup id="operation-menu-catalog-panel">
    <h:commandButton id="edit-menu-catalog-action" action="menu-catalog-edit"
                     value="#{msg['edit_menu_catalog_action']}"
                     styleClass="menu-button-action"
        >
    </h:commandButton>
    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
    <h:commandButton id="delete-menu-catalog-action" action="menu-catalog-delete"
                     value="#{msg['delete_menu_catalog_action']}"
                     styleClass="menu-button-action"
        >
    </h:commandButton>
</h:panelGroup>
