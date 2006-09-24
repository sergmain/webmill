<%--
  ~ org.riverock.commerce - Commerce application
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

<f:loadBundle basename="org.riverock.portlet.shop.resource.Commerce" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }
</style>

<f:view>
    <h:form id="foo" enctype="multipart/form-data">

        <h:panelGrid columns="1">

            <h:outputText value="#{msg.xml_format_warning}" styleClass="standard_bold"/>

            <t:inputFileUpload id="fileupload"
                               value="#{priceUploadForm.upFile}"
                               storage="file"
                               styleClass="fileUploadInput"
                               required="true"/>

            <h:commandButton action="#{priceUploadForm.upload}" value="#{msg.upload_action}"/>

            <h:panelGroup rendered="#{priceUploadForm.uploaded}">
                <h:outputText value="#{msg.success_upload}" rendered="#{priceUploadForm.success}"/>
                <h:outputText value="#{msg.error_upload}" rendered="#{not priceUploadForm.success}"/>
            </h:panelGroup>

        </h:panelGrid>

        <h:panelGrid columns="1" rendered="#{fileUploadForm.uploaded}">
            <h:outputText value="filename: #{fileUploadForm.name}"/>
        </h:panelGrid>

    </h:form>
</f:view>

