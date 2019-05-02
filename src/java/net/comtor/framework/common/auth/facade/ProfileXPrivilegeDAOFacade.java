package net.comtor.framework.common.auth.facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import web.connection.ApplicationDAO;
import net.comtor.framework.common.auth.element.ProfileXPrivilege;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ProfileXPrivilegeDAOFacade extends ComtorDaoElementLogicFacade<ProfileXPrivilege, Long> {

    public void deleteAllByProfile(long profile) throws ComtorDaoException {
        ApplicationDAO dao = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String query = "\n"
                    + " DELETE FROM \n"
                    + "     profile_x_privilege \n"
                    + " WHERE \n"
                    + "     profile_x_privilege.profile = ? \n";
            dao = new ApplicationDAO();
            conn = dao.getJdbcConnection();
            ps = conn.prepareStatement(query);

            int pos = 1;
            ps.setLong(pos++, profile);

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new ComtorDaoException(ex);
        } finally {
            ComtorJDBCDao.safeClose(dao, conn, ps, null);
        }
    }
}
