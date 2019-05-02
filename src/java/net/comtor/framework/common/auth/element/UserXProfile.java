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
@ComtorElement(tableName = "user_x_profile")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class UserXProfile implements Serializable {

    private static final long serialVersionUID = -7300845165808896781L;

    @ComtorId
    private String login;
    @ComtorId
    private long profile;

    public UserXProfile() {
    }

    public UserXProfile(String login, long profile) {
        this.login = login;
        this.profile = profile;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getProfile() {
        return profile;
    }

    public void setProfile(long profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "UserXProfile{"
                + "login=" + login
                + ", profile=" + profile
                + '}';
    }

}
