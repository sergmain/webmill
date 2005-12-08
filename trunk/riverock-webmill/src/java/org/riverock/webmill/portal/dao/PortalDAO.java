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
package org.riverock.webmill.portal.dao;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.schema.core.MainLanguageListType;
import org.riverock.webmill.schema.core.MainLanguageItemType;
import org.riverock.webmill.core.GetMainLanguageFullList;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:23:06
 *         $Id$
 */
public class PortalDAO {
    private final static Logger log = Logger.getLogger(PortalDAO.class);

    private Collection<String> supportedLocales = null;

    public PortalDAO() {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            supportedLocales = initSupportedLocales(adapter);
        }
        catch (Exception e) {
            String es = "Error get list";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }


    public Collection<String> getSupportedLocales() {
        return supportedLocales;
    }

    private Collection<String> initSupportedLocales( DatabaseAdapter adapter ) throws PortalPersistenceException {
        Set<String> list = new HashSet<String>();
        MainLanguageListType languages = GetMainLanguageFullList.getInstance( adapter, 1 ).item;
        for (int i=0; i<languages.getMainLanguageCount(); i++){
            MainLanguageItemType item = languages.getMainLanguage( i );
            list.add( item.getShortNameLanguage() );
        }
        return list;
    }

}
