package web.gui;

import java.util.LinkedList;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.gui.aaa.ProfileAdmin;
import net.comtor.framework.common.auth.gui.aaa.ProfileController;
import net.comtor.framework.common.auth.gui.aaa.UserAdmin;
import net.comtor.framework.common.auth.gui.aaa.UserController;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.html.administrable.AbstractComtorFacadeAdministratorController;
import net.comtor.framework.html.advanced.ComtorLinkIconFish;
import net.comtor.framework.pagefactory.index.TableIndexFactory;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.i18n.html.ComtorLinkIconFishI18n;
import org.unlitrodeluzcolombia.mediamanager.gui.db.GenerateDatabaseDumpPage;
import org.unlitrodeluzcolombia.mediamanager.gui.movies.FilmGenreAdmin;
import org.unlitrodeluzcolombia.mediamanager.gui.movies.FilmGenreController;
import org.unlitrodeluzcolombia.mediamanager.gui.movies.MovieAdmin;
import org.unlitrodeluzcolombia.mediamanager.gui.movies.MovieController;
import org.unlitrodeluzcolombia.mediamanager.gui.music.MusicGenreAdmin;
import org.unlitrodeluzcolombia.mediamanager.gui.music.MusicGenreController;
import org.unlitrodeluzcolombia.mediamanager.gui.music.SongAdmin;
import org.unlitrodeluzcolombia.mediamanager.gui.music.SongController;
import static web.global.LitroDeLuzImages.DUMP_INDEX;
import static web.global.LitroDeLuzImages.MOVIES_INDEX;
import static web.global.LitroDeLuzImages.MUSIC_INDEX;
import static web.global.LitroDeLuzImages.PROFILE_INDEX;
import static web.global.LitroDeLuzImages.USER_INDEX;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class Index extends TableIndexFactory {

    @Override
    public LinkedList<ComtorLinkIconFish> getIconLinks() {
        LinkedList<ComtorLinkIconFish> linkIcons = new LinkedList<>();

        if (can(new UserController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(UserAdmin.class),
                    USER_INDEX, "Usuarios", getRequest()));
        }

        if (can(new ProfileController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(ProfileAdmin.class),
                    PROFILE_INDEX, "PERFILES", getRequest()));
        }

        if (can(new FilmGenreController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(FilmGenreAdmin.class),
                    MOVIES_INDEX, "Géneros de Películas", getRequest()));
        }

        if (can(new MovieController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(MovieAdmin.class),
                    MOVIES_INDEX, "Películas", getRequest()));
        }

        if (can(new MusicGenreController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(MusicGenreAdmin.class),
                    MUSIC_INDEX, "Géneros Musicales", getRequest()));
        }

        if (can(new SongController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(SongAdmin.class),
                    MUSIC_INDEX, "Canciones", getRequest()));
        }

        linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(GenerateDatabaseDumpPage.class),
                DUMP_INDEX, "Generar copia de DB", getRequest()));

        return linkIcons;
    }

    @Override
    public int getCols() {
        return 4;
    }

    @Override
    public String getWelcomeMessage() {
        User user = (User) UserHelper.getCurrentUser(getRequest()).getUser();

        return "Bienvenido, " + user.getName();
    }

    private boolean can(AbstractComtorFacadeAdministratorController controller) {
        return SecurityHelper.can(controller.getDeletePrivilege(), getRequest())
                || SecurityHelper.can(controller.getEditPrivilege(), getRequest())
                || SecurityHelper.can(controller.getAddPrivilege(), getRequest())
                || SecurityHelper.can(controller.getViewPrivilege(), getRequest());
    }

    private boolean can(String privilege) {
        return SecurityHelper.can(privilege, getRequest());
    }

}
