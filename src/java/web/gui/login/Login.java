package web.gui.login;

import net.comtor.aaa.ComtorAAAFacade;
import net.comtor.aaa.pagefactory.LoginFactory2;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.ajax.HtmlJavaScript;
import net.comtor.framework.common.auth.DefaultComtorAAA;
import net.comtor.framework.images.Images;
import net.comtor.html.HtmlImg;
import net.comtor.html.form.HtmlButton;
import net.comtor.html.form.HtmlInputPassword;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.DivFormI18n;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class Login extends LoginFactory2 {

    private static final String LOGIN_FIELD_CSS_CLASS = "login_field";

    public Login() {
    }

    @Override
    public ComtorAAAFacade getComtorAAAFacade() {
        return new DefaultComtorAAA();
    }

    @Override
    protected AdministrableForm getForm(String login, String password, String errorMessage) {
        DivFormI18n form = new DivFormI18n(getRequest().getJspNameWithPath(), 
                AdministrableForm.METHOD_POST, getRequest());
        setTitle("login_form");
        form.setTitle(getTitle());
        form.addAttribute("id", "loginForm");

        HtmlImg logo = new HtmlImg(Images.CLIENT_LOGO);
        logo.addAttribute("id", "logoLogin");
        form.addElement(logo);

        HtmlInputText username = new HtmlInputText("login", login, 20, 64);
        username.addAttribute("class", LOGIN_FIELD_CSS_CLASS);
        username.addAttribute("tabindex", "1");
        form.addField("login", getLoginText(), username, null);

        HtmlInputPassword passwordField = new HtmlInputPassword("password", 
                password, 20, 256);
        passwordField.addAttribute("class", LOGIN_FIELD_CSS_CLASS);
        passwordField.addAttribute("id", "password");
        passwordField.addAttribute("tabindex", "2");
        form.addField("password", getPwdText(), passwordField, null);

        form.addErrorDiv(getErrorMessage(errorMessage));

        HtmlButton submitButton = new HtmlButton(HtmlButton.SUBMIT_BUTTON, "ok", 
                getBtnOkValue());
        submitButton.addAttribute("tabindex", "3");
        form.addElement(submitButton);

        form.addInputHidden("option", getClass().getCanonicalName());
        form.addRowInOneCell(getJS());

        return form;
    }

    //TODO: Quitar ese JS o ponerlo en otro lado
    private HtmlJavaScript getJS() {
        return new HtmlJavaScript("\n"
                + "// Afecta la apariencia del div 'Opciones Avanzadas' \n"
                + "$(document).ready(function() { \n"
                + "     var tab = $('#login form #extendedLoginContainer_link'); \n"
                + "     var openTab = false; \n"
                + "\n"
                + "     tab.click(function() { \n"
                + "         if (openTab == false) { \n"
                + "             $(this).css('border-radius', '7px 7px 0 0'); \n"
                + "             openTab = true; \n"
                + "         } else { \n"
                + "             $(this).css('border-radius', '7px'); \n"
                + "             openTab = false; \n"
                + "         } \n"
                + "     }); \n"
                + "\n"
                + "     // Hace que la página no tenga color de fondo en el login \n"
                + "     $('#content').css('background-color', 'transparent'); \n"
                + "     $('#footer').css('position', 'fixed'); \n"
                + "     $('#footer').css('bottom', '0'); \n"
                + "     $('#content').css('position', 'absolute'); \n"
                + "}); \n"
                + "");
    }
}
