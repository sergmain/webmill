<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:subview id="site-site-view-action-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteType and !empty siteSessionBean.siteExtended}">
    <jsp:include page="site-view-action.jsp"/>
</f:subview>

<f:subview id="site-site-language-view-action-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.siteLanguageType and !empty siteSessionBean.siteLanguage}">
    <jsp:include page="site-language-view-action.jsp"/>
</f:subview>

<f:subview id="site-template-view-action-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.templateType and !empty siteSessionBean.template}">
    <jsp:include page="template-view-action.jsp"/>
</f:subview>

<f:subview id="site-css-view-action-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.cssType and !empty siteSessionBean.css}">
    <jsp:include page="css-view-action.jsp"/>
</f:subview>

<f:subview id="site-xslt-view-action-subview" rendered="#{siteSessionBean.objectType == siteSessionBean.xsltType and !empty siteSessionBean.xslt}">
    <jsp:include page="xslt-view-action.jsp"/>
</f:subview>

