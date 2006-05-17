/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

/**
 * @author Sergei Maslyukov
 *         16.05.2006
 *         20:24:52
 */
public class SiteSessionBean {
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int TEMPLATE_TYPE = 3;

    private boolean isEdit;
    private Long id = null;
    private int objectType=0;

    public int getSiteType() {
        return SITE_TYPE;
    }

    public int getSiteLanguageType() {
        return SITE_LANGUAGE_TYPE;
    }

    public int getTemplateType() {
        return TEMPLATE_TYPE;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
