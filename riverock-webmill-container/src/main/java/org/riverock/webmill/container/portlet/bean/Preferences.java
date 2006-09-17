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
