/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.as.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.main.CacheFile;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.schema.appl_server.Application;
import org.riverock.webmill.schema.appl_server.ApplicationModuleType;

/**
 * $Id$
 */
public class ApplicationFile extends CacheFile {
    private static Logger log = Logger.getLogger(ApplicationFile.class);

    private Map<String, ApplicationModuleType> applHash = null;

    protected void finalize() throws Throwable {
        if (applHash != null) {
            applHash.clear();
            applHash = null;
        }
        super.finalize();
    }

    public int getApplicationCount() {
        if (applHash == null)
            return 0;

        return applHash.size();
    }

    public ApplicationModuleType getApplModule(String nameModule_) {
        if (applHash == null)
            return null;

        if (log.isDebugEnabled())
            log.debug("Get application '" + nameModule_ + "' from file '" + getFile().getName() + "'");

        if (isUseCache())
            return applHash.get(nameModule_);

        try {
            if (log.isDebugEnabled()) {
                log.debug("#7.00.01 " + getFile());
                log.debug("#7.01.00 check was file modified");
            }

            if (isNeedReload()) {

                if (log.isInfoEnabled())
                    log.info("Redeploy member file " + getFile().getName());

                setLastModified();

                processFile();

                if (log.isDebugEnabled())
                    log.debug("#7.01.02 Unmarshal done");

                return applHash.get(nameModule_);
            }
        }
        catch (Exception e) {
            log.error("Exception while get module " + nameModule_, e);
            return null;
        }

        if (log.isDebugEnabled())
            log.debug("#7.02 file not changed. Get module from cache");

        return (ApplicationModuleType) applHash.get(nameModule_);
//        return getApplModule(appl, nameModule_);
    }

    public ApplicationFile(File tempFile)
        throws Exception {
        super(tempFile, 1000 * 10);

        if (log.isDebugEnabled())
            log.debug("Start unmarshalling file " + tempFile);

        try {
            if (log.isDebugEnabled())
                log.debug("Unmarshal new file: " + tempFile.getName());

            processFile();
        }
        catch (Exception e) {
            log.error("Exception while unmarshalling member file + " + getFile(), e);
            if (applHash != null) {
                applHash.clear();
                applHash = null;
            }
            throw e;
        }
    }


//> > How are you calling the Unmarshaller?
//> >
//> > I'm not sure if you're already doing this, but try caching the
//> > ClassDescriptorResolver:
//> >
//> > import org.exolab.castor.xml.ClassDescriptorResolver;
//> > import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;
//> >
//> > ...
//> >
//> > ClassDescriptorResolver cdr = new ClassDescriptorResolverImpl();
//> > ...
//> >
//> > ///-- somewhere inside your loop for listening on your socket
//> >    Unmarshaller unmarshaller = new Unmarshaller(Foo.class);
//> >    unmarshaller.setResolver(cdr);
//> >
//> > Hope that helps, if not...we'll probably need to see some sample

    private static Object syncObj = new Object();

    private void processFile()
        throws Exception {
        try {
            InputSource inSrc = new InputSource(new FileInputStream(getFile()));
            Application appl = (Application) Unmarshaller.unmarshal(Application.class, inSrc);
            appl.validate();

            applHash = new HashMap<String, ApplicationModuleType>();
            for (int i = 0; i < appl.getApplicationModuleCount(); i++) {
                ApplicationModuleType desc = appl.getApplicationModule(i);
                applHash.put(desc.getCodeResource(), desc);
            }

            if (log.isDebugEnabled()) {
                synchronized (syncObj) {
                    try {
                        FileWriter w = new FileWriter(WebmillConfig.getWebmillDebugDir() + "application.xml");
                        Marshaller.marshal(appl, w);
                    }
                    catch (Exception e) {
                    }
                }
            }
        }
        catch (Exception e) {
            String errorString = "Error processinf member file";
            log.error(errorString, e);
            throw e;
        }
    }
}
