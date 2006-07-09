/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.Constants;
import org.riverock.generic.schema.db.DataDefinitionActionDataListType;
import org.riverock.generic.schema.db.structure.DbSchemaType;

/**
 * User: Admin
 * Date: May 20, 2003
 * Time: 9:49:58 PM
 *
 * $Id$
 */
public class InitWebmillStructureV2 implements DefinitionProcessingInterface
{
    private static Logger log = Logger.getLogger( InitWebmillStructureV2.class );

    public InitWebmillStructureV2(){}

    public void processAction(DatabaseAdapter db_, DataDefinitionActionDataListType parameters)
        throws Exception
    {
        try
        {
            if (log.isDebugEnabled())
                log.debug( "db connect - "+db_.getClass().getName() );

            int i = 0;

            if (log.isDebugEnabled())
                log.debug("Unmarshal data from file");

            String defFileName =
                PropertiesProvider.getConfigPath() + File.separator +
                Constants.MILL_DEFINITION_DIR + File.separator +
                "data" + File.separator +
                "webmill-def-v2.xml";

            InputSource inSrc = new InputSource( new FileInputStream( defFileName ));
            DbSchemaType millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

            DatabaseManager.createDbStructure( db_, millSchema );

        }
        finally
        {
            db_.commit();
        }
    }
}
