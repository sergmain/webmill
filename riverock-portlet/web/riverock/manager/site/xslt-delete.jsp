<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

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
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{!siteDataProvider.xslt.current and isUserInRole['webmill.site-manager']}">

        <f:subview id="site-top-actions-subview">
            <jsp:include page="site-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="site-tree-subview">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="operation-xslt-delete-panel">

                <f:subview id="delete-xslt-subview">
                    <jsp:include page="xslt-description.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_xslt_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:commandButton id="xslt-delete-process-action" action="#{xsltAction.processDeleteXsltAction}"
                                 value="#{msg['process_delete_xslt_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
                <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                <h:commandButton id="xslt-delete-cancel-action" action="#{xsltAction.cancelDeleteXsltAction}"
                                 value="#{msg['cancel_delete_xslt_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
    <h:outputText value="#{msg.can_not_delete_current_xslt}" rendered="#{siteDataProvider.xslt.current}"/>
</f:view>
