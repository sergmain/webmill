/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.action;

import java.io.File;
import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.system.DbStructureImport;
import org.riverock.webmill.admin.StructureSessionBean;

/**
 * @author Sergei Maslyukov
 *         Date: 17.07.2006
 *         Time: 20:31:13
 */
public class StructureAction implements Serializable {
    private static Logger log = Logger.getLogger(StructureAction.class);
    private static final long serialVersionUID = 2055005501L;

    private StructureSessionBean structureSessionBean=null;


    public StructureAction() {
    }

    public StructureSessionBean getStructureSessionBean() {
        return structureSessionBean;
    }

    public void setStructureSessionBean(StructureSessionBean structureSessionBean) {
        this.structureSessionBean = structureSessionBean;
    }

    public String createDbStructure() {
        String strucruteFileName =
            PropertiesProvider.getApplicationPath()+ File.separatorChar+
                "WEB-INF" + File.separatorChar+
                "webmill" + File.separatorChar+
                "structure" + File.separatorChar+
                "webmill-schema.xml";

        log.debug("structure file name: " +strucruteFileName);

        DatabaseAdapter db=null;
        try {
            db = DatabaseAdapter.getInstance();
            structureSessionBean.setErrorMessage(null);
            DbStructureImport.importStructure(strucruteFileName, false, db);
        }
        catch (Throwable e) {
            String es = "Error create db structure";
            log.error(es, e);
            structureSessionBean.setErrorMessage(e.toString());
        }
        finally {
            DatabaseManager.close(db);
        }

        return "create-structure-result";

    }
}
