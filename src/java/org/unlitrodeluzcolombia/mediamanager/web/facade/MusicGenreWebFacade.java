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
import org.unlitrodeluzcolombia.mediamanager.element.MusicGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Song;
import org.unlitrodeluzcolombia.mediamanager.facade.MusicGenreDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.facade.SongDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MusicGenreWebFacade
        extends AbstractWebLogicFacade<MusicGenre, Long, MusicGenreDAOFacade> {

    private static final Logger LOG = Logger.getLogger(MusicGenreWebFacade.class.getName());

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
                                + "     music_genre.name LIKE ? \n"
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
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(MusicGenre genre) {
        return validate(genre);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(MusicGenre genre) {
        return validate(genre);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreDelete(MusicGenre genre) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();
        try {
            LinkedList<Song> songs = new SongDAOFacade().findAllByProperty("music_genre",
                    genre.getId());

            if (!songs.isEmpty()) {
                exceptions.add(new ObjectValidatorException("", "Hay " + songs.size()
                        + " canciones asociadas a esta categoría. No se puede borrar."));
            }
        } catch (ComtorDaoException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return exceptions;
    }

    private LinkedList<ObjectValidatorException> validate(MusicGenre genre) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        final String name = genre.getName();

        if (StringUtil.isValid(name)) {
            try {
                MusicGenre other = getDaoFacade().findByProperty("name", name);

                if ((other != null) && (other.getId() != genre.getId())) {
                    exceptions.add(new ObjectValidatorException("name", "El nombre"
                            + " <b><i>" + name + "</i></b> ya está en uso."));
                }
            } catch (ComtorDaoException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("name", "Debe ingresar"
                    + " el nombre del género."));
        }

        return exceptions;
    }

}
