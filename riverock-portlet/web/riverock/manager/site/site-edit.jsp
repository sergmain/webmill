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

            <f:subview id="site-tree-subview" rendered="#{!siteSessionBean.edit}">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGrid columns="1">

                <f:subview id="subview-site-info">
                    <jsp:include page="site-add-edit.jsp"/>
                </f:subview>

                <h:panelGroup id="operation-site-edit-panel">
                    <h:commandButton id="site-edit-process-action" action="#{siteAction.processEditSiteAction}"
                                     value="#{msg['process_edit_site_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="site-edit-cancel-action" action="#{siteAction.cancelEditSiteAction}"
                                     value="#{msg['cancel_edit_site_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                </h:panelGroup>
            </h:panelGrid>

        </h:panelGrid>

    </h:form>
</f:view>
