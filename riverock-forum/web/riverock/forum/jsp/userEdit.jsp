<%@page language="java" contentType="text/html; charset=utf-8" import="org.riverock.forum.util.*,org.riverock.forum.bean.*"%>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td>
			<b><IMG SRC="/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">&nbsp;<a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>&nbsp;&gt;&nbsp;Edit Profile</b>
		</td>
	</tr>
</table>
<BR>
<!--user-->
<SCRIPT LANGUAGE="JavaScript">
<!--
var submitFlag=false;

function sCheck(){
	if(document.FormPost.u_password.value.length<3
		||document.FormPost.u_email.value.length<8
		||document.FormPost.u_email.value.indexOf("@")<=0){
		alert("Invalid input!");
		return false;
	}
	if(submitFlag){		
		alert('Please wait...');
		return false;
	}else{
		submitFlag=true;
	}
	FormPost.submit();
}

function changegAvatar(){   	document.images.avatar.src="/riverock/forum/img/avatars/"+document.FormPost.u_avatar_id.options[document.FormPost.u_avatar_id.selectedIndex].value+".gif";
}

//-->
</SCRIPT>

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
	<tr class="forum-th">
		<td align="center"  colspan="2"><font color="<%=TH_TEXT_COLOR%>">Edit Profile</font></td>
	</tr>
	<tr class="forum-td">
		<td>
			<BR>
			<table align="center" border="0" >
			<form name="FormPost" method="post" action="userEditP.do">
				<tr>
					<td>User Name</td>
					<td><%=userBean.getU_name()%></td>
				</tr>
				<tr>
					<td>Avatar</td>
					<td>

					<select name="u_avatar_id" size=1 onChange="changegAvatar()">
						  <% for (int i=0;i<13;i++){%>
								<option value="<%=i%>" <%if (i==userBean.getU_avatar_id()) {%>selected<%}%>>&nbsp;<%=i%></option>
						  <%}%>
					</select>
					<img name="avatar" src="/riverock/forum/img/avatars/<%=userBean.getU_avatar_id()%>.gif"  width=36 height=36 border=0 >

					</td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input name="u_password" type="text"size="15" maxlength="15" value="<%=userBean.getU_password()%>"></td>
				</tr>
				<tr>
					<td  >Email</td>
					<td ><input name="u_email" type="text" size="30" maxlength="40" value="<%=userBean.getU_email()%>"></td>
				</tr>
				<tr>
				<td  >Address</td>
					<td><input name="u_address" type="text"size="30" maxlength="40" value="<%=userBean.getU_address()%>"></td>
				</tr>
				<tr>
				<td  >Sign</td>
					<td>
					<TEXTAREA NAME="u_sign" ROWS="3" COLS="30"><%=userBean.getU_sign()%></TEXTAREA></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input name="" type="button" value="OK" onclick="sCheck();">
					&nbsp;&nbsp; 
					<input name="" type="reset" value="NG"></td>
				</tr>
			</form>
			</table>
			<BR>
		</td>
	</tr>
</table>

<%@include file="inc/footer.jsp"%>
