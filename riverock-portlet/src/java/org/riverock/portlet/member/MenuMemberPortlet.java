package org.riverock.portlet.member;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.webmill.portlet.GenericWebmillPortlet;
import org.riverock.portlet.menu.MenuPortlet;
import org.riverock.portlet.menu.MenuSimple;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 18.12.2004
 * Time: 20:25:05
 * $Id$
 */
public final class MenuMemberPortlet extends GenericWebmillPortlet {
    private final static Logger log = Logger.getLogger( MenuMemberPortlet.class );

    public MenuMemberPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        MenuMember shopPage = new MenuMember();
        doRender(renderRequest, renderResponse, shopPage);
    }
}
