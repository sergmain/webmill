<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="site-site-extend-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteType}">
    <jsp:include page="site-extend-description.jsp"/>
</f:subview>

<f:subview id="site-site-language-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteLanguageType}">
    <jsp:include page="site-language-description.jsp"/>
</f:subview>

<f:subview id="site-template-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.templateType}">
    <jsp:include page="template-description.jsp"/>
</f:subview>

<f:subview id="site-css-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.cssType}">
    <jsp:include page="css-description.jsp"/>
</f:subview>

<f:subview id="site-xslt-desc-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.xsltType}">
    <jsp:include page="xslt-description.jsp"/>
</f:subview>

