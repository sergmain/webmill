package org.riverock.module.web.dispatcher;

import java.io.IOException;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;

import org.riverock.module.web.request.ModuleRequest;
import org.riverock.module.web.response.ModuleResponse;
import org.riverock.module.exception.ModuleException;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 15:33:49
 *         $Id$
 */
public interface ModuleRequestDispatcher {

    void include(ModuleRequest moduleRequest, ModuleResponse moduleResponse) throws ModuleException, IOException;
}
