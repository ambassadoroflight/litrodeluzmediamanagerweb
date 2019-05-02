package net.comtor.framework.common.auth.gui.aaa;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.common.auth.element.Privilege;
import net.comtor.framework.html.administrable.ComtorAdministrableHtmlException;
import net.comtor.framework.html.administrable.ComtorAdministratorControllerHelperI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.html.HtmlElement;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.I18n;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.facade.PrivilegeDAOFacade;
import net.comtor.framework.common.auth.web.components.PrivilegeProfileAccordionCheck;
import net.comtor.framework.common.auth.web.facade.ProfileWebFacade;
import net.comtor.framework.images.Images;
import net.comtor.framework.util.FormUtil;
import net.comtor.html.HtmlText;
import net.comtor.html.HtmlUl;
import net.comtor.html.form.HtmlSelect;
import net.comtor.i18n.html.DivFormI18n;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ProfileController extends AbstractComtorFacadeAdministratorControllerI18n<Profile, Long> {

    private static final Logger LOG = Logger.getLogger(ProfileController.class.getName());

    @Override
    public WebLogicFacade<Profile, Long> getLogicFacade() {
        return new ProfileWebFacade();
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_PROFILE";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_PROFILE";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_PROFILE";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_PROFILE";
    }

    @Override
    public String getEntityName() {
        return "profile.entityname";
    }

    @Override
    public String getLogModule() {
        return I18n.tr(getLang(), "profile.logmodule");
    }

    @Override
    protected String getTitleImgPath() {
        return Images.PROFILES_CONTROLLER;
    }

    @Override
    public String getFormName() {
        return "profile_form";
    }

    @Override
    public void initForm(AdministrableForm form, Profile profile)
            throws BusinessLogicException {
        if (profile != null) {
            form.addInputHidden("id", profile.getId());
        }

        HtmlInputText name = new HtmlInputText("name", "", 32, 128);
        form.addField("profile.field.name", name, null, true);

        HtmlSelect editable = FormUtil.getYesNoSelect("editable");
        form.addField("editable", "profile.field.editable", editable, "profile.field.editable.hint");

        PrivilegeProfileAccordionCheck privileges = new PrivilegeProfileAccordionCheck("privileges", profile);
        form.addField("profile.field.privileges", privileges, "profile.field.privileges.hint", true);
    }

    @Override
    public HtmlElement getEditForm(Long key, HttpServletMixedRequest request, HttpServletResponse response)
            throws ComtorDaoException {
        try {
            Profile profile = getLogicFacade().find(key);

            if (profile.getEditable() == 0) {
                return ComtorAdministratorControllerHelperI18n.continueFormMessageInfo(getBaseUrl(),
                        getNoEditableProfileMessage(profile), request);
            }
        } catch (Exception ex) {
            throw new ComtorDaoException(ex);
        }

        return super.getEditForm(key, request, response);
    }

    @Override
    public HtmlElement getViewForm(Long key, HttpServletMixedRequest request,
            HttpServletResponse response) throws ComtorDaoException {
        try {
            Profile profile = getLogicFacade().find(key);
            LinkedList<String> profilePrivileges = profile.getPrivileges();
            PrivilegeDAOFacade privilegeDaoFacade = new PrivilegeDAOFacade();
            Privilege privilege;

            AdministrableForm form = new DivFormI18n(getBaseUrl(),
                    AdministrableForm.METHOD_POST, getRequest());
            form.setTitle(profile.getName());

            HtmlUl list = new HtmlUl();

            if (!profilePrivileges.isEmpty()) {
                for (String profilePrivilege : profilePrivileges) {
                    privilege = privilegeDaoFacade.find(profilePrivilege);
                    list.addListElement(privilege.getDescription());
                }
            }

            HtmlText profileName = new HtmlText(profile.getName());
            form.addField("profile.field.name", profileName, null);

            form.addField("profile.field.privileges", list, null);

            form.addButton("ok", "Regresar");

            return form;
        } catch (BusinessLogicException ex) {
            throw new ComtorDaoException(ex);
        }
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("name", "profile.field.name");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Profile profile) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(profile.getName());

        return row;
    }

    @Override
    public String getAddNewObjectLabel() {
        return I18n.tr(getLang(), "profile.add.title");
    }

    @Override
    public String getAddFormLabel() {
        return "profile.new.title";
    }

    @Override
    public String getEditFormLabel() {
        return "profile.edit.title";
    }

    @Override
    public String getAddedMessage(Profile profile) {
        return getI18nMessageWithParameters(profile.getName(), "profile.message.added");
    }

    @Override
    public String getUpdatedMessage(Profile profile) {
        return getI18nMessageWithParameters(profile.getName(), "profile.message.updated");
    }

    @Override
    public String getConfirmDeleteMessage(Profile profile) {
        return getI18nMessageWithParameters(profile.getName(), "profile.message.confirmdelete");
    }

    @Override
    public String getDeletedMessage(Profile profile) {
        return getI18nMessageWithParameters(profile.getName(), "profile.message.deleted");
    }

    @Override
    public String getAddPrivilegeMsg() {
        return "profile.message.cantadd";
    }

    @Override
    public String getEditPrivilegeMsg() {
        return "profile.message.cantedit";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "profile.message.canview";
    }

    @Override
    public String getDeletePrivilegeMsg() {
        return "profile.message.cantdelete";
    }

    @Override
    public Profile getObjectFromRequest(HttpServletMixedRequest request)
            throws ComtorAdministrableHtmlException {
        Profile profile = super.getObjectFromRequest(request);
        profile.setPrivileges(getPrivileges(request));

        return profile;
    }

    @Override
    protected boolean withGenericFilter() {
        return false;
    }

    private LinkedList<String> getPrivileges(HttpServletMixedRequest request) {
        String[] values = request.getParameterValues("privileges");
        LinkedList<String> list = new LinkedList<>();

        if (values != null) {
            list.addAll(Arrays.asList(values));
        }

        return list;
    }

    private String getNoEditableProfileMessage(Profile profile) {
        return I18n.tr(getLang(), "profile.message.noeditable").replace("${name}",
                profile.getName());
    }

}
