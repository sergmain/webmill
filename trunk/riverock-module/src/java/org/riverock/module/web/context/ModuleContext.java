package org.riverock.module.web.context;

import org.riverock.module.web.dispatcher.ModuleRequestDispatcher;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:54
 *         $Id$
 */
public interface ModuleContext {
    public ModuleRequestDispatcher getRequestDispatcher(String url);
}
