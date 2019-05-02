package net.comtor.framework.common.auth.element;

import java.io.Serializable;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorId;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
@ComtorElement(tableName = "profile_x_privilege")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class ProfileXPrivilege implements Serializable {

    private static final long serialVersionUID = -5259762242126904311L;

    @ComtorId
    private long profile;

    @ComtorId
    private String privilege;

    public ProfileXPrivilege() {
    }

    public ProfileXPrivilege(long profile, String privilege) {
        this.profile = profile;
        this.privilege = privilege;
    }

    public long getProfile() {
        return profile;
    }

    public void setProfile(long profile) {
        this.profile = profile;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "ProfileXPrivilege{"
                + "profile=" + profile
                + ", privilege=" + privilege
                + '}';
    }

}
