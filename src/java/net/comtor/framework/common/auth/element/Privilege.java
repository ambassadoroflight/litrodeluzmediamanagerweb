package net.comtor.framework.common.auth.element;

import java.io.Serializable;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorId;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Jan 23, 2019
 */
@ComtorElement(tableName = "privilege")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Privilege implements Serializable {

    private static final long serialVersionUID = -1518373117958624477L;

    @ComtorId
    private String code;
    private String description;
    private String category;

    public Privilege() {
    }

    public Privilege(String code, String description, String category) {
        this.code = code;
        this.description = description;
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Privilege{"
                + "code=" + code
                + ", description=" + description
                + ", category=" + category
                + '}';
    }

}
