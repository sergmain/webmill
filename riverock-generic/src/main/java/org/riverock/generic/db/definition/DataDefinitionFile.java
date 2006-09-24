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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.main.CacheFile;
import org.riverock.generic.schema.db.DefinitionListType;
import org.riverock.generic.config.GenericConfig;

/**
 * $Id$
 */
public class DataDefinitionFile extends CacheFile {
    private static Logger log = Logger.getLogger(DataDefinitionFile.class);

    public DefinitionListType definitionList = null;

    public DataDefinitionFile(File tempFile) {
        super(tempFile, 1000 * 10);

        if (log.isDebugEnabled())
            log.debug("Start unmarshalling file " + tempFile);

        processFile();
    }

    private final static Object syncObj = new Object();

    private void processFile() {
        try {
            InputSource inSrc = new InputSource(new FileInputStream(getFile()));
            definitionList = (DefinitionListType) Unmarshaller.unmarshal(DefinitionListType.class, inSrc);
            definitionList.validate();

            if (log.isDebugEnabled()) {
                synchronized (syncObj) {
                    try {
                        FileWriter w = new FileWriter(GenericConfig.getGenericDebugDir() + "definitionList.xml");
                        Marshaller.marshal(definitionList, w);
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
