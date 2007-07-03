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
import java.io.InputStream;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.webmill.admin.StructureSessionBean;
import org.riverock.webmill.utils.HibernateUtils;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseAdapterProvider;
import org.riverock.dbrevision.system.DbStructureImport;

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

        try {
            structureSessionBean.setErrorMessage(null);

            String family;
            String name = "java:comp/env/dbFamily";
            try {
                InitialContext ic = new InitialContext();
                family = (String) ic.lookup(name);
            }
            catch (NamingException e) {
                String es = "Error get value of DB family from JNDI. JNDI env: " + name;
                log.error(es, e);
                throw new IllegalArgumentException(es, e);
            }

            Session session = HibernateUtils.getSession();
            session.beginTransaction();

            Connection connection = session.connection();
            DatabaseAdapter db  = DatabaseAdapterProvider.getInstance(connection, family);
            InputStream stream = new FileInputStream(strucruteFileName);
            DbStructureImport.importStructure(db, stream, false);

            session.getTransaction().commit();
        }
        catch (Throwable e) {
            String es = "Error create db structure";
            log.error(es, e);
            structureSessionBean.setErrorMessage(e.toString());
        }

        return "create-structure-result";

    }
}
