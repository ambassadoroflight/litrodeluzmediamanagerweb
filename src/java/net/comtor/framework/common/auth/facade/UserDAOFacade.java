package net.comtor.framework.common.auth.facade;

import java.util.LinkedList;
import net.comtor.aaa.helper.PasswordHelper2;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.element.UserXProfile;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class UserDAOFacade extends ComtorDaoElementLogicFacade<User, String> {

    @Override
    public void create(User user) throws ComtorDaoException {
        encryptPassword(user);
        
        super.create(user);
        
        createUserXProfiles(user);
    }

    @Override
    public void edit(User user) throws ComtorDaoException {
        editPassword(user);
        
        super.edit(user);
        
        recreateUserXProfiles(user);
    }

    @Override
    public User find(String login) throws ComtorDaoException {
        User user = super.find(login);

        if (user == null) {
            return null;
        }

        LinkedList<Long> profiles = new ProfileDAOFacade().findAllIDByUser(user.getLogin());
        user.setProfiles(profiles);

        return user;
    }

    @Override
    public void remove(User user) throws ComtorDaoException {
        new UserXProfileDAOFacade().deleteAllByLogin(user.getLogin());
        super.remove(user);
    }

    private void editPassword(User user) throws ComtorDaoException {
        if (user.getPassword().equals("nochange")) {
            User current = find(user.getLogin());
            user.setPassword(current.getPassword());
            user.setSalt(current.getSalt());

            return;
        }

        encryptPassword(user);
    }

    private void encryptPassword(User user) throws ComtorDaoException {
        try {
            final String salt = PasswordHelper2.getHelper().createSalt();
            user.setSalt(salt);
            user.setPassword(PasswordHelper2.getHelper().encryption(user.getPassword(), salt));
        } catch (Exception ex) {
            throw new ComtorDaoException(ex);
        }
    }

    private void createUserXProfiles(User user) throws ComtorDaoException {
        UserXProfileDAOFacade facade = new UserXProfileDAOFacade();

        for (Long profile : user.getProfiles()) {
            facade.create(new UserXProfile(user.getLogin(), profile));
        }
    }

    private void recreateUserXProfiles(User user) throws ComtorDaoException {
        UserXProfileDAOFacade facade = new UserXProfileDAOFacade();
        facade.deleteAllByLogin(user.getLogin());
        createUserXProfiles(user);
    }

}
