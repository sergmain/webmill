<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

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

             <h:panelGroup id="operation-xslt-delete-panel">
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
	 	
	 	
	 </h:panelGrid>
	 
	 
	
</h:form>
</f:view>
