<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
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

<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>


<f:verbatim><br/></f:verbatim>
<h:outputText value="#{msg['role_list']}" styleClass="standard_bold"/>
<f:verbatim><br/><br/></f:verbatim>

<h:selectOneMenu value="#{dataProvider.currentUser.newRoleId}"
                 styleClass="selectOneMenu" required="false"
    >
    <f:selectItems value="#{dataProvider.roleList}"/>
</h:selectOneMenu>

<t:commandButton value="#{msg['add_role']}" action="#{authUserAction.addRoleAction}"
                 styleClass="auth-sub-button-action"
    >
</t:commandButton>

<f:verbatim><br/></f:verbatim>

<t:dataTable id="role-edit-list"
             styleClass="standardTable"
             headerClass="standardTable_Header"
             rowClasses="standardTable_Row1,standardTable_Row2"
             columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
             var="roleBean"
             value="#{dataProvider.currentUser.currentRoles}"
             preserveDataModel="true">

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg['role_name']}"/>
        </f:facet>
        <h:outputText id="role-name" value="#{roleBean.name}"/>
    </h:column>

    <h:column>
        <t:commandButton value="#{msg['delete_role']}" action="#{authUserAction.deleteRoleAction}"
                         styleClass="auth-sub-button-action"
            >
            <t:updateActionListener property="#{userSessionBean.currentRoleId}" value="#{roleBean.roleId}"/>
        </t:commandButton>
    </h:column>
</t:dataTable>
