package org.riverock.portlet.auth.role.dao;

import java.util.List;
import org.riverock.portlet.auth.role.bean.RoleBean;
import org.riverock.interfaces.sso.a3.AuthSession;

public interface RoleDAO {

	public RoleBean loadRole( Long id, AuthSession authSession );
	public List<RoleBean> getRoleList( AuthSession authSession );
    public Long processAddRole( RoleBean roleBean, AuthSession authSession );
    public void processSaveRole( RoleBean roleBean, AuthSession authSession );
    public void processDeleteRole( RoleBean roleBean, AuthSession authSession );
}