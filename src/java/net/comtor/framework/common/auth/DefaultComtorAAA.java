package net.comtor.framework.common.auth;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.aaa.ComtorAAAFacade;
import net.comtor.aaa.ComtorLoginResponse;
import net.comtor.aaa.ComtorPrivilege;
import net.comtor.aaa.ComtorUser;
import net.comtor.aaa.helper.PasswordHelper2;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.web.facade.PrivilegeWebFacade;
import net.comtor.framework.common.auth.web.facade.UserWebFacade;
import net.comtor.i18n.I18n;
import net.comtor.i18n.LocaleHelper;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class DefaultComtorAAA extends ComtorAAAFacade {

    private static final Logger LOG = Logger.getLogger(DefaultComtorAAA.class.getName());

    @Override
    public ComtorLoginResponse validateLogin(String login, String password) {
        try {
            UserWebFacade userFacade = new UserWebFacade();
            PrivilegeWebFacade privilegeFacade = new PrivilegeWebFacade();
            User user = userFacade.find(login);

            if ((user != null)
                    && (user.getActive() == 1)
                    && validateLoginPassword(user, (user.getSalt() + password))) {
                LinkedList<ComtorPrivilege> privileges = privilegeFacade.getUserPrivileges(user.getLogin());
                ComtorUser comtorUser = new ComtorUser(login, user.getName(), privileges, user);
                printPrivileges(comtorUser);

                return new ComtorLoginResponse(true, comtorUser, I18n.tr(getLang(),
                        "aaa.welcome").replace("${userName}", comtorUser.getFullName()));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return new ComtorLoginResponse(false, null, I18n.tr(getLang(),
                    "aaa.err.title").replace("${err}", ex.getMessage()));
        }

        return new ComtorLoginResponse(false, null, I18n.tr(getLang(), "login.field.login.exception"));
    }

    /**
     *
     * @param user
     * @param password
     * @return
     */
    public boolean validateLoginPassword(User user, String password) {
        try {
            return ((user != null) && isSamePassword(password, user.getPassword()));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return false;
        }
    }

    /**
     *
     * @param inPassword
     * @param realPassword
     * @return
     */
    private boolean isSamePassword(String inPassword, String realPassword) {
        try {
            String encryption = PasswordHelper2.getHelper().encryption(inPassword);

            return encryption.equals(realPassword);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return false;
        }
    }

    /**
     *
     * @param user
     */
    private void printPrivileges(ComtorUser user) {
        StringBuilder sb = new StringBuilder("[USER=").append(user.getLogin()).append("]");
        LinkedList<ComtorPrivilege> privileges = user.getPrivileges();

        if (privileges != null) {
            for (ComtorPrivilege privilege : privileges) {
                sb.append("[").append(privilege.getCode()).append("]");
            }
        }

        LOG.log(Level.INFO, sb.toString());
    }

    /**
     * Returns the default user-defined language
     */
    protected final String getLang() {
        return LocaleHelper.getLocale(getRequest());
    }
}
