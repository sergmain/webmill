<!--
/*
 * org.riverock.webmill - Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
-->
	<xsl:template match="ItemList">
		<table border="0" width="100%" cellpadding="2px" cellspacing="2px">
			<xsl:apply-templates select="PriceFieldName"/>
			<xsl:for-each select="PriceItem">
				<tr>
					<td class="priceData{ItemInBasket}">
						<table>
							<tr>
								<td valign="top">
QQQ									<img src="{ItemImageFileName}" alt="image"/>
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
	</xsl:template>
