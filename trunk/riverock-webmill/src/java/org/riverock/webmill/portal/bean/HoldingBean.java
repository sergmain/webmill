package org.riverock.webmill.portal.bean;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:16:22
 *         $Id$
 */
public class HoldingBean implements Serializable, Holding {
    private static final long serialVersionUID = 4055005512L;

	private String name = null;
	private Long id = null;
	private String shortName = null;
	private List<Long> companyIdList = null;

    public HoldingBean() {
    }

    public HoldingBean(Holding holding) {
        this.id = holding.getId();
        this.name = holding.getName();
        this.shortName = holding.getShortName();
        this.companyIdList = holding.getCompanyIdList();        
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

	public List<Long> getCompanyIdList() {
		return companyIdList;
	}

	public void setCompanyIdList(List<Long> companyIdList) {
		this.companyIdList = companyIdList;
	}
}
