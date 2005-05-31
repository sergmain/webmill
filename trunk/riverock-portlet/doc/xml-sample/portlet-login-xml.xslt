<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" omit-xml-declaration="no"/>
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="node()"/>
	</xsl:template>
	<xsl:template match="Login">
		<xsl:choose>
			<xsl:when test="@isLogged=1">
				<table border="0">
					<tr>
						<td>
							<xsl:value-of select="InviteMessage"/>
						</td>
					</tr>
					<tr>
						<td>
            Username: <xsl:value-of select="UserName"/>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<form method="POST" action="{ActionUrl}">
					<input type="hidden" name="mill.context" value="{PortletName}"/>
					<input type="hidden" name="mill.tourl" value="{ToUrl}"/>
					<table border="0" cellspacing="0" cellpadding="2" align="center">
						<tr>
							<th>
								<xsl:value-of select="InviteMessage"/>
							</th>
						</tr>
						<tr>
							<td>
								<table border="0">
									<tr>
										<td align="right">
											<xsl:value-of select="LoginMessage"/>
										</td>
										<td/>
										<td>
											<input type="text" name="mill.username" tabindex="1"/>
										</td>
										<td align="left" valing="top" cellspan="2">
											<input type="submit" name="button" value="{ButtonMessage}" tabindex="3"/>
										</td>
									</tr>
									<tr>
										<td align="right">
											<xsl:value-of select="PasswordMessage"/>
										</td>
										<td/>
										<td aling="left">
											<input type="password" name="mill.password" value="" tabindex="2"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
