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

