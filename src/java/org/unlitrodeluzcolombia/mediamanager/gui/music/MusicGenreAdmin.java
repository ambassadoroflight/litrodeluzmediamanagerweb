package org.unlitrodeluzcolombia.mediamanager.gui.music;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MusicGenreAdmin extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new MusicGenreController();
    }

}
