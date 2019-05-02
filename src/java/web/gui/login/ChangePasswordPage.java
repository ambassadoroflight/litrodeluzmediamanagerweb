package web.gui.login;

import net.comtor.framework.html.administrable.ComtorFormValidateAction;
import net.comtor.framework.html.administrable.ComtorFormValidateActionPageFactory;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
public class ChangePasswordPage extends ComtorFormValidateActionPageFactory {

    @Override
    public ComtorFormValidateAction getFormAction() {
        return new ChangePasswordForm(getClass());
    }

    @Override
    public boolean requireComtorSession() {
        return true;
    }
}
