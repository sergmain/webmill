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

import java.util.*;
import java.io.Serializable;
import java.io.IOException;

import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.PortletPreferences;

import org.riverock.webmill.container.portlet.bean.Preference;

/**
 * @author Serge Maslyukov
 * Portlet persistent preference store.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class PortletPreferencesImpl implements PortletPreferences, Serializable {
    private static final long serialVersionUID = 30434672384237160L;

    private Map<String, String> portletMetadata = null;

    private PortletPreferencePersistencer persistencer = null;

    private List<Preference> preferences;

    public boolean isReadOnly(String key) {
        if (key==null) {
           throw new IllegalArgumentException("Can't get value of preference. Key is null");
        }

        Iterator<Preference> iterator = _preferenceList.iterator();
        while (iterator.hasNext()) {
            Preference preference = iterator.next();
            if ( preference.getName().equals(key) ) {
                if ( preference.getReadOnly()!=null && Boolean.TRUE.equals(preference.getReadOnly()) ) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    public String getValue(String key, String def) {
        if (key==null) {
           throw new IllegalArgumentException("Can't get value of preference. Key is null");
        }

        String[] values = userPreferences.get( key );
        if (values!=null) {
            if (values.length>0) {
                return values[0];
            }
            else {
                return def;
            }
        }
        if (_preferenceList!=null && !_preferenceList.isEmpty() ) {
            Iterator<Preference> iterator = _preferenceList.iterator();
            while (iterator.hasNext()) {
                Preference preference = iterator.next();
                if (preference.getName().equals( key )) {
                    Collection<String> coll = preference.getValueAsRef();
                    if (coll.size()>0) {
                        return coll.iterator().next();
                    }
                    else {
                        return def;
                    }
                }
            }
        }
        return def;
    }

    public String[] getValues(String key, String[] def) {
        if (key==null) {
           throw new IllegalArgumentException("Can't get value of preference. Key is null");
        }

        String[] values = userPreferences.get( key );
        if (values!=null) {
            return values;
        }
        if (_preferenceList!=null && !_preferenceList.isEmpty() ) {
            Iterator<Preference> iterator = _preferenceList.iterator();
            while (iterator.hasNext()) {
                Preference preference = iterator.next();
                if (preference.getName().equals( key )) {
                    Collection<String> coll = preference.getValueAsRef();
                    values = new String[coll.size()];
                    Iterator<String> it = coll.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        values[i++] = it.next();
                    }
                    return values;
                }
            }
        }
        return def;
    }

    public void setValue(String s, String s1) throws ReadOnlyException {
        if (true) throw new IllegalStateException("not implemented");
    }

    // Todo need optimize
    public void setValues(String key, String[] values) throws ReadOnlyException {
        if ( isReadOnly( key ) ) {
            throw new ReadOnlyException( "Preference '"+key+"' is read only" );
        }

        userPreferences.put( key, values );
    }

    public Enumeration getNames() {
        Set<String> set = new HashSet<String>();
        if (_preferenceList!=null && !_preferenceList.isEmpty() ) {
            Iterator<Preference> iterator = _preferenceList.iterator();
            while (iterator.hasNext()) {
                Preference preference = iterator.next();
                set.add( preference.getName() );
            }
        }

        set.addAll( userPreferences.keySet() );
        return Collections.enumeration( set );
    }

    public Map getMap() {
        Map<String, String[]> maps = new HashMap<String, String[]>();

        Iterator<Map.Entry<String,String[]>> mapIterator = userPreferences.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Map.Entry<String, String[]> entry = mapIterator.next();

            String[] values = new String[entry.getValue().length];
            System.arraycopy( entry.getValue(), 0, values, 0, entry.getValue().length);;
            maps.put( entry.getKey(), values );
        }

        if (_preferenceList!=null && !_preferenceList.isEmpty() ) {
            Iterator<Preference> iterator = _preferenceList.iterator();
            while (iterator.hasNext()) {
                Preference preference = iterator.next();
                if (!maps.containsKey( preference.getName() )) {
                    Collection<String> coll = preference.getValueAsRef();

                    String[] values = new String[coll.size()];
                    Iterator<String> it = coll.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        values[i++] = it.next();
                    }
                    maps.put( preference.getName(), values );
                }
            }
        }

        if (maps.isEmpty()) {
            return null;
        }
        else {
            return maps;
        }
    }

    public void reset(String key) throws ReadOnlyException {
        if (key==null) {
           throw new IllegalArgumentException("Can't reset preference. Key is null");
        }

        if ( isReadOnly( key ) ) {
            throw new ReadOnlyException( "Preference '"+key+"' is read only" );
        }

        userPreferences.remove( key );
    }

    public void store() throws IOException, ValidatorException {
        // Todo need implement persistence store
//        if (true) throw new IllegalStateException("not implemented");
    }
}
