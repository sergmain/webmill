/*

 * org.riverock.portlet -- Portlet Library

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

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



package org.riverock.portlet.b2b.processing;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.price.BasketShopSession;

import org.riverock.portlet.price.PriceListItemOrder;

import org.riverock.portlet.price.Shop;

import org.riverock.generic.security.SecurityTools;

import org.riverock.common.tools.Base64;

import org.apache.log4j.Logger;



import java.util.Vector;





// OID 1.2.840.113549.1.1.4 - md5WithRSAEncryption

// https://www.menatepspb.com/ib/eps2/enter/?basket_url=http://www......

// --- START BASKET ---; строка --- END BASKET ---



public class Menatep

{



    private static Logger cat = Logger.getLogger( "org.riverock.b2b.processing.Menatep"  );



// ода валют в которых данный процессинговый центр принимает платежи

    public ProcessingInCurrency pic[] = {

        new ProcessingInCurrency(Constants.CURRENCY_RUB, "RUR", 0L, 1)

    };



    public static boolean isOrderCanProcessed()

    {

        return true;

    }





    public static String getSignedBasket(BasketShopSession ss, String nameFilePrivateKey,

                                         javax.servlet.http.HttpServletRequest request,

                                         javax.servlet.http.HttpServletResponse response)

            throws ProcessingException

    {

        if (true)

            throw new ProcessingException("This method was changed and must be rewrited");



        try

        {

            javax.servlet.http.HttpSession session = request.getSession(true);



            String mainURL =

                    "http://" + request.getServerName() + "/processing.jsp;jsessionid=" + session.getId();



            String askURL = mainURL + "?s=1";

            String cancelURL = mainURL + "?s=2";

            String confirmURL = mainURL + "?s=3";



            Shop shop = Shop.getInstance(DatabaseAdapter.getInstance(false), ss.id_shop);



            String storeTAG =

                    "<store id=\"" + shop.name_shop + "\" " +

                    "refNum=\"\" \n" +

                    "cmdAckUrl=\"" + askURL + "\" \n" +

                    "cmdCancelUrl=\"" + cancelURL + "\" \n" +

                    "returnUrl=\"" + confirmURL + "\"/>\n";



            String str = "";



            Vector v = null;

            PriceListItemOrder item = null;



//            v = PriceList.getOrderItems(DatabaseAdapter.getInstance(false),

//                    ss.id_shop, 0, request.getServerName(), ss.id_order);



            float totalPrice = 0;

            for (int i = 0; i < v.size(); i++)

            {

                item = (PriceListItemOrder) v.elementAt(i);



                str +=

                        "<merchandiseItem>\n" +

                        "<itemParam name=\"Quantity\" value=\"" + item.qty + "\"/>\n" +

                        "<itemParam name=\"SKU\" value=\"" + item.itemID + "\"/>\n" +

                        "<itemParam name=\"Desc.\" value=\"" + item.nameItem + "\"/>\n" +

                        "<itemParam name=\"Price\" value=\"" + item.priceItem + "\"/>\n" +

                        "</merchandiseItem>\n";

                totalPrice += item.priceItem;

            }



/*

str +=

<basketTotals>

<totalsParam name="Subtotal" value="318"/>

<totalsParam name="Total tax" value="0"/>

<totalsParam name="TOTAL" value="9253.8"/>

</basketTotals>

*/

            str += "<basketAmount value=\"" + totalPrice + "\" currency=\"RUR\"/>\n" +

                    "<selectedPaymentMethod type=\"-\"/>\n";



            String shoppingBasketTAG = "<shoppingBasket>\n" + str + "</shoppingBasket>\n";



            String dataToSign = storeTAG + shoppingBasketTAG;



            byte[] signature = SecurityTools.sign(nameFilePrivateKey,

                    dataToSign.getBytes(),

                    Constants.FORGE_MD5RSA);



            Base64 base64 = new Base64();

            String signatureTAG =

                    "<signature signer=\"" + shop.name_shop + "\" method=\"RSA-MD5\">\n" +

                    new String(base64.encode(signature)) +

                    "\n</signature>\n";



            return "<purchase>\n" +

                    dataToSign +

                    signatureTAG +

                    "</purchase>";

        }

        catch (Exception e)

        {

            throw new ProcessingException(e.toString());

        }

    }

}

