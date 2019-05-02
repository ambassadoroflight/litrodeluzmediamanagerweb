package net.comtor.framework.common.auth.gui.aaa;

import i18n.WebFrameWorkTranslationHelper;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.aaa.ComtorUser;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.exception.BusinessLogicException;
import net.comtor.exception.ComtorException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.html.HtmlLink;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlFormElement;
import net.comtor.html.form.HtmlInputPassword;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.I18n;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.framework.common.auth.web.components.ProfileUserSelectI18n;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.web.facade.ProfileWebFacade;
import net.comtor.framework.common.auth.web.facade.UserWebFacade;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.html.administrable.ComtorAdministrableHtmlException;
import net.comtor.framework.html.administrable.ComtorAdministratorControllerHelperI18n;
import net.comtor.framework.images.Images;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.util.security.SecurityHelper;
import net.comtor.html.form.HtmlSelect;
import net.comtor.framework.util.FormUtil;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 24, 2019
 */
public class UserController extends AbstractComtorFacadeAdministratorControllerI18n<User, String> {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @Override
    public WebLogicFacade<User, String> getLogicFacade() {
        return new UserWebFacade();
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_USER";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_USER";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_USER";
    }

    @Override
    public String getEntityName() {
        return "user.entityname";
    }

    @Override
    public String getLogModule() {
        return I18n.tr(getLang(), "user.logmodule");
    }

    @Override
    protected String getTitleImgPath() {
        return Images.USERS_CONTROLLER;
    }

    @Override
    public String getFormName() {
        return "user_form";
    }

    @Override
    public void initForm(AdministrableForm form, User user) throws BusinessLogicException {
        if (user == null) {
            HtmlInputText login = new HtmlInputText("login", 16, 64);
            form.addField("user.field.login", login, null, true);
        } else {
            form.addInputHidden("login", user.getLogin());

            HtmlText username = new HtmlText(user.getLogin());
            form.addField("user.field.login", username, null);
        }

        HtmlInputPassword password = new HtmlInputPassword("password", 16, 256);
        form.addField("user.field.password", password, null, true);

        HtmlInputPassword confirmPassword = new HtmlInputPassword("confirm_password", "", 16, 256);
        form.addField("user.field.confirmpassword", confirmPassword, null, true);

        HtmlInputText name = new HtmlInputText("name", 32, 256);
        form.addField("user.field.name", name, null);

        HtmlInputText email = new HtmlInputText("email", 32, 128);
        form.addField("user.field.email", email, null);

        HtmlInputText phone = new HtmlInputText("phone", 16, 32);
        form.addField("user.field.phone", phone, null);

        HtmlSelect active = FormUtil.getYesNoSelect("active");
        form.addField("user.field.active", active, "user.field.active.hint");

        ProfileUserSelectI18n profiles = new ProfileUserSelectI18n("profiles",
                user, getRequest().getOriginalRequest());
        form.addField("user.field.profiles", profiles, "user.field.profiles.hint");
    }

    @Override
    public User getObjectFromRequest(HttpServletMixedRequest request)
            throws ComtorAdministrableHtmlException {
        User user = super.getObjectFromRequest(request);
        String login = user.getLogin();

        if (!StringUtil.isValid(login)) {
            return user;
        }

        try {
            LinkedList<Long> profiles = new ProfileWebFacade().findAllIDByUser(login);
            user.setProfiles(profiles);

            return user;
        } catch (BusinessLogicException ex) {
            throw new ComtorAdministrableHtmlException(ex);
        }
    }

    @Override
    public String processAddForm(HttpServletMixedRequest request, HttpServletResponse response)
            throws ComtorException {
        if (request.getParameter("cancel") != null) {
            return redirectByCancel(response);
        }

        User user = getObjectFromRequest(request);
        LinkedList<ObjectValidatorException> exceptions
                = getLogicFacade().validateObjectPreAdd(user);

        if (exceptions.size() > 0) {
            return createFormWithErrors(user, getAddFormLabel(), exceptions,
                    AdministrableForm.ADD_FORM, getRequest()).getHtml();
        }

        setProfiles(request, user);
        getLogicFacade().insert(user);

        return ComtorAdministratorControllerHelperI18n.continueMessageOK(getBaseUrl(),
                getLabelConfirmMessage(), getAddedMessage(user), getLabelContinue(), request);
    }

    @Override
    public String processEditForm(HttpServletMixedRequest request, HttpServletResponse response)
            throws ComtorException {
        if (request.getParameter("cancel") != null) {
            return redirectByCancel(response);
        }

        User user = getObjectFromRequest(request);
        LinkedList<ObjectValidatorException> exceptions = getLogicFacade()
                .validateObjectPreEdit(user);

        if (exceptions.size() > 0) {
            return createFormWithErrors(user, getEditFormLabel(), exceptions,
                    AdministrableForm.EDIT_FORM, getRequest()).getHtml();
        }

        setProfiles(request, user);
        getLogicFacade().update(user);

        return ComtorAdministratorControllerHelperI18n.continueMessageOK(getBaseUrl(),
                getLabelConfirmMessage(), getUpdatedMessage(user), getLabelContinue(),
                request);
    }

    @Override
    protected void fillFormOnEdit(User user, AdministrableForm form) {
        super.fillFormOnEdit(user, form);

        HtmlFormElement password = (HtmlFormElement) form.getHtmlElement("password");
        password.setValue("nochange");

        HtmlFormElement confirmPassword = (HtmlFormElement) form.getHtmlElement("confirm_password");
        confirmPassword.setValue("nochange");
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("login", "user.field.login");
        headers.put("name", "user.field.name");
        headers.put("email", "user.field.email");
        headers.put("phone", "user.field.phone");
        headers.put("active", "user.field.active");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(User user) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(user.getLogin());
        row.add(user.getName());
        row.add(new HtmlLink(user.getEmail(), "mailto:" + user.getEmail()).getHtml());
        row.add(user.getPhone());
        row.add((user.getActive() == 1)
                ? WebFrameWorkTranslationHelper.translate("answer.yes", getLang())
                : WebFrameWorkTranslationHelper.translate("answer.no", getLang()));

        return row;
    }

    @Override
    protected LinkedList<String> getBasicActionLinks(User user) {
        LinkedList<String> actions = new LinkedList<>();
        ComtorUser admin = UserHelper.getCurrentUser(getRequest());

        if ((!user.getLogin().equals(ComtorGlobal.SUPER_USER))
                || (admin.getLogin().equals(ComtorGlobal.SUPER_USER))) {
            if ((getEditPrivilege() != null)
                    && (SecurityHelper.can(getEditPrivilege(), getRequest()))) {
                actions.add(getEditIcon(getBaseUrl(), user));
            }

            if ((getDeletePrivilege() != null)
                    && (SecurityHelper.can(getDeletePrivilege(), getRequest()))) {
                actions.add(getDeleteIcon(getBaseUrl(), user));
            }
        }

        return actions;
    }

    @Override
    public String getAddNewObjectLabel() {
        return I18n.tr(getLang(), "user.add.title");
    }

    @Override
    public String getAddFormLabel() {
        return I18n.tr(getLang(), "user.new.title");
    }

    @Override
    public String getEditFormLabel() {
        return I18n.tr(getLang(), "user.edit.title");
    }

    @Override
    public String getAddedMessage(User user) {
        return getI18nMessageWithParameters(user.getLogin(), "user.message.added");
    }

    @Override
    public String getUpdatedMessage(User user) {
        return getI18nMessageWithParameters(user.getLogin(), "user.message.updated");
    }

    @Override
    public String getConfirmDeleteMessage(User user) {
        return getI18nMessageWithParameters(user.getLogin(), "user.message.confirmdelete");
    }

    @Override
    public String getDeletedMessage(User user) {
        return getI18nMessageWithParameters(user.getLogin(), "user.message.active");
    }

    @Override
    public String getAddPrivilegeMsg() {
        return "user.message.cantadd";
    }

    @Override
    public String getEditPrivilegeMsg() {
        return "user.message.cantedit";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "user.message.cantview";
    }

    @Override
    public String getDeletePrivilegeMsg() {
        return "user.message.cantdelete";
    }

    private void setProfiles(HttpServletMixedRequest request, User user) {
        String profilesID = request.getParameter("profiles");
        LinkedList<Long> profiles = new LinkedList<>();
        StringTokenizer stt = new StringTokenizer(profilesID, ";");

        while (stt.hasMoreTokens()) {
            String token = stt.nextToken();
            String id = token.replace("[", "").replace("]", "").trim();

            if (StringUtil.isValid(id)) {
                Long profile = Long.parseLong(id);
                profiles.add(profile);
            }
        }

        user.setProfiles(profiles);
    }

}
