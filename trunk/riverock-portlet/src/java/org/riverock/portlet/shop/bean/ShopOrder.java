package org.riverock.portlet.shop.bean;

import org.riverock.portlet.schema.price.OrderType;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 16:36:41
 *         $Id$
 */
public class ShopOrder extends OrderType {

    public AuthSession getAuthSession() {
        return authSession;
    }

    public void setAuthSession( AuthSession authSession ) {
        this.authSession = authSession;
    }

    private AuthSession authSession = null;


}
