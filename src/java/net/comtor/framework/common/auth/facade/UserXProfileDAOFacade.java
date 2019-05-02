package net.comtor.framework.common.auth.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import web.connection.ApplicationDAO;
import net.comtor.framework.common.auth.element.UserXProfile;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class UserXProfileDAOFacade extends ComtorDaoElementLogicFacade<UserXProfile, String> {

    void deleteAllByLogin(String login) throws ComtorDaoException {
        ApplicationDAO dao = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String query = "\n"
                    + " DELETE FROM \n"
                    + "     user_x_profile \n"
                    + " WHERE \n"
                    + "     user_x_profile.login = ? \n";
            dao = new ApplicationDAO();
            conn = dao.getJdbcConnection();
            ps = conn.prepareStatement(query);

            int pos = 1;
            ps.setString(pos++, login);

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ComtorDaoException(ex);
        } finally {
            ComtorJDBCDao.safeClose(dao, conn, ps, null);
        }
    }
}
