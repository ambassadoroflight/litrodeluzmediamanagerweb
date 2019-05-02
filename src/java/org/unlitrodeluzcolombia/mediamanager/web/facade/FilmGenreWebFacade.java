package org.unlitrodeluzcolombia.mediamanager.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import org.unlitrodeluzcolombia.mediamanager.element.FilmGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Movie;
import org.unlitrodeluzcolombia.mediamanager.facade.FilmGenreDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.facade.MovieDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class FilmGenreWebFacade
        extends AbstractWebLogicFacade<FilmGenre, Long, FilmGenreDAOFacade> {

    private static final Logger LOG = Logger.getLogger(FilmGenreWebFacade.class.getName());

    @Override
    public String getWhere(ComtorObjectCriterions criterions, LinkedList<Object> params) {
        String where = ""
                + " WHERE \n"
                + "     1 = 1 \n";

        for (ComtorObjectListFilter filter : criterions.getFilters()) {
            final String id = filter.getId();
            String value = filter.getValue().toString().trim();

            if (StringUtil.isValid(value)) {
                if (id.equals(ComtorFilterHelper.FILTER_NAME)) {
                    StringTokenizer stt = new StringTokenizer(value, " ");

                    while (stt.hasMoreTokens()) {
                        String token = "%" + stt.nextToken().replaceAll("'", " ") + "%";

                        where += ""
                                + " AND ( \n"
                                + "     film_genre.name LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME)
                        && (value != null)) {
                    // Si hay finder con parámetro
                } else if (filter.getType() == ComtorObjectListFilter.TYPE_EQUALS) {
                    // Si hay filtro avanzado
                } else {
                    where += " AND " + id + " = ? \n";
                    params.add(value);
                }
            }
        }

        return where;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(FilmGenre genre) {
        return validate(genre);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(FilmGenre genre) {
        return validate(genre);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreDelete(FilmGenre genre) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();
        try {
            LinkedList<Movie> songs = new MovieDAOFacade().findAllByProperty("film_genre", 
                    genre.getId());

            if (!songs.isEmpty()) {
                exceptions.add(new ObjectValidatorException("", "Hay " + songs.size()
                        + " películas asociadas a esta categoría. No se puede borrar."));
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return exceptions;
    }

    private LinkedList<ObjectValidatorException> validate(FilmGenre genre) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        final String name = genre.getName();

        if (StringUtil.isValid(name)) {
            try {
                FilmGenre other = getDaoFacade().findByProperty("name", name);

                if ((other != null) && (other.getId() != genre.getId())) {
                    exceptions.add(new ObjectValidatorException("name",
                            "filmgenre.field.name.exception.inuse"));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("name",
                    "filmgenre.field.name.exception.notempty"));
        }

        return exceptions;
    }

}
