/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.container.portlet.bean;

import javax.portlet.PreferencesValidator;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergei Maslyukov
 *         Date: 14.08.2006
 *         Time: 22:03:17
 */
public class Preferences implements Serializable {
    private static final long serialVersionUID = 30434672384234546L;

    private java.lang.String id;

    private List<Preference> preferenceList=new ArrayList<Preference>();

    private java.lang.String preferencesValidatorClass =null;
    private PreferencesValidator preferencesValidator =null;

    public void addPreference(Preference preference) {
        Collection<String> values = preference.getValue();
        if (values==null || values.isEmpty()) {
            return;
        }
        preferenceList.add(preference);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Preference> getPreferenceList() {
        return preferenceList;
    }

    public void setPreferenceList(List<Preference> preferenceList) {
        List<Preference> list = new ArrayList<Preference>(preferenceList.size());
        for (Preference preference : preferenceList) {
            List<String> values = preference.getValue();
            if (values==null || values.isEmpty()) {
                continue;
            }
            list.add(preference);
        }
        this.preferenceList = list;
    }

    public String getPreferencesValidatorClass() {
        return preferencesValidatorClass;
    }

    public void setPreferencesValidatorClass(String preferencesValidatorClass) {
        this.preferencesValidatorClass = preferencesValidatorClass;
    }


    public PreferencesValidator getPreferencesValidator() {
        if (preferencesValidator!=null) {
            return preferencesValidator;
        }

        if (preferencesValidatorClass==null) {
            return null;
        }
        else {
            synchronized(this) {
                if (preferencesValidator!=null) {
                    return preferencesValidator;
                }

                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                try {
                    Class clazz = loader.loadClass(preferencesValidatorClass);
                    PreferencesValidator validator = (PreferencesValidator) clazz.newInstance();
                    preferencesValidator = validator;
                }
                catch (Throwable th) {
                    String es = "Error create validator for class " + preferencesValidatorClass+".";
                    th.printStackTrace();
                    throw new IllegalStateException(es, th);
                }
            }
        }
        return preferencesValidator;
    }
}
