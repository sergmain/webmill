<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.cms.resource.Manager" var="manager"/>

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
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="news-top-actions-subview">
            <jsp:include page="news-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="1" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}" >

            <h:panelGrid columns="2">

                <f:subview id="site-tree-subview">                                        l
                    <jsp:include page="news-tree.jsp"/>
                </f:subview>

                <f:subview id="site-right-subview">
                    <jsp:include page="news-right-panel.jsp"/>
                </f:subview>

            </h:panelGrid>


        </h:panelGrid>


    </h:form>
</f:view>
