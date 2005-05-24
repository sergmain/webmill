<!--forum.jsp-->
<%@page language="java" contentType="text/html; charset=utf-8" import="org.riverock.forum.util.*,org.riverock.forum.bean.*,java.util.Iterator"%>
<%@include file="inc/header.jsp" %>

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
	<tr>
		<td  >
			<b><IMG SRC="/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">&nbsp;<a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>&nbsp;&gt;&nbsp;User List</b>
		</td>
	</tr>
</table>
<BR>
<!--users-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
  <tr class="forum-th">
	<td width="15%" ><font color="<%=TH_TEXT_COLOR%>">User Name</font></td>
	<td width="7%"><font color="<%=TH_TEXT_COLOR%>">Avatar</font></td>
	<td width="15%"><font color="<%=TH_TEXT_COLOR%>">Role</font></td>
	<td width="7%" ><font color="<%=TH_TEXT_COLOR%>">Email</font></td>
	<td width="15%" ><font color="<%=TH_TEXT_COLOR%>">Register Time</font></td>
	<td width="20%"><font color="<%=TH_TEXT_COLOR%>">Address</font></td>
	<td width="6%"><font color="<%=TH_TEXT_COLOR%>">Posts</font></td>
	<td width="15%"><font color="<%=TH_TEXT_COLOR%>">Last post time</font></td>
  </tr>
<!--loop begin-->
<%
Iterator users=userListBean.getUsers().iterator();
while(users.hasNext()){
	User user=(User)users.next();
%>
  <tr>
	<td width="15%" class="forum-td">
    <%=StringTools.encodeXml(user.getU_name())%></td>
	<td width="7%" bgcolor="<%=TD_BGCOLOR2%>"><img src="/riverock/forum/img/avatars/<%=user.getU_avatar_id()%>.gif" width=36 height=36 border="0"></td>
	<td width="15%" class="forum-td"><%=user.getR_name()%></td>
	<td width="7%" bgcolor="<%=TD_BGCOLOR2%>"><A HREF="mailto:
    <%=StringTools.encodeXml(user.getU_email())%>"><IMG SRC="/riverock/forum/img/email.gif" WIDTH="16" HEIGHT="16" BORDER=0 ALT="Email"></A></td>
	<td width="15%" class="forum-td"><%=DateTimeUtil.shortFmt(user.getU_regtime())%></td>
	<td width="20%" bgcolor="<%=TD_BGCOLOR2%>">
    <%=StringTools.encodeXml(user.getU_address())%></td>
	<td width="6%"  class="forum-td"><%=user.getU_post()%></td>
	<td width="15%" bgcolor="<%=TD_BGCOLOR2%>"><%=DateTimeUtil.shortFmt(user.getU_lasttime())%></td>
  </tr>

<%
}
%>
<!--loop end-->
	<TR>
		<TD colspan="8" class="forum-td">
			<!--split page-->
            <%
                String param = userListBean.getKeyword();
                String keyword= (param == null ? "" : param.trim());
				int start=userListBean.getStart();
				int range=userListBean.getRange();
				int count=userListBean.getCount();
			%>
			<TABLE width="100%" border="0"  cellpadding="0" cellspacing="0">
			<TR>
				<TD width="20%">
				<%if(start>range){%><A HREF="userList.do?start=<%=start-range%>&keyword=<%=keyword%>">PREV</A><%}else{%>PREV<%}%>
				</TD>
				<TD align="center">
				Users:&nbsp;<%=count%>&nbsp;&nbsp;RPP:&nbsp;<%=range%>&nbsp;&nbsp;Start:&nbsp;<%=start%>
				</TD>
				<TD width="20%" ALIGN="right">
				<%if(start+range<=count){%><A HREF="userList.do?start=<%=start+range%>&keyword=<%=keyword%>">NEXT</A><%}else{%>NEXT<%}%>
				</TD>
			</TR>
			</TABLE>
		</TD>
	</TR>
</table>
<!-- search bar -->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
<FORM NAME="formSearch" METHOD="post" ACTION="userList.do">
	<tr>
		<td align="right">
			   Search:
			  <INPUT TYPE="text" NAME="keyword" SIZE="10" MAXLENGTH="10" VALUE="<%=keyword%>">
			  <INPUT TYPE="submit" VALUE="GO">
		</td>
	</tr>
</FORM>
</table>

<%@include file="inc/footer.jsp"%>