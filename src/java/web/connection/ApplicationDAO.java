package web.connection;

import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.ComtorJDBCDataSourceDao;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ApplicationDAO extends ComtorJDBCDataSourceDao {

    private static final Logger LOG = Logger.getLogger(ApplicationDAO.class.getName());

    private static String DRIVER = GlobalWeb.getInstance().getDatabaseDriver();
    private static String URL = GlobalWeb.getInstance().getDatabaseURL();
    private static String USER = GlobalWeb.getInstance().getDatabaseUser();
    private static String PASSWORD = GlobalWeb.getInstance().getDatabasePassword();

    public ApplicationDAO() throws ComtorDaoException {
        super(DRIVER, URL, USER, PASSWORD);
    }
}
