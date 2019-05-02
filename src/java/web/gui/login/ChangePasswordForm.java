package web.gui.login;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.exception.ComtorException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorMessageHelperI18n;
import net.comtor.framework.jsptag.HtmlGuiInterface;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.framework.request.validator.RequestBasicValidator;
import net.comtor.aaa.ComtorUser;
import net.comtor.html.form.HtmlInputPassword;
import net.comtor.aaa.helper.PasswordHelper2;
import net.comtor.aaa.helper.UserHelper;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.web.facade.UserWebFacade;
import net.comtor.framework.images.Images;
import net.comtor.i18n.I18n;
import net.comtor.i18n.html.AbstractComtorFormValidateActionI18n;
import net.comtor.i18n.html.DivFormI18n;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ChangePasswordForm extends AbstractComtorFormValidateActionI18n {

    private static final Logger LOG = Logger.getLogger(ChangePasswordForm.class.getName());

    private static final String CURRENT_PASSWORD = "current_password";
    private static final String NEW_PASSWORD = "new_password";
    private static final String CONFIRM_NEW_PASSWORD = "confirm_new_password";

    public ChangePasswordForm(String option) {
        super(option);
    }

    public ChangePasswordForm(Class clazz) {
        super(clazz);
    }

    @Override
    protected String getTitleImgPath() {
        return Images.PASSWORD_CONTROLLER;
    }

    @Override
    protected String getTitleText() {
        return I18n.tr(getLang(), "changepassword.titletext");
    }

    @Override
    protected AdministrableForm createForm(HttpServletMixedRequest request,
            HttpServletResponse response) {
        AdministrableForm form = new DivFormI18n(getOption(),
                AdministrableForm.METHOD_POST, getRequest());
        form.setFormName("change_password_form");
        form.setTitle(getTitleText());
        form.addInputHidden("user", UserHelper.getCurrentUser(request).getLogin());

        HtmlInputPassword current_password = new HtmlInputPassword(CURRENT_PASSWORD, 16, 256);
        form.addField("changepassword.field.currentpassword", current_password, null, true);

        HtmlInputPassword new_password = new HtmlInputPassword(NEW_PASSWORD, 16, 256);
        form.addField("changepassword.field.newpassword", new_password, null, true);

        HtmlInputPassword confirm_new_password = new HtmlInputPassword(CONFIRM_NEW_PASSWORD, 16, 256);
        form.addField("changepassword.field.confirmpassword", confirm_new_password, null, true);

        form.addBasicButtons("button.ok", "button.cancel");

        return form;
    }

    @Override
    public void fillFormAtInit(HttpServletMixedRequest request) {
    }

    @Override
    public LinkedList<ObjectValidatorException> validateRequest(HttpServletMixedRequest request) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        try {
            ComtorUser comtorUser = (ComtorUser) request.getSession()
                    .getAttribute(HtmlGuiInterface.COMTOR_SESSION);
            User user = new UserWebFacade().find(comtorUser.getLogin());
            String current_password = RequestBasicValidator.getStringFromRequest(request, CURRENT_PASSWORD);
            String new_password = RequestBasicValidator.getStringFromRequest(request, NEW_PASSWORD);
            String confirm_new_password = RequestBasicValidator.getStringFromRequest(request, CONFIRM_NEW_PASSWORD);

            if (!StringUtil.isValid(current_password)) {
                exceptions.add(new ObjectValidatorException(CURRENT_PASSWORD,
                        I18n.tr(getLang(), "changepassword.field.currentpassword.exception1")));
            } else {
                if (!isSamePassword((user.getSalt() + current_password), user.getPassword())) {
                    exceptions.add(new ObjectValidatorException(CURRENT_PASSWORD,
                            I18n.tr(getLang(), "changepassword.field.currentpassword.exception2")));
                }
            }

            if (StringUtil.isValid(new_password)) {
                if (!new_password.equals(confirm_new_password)) {
                    exceptions.add(new ObjectValidatorException(NEW_PASSWORD,
                            I18n.tr(getLang(), "changepassword.field.newpassword.exepcetion2")));
                    exceptions.add(new ObjectValidatorException(CONFIRM_NEW_PASSWORD,
                            I18n.tr(getLang(), "changepassword.field.newpassword.exepcetion2")));
                }
            } else {
                exceptions.add(new ObjectValidatorException(NEW_PASSWORD,
                        I18n.tr(getLang(), "changepassword.field.newpassword.exepcetion1")));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return exceptions;
    }

    @Override
    public String processForm(HttpServletMixedRequest request, HttpServletResponse response)
            throws ComtorException {
        if (request.getParameter("cancel") == null) {
            try {
                ComtorUser comtorUser = (ComtorUser) request.getSession()
                        .getAttribute(HtmlGuiInterface.COMTOR_SESSION);
                String new_password = RequestBasicValidator.getStringFromRequest(request, NEW_PASSWORD);
                UserWebFacade userWebFacade = new UserWebFacade();

                User user = userWebFacade.find(comtorUser.getLogin());
                user.setPassword(new_password);
                userWebFacade.update(user);

                return ComtorMessageHelperI18n.getInfoForm(getTitleText(),
                        getRequest().getJspNameWithPath(), "changepassword.message.updated",
                        request).getHtml();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        redirectFromCancel(request, response);

        return "";
    }

    private boolean isSamePassword(String inPassword, String encryptedPassword) {
        return PasswordHelper2.getHelper().encryption(inPassword).equals(encryptedPassword);
    }

}
