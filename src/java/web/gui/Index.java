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
import net.comtor.i18n.I18n;
import net.comtor.i18n.html.ComtorLinkIconFishI18n;
import static net.comtor.framework.images.Images.PROFILES_INDEX;
import static net.comtor.framework.images.Images.USERS_INDEX;

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
                    USERS_INDEX, "user.entityname.plural", getRequest()));
        }

        if (can(new ProfileController())) {
            linkIcons.add(new ComtorLinkIconFishI18n(ComtorGlobal.getLink(ProfileAdmin.class),
                    PROFILES_INDEX, "profile.entityname.plural", getRequest()));
        }

        return linkIcons;
    }

    @Override
    public int getCols() {
        return 4;
    }

    @Override
    public String getWelcomeMessage() {
        User user = (User) UserHelper.getCurrentUser(getRequest()).getUser();
        String currentUser = user.getName();
        String welcomeMessage = I18n.tr(getLang(), "index.welcomemessage")
                .replace("${currentUser}", currentUser);

        return welcomeMessage;
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
