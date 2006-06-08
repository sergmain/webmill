package org.riverock.portlet.main;

import java.util.StringTokenizer;

import org.apache.myfaces.shared_tomahawk.util.el.TestsMap;

import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 08.06.2006
 *         Time: 18:47:28
 */
public class UserInRoleChecker extends TestsMap {

    public boolean getTest(String role) {
        if (role==null) {
            return false;
        }
        StringTokenizer st =  new StringTokenizer(role, ", ",false);
        while (st.hasMoreTokens()) {
            if (FacesTools.isUserInRole(st.nextToken()) ) {
                return true;
            }
        }
        return false;
    }
}

