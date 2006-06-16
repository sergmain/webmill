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

