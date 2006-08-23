package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Company;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 16:06:41
 */
public class CompanyBean implements Serializable, Company {
    private static final long serialVersionUID = 41074436602L;

    private String name = null;
    private Long id = null;
    private String shortName = null;
    private String address = null;
    private String ceo = null;
    private String cfo = null;
    private String website = null;
    private String info = null;
    private boolean isDeleted = false;

    public CompanyBean() {
    }

    public CompanyBean(Company company) {
        this.name = company.getName();
        this.id = company.getId();
        this.shortName = company.getShortName();
        this.address = company.getAddress();
        this.ceo = company.getCeo();
        this.cfo = company.getCfo();
        this.website = company.getWebsite();
        this.info = company.getInfo();
        this.isDeleted = company.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getCfo() {
        return cfo;
    }

    public void setCfo(String cfo) {
        this.cfo = cfo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
