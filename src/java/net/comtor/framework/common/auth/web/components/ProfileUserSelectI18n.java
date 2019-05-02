package net.comtor.framework.common.auth.web.components;

import i18n.WebFrameWorkTranslationHelper;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.comtor.dao.ComtorDaoException;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlTable;
import net.comtor.html.HtmlTd;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlFormElement;
import net.comtor.html.form.HtmlInputHidden;
import net.comtor.html.form.HtmlSelect;
import net.comtor.i18n.LocaleHelper;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.facade.ProfileDAOFacade;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2019
 */
public class ProfileUserSelectI18n implements HtmlFormElement, HtmlElement {

    private static final Logger LOG = Logger.getLogger(ProfileUserSelectI18n.class.getName());

    private HttpServletRequest request;
    private HtmlSelect selected;
    private HtmlSelect available;
    private HtmlTable table;
    private String name;
    private HtmlInputHidden hidden;
    private int selectSize;

    /**
     *
     * @param name
     * @param user
     * @param request
     */
    public ProfileUserSelectI18n(String name, User user, HttpServletRequest request) {
        this(name, user, 5, request);
    }

    public ProfileUserSelectI18n(String name, User user, int selectSize, HttpServletRequest request) {
        this.name = name;
        this.request = request;
        this.selectSize = selectSize;

        initHtmlSelect(user);
        initProfiles(user);
    }

    @Override
    public String getHtml() {
        return table.getHtml();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return hidden.getValue();
    }

    @Override
    public void setEnable(boolean enable) {
        selected.setEnable(enable);
        available.setEnable(enable);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setValue(String value) {
        try {
            available.removeAllElements();
            selected.removeAllElements();
            hidden.setValue(value);

            StringTokenizer stt = new StringTokenizer(value, ";");
            LinkedList<Long> channels = new LinkedList<>();

            while (stt.hasMoreTokens()) {
                String token = stt.nextToken();
                String channel = token.replace("[", "").replace("]", "").trim();

                if (StringUtil.isValid(channel)) {
                    channels.add(Long.parseLong(channel));
                }
            }

            LinkedList<Profile> profiles = new ProfileDAOFacade().findAll();

            for (Profile profile : profiles) {
                if (containsProfile(channels, profile.getId())) {
                    selected.addOption("" + profile.getId(), profile.getName());
                } else {
                    available.addOption("" + profile.getId(), profile.getName());
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void setTabIndex(int index) {
        available.setTabIndex(index);
        selected.setTabIndex(index);
    }

    /**
     * Returns the default user-defined language
     *
     * @return
     */
    protected final String getLang() {
        return LocaleHelper.getLocale(request);
    }

    /**
     *
     * @param user
     */
    private void initProfiles(User user) {
        try {
            LinkedList<Profile> profiles = new ProfileDAOFacade().findAll();

            for (Profile profile : profiles) {
                if ((user != null) && containsProfile(user.getProfiles(), profile.getId())) {
                    selected.addOption("" + profile.getId(), profile.getName());
                } else {
                    available.addOption("" + profile.getId(), profile.getName());
                }
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private boolean containsProfile(LinkedList<Long> userProfiles, Long profile) {
        for (Long userProfile : userProfiles) {
            if (userProfile == profile) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param user
     */
    private void initHtmlSelect(User user) {
        available = new HtmlSelect(name + "_available", 200);
        available.addAttribute("size", "" + selectSize);
        available.addAttribute("multiple", null);

        selected = new HtmlSelect(name + "_selected", 200);
        selected.addAttribute("size", "" + selectSize);
        selected.addAttribute("multiple", null);

        table = new HtmlTable();
        table.addAttribute("id", "profile-selector");
        table.addCell(new HtmlText("<b>" + getLabelAvailable() + "</b>", false),
                HtmlTd.ALIGN_CENTER);
        table.addCell(new HtmlText(""));
        table.addCell(new HtmlText("<b>" + getLabelSelected() + "</b>", false),
                HtmlTd.ALIGN_CENTER);
        table.nextRow();

        HtmlTd td = table.addCell(available);
        td.addAttribute("rowSpan", "2");
        HtmlButton button = new HtmlButton(HtmlButton.SCRIPT_BUTTON,
                "add_profile_button", ">>");
        button.onClick("addProfiles('" + name + "_selected', '" + name
                + "_available', '" + name + "')");
        table.addCell(button);
        td = table.addCell(selected);
        td.addAttribute("rowSpan", "2");
        table.nextRow();

        String value = "";

        if (user != null) {
            for (Long profile : user.getProfiles()) {
                value += (profile + ";");
            }

            value = value.endsWith(";")
                    ? value.substring(0, value.length() - 1)
                    : value;
        }

        hidden = new HtmlInputHidden(name, name, value);
        td.addElement(hidden);

        button = new HtmlButton(HtmlButton.SCRIPT_BUTTON, "delete_profile_button", "<<");
        button.onClick("deleteProfiles('" + name + "_selected', '" + name
                + "_available', '" + name + "')");
        table.addCell(button);
        table.nextRow();
    }

    /**
     *
     * @return
     */
    private String getLabelAvailable() {
        return WebFrameWorkTranslationHelper.translate("profile.available", getLang());
    }

    /**
     *
     * @return
     */
    private String getLabelSelected() {
        return WebFrameWorkTranslationHelper.translate("profile.selected", getLang());
    }

}
