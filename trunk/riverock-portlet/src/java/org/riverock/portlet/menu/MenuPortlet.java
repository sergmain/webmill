package org.riverock.portlet.menu;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.GenericWebmillPortlet;
import org.riverock.portlet.price.ShopPage;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 15:54:17
 * $Id$
 */
public final class MenuPortlet extends GenericWebmillPortlet {
    private final static Logger log = Logger.getLogger( MenuPortlet.class );

    public MenuPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        MenuSimple shopPage = new MenuSimple();
        doRender(renderRequest, renderResponse, shopPage);
    }
}
