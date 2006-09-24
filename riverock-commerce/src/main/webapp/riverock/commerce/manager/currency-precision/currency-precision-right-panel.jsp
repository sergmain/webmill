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


<f:subview id="currency-precision-shop-view-action-subview"
 rendered="#{currencyPrecisionSessionBean.objectType == currencyPrecisionSessionBean.shopType and !empty currencyPrecisionSessionBean.shopExtendedBean and
	isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">

    <jsp:include page="shop-view-action.jsp"/>
</f:subview>

<f:subview id="currency-precision-currency-precision-view-action-subview"
 rendered="#{currencyPrecisionSessionBean.objectType == currencyPrecisionSessionBean.currencyPrecisionType and !empty currencyPrecisionSessionBean.currencyPrecisionBean and
	isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">

    <jsp:include page="currency-precision-view-action.jsp"/>
</f:subview>


