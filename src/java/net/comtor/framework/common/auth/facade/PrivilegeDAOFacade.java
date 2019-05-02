package net.comtor.framework.common.auth.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import net.comtor.aaa.ComtorPrivilege;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import web.connection.ApplicationDAO;
import net.comtor.framework.common.auth.element.Privilege;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class PrivilegeDAOFacade extends ComtorDaoElementLogicFacade<Privilege, String> {

    public LinkedList<ComtorPrivilege> getUserPrivileges(String login) throws ComtorDaoException {
        LinkedList<ComtorPrivilege> privileges = new LinkedList<>();

        try {
            String query = "\n"
                    + " SELECT DISTINCT \n"
                    + "     privilege.* \n"
                    + " FROM \n"
                    + "     users \n"
                    + " JOIN user_x_profile         ON (users.login                 = user_x_profile.login)             \n"
                    + " JOIN profile                ON (profile.id                  = user_x_profile.profile)           \n"
                    + " JOIN profile_x_privilege    ON (profile_x_privilege.profile = profile.id)                       \n"
                    + " JOIN privilege              ON (privilege.code              = profile_x_privilege.privilege)    \n"
                    + " WHERE   \n"
                    + "     users.login = ? \n"
                    + "";

            LinkedList<Privilege> list = new PrivilegeDAOFacade().findAll(query, login);

            for (Privilege privilege : list) {
                privileges.add(new ComtorPrivilege(privilege.getCode(), privilege.getDescription(), privilege.getCategory()));
            }

            return privileges;
        } catch (Exception ex) {
            throw new ComtorDaoException(ex);
        }
    }

    public LinkedList<Privilege> findAll() throws ComtorDaoException {
        String query = getFindQuery()
                + " ORDER BY \n"
                + "     privilege.code \n";

        return findAll(query);
    }

    LinkedList<String> findAllByProfile(long profile) throws ComtorDaoException {
        ApplicationDAO dao = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String query = "\n"
                    + " SELECT \n"
                    + "     profile_x_privilege.privilege \n"
                    + " FROM    \n"
                    + "     profile_x_privilege \n"
                    + " WHERE   \n"
                    + "     profile_x_privilege.profile = ? \n";
            dao = new ApplicationDAO();
            conn = dao.getJdbcConnection();
            ps = conn.prepareStatement(query);

            int pos = 1;
            ps.setLong(pos++, profile);

            rs = ps.executeQuery();

            LinkedList<String> privileges = new LinkedList<>();

            while (rs.next()) {
                privileges.add(rs.getString(1));
            }

            return privileges;
        } catch (ComtorDaoException | SQLException ex) {
            throw new ComtorDaoException(ex);
        } finally {
            ComtorJDBCDao.safeClose(dao, conn, ps, rs);
        }
    }
}
