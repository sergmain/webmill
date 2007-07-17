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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://webmill.riverock.org/portlet" prefix="w" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:outputText value="#{msg.xslt_info}"/>
<h:panelGrid columns="2">
    <h:outputText value="#{msg.xslt_is_current}"/>
    <h:selectBooleanCheckbox id="select_xslt_checkbox" value="#{siteSessionBean.xslt.current}"/>

    <h:outputText value="#{msg.xslt_name}"/>
    <h:inputText id="xslt-name-field" value="#{siteSessionBean.xslt.name}"/>

</h:panelGrid>

<h:panelGrid columns="1" style="width:100%">
    <h:outputText value="#{msg.xslt_data}"/>
    <h:inputTextarea id="xslt-data-field" value="#{siteSessionBean.xslt.xsltData}"
                     rows="20" cols="70" style="width:100%"
        >
        <w:textValidator type="xml"/>
    </h:inputTextarea>
</h:panelGrid>



