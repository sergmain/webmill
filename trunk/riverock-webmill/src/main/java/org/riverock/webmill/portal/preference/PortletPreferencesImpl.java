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
import org.riverock.webmill.container.portlet.bean.Preferences;
import org.apache.log4j.Logger;

/**
 * The <CODE>PortletPreferences</CODE> interface allows the portlet to store
 * configuration data. It is not the
 * purpose of this interface to replace general purpose databases.
 * <p>
 * There are two different types of preferences:
 * <ul>
 * <li>modifiable preferences - these preferences can be changed by the
 *     portlet in any standard portlet mode (<code>EDIT, HELP, VIEW</code>).
 *     Per default every preference is modifiable.
 * <li>read-only preferences - these preferences cannot be changed by the
 *     portlet in any standard portlet mode, but may be changed by administrative modes.
 *     Preferences are read-only, if the are defined in the
 *     deployment descriptor with <code>read-only</code> set to <code>true</code>,
 *     or if the portlet container restricts write access.
 * </ul>
 * <p>
 * Changes are persisted when the <code>store</code> method is called. The <code>store</code> method
 * can only be invoked within the scope of a <code>processAction</code> call.
 * Changes that are not persisted are discarded when the
 * <code>processAction</code> or <code>render</code> method ends.
 * 
 * @author Serge Maslyukov
 * Portlet persistent preference store.
 * Used in: portlet
 *
 * @version $Revision$ $Date$
 */
public class PortletPreferencesImpl implements PortletPreferences, Serializable {
    private static final long serialVersionUID = 30434672384237160L;
    private final static Logger log = Logger.getLogger( PortletPreferencesImpl.class );

    private Map<String, List<String>> portletMetadata = null;
    private Map<String, List<String>> defaultPortletMetadata = null;

    private PortletPreferencePersistencer persistencer = null;

    private Preferences preferences;
    private boolean isStandardPortletMode;
    private boolean isRenderRequest;
    private boolean isStoreProcessed=false;

    /**
     *
     * @param portletMetadata Map<String, List<String>>
     * @param persistencer PortletPreferencePersistencer
     * @param preferences Preferences
     * @param isStandardPortletMode boolean if Portlet mode of request is EDIT, HELP or VIEW, return true, otherwise false
     * @param isRenderRequest boolean
     */
    public PortletPreferencesImpl(Map<String, List<String>> portletMetadata, PortletPreferencePersistencer persistencer, Preferences preferences, boolean isStandardPortletMode, boolean isRenderRequest) {
        this.portletMetadata = portletMetadata;
        if (this.portletMetadata==null) {
            this.portletMetadata = new HashMap<String, List<String>>();
        }
        this.defaultPortletMetadata = new HashMap<String, List<String>>(portletMetadata);
        this.persistencer = persistencer;
        this.preferences = preferences;
        this.isStandardPortletMode = isStandardPortletMode;
        this.isRenderRequest = isRenderRequest;
    }

    /**
     * Returns true, if the value of this key cannot be modified by the user.
     * <p>
     * Modifiable preferences can be changed by the
     * portlet in any standard portlet mode (<code>EDIT, HELP, VIEW</code>).
     * Per default every preference is modifiable.
     * <p>
     * Read-only preferences cannot be changed by the
     * portlet in any standard portlet mode, but inside of custom modes
     * it may be allowed changing them.
     * Preferences are read-only, if they are defined in the
     * deployment descriptor with <code>read-only</code> set to <code>true</code>,
     * or if the portlet container restricts write access.
     *
     * @return  false, if the value of this key can be changed, or
     *          if the key is not known
     *
     * @exception java.lang.IllegalArgumentException
     *         if <code>key</code> is <code>null</code>.
     */
    public boolean isReadOnly(String key) {
        if (key==null) {
           throw new IllegalArgumentException("Can't evaluate preference read-only status. Key is null");
        }

        if (!isStandardPortletMode) {
            return true;
        }

        if (preferences!=null) {
            for (Preference preference : preferences.getPreferenceList()) {
                if ( preference.getName().equals(key) ) {
                    return preference.getReadOnly() != null && preference.getReadOnly();
                }
            }
        }
        return false;
    }

    /**
     * Returns the first String value associated with the specified key of this preference.
     * If there is one or more preference values associated with the given key
     * it returns the first associated value.
     * If there are no preference values associated with the given key, or the
     * backing preference database is unavailable, it returns the given
     * default value.
     *
     * @param key key for which the associated value is to be returned
     * @param def the value to be returned in the event that there is no
     *            value available associated with this <code>key</code>.
     *
     * @return the value associated with <code>key</code>, or <code>def</code>
     *         if no value is associated with <code>key</code>, or the backing
     *         store is inaccessible.
     *
     * @exception java.lang.IllegalArgumentException
     *         if <code>key</code> is <code>null</code>. (A
     *         <code>null</code> value for <code>def</code> <i>is</i> permitted.)
     *
     * @see #getValues(String, String[])
     */
    public String getValue(String key, String def) {
        if (key==null) {
           throw new IllegalArgumentException("Can't get value of preference. Key is null");
        }

        List<String> valuesList = portletMetadata.get(key);

        if (log.isDebugEnabled()) {
            log.debug("Get preference.");
            log.debug("    key: " + key);
            log.debug("    valuesList: " + valuesList);
            if (valuesList!=null) {
                log.debug("    result isEmpty: " + valuesList.isEmpty());
                if (!valuesList.isEmpty()) {
                    log.debug("    result: " + valuesList.get(0));
                }
            }
        }

        if (valuesList!=null) {
            if (valuesList.isEmpty()) {
                return def;
            }
            else {
                return valuesList.get(0);
            }
        }
        if (preferences!=null) {
            for (Preference preference : preferences.getPreferenceList()) {
                if (preference.getName().equals( key )) {
                    Collection<String> coll = preference.getValue();
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

    /**
     * Returns the String array value associated with the specified key in this preference.
     *
     * <p>Returns the specified default if there is no value
     * associated with the key, or if the backing store is inaccessible.
     *
     * <p>If the implementation supports <i>stored defaults</i> and such a
     * default exists and is accessible, it is used in favor of the
     * specified default.
     *
     *
     * @param key key for which associated value is to be returned.
     * @param def the value to be returned in the event that this
     *        preference node has no value associated with <code>key</code>
     *        or the associated value cannot be interpreted as a String array,
     *        or the backing store is inaccessible.
     *
     * @return the String array value associated with
     *         <code>key</code>, or <code>def</code> if the
     *         associated value does not exist.
     *
     * @exception java.lang.IllegalArgumentException if <code>key</code> is <code>null</code>.  (A
     *         <code>null</code> value for <code>def</code> <i>is</i> permitted.)
     *
     * @see #getValue(String,String)
     */
    public String[] getValues(String key, String[] def) {
        if (key==null) {
           throw new IllegalArgumentException("Can't get value of preference. Key is null");
        }

        List<String> valuesList = portletMetadata.get(key);
        if (valuesList!=null) {
            return valuesList.toArray(new String[0]);
        }
        if (preferences!=null) {
            for (Preference preference : preferences.getPreferenceList()) {
                if (preference.getName().equals( key )) {
                    Collection<String> coll = preference.getValue();
                    return coll.toArray(new String[0]);
                }
            }
        }
        return def;
    }

    /**
     * Associates the specified String value with the specified key in this
     * preference.
     * <p>
     * The key cannot be <code>null</code>, but <code>null</code> values
     * for the value parameter are allowed.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     *
     * @exception  ReadOnlyException
     *                 if this preference cannot be modified for this request
     * @exception java.lang.IllegalArgumentException if key is <code>null</code>,
     *                 or <code>key.length()</code>
     *                 or <code>value.length</code> are to long. The maximum length
     *                 for key and value are implementation specific.
     *
     * @see #setValues(String, String[])
     */
    public void setValue(String key, String value) throws ReadOnlyException {
        if (key==null) {
           throw new IllegalArgumentException("Can't set value of preference. Key is null");
        }

        if ( isReadOnly( key ) ) {
            throw new ReadOnlyException("Preference '"+key+"' is read only" );
        }

        if (value==null) {
            portletMetadata.remove(key);
            return;
        }

        ArrayList<String> list = new ArrayList<String>();
        list.add(value);
        portletMetadata.put(key, list);

        if (log.isDebugEnabled()) {
            log.debug("Set new preference. ");
            log.debug("    key: " + key+", value: " + value);
            log.debug("    result: " + portletMetadata.get(key));
        }
    }

    /**
     * Associates the specified String array value with the specified key in this
     * preference.
     * <p>
     * The key cannot be <code>null</code>, but <code>null</code> values
     * in the values parameter are allowed.
     *
     * @param key key with which the  value is to be associated
     * @param values values to be associated with key
     *
     * @exception  java.lang.IllegalArgumentException if key is <code>null</code>, or
     *                 <code>key.length()</code>
     *                 is to long or <code>value.size</code> is to large.  The maximum
     *                 length for key and maximum size for value are implementation specific.
     * @exception  ReadOnlyException
     *                 if this preference cannot be modified for this request
     *
     * @see #setValue(String,String)
     */
    public void setValues(String key, String[] values) throws ReadOnlyException {
        if (key==null) {
           throw new IllegalArgumentException("Can't set value of preference. Key is null");
        }
        if ( isReadOnly( key ) ) {
            throw new ReadOnlyException("Preference '"+key+"' is read only" );
        }

        if (values==null) {
            return;
        }

        List<String> list = new ArrayList<String>(values.length);
        for (String value : values) {
            if (value==null) {
                continue;
            }
            list.add(value);
        }

        if (list.isEmpty()) {
            portletMetadata.remove(key);
            return;
        }

        portletMetadata.put(key, list);
    }

    /**
     * Returns all of the keys that have an associated value,
     * or an empty <code>Enumeration</code> if no keys are
     * available.
     *
     * @return an Enumeration of the keys that have an associated value,
     *         or an empty <code>Enumeration</code> if no keys are
     *         available.
     */
    public Enumeration getNames() {
        Set<String> set = new HashSet<String>();
        if (preferences!=null && !preferences.getPreferenceList().isEmpty() ) {
            for (Preference preference : preferences.getPreferenceList()) {
                set.add( preference.getName() );
            }
        }

        set.addAll( portletMetadata.keySet() );
        return Collections.enumeration( set );
    }

    /**
     * Returns a <code>Map</code> of the preferences.
     * <p>
     * The values in the returned <code>Map</code> are from type
     * String array (<code>String[]</code>).
     * <p>
     * If no preferences exist this method returns an empty <code>Map</code>.
     *
     * @return     an immutable <code>Map</code> containing preference names as
     *             keys and preference values as map values, or an empty <code>Map</code>
     *             if no preference exist. The keys in the preference
     *             map are of type String. The values in the preference map are of type
     *             String array (<code>String[]</code>).
     */
    public Map getMap() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if (portletMetadata!=null) {
            map.putAll(portletMetadata);
        }

        if (preferences!=null) {
            for (Preference preference : preferences.getPreferenceList()) {
                String key = preference.getName();
                List<String> list = map.get(key);
                if (list!=null) {
                    list.addAll(preference.getValue());
                }
                else {
                    list = new ArrayList<String>(preference.getValue());
                }
                map.put(key, list);
            }
        }

        Map<String, String[]> result = new HashMap<String, String[]>();
        for ( Map.Entry<String, List<String>> entry : map.entrySet()) {
            result.put( entry.getKey(), entry.getValue().toArray(new String[0]));
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * Resets or removes the value associated with the specified key.
     * <p>
     * If this implementation supports stored defaults, and there is such
     * a default for the specified preference, the given key will be
     * reset to the stored default.
     * <p>
     * If there is no default available the key will be removed.
     *
     * @param  key to reset
     *
     * @exception  java.lang.IllegalArgumentException if key is <code>null</code>.
     * @exception  ReadOnlyException
     *                 if this preference cannot be modified for this request
     */
    public void reset(String key) throws ReadOnlyException {
        if (key==null) {
           throw new IllegalArgumentException("Can't reset preference. Key is null");
        }

        if ( isReadOnly( key ) ) {
            throw new ReadOnlyException( "Preference '"+key+"' is read only" );
        }

        List<String> def = defaultPortletMetadata.get(key);
        if (def!=null) {
            portletMetadata.put(key, def);
        }
        else {
            portletMetadata.remove(key);
        }
    }

    /**
     * Commits all changes made to the preferences via the
     * <code>set</code> methods in the persistent store.
     * <P>
     * If this call returns succesfull, all changes are made
     * persistent. If this call fails, no changes are made
     * in the persistent store. This call is an atomic operation
     * regardless of how many preference attributes have been modified.
     * <P>
     * All changes made to preferences not followed by a call
     * to the <code>store</code> method are discarded when the
     * portlet finishes the <code>processAction</code> method.
     * <P>
     * If a validator is defined for this preferences in the
     * deployment descriptor, this validator is called before
     * the actual store is performed to check wether the given
     * preferences are vaild. If this check fails a
     * <code>ValidatorException</code> is thrown.
     *
     * @exception  java.io.IOException
     *                 if changes cannot be written into
     *                 the backend store
     * @exception  ValidatorException
     *                 if the validation performed by the
     *                 associated validator fails
     * @exception  java.lang.IllegalStateException
     *                 if this method is called inside a render call
     *
     *
     * PLT.14.4 Validating Preferences Values
     * [This section supplements section PLT.14.4 on page 60 in the Portlet 1.0
     * specification, add the text to line 26]
     *
     * Portlet preferences cannot be modified when they are being validated by a
     * PreferencesValidator object. If the store method is invoked within
     * the scope of the PreferenceValidator's validate method invocation,
     * an IllegalStateException must be thrown.
     *
     * @see  javax.portlet.PreferencesValidator
     */
    public void store() throws IOException, ValidatorException {
        if (isRenderRequest) {
            throw new IllegalStateException("Can't store preference inside render request.");
        }

        if (isStoreProcessed) {
            throw new IllegalStateException(
                "Portlet preferences cannot be modified when they are being validated by a PreferencesValidator"
            ); 
        }

        if (preferences.getPreferencesValidator()!=null) {
            isStoreProcessed=true;
            try {
                preferences.getPreferencesValidator().validate(this);
            } finally {
                isStoreProcessed=false;
            }
        }
        persistencer.store(portletMetadata);
    }
}
