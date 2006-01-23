/**
 * 
 */
package org.riverock.portlet.company.bean;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyBean implements Serializable {
    private static final long serialVersionUID = 2055005502L;

	private String name = null;
	private Long id = null;
	private String shortName = null;
	private String address = null;
	private String ceo = null;
	private String cfo = null;
	private String website = null;
	private String info = null;

	public CompanyBean() {
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
