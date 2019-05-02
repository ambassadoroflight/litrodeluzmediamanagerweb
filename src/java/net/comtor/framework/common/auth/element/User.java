package net.comtor.framework.common.auth.element;

import java.io.Serializable;
import java.util.LinkedList;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorField;
import net.comtor.dao.annotations.ComtorId;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
@ComtorElement(tableName = "users")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class User implements Serializable {

    private static final long serialVersionUID = -1105035484336157551L;

    @ComtorId
    private String login;
    private String password;
    private String salt;
    @ComtorField(insertable = false, updatable = false, selectable = false)
    private String confirm_password;
    private String name;
    private String email;
    private String phone;
    private int active;

    @ComtorField(insertable = false, updatable = false, findable = false, selectable = false)
    private LinkedList<Long> profiles;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public LinkedList<Long> getProfiles() {
        return profiles;
    }

    public void setProfiles(LinkedList<Long> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return "User{"
                + "login=" + login
                + ", name=" + name
                + ", email=" + email
                + ", phone=" + phone
                + ", active=" + active
                + '}';
    }

}
