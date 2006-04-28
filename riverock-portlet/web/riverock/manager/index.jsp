<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<f:view>
<h:form id="foo">

        <f:subview id="select-index-subview" >
            <jsp:include page="index-inc.jsp"/>
        </f:subview>

</h:form>
</f:view>

