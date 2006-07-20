<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~ Riverock - The Open-source Java Development Community,
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.webmill.admin.resource.Site" var="msg"/>

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