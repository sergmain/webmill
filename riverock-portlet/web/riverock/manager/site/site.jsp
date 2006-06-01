<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

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

        <h:panelGrid columns="1">

            <h:panelGrid columns="2">

                <f:subview id="site-tree-subview" rendered="#{!siteSessionBean.edit}">
                    <jsp:include page="site-tree.jsp"/>
                </f:subview>

                <f:subview id="site-right-subview">
                    <jsp:include page="site-right-panel.jsp"/>
                </f:subview>

            </h:panelGrid>


        </h:panelGrid>


    </h:form>
</f:view>
