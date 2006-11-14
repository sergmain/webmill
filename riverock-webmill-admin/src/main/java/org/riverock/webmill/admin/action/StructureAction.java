/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
