package org.riverock.portlet.manager.holding;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class HoldingSessionBean {

	private HoldingBean holdingBean = null;
	private Long currentCompanyId = null;
	private Long currentHoldingId = null;

    private boolean isAdd = false;
    private boolean isEdit = false;
    private boolean isDelete = false;

    public HoldingSessionBean() {
    }

    public void resetStatus() {
	isAdd = false;
	isEdit = false;
	isDelete = false;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
	if (isAdd) {
		isEdit = false;
		isDelete = false;
	}
    }

    public boolean getAdd() {
        return isAdd;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
	if (isEdit) {
		isAdd = false;
		isDelete = false;
	}
    }

    public boolean getEdit() {
        return isEdit;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
	if (isDelete) {
		isEdit = false;
		isAdd = false;
	}
    }

    public boolean getDelete() {
        return isDelete;
    }

	public void setHoldingBean(HoldingBean bean) {
		this.holdingBean = bean;
	}

	public HoldingBean getHoldingBean() {
		return holdingBean;
	}

	public Long getCurrentHoldingId() {
		return currentHoldingId;
	}

	public void setCurrentHoldingId( Long id ) {
		this.currentHoldingId = id;
	}

	public Long getCurrentCompanyId() {
		return currentCompanyId;
	}

	public void setCurrentCompanyId( Long id ) {
		this.currentCompanyId = id;
	}
}
