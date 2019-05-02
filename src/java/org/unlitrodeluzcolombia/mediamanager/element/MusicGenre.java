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
@ComtorElement(tableName = "music_genre")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class MusicGenre implements Serializable {

    private static final long serialVersionUID = 4925133856881757796L;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String name;
    
    public MusicGenre() {
    }

    public MusicGenre(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MusicGenre{"
                + "id=" + id
                + ", name=" + name
                + '}';
    }

}
