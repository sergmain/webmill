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
             var="hostBean"
             value="#{siteSessionBean.siteExtended.virtualHosts}"
             preserveDataModel="true">

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msg.site_host_name}"/>
        </f:facet>
        <t:selectBooleanCheckbox forceId="true" id="defaultHostCheckbox" onclick="uncheckCheckboxes(this);" value="#{hostBean.defaultHost}"/>
        <h:outputText id="host-name" value="#{hostBean.host}"/>
    </h:column>

    <h:column>
        <f:facet name="header">
        </f:facet>
        <t:commandButton value="#{msg.delete_virtual_host_action}" action="#{siteAction.deleteVirtualHostAction}"
            styleClass="site-button-action"
            id="deleteVirtualHostButton"
            forceId="true" disabled="#{hostBean.defaultHost}">
            <t:updateActionListener property="#{siteSessionBean.currentVirtualHost}" value="#{hostBean.host}"/>
        </t:commandButton>
    </h:column>
</t:dataTable>

<f:verbatim>
<script type="text/javascript" language="JavaScript">
    function uncheckCheckboxes(checkbox) {
//        alert('checkbox: ' + checkbox.id+'\nchecked: ' + checkbox.checked);
        var fields = siteManagerForm.all;
        var count = countCheckedBoxes(fields);
        if (count==0) {
            checkbox.checked=true;
            return;
        }

//        var s = '';
//        alert('#1 count: ');
        for ( var i = 0; i < fields.length; i++ ) {
            if (fields[i].id!=undefined && fields[i].id!='') {
//                s += (fields[i].id + '\n');

                if (checkbox.checked) {
                    if (fields[i].id.indexOf('defaultHostCheckbox[')==0 ) {
                        if (fields[i].id!=checkbox.id) {
                            fields[i].checked=false;
                            disableButton(fields[i], false);
                        }
                        else {
                            disableButton(fields[i], true);
                        }
                    }
                }
                else {
                    if (fields[i].id==checkbox.id) {
                        disableButton(fields[i], false);
                    }
                    else if (fields[i].id.indexOf('defaultHostCheckbox[')==0 && fields[i].checked) {
                        disableButton(fields[i], true);
                    }
                }
            }
        }
        count = countCheckedBoxes(fields);
        if (count==1) {
            document.getElementById("siteProcessActionButton").disabled=false;
        }
//        alert(s);
    }

    function disableButton(formObject, isDisableButton) {
        var idx = formObject.id.substring('defaultHostCheckbox['.length, formObject.id.indexOf(']') );
        var button = document.getElementById("deleteVirtualHostButton["+idx+']');
        if (button!=undefined) {
            button.disabled=isDisableButton;
        }
    }

    function countCheckedBoxes(fields) {
        var count = 0;
        for ( var i = 0; i < fields.length; i++ ) {
            if (fields[i].id!=undefined && fields[i].id!='' && fields[i].id.indexOf('defaultHostCheckbox[')==0 &&
                fields[i].checked) {
                count++;
            }
        }
        return count;
    }

</script>
</f:verbatim>