package org.riverock.interfaces.portal.bean;

import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 02.02.2006
 *         Time: 15:02:39
 *         $Id$
 */
public interface Holding {
	public String getName();
	public String getShortName();
	public Long getId();

	public List<Long> getCompanyIdList();
}
