package org.unlitrodeluzcolombia.mediamanager.facade;

import java.sql.Timestamp;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import org.unlitrodeluzcolombia.mediamanager.element.Movie;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MovieDAOFacade extends ComtorDaoElementLogicFacade<Movie, String> {

    @Override
    public void create(Movie movie) throws ComtorDaoException {
        long now = System.currentTimeMillis();

        movie.setUpload_date(new Timestamp(now));
        movie.setUpload_ddate(new java.sql.Date(now));
        movie.setLast_update(new Timestamp(now));

        super.create(movie);
    }

    @Override
    public void edit(Movie movie) throws ComtorDaoException {
        long now = System.currentTimeMillis();

        movie.setLast_update(new Timestamp(now));

        super.edit(movie);
    }

}
