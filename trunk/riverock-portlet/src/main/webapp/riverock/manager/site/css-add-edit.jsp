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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:outputText value="#{msg.css_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.css_is_current}"/>
    <h:selectBooleanCheckbox id="select_css_checkbox" value="#{siteSessionBean.css.current}"/>

    <h:outputText value="#{msg.css_name}"/>
    <h:inputText id="css-name-field" value="#{siteSessionBean.css.cssComment}"/>

</h:panelGrid>

<h:panelGrid columns="1">
    <h:outputText value="#{msg.css_data}"/>
    <h:inputTextarea id="css-data-field" value="#{siteSessionBean.css.css}"
                     rows="20" cols="70"
        />
</h:panelGrid>



