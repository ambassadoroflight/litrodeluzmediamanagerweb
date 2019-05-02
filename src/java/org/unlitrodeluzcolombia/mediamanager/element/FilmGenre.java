package org.unlitrodeluzcolombia.mediamanager.element;

import java.io.Serializable;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorId;
import net.comtor.dao.annotations.ComtorSequence;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
@ComtorElement(tableName = "film_genre")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class FilmGenre implements Serializable {

    private static final long serialVersionUID = -7225494829291390994L;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String name;

    public FilmGenre() {
    }

    public FilmGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FilmGenre{"
                + "id=" + id
                + ", name=" + name
                + '}';
    }

}
