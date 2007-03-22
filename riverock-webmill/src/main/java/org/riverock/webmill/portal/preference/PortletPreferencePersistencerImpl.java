/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.webmill.portal.preference;

import org.riverock.webmill.portal.dao.InternalDaoFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergei Maslyukov
 *         Date: 14.08.2006
 *         Time: 20:43:07
 */
public class PortletPreferencePersistencerImpl implements PortletPreferencePersistencer {

    private Long contextId=null;
    private Map<String, List<String>> pref=null;

    public PortletPreferencePersistencerImpl(Long contextId) {
        this.contextId = contextId;
    }

    public Map<String, List<String>> load() {
        if (contextId==null) {
            pref = new HashMap<String, List<String>>();
        }
        else if (pref==null) {
            pref = InternalDaoFactory.getInternalPreferencesDao().load(contextId); 
        }
        return pref; 
    }

    public void store(Map<String, List<String>> preferences) {
        if (contextId!=null) {
            InternalDaoFactory.getInternalPreferencesDao().store(preferences, contextId);
            if (pref!=null) {
                pref.clear();
                pref = null;
            }
            pref = load();
        }
    }
}

