<?xml version="1.0" encoding="windows-1251"?>

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

					<a href="/member/auth/logout.jsp" class="mainMenu" title="���������� ������">�����</a>

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

