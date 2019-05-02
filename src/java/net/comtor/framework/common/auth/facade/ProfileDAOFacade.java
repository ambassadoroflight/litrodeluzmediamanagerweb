package net.comtor.framework.common.auth.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import web.connection.ApplicationDAO;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.element.ProfileXPrivilege;
import net.comtor.dao.ComtorJDBCDao;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ProfileDAOFacade extends ComtorDaoElementLogicFacade<Profile, Long> {

    @Override
    public void create(Profile profile) throws ComtorDaoException {
        super.create(profile);
        insertPrivileges(profile);
    }

    @Override
    public void edit(Profile profile) throws ComtorDaoException {
        super.edit(profile);
        updatePrivileges(profile);
    }

    @Override
    public void remove(Profile profile) throws ComtorDaoException {
        deletePrivileges(profile);
        super.remove(profile);
    }

    @Override
    public Profile find(Long id) throws ComtorDaoException {
        Profile profile = super.find(id);

        if (profile == null) {
            return profile;
        }

        LinkedList<String> privilege = new PrivilegeDAOFacade().findAllByProfile(profile.getId());
        profile.setPrivileges(privilege);

        return profile;
    }

    public LinkedList<Profile> findAll() throws ComtorDaoException {
        String query = getFindQuery()
                + " ORDER BY \n"
                + "     profile.id \n";

        return findAll(query);
    }

    public LinkedList<Long> findAllIDByUser(String login) throws ComtorDaoException {
        ApplicationDAO dao = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "\n"
                    + " SELECT DISTINCT \n"
                    + "     user_x_profile.profile \n"
                    + " FROM \n"
                    + "     user_x_profile \n"
                    + " WHERE   \n"
                    + "     user_x_profile.login = ? \n";
            dao = new ApplicationDAO();
            conn = dao.getJdbcConnection();
            ps = conn.prepareStatement(query);

            int pos = 1;
            ps.setString(pos++, login);
            rs = ps.executeQuery();

            LinkedList<Long> ids = new LinkedList<>();

            while (rs.next()) {
                ids.add(rs.getLong(1));
            }

            return ids;
        } catch (SQLException ex) {
            throw new ComtorDaoException(ex);
        } finally {
            ComtorJDBCDao.safeClose(dao, conn, ps, rs);
        }
    }

    public LinkedList<Profile> findAllByUser(String login) throws ComtorDaoException {
        ApplicationDAO dao = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "\n"
                    + " SELECT DISTINCT \n"
                    + "     profile.* \n"
                    + " FROM \n"
                    + "     profile \n"
                    + " JOIN user_x_profile ON (user_x_profile.profile  = profile.id) \n"
                    + " JOIN users          ON (users.login             = user_x_profile.login) \n"
                    + " WHERE \n"
                    + "     users.login = ? \n"
                    + " ORDER BY \n"
                    + "     profile.name \n";
            dao = new ApplicationDAO();
            conn = dao.getJdbcConnection();
            ps = conn.prepareStatement(query);

            int pos = 1;
            ps.setString(pos++, login);

            rs = ps.executeQuery();

            LinkedList<Profile> profiles = new LinkedList<>();

            while (rs.next()) {
                Profile profile = new Profile();
                ComtorJDBCDao.fillObject(profile, rs, getTableDescriptorType());
                profiles.add(profile);
            }

            return profiles;
        } catch (SQLException ex) {
            throw new ComtorDaoException(ex);
        } finally {
            ComtorJDBCDao.safeClose(dao, conn, ps, rs);
        }
    }

    private void deletePrivileges(Profile profile) throws ComtorDaoException {
        ProfileXPrivilegeDAOFacade facade = new ProfileXPrivilegeDAOFacade();
        facade.deleteAllByProfile(profile.getId());
    }

    private void insertPrivileges(Profile profile) throws ComtorDaoException {
        LinkedList<String> privileges = profile.getPrivileges();

        if ((privileges == null) || privileges.isEmpty()) {
            return;
        }

        ProfileXPrivilegeDAOFacade facade = new ProfileXPrivilegeDAOFacade();

        for (String privilege : privileges) {
            facade.create(new ProfileXPrivilege(profile.getId(), privilege));
        }
    }

    private void updatePrivileges(Profile profile) throws ComtorDaoException {
        deletePrivileges(profile);
        insertPrivileges(profile);
    }
}
