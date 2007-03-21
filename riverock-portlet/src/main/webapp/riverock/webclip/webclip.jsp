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
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<portlet:defineObjects/>

<%--
<script language="JavaScript" type="text/javascript">
var portletReq;

// Generic AJAX function
function asynchGet(updateURL){
    if (window.XMLHttpRequest) {
        portletReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        portletReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
    portletReq.onreadystatechange = processReqChange;
    portletReq.open("GET", updateURL, true);
    portletReq.send(null);
}

// Generic AJAX function
// process the response when available
function processReqChange() {
    if (portletReq.readyState == 4) {
        if (portletReq.status == 200) {
            // process response
            displayInvoice();
        }
    }
}

function selectInvoice(evt) {
    evt = (evt) ? evt : ((window.event) ? window.event : null);
    if (evt) {
        var select = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
        if (select && select.options.length > 1) {
            // When invoice is selected, call underlying web-app to return
            // content.
            // For this sample, the content is hard-coded HTML, but in a real
            // deployment a call to servlet or JSP in the web-app can be
            // used to return content from some other back-end store.
            asynchGet("/AJAXPortlet/InvoiceServlet?invoice=" + select.value);
        }
    }
}

function displayInvoice() {
    // substitute new invoice HTML content into "portletcontent" <div> tag
    var div = document.getElementById("portletcontent");
    div.innerHTML = "";
    div.innerHTML = portletReq.responseText;

}
</script>

<%
    // hard-coded invoice numbers
    String[] invoiceList = {"439089", "439090", "439091", "439092"};
%>

<select onchange="selectInvoice(event)" size="1">
    <option>Select an invoice...</option>
<%
    for (String anInvoiceList : invoiceList) {
%>
    <option value="<%=anInvoiceList%>"><%=anInvoiceList%>
    </option>
    <%
        }
    %>
</select>
<div id="portletcontent"></div>
--%>

<%
    Object locale = request.getAttribute("request-locale");

    if (!(locale instanceof String) || StringUtils.isBlank((String)locale)) {
        locale = "en";
    }
    pageContext.setAttribute("locale", locale);
%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:setBundle basename="org.riverock.portlet.resource.Webclip" scope="request"/>


<table border="0">
    <%
        if (request.isUserInRole("webmill.webclip-manager") ||
            request.isUserInRole("webmill.portal-manager")) {
    %>
    <portlet:actionURL var="portletUrl"/>
    <form method="POST" action="<c:out value='${portletUrl}'/>">
        <tr>
            <td width="25%"><fmt:message key="webclip_url"/></td>
            <td colspan="3" width="50%"><input type="text" name="sourceUrl" size="100" value="<c:out value='${sourceUrl}'/>"></td>
        </tr>
        <tr>                              
            <td width="25%"><fmt:message key="webclip_href_start_page"/></td>
            <td width="25%"><input type="text" name="hrefStartPart" size="30" value="<c:out value='${hrefStartPart}'/>"></td>
            <td width="25%"><fmt:message key="webclip_new_href_prefix"/></td>
            <td width="25%"><input type="text" name="newHrefPrefix" size="30" value="<c:out value='${newHrefPrefix}'/>"></td>
        </tr>
        <tr>
            <td colspan="4"><input style="width:150px;" type="submit" name="save" value="<fmt:message key='save_webclip_action'/>"></td>
        </tr>
        <tr>
            <td colspan="4"><c:out value='${webclipError}'/></td>
        </tr>
        <tr>
            <td colspan="4">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="4"><input style="width:150px;" type="submit" name="refresh" value="<fmt:message key='refresh_webclip_data_action'/>" ></td>
        </tr>
    </form>
    <%
        }
    %>
</table>

<c:out value="${webclipErrorMessage}" escapeXml="true"/>

<c:out value="${webclipBean.webclipData}" escapeXml="false"/>



