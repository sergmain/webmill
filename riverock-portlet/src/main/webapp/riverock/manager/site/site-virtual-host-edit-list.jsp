<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<f:verbatim><br/></f:verbatim>
<h:outputText value="#{msg['virtual_host_list']}" styleClass="standard_bold"/>
<f:verbatim><br/><br/></f:verbatim>

<h:inputText id="site-new-virtual-host-field" value="#{siteSessionBean.newVirtualHost}"/>

<t:commandButton value="#{msg.site_add_virtual_host_action}" action="#{siteAction.addVirtualHostAction}"
    styleClass="site-button-action"/>

<f:verbatim><br/></f:verbatim>

<t:dataTable id="virtual-host-list"
             styleClass="standardTable"
             headerClass="standardTable_Header"
             rowClasses="standardTable_Row1,standardTable_Row2"
             columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
             var="host"
             value="#{siteSessionBean.siteExtended.virtualHosts}"
             preserveDataModel="true">

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg.site_host_name}"/>
        </f:facet>
        <h:outputText id="host-name" value="#{host}"/>
    </h:column>

    <h:column>
        <f:facet name="header">
        </f:facet>
        <t:commandButton value="#{msg.delete_virtual_host_action}" actionListener="#{siteAction.deleteVirtualHostActionListener}"
            styleClass="site-button-action">
            <t:updateActionListener property="#{siteSessionBean.currentVirtualHost}" value="#{host}"/>
        </t:commandButton>
    </h:column>
</t:dataTable>
