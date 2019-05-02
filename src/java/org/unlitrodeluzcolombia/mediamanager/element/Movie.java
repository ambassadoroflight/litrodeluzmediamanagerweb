package org.unlitrodeluzcolombia.mediamanager.element;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import net.comtor.dao.annotations.ComtorDaoFactory;
import net.comtor.dao.annotations.ComtorElement;
import net.comtor.dao.annotations.ComtorField;
import net.comtor.dao.annotations.ComtorForeingField;
import net.comtor.dao.annotations.ComtorId;
import web.connection.ApplicationDAO;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
@ComtorElement(tableName = "movie")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Movie implements Serializable {

    private static final long serialVersionUID = 3339094240759901164L;

    @ComtorId
    private String code;
    private String title;
    private long film_genre;
    private int release_year;
    private String synopsis;
    private java.sql.Timestamp upload_date;
    private java.sql.Date upload_ddate;
    private java.sql.Timestamp last_update;
    private String filename;
    private String poster;
    private boolean active;

    @ComtorForeingField(referencesClass = FilmGenre.class, foreingColumn = "name", referencesColumn = "film_genre")
    private String film_genre_name;

    @ComtorField(insertable = false, updatable = false, selectable = false, findable = false)
    private String original_filename;

    public Movie() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getFilm_genre() {
        return film_genre;
    }

    public void setFilm_genre(long film_genre) {
        this.film_genre = film_genre;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Timestamp getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Timestamp upload_date) {
        this.upload_date = upload_date;
    }

    public Date getUpload_ddate() {
        return upload_ddate;
    }

    public void setUpload_ddate(Date upload_ddate) {
        this.upload_ddate = upload_ddate;
    }

    public java.sql.Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(java.sql.Timestamp last_update) {
        this.last_update = last_update;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFilm_genre_name() {
        return film_genre_name;
    }

    public void setFilm_genre_name(String film_genre_name) {
        this.film_genre_name = film_genre_name;
    }

    public String getStatus() {
        return active ? "Disponible" : "No disponible";
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }

    @Override
    public String toString() {
        return "Movie{"
                + "code=" + code
                + ", title=" + title
                + ", film_genre=" + film_genre
                + ", release_year=" + release_year
                + ", synopsis=" + synopsis
                + ", upload_date=" + upload_date
                + ", last_update=" + last_update
                + ", filename=" + filename
                + ", poster=" + poster
                + ", active=" + active
                + '}';
    }

}
