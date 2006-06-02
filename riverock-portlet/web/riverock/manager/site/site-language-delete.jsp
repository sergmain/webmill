<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .site-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:form>

        <f:subview id="site-top-actions-subview">
            <jsp:include page="site-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="site-tree-subview">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="operation-site-language-delete-panel">

                <f:subview id="delete-site-language-subview">
                    <jsp:include page="site-language-description.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_site_language_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:commandButton id="site-language-delete-process-action" action="#{siteLanguageAction.processDeleteSiteLanguageAction}"
                                 value="#{msg['process_delete_site_language_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
                <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                <h:commandButton id="site-language-delete-cancel-action" action="#{siteLanguageAction.cancelDeleteSiteLanguageAction}"
                                 value="#{msg['cancel_delete_site_language_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
