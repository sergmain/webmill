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

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 3:14:38 PM
 *
 * $Id$
 */

package org.riverock.portlet.shop.processing.menatep;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.Base64;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.b2b.processing.Menatep;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.price.BasketShopSession;
import org.riverock.portlet.price.ShopPortlet;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.PortletTools;




public class ShopMenatepBasket extends HttpServlet
{
    private static Logger cat = Logger.getLogger(ShopMenatepBasket.class);

    public ShopMenatepBasket()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (cat.isDebugEnabled())
            cat.debug("method is POST");

        doGet(request, response);
    }

    /**
     * �������� ��� ������ ����������� � ���������.
     * �� 03.12.2002 �� ��������
     * @param request_
     * @param response
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Writer out = null;
        try
        {
//            CtxInstance ctxInstance =
//                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );
            RenderRequest renderRequest = null;

            ContextNavigator.setContentType(response);

            out = response.getWriter();


            PortletSession session = renderRequest.getPortletSession();

            BasketShopSession b = (BasketShopSession) session.getAttribute(ShopPortlet.BASKET_SHOP_SESSION);
            if (b == null)
            {
                response.sendRedirect(PortletTools.ctx( renderRequest ));
                return;
            }
            String basketXML =
                    Menatep.getSignedBasket(
                        b,
                        "/opt2/keys/menatep/testshop.key.der",
                        renderRequest, response
                    );

            byte bytes[] = basketXML.getBytes();
            bytes = Base64.encode(bytes);

            out.write("--- START BASKET ---\n" +
                    new String(bytes) +
                    "\n--- END BASKET ---"+
                    new String(StringTools.formatArray(bytes))
            );

        }
        catch (Exception e)
        {
            cat.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }

    }
}
