/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import org.riverock.generic.annotation.schema.db.DefinitionList;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.main.CacheFile;

/**
 * $Id$
 */
public class DataDefinitionFile extends CacheFile {
    private static Logger log = Logger.getLogger(DataDefinitionFile.class);

    public DefinitionList definitionList = null;

    public DataDefinitionFile(File tempFile) {
        super(tempFile, 1000 * 10);

        if (log.isDebugEnabled())
            log.debug("Start unmarshalling file " + tempFile);

        processFile();
    }

    private final static Object syncObj = new Object();

    private void processFile() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance ( DefinitionList.class.getPackage().getName());

            FileInputStream stream = new FileInputStream(getFile());
            Source source =  new StreamSource( stream );
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            definitionList =  unmarshaller.unmarshal( source, DefinitionList.class ).getValue();

            if (log.isDebugEnabled()) {
                synchronized (syncObj) {
                    try {
                        FileWriter w = new FileWriter(GenericConfig.getGenericDebugDir() + "definitionList.xml");
                        jaxbContext.createMarshaller().marshal(definitionList, w);
                    }
                    catch (Exception e) {
                        // catch debug
                    }
                }
            }
        }
        catch (Exception e) {
            String errorString = "Error processing data definition file";
            log.error(errorString, e);
            throw new IllegalStateException(errorString, e);
        }
    }
}
