package org.riverock.portlet.news;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.webmill.portlet.GenericWebmillPortlet;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 18.12.2004
 * Time: 18:21:10
 * $Id$
 */
public final class NewsPortlet extends GenericWebmillPortlet {

    private final static Logger log = Logger.getLogger( NewsPortlet.class );

    public NewsPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        NewsSite newsSite = new NewsSite();
        doRender( renderRequest, renderResponse, newsSite );
    }
}
