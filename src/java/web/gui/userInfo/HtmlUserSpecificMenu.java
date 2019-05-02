package web.gui.userInfo;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.aaa.ComtorUser;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.aaa.pagefactory.QuitFactory;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.facade.ProfileDAOFacade;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.jsptag.HtmlGuiInterface;
import net.comtor.html.HtmlBr;
import net.comtor.html.HtmlImg;
import net.comtor.html.HtmlLi;
import net.comtor.html.HtmlLink;
import net.comtor.html.HtmlSpan;
import net.comtor.html.HtmlUl;
import net.comtor.i18n.I18n;
import net.comtor.i18n.LocaleHelper;
import web.gui.login.ChangePasswordPage;
import net.comtor.framework.images.Images;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class HtmlUserSpecificMenu extends HtmlGuiInterface {

    private static final Logger LOG = Logger.getLogger(HtmlUserSpecificMenu.class.getName());

    public HtmlUserSpecificMenu() {
    }

    @Override
    public String getHtml() {
        ComtorUser currentUser = UserHelper.getCurrentUser(getRequest());

        HtmlSpan span = new HtmlSpan();

        HtmlUl mainList = new HtmlUl();
        mainList.setClass("sf-menu user-menu");

        HtmlLi userListItem = new HtmlLi();
        userListItem.addElement(new HtmlImg(Images.CURRENT_USER_WHITE_16));
        userListItem.addElement(currentUser.getFullName());

//        List of user Privileges
        HtmlUl ulUserPrivileges = new HtmlUl();
        HtmlLi liUserPrivileges = new HtmlLi();
        liUserPrivileges.addAttribute("class", "user-privileges");

        HtmlSpan privilegesTitle = new HtmlSpan("privileges_title", I18n.tr(getLang(),
                "menu.privileges.title"));
        liUserPrivileges.addElement(privilegesTitle);

        HtmlSpan privileges = new HtmlSpan();
        privileges.addAttribute("id", "privileges_span");
        privileges.addElement(new HtmlBr());

        HtmlUl privileges_list = new HtmlUl();
        privileges_list.addAttribute("id", "privileges_list");
        privileges.addElement(privileges_list);

        try {
            LinkedList<Profile> profiles = new ProfileDAOFacade()
                    .findAllByUser(currentUser.getLogin());

            for (Profile profile : profiles) {
                privileges_list.addListElement(profile.getName());
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, currentUser.getLogin(), ex);
        }

        liUserPrivileges.addElement(privileges);

        HtmlLi changePassword = new HtmlLi();
        HtmlLink changePasswdLink = new HtmlLink((new HtmlImg(Images.PASSWORD_16)
                .getHtml() + I18n.tr(getLang(), "menu.changepassword")),
                ComtorGlobal.getLink(ChangePasswordPage.class), false);

        changePassword.addElement(changePasswdLink);
        liUserPrivileges.addElement(changePassword);

        HtmlLi about = new HtmlLi();
        HtmlLink aboutLink = new HtmlLink(new HtmlImg(Images.INFO_16).getHtml()
                + "Acerca de...", "#", false);
        aboutLink.addAttribute("onclick", "showAboutWindow('" + Images.CLIENT_LOGO
                + "', '" + GlobalWeb.PROJECT_NAME + "', '" + GlobalWeb.VERSION + "');");
        about.addElement(aboutLink);
        liUserPrivileges.addElement(about);

        HtmlLi quitItem = new HtmlLi();
        HtmlLink linkQuit = new HtmlLink(new HtmlImg(Images.CLOSE_16).getHtml()
                + I18n.tr(getLang(), "menu.exit"), ComtorGlobal.getLink(QuitFactory.class),
                false);
        quitItem.addElement(linkQuit);
        liUserPrivileges.addElement(quitItem);

        ulUserPrivileges.addElement(liUserPrivileges);
        userListItem.addElement(ulUserPrivileges);
        mainList.addElement(userListItem);

        span.addElement(mainList);

        return span.getHtml();
    }

    @Override
    public boolean requireComtorSession() {
        return true;
    }

    private String getLang() {
        return LocaleHelper.getLocale(getRequest());
    }
}
