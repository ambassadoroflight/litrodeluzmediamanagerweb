package org.unlitrodeluzcolombia.mediamanager.gui.finder;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import net.comtor.framework.html.administrable.AbstractComtorFinderFactoryI18n;
import net.comtor.framework.logic.facade.WebLogicFacade;
import org.unlitrodeluzcolombia.mediamanager.element.FilmGenre;
import org.unlitrodeluzcolombia.mediamanager.web.facade.FilmGenreWebFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class FilmGenreFinder
        extends AbstractComtorFinderFactoryI18n<FilmGenre, Long> {

    @Override
    protected String getValueToHide(FilmGenre genre) {
        return genre.getId() + "";
    }

    @Override
    public String getValueToShow(FilmGenre genre) {
        return "[" + genre.getId() + "] " + genre.getName();
    }

    @Override
    public WebLogicFacade<FilmGenre, Long> getFacade() {
       return new FilmGenreWebFacade();
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(FilmGenre genre) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(genre.getId());
        row.add(genre.getName());

        return row;
    }

}
