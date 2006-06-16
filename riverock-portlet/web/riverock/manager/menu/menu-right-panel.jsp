<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="menu-menu-item-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.menuItemType and !empty menuSessionBean.menuItem">

    <jsp:include page="menu-view-action.jsp"/>
</f:subview>

<f:subview id="menu-menu-catalog-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.menuCatalogType and !empty menuSessionBean.menuCatalog and
	isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.menu']}">

    <jsp:include page="menu-catalog-view-action.jsp"/>
</f:subview>

<f:subview id="menu-site-view-action-subview"
 rendered="#{menuSessionBean.objectType == menuSessionBean.siteType and !empty menuSessionBean.siteExtended and
	isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.site,webmill.css,webmill.template,webmill.xslt']}">

    <jsp:include page="site-view-action.jsp"/>
</f:subview>


