package org.unlitrodeluzcolombia.mediamanager.gui.movies;

import net.comtor.framework.html.administrable.AbstractComtorAdministrable;
import net.comtor.framework.html.administrable.ComtorAdministratorController;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class FilmGenreAdmin extends AbstractComtorAdministrable {

    @Override
    protected ComtorAdministratorController getComtorAdministratorController() {
        return new FilmGenreController();
    }

}
