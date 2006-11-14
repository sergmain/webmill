<?xml version="1.0" encoding="windows-1251"?>
<!--
  ~ org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
  ~ For more information, please visit project site http://webmill.riverock.org
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" omit-xml-declaration="no"/>
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<!--
-->
	<xsl:template match="MemberMenu">
		<table class="menu" cellspacing="2">
			<tr>
				<xsl:for-each select="MemberApplication">
					<td id="menu{ApplicationRecordNumber}" class="menu" onMouseOver="showSub('sub{ApplicationRecordNumber}', this.id, true);" onMouseOut="showSub('sub{ApplicationRecordNumber}', this.id, false);" nowrap="1">
						<a href="#" class="mainMenu">
							<xsl:value-of select="MemberApplicationName"/>
						</a>
						<br/>
						<table id="sub{ApplicationRecordNumber}" class="subMenu" cellpadding="2" cellspacing="0">
							<xsl:apply-templates/>
						</table>
					</td>
				</xsl:for-each>
				<td id="menuExit" class="menu" onMouseOver="{this.className='menuOver'}" onMouseOut="{this.className='menu'}">
					<a href="/member/auth/logout.jsp" class="mainMenu" title="Завершение работы">Выход</a>
				</td>
				<td width="100%" style="border:none">
					<img id="logo" src="/source/logo.gif" width="150px" height="40"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="MemberApplicationName"/>
	<xsl:template match="ApplicationRecordNumber"/>
	<xsl:template match="ModuleRecordNumber"/>
	<xsl:template match="ApplRecordNumber"/>
	<xsl:template match="MemberModule">
		<tr>
			<td id="subItem{ApplRecordNumber}_{ModuleRecordNumber}" class="subMenu" onMouseOver="subStyle(this.id, true);" onMouseOut="subStyle(this.id, false);" onClick="clickAct(this.id);">
				<a href="{ModuleUrl}" class="subMenu">
					<xsl:value-of select="ModuleName"/>
				</a>
			</td>
		</tr>
	</xsl:template>
	<!--
<xsl:template name="nbsp-ref">
      <xsl:text>#160;</xsl:text>
</xsl:template>
-->
</xsl:stylesheet>
