package org.riverock.webmill.container.portlet.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Sergei Maslyukov
 *         Date: 14.08.2006
 *         Time: 22:03:17
 */
public class Preferences implements Serializable {
    private static final long serialVersionUID = 30434672384234546L;

    private java.lang.String id;

    private List<Preference> preferenceList=new ArrayList<Preference>();

    private java.lang.String preferencesValidator;

    public void addPreference(Preference vPreference) {
        preferenceList.add(vPreference);
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
        this.preferenceList = preferenceList;
    }

    public String getPreferencesValidator() {
        return preferencesValidator;
    }

    public void setPreferencesValidator(String preferencesValidator) {
        this.preferencesValidator = preferencesValidator;
    }
}
