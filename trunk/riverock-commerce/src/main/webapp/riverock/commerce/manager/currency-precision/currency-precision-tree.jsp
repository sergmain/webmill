<%--
  ~ org.riverock.commerce - Commerce application
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

<h:panelGroup id="currency-precision-tree-group">

    <t:tree2 id="serverTree" value="#{currencyPrecisionTree.currencyPrecisionTree}" var="node"
   			varNodeToggler="t" clientSideToggle="false"
            showRootNode="false">
        <f:facet name="tree-root">
            <h:panelGroup id="currency-precision-tree-tree-root-group">
            </h:panelGroup>
        </f:facet>
        <f:facet name="shop">
            <h:panelGroup id="currency-precision-tree-shop-group">
                <h:commandLink id="select-shop-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{currencyPrecisionAction.selectShop}"
                    >

                    <t:graphicImage id="currency-precision-tree-sile-image-open" value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                    <t:graphicImage id="currency-precision-tree-sile-image-close" value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>

                    <h:outputText id="currency-precision-tree-shop-name" value="#{node.description}"/>

                    <t:updateActionListener property="#{currencyPrecisionSessionBean.currentShopId}" value="#{node.identifier}" />
                    <t:updateActionListener property="#{currencyPrecisionSessionBean.objectType}" value="#{currencyPrecisionSessionBean.shopType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
        <f:facet name="currency-precision">
            <h:panelGroup id="currency-precision-tree-currency-precision-group">
                <h:commandLink id="select-currency-precision-action-id" styleClass="#{t.nodeSelected ? 'documentSelected':''}"
                               action="#{currencyPrecisionAction.selectCurrencyPrecision}"
                    >

                    <t:graphicImage id="currency-precision-tree-currency-precision-image" value="/images/user.png" border="0"/>
                    <h:outputText id="currency-precision-tree-currency-precision-name" value="#{node.description}"/>
                    <h:outputText id="currency-precision-tree-currency-precision-counter" value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>

                    <t:updateActionListener property="#{currencyPrecisionSessionBean.currentCurrencyPrecisionId}" value="#{node.identifier}"/>
                    <t:updateActionListener property="#{currencyPrecisionSessionBean.objectType}" value="#{currencyPrecisionSessionBean.currencyPrecisionType}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>

    </t:tree2>
</h:panelGroup>
