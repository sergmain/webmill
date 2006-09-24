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
