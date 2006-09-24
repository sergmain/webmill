<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
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


<f:subview id="menu-menu-item-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.menuItemType and !empty menuSessionBean.menuItem and
	isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <jsp:include page="menu-item-view-action.jsp"/>
</f:subview>

<f:subview id="menu-menu-catalog-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.menuCatalogType and !empty menuSessionBean.menuCatalog and
	isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <jsp:include page="menu-catalog-view-action.jsp"/>
</f:subview>

<f:subview id="menu-site-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.siteType and !empty menuSessionBean.siteExtended and
	isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <jsp:include page="site-view-action.jsp"/>
</f:subview>


