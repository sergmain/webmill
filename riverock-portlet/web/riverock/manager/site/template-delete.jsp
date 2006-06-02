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

            <h:panelGroup id="operation-template-delete-panel">

                <f:subview id="delete-template-subview">
                    <jsp:include page="template-description.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_template_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:commandButton id="template-delete-process-action" action="#{templateAction.processDeleteTemplateAction}"
                                 value="#{msg['process_delete_template_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
                <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                <h:commandButton id="template-delete-cancel-action" action="#{templateAction.cancelDeleteTemplateAction}"
                                 value="#{msg['cancel_delete_template_action']}"
                                 styleClass="site-button-action"
                    >
                </h:commandButton>
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
