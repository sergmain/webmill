<%--
  ~ org.riverock.commerce - Commerce application
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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.commerce.resource.StandardCurrency" var="msg"/>

    <h:commandButton value="#{msg.add_standard_currency_action}" action="#{standardCurrencyAction.addStandardCurrency}" style=" width : 106px; height : 22px;"/>

                <t:dataTable id="standardCurrencyDataTable"
                        var="standardCurrencyBean"
                        value="#{standardCurrencyService.standardCurrencyList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_standard_currency_name}" />
                       </f:facet>
                       <t:commandLink action="#{standardCurrencyAction.selectStandardCurrency}" immediate="true" >
                            <h:outputText value="#{standardCurrencyBean.standardCurrencyName}" />
                            <t:updateActionListener property="#{standardCurrencySessionBean.currentStandardCurrencyId}" value="#{standardCurrencyBean.standardCurrencyId}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
