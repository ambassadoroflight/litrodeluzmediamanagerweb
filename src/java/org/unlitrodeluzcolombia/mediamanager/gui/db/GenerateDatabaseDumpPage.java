package org.unlitrodeluzcolombia.mediamanager.gui.db;

import net.comtor.framework.html.administrable.ComtorFormValidateAction;
import net.comtor.framework.html.administrable.ComtorFormValidateActionPageFactory;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Mar 08, 2019
 */
public class GenerateDatabaseDumpPage extends ComtorFormValidateActionPageFactory {

    @Override
    public ComtorFormValidateAction getFormAction() {
        return new GenerateDatabaseDumpForm(this.getClass());
    }

    @Override
    public boolean requireComtorSession() {
        return true;
    }

}
