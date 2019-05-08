package web.global;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import net.comtor.util.connection.ComtorConnection;
import net.comtor.util.connection.ComtorConnections;
import net.comtor.util.connection.ComtorConnectionsXmlReader;
import net.comtor.util.connection.ConnectionType;
import org.xml.sax.SAXException;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class GlobalWeb {

    private static final Logger LOG = Logger.getLogger(GlobalWeb.class.getName());

    public static final String PROJECT_NAME = "Media Manager";
    public static String VERSION = "1.0";
    public static final String LAST_UPDATED = "[18-Feb-2019]";
    public static final ConnectionType CONNECTION_TYPE = ConnectionType.DEVELOPMENT;
    public static final String NAME_CONNECTION = "litrodeluz_mediamanager";

    public static final String BASE_PATH = File.separator + "opt";

    public static final String FILES_DIRECTORY_PATH = BASE_PATH + File.separator
            + "litro_de_luz" + File.separator + "media_manager";
    public static final String MOVIES_DIRECTORY_PATH = FILES_DIRECTORY_PATH
            + File.separator + "peliculas";
    public static final String MUSIC_DIRECTORY_PATH = FILES_DIRECTORY_PATH
            + File.separator + "musica";
    public static final String IMAGES_DIRECTORY_PATH = FILES_DIRECTORY_PATH
            + File.separator + "imagenes";

    private static final String UPLOADS_HOME = CONNECTION_TYPE.equals(ConnectionType.DEVELOPMENT)
            ? "juandiego" : "uploads";

    public static final String UPLOADS_PATH = File.separator + "home"
            + File.separator + UPLOADS_HOME + File.separator + "uploads";

    public static final String UPLOADS_MOVIES_PATH = UPLOADS_PATH + File.separator
            + "peliculas";
    public static final String UPLOADS_MUSIC_PATH = UPLOADS_PATH + File.separator
            + "musica";

    private String databaseDriver;
    private String databaseURL;
    private String databaseUser;
    private String databasePassword;

    private static GlobalWeb instance;

    static {
        switch (CONNECTION_TYPE) {
            case DEVELOPMENT:
            case TEST:
                VERSION += " (Desarrollo)";
                break;
        }
    }

    public static GlobalWeb getInstance(String pathProject) {
        if (instance == null) {
            instance = new GlobalWeb(pathProject);
        }

        return instance;
    }

    public static GlobalWeb getInstance() {
        return instance;
    }

    public static void dispose() {
        instance = null;
    }

    private GlobalWeb(String pathProject) {
        try {
            ComtorConnections connections = ComtorConnectionsXmlReader.loadComtorConnections(pathProject);
            ComtorConnection connection = connections.getComtorConnectionByName(NAME_CONNECTION);

            databaseDriver = connection.getDriver();
            databasePassword = connection.getPassword();
            databaseURL = connection.getUrl();
            databaseUser = connection.getUser();
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            LOG.log(Level.SEVERE, NAME_CONNECTION, ex);
        }
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}
