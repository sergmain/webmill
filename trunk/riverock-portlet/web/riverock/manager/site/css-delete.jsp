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
        width: 150px;
        height: 22px;
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
	
	<h:panelGrid columns="1">
	 	 
	 	<h:panelGrid columns="2">
	 	
             <f:subview id="site-tree-subview" rendered="#{!siteSessionBean.edit}">
                 <jsp:include page="site-tree.jsp"/>
             </f:subview>

             <h:panelGroup id="css-delete-panel">

                 <h:panelGroup id="operation-css-delete-panel">
                     <h:commandButton id="css-delete-process-action" action="#{cssAction.processDeleteCssAction}"
                                      value="#{msg['process_delete_css_action']}" immediate="true"
                                      styleClass="site-button-action"
                         >
                     </h:commandButton>
                     <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                     <h:commandButton id="css-delete-cancel-action" action="#{cssAction.cancelDeleteCssAction}"
                                      value="#{msg['cancel_delete_css_action']}" immediate="true"
                                      styleClass="site-button-action"
                         >
                     </h:commandButton>
                 </h:panelGroup>
             </h:panelGroup>


         </h:panelGrid>
	 </h:panelGrid>
	 
	 
	
</h:form>
</f:view>
