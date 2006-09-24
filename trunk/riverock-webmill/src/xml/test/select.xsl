<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ org.riverock.webmill - Portal framework implementation
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
  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html" omit-xml-declaration="yes"/>
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="node()"/>
	</xsl:template>

<!--
				<img>
					<xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
					<xsl:attribute name="border"><xsl:value-of select="@border"/></xsl:attribute>
					<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
					<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
<xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute>
<xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute>
</img>
-->
	<xsl:template match="SelectElement">
	<select name="{@name}">
			<xsl:for-each select="Option">
			<option>
					<xsl:if test="@isSelected='true'">
					<xsl:attribute name="SELECTED"/>
					</xsl:if>
					<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
					<xsl:value-of select="Text"/>
			</option>
			</xsl:for-each>
	</select>
	</xsl:template>
	<!--
			<xsl:apply-templates select="PriceFieldName"/>
			<xsl:for-each select="PriceItem">
				<tr>
					<td class="priceData{ItemInBasket}">
						<table>
							<tr>
								<td valign="top">
<img src="{ItemImageFileName}" alt="image"/>
								</td>
								<td valign="top">
									<xsl:value-of select="ItemName"/>
								</td>
							</tr>
						</table>
					</td>
					<td class="priceData{ItemInBasket}" align="right">
						<xsl:value-of select="ItemPrice"/>
					</td>
					<td class="priceData{ItemInBasket}">
						<xsl:value-of select="ItemNameCurrency"/>
					</td>
					<xsl:if test="//ShopPage/@isProcessInvoice='true'">
						<form action="{ItemToInvoice}">
							<xsl:apply-templates select="HiddenParam"/>
							<td class="priceData{ItemInBasket}" nowrap="0">
								<input type="text" size="3" value="1" name="{ItemToInvoiceCountParam}"/>
								<input type="submit" value="+"/>
							</td>
						</form>
					</xsl:if>
				</tr>
			</xsl:for-each>
		</table>
		-->
</xsl:stylesheet>
