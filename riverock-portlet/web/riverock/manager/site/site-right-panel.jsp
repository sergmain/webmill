<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="site-site-extend-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteType}">
    <jsp:include page="site-extend-description.jsp"/>
</f:subview>
