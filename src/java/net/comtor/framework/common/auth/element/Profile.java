package net.comtor.framework.common.auth.element;

import java.io.Serializable;
import java.util.LinkedList;
import net.comtor.dao.ComtorJDBCDao;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorField;
import net.comtor.dao.annotations.ComtorId;
import net.comtor.dao.annotations.ComtorSequence;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
@ComtorElement(tableName = "profile")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Profile implements Serializable {

    private static final long serialVersionUID = -686184832394688266L;

    public static final int ADMIN_PROFILE = 1;

    @ComtorId
    @ComtorSequence(name = ComtorJDBCDao.MYSQL_SEQUENCE, typeInsert = ComtorSequence.POST_INSERT)
    private long id;
    private String name;
    private int editable;

    @ComtorField(insertable = false, updatable = false, findable = false, selectable = false)
    private LinkedList<String> privileges;

    public Profile() {
    }

    public Profile(String name, int editable) {
        this.name = name;
        this.editable = editable;
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

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public LinkedList<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(LinkedList<String> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "Profile{"
                + "id=" + id
                + ", name=" + name
                + ", editable=" + editable
                + '}';
    }

}
