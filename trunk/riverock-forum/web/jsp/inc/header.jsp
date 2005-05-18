<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="session" uri="/tld/jakarta-session" %>

<c:set var="tableBgColor" value="#999999" scope="request"/>
<c:set var="thBgColor" value="#162F49" scope="request"/>
<c:set var="thTextColor" value="#CDCDCD" scope="request"/>
<c:set var="tdBgColor" value="#F1F1F1" scope="request"/>
<c:set var="tdBgColor2" value="#E3E3E3" scope="request"/>

<style type="text/css">
tr.forum-th {
	border:solid 1px black;
	BACKGROUND-COLOR: #162F49;
	COLOR: #CDCDCD;
}

td.forum-th {
	border:solid 1px #667799;
	BACKGROUND-COLOR: #162F49;
	COLOR: #CDCDCD;
}

td.forum-td {
	border:solid 1px #667799;
	BACKGROUND-COLOR: #F1F1F1;
}

td.forum-td2 {
	border:solid 1px #667799;
	BACKGROUND-COLOR: #E3E3E3;
}

		BODY {
			SCROLLBAR-BASE-COLOR: #597794;
			SCROLLBAR-ARROW-COLOR: #f5f5f5;
			FONT-SIZE: 9pt;
		}
		TR    {FONT-SIZE: 9pt}
		TD    {FONT-SIZE: 9pt}
		A:link  {
			COLOR: #000000;
			TEXT-DECORATION: none;
		}
		A:visited {
			COLOR: #000000;
			TEXT-DECORATION: none;
		}
		A:active {
			COLOR: #000000;
			TEXT-DECORATION: none;
		}
		A:hover {
			COLOR: #000000;
			TEXT-DECORATION: underline;
		}
		input {
			BACKGROUND-COLOR: #cfcfcf
		}
		SELECT {
			BACKGROUND-COLOR: #cfcfcf
		}
		TEXTAREA {
			BACKGROUND-COLOR: #cfcfcf
		}
	</style>

<table width="758" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#999999">
  <tr>
	<td bgcolor="#e2e3e4">
		<TABLE width="95%" border="0" align="center" height="80">
			<TR>
				<TD>
<%--                          <a href="<c:out value='${genericBean.forumHomeUrl}'/>">--%>
<%--                             <IMG SRC="/forum/img/lb_logo.gif" WIDTH="253" HEIGHT="32" BORDER="0" ALT="">--%>
<%--                          </a>--%>
                </TD>
				<TD align="right">
                     <c:choose>
                         <c:when test="${empty principal}">
                            <c:if test="${!empty genericBean.loginUrl}">
                            <A HREF="<c:out value='${genericBean.loginUrl}'/>">Login</A>&nbsp;|&nbsp;
                            </c:if>
                            <c:if test="${!empty genericBean.registerUrl}">
                            <A HREF="<c:out value='${genericBean.registerUrl}'/>">Register</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:when>
                         <c:otherwise>
                            <c:if test="${!empty genericBean.logoutUrl}">
                               <A HREF="<c:out value='${genericBean.logoutUrl}'/>">Logout</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:otherwise>
                     </c:choose>

                     <c:if test="${!empty genericBean.membersUrl}">
                     <A HREF="<c:out value='${genericBean.membersUrl}'/>">Members</A>&nbsp;|&nbsp;
                     </c:if>

                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=help">Help</A>&nbsp;|&nbsp;
                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>">Home</A>
				</TD>
			</TR>
		</TABLE>


