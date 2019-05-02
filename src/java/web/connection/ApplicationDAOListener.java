package web.connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ApplicationDAOListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GlobalWeb.getInstance(sce.getServletContext().getRealPath(""));

        System.out.println("\n> Cargando Parametros de Conexion "
                + GlobalWeb.PROJECT_NAME + " [" + GlobalWeb.VERSION + "]: "
                + GlobalWeb.NAME_CONNECTION + "\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GlobalWeb.dispose();

        System.out.println("\n> Destruyendo Parametros de Conexion "
                + GlobalWeb.PROJECT_NAME + " [" + GlobalWeb.VERSION + "]: "
                + GlobalWeb.NAME_CONNECTION + "\n");
    }
}
