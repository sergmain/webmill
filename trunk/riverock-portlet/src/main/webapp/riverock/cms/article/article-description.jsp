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


<f:loadBundle basename="org.riverock.portlet.cms.resource.Article" var="msg"/>

<h:panelGroup rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.article-manager']}">
    <h:panelGrid columns="1">

        <h:outputText value="#{msg.article_is_xml}"/>
        <h:outputText value="#{articleSessionBean.article.xml}"/>

        <h:outputText value="#{msg.article_name}"/>
        <h:outputText value="#{articleSessionBean.article.articleName}"/>

        <h:outputText value="#{msg.article_code}"/>
        <h:outputText value="#{articleSessionBean.article.articleCode}"/>

        <h:outputText value="#{msg.article_text}"/>
        <h:outputText value="#{articleSessionBean.article.articleText}"/>

    </h:panelGrid>
</h:panelGroup>

