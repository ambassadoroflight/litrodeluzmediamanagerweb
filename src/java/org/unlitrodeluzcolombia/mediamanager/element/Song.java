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
@ComtorElement(tableName = "song")
@ComtorDaoFactory(factory = ApplicationDAO.class)
public class Song implements Serializable {

    private static final long serialVersionUID = -7612257264115855628L;

    @ComtorId
    private String code;
    private String title;
    private long music_genre;
    private String album;
    private String artist;
    private int release_year;
    private java.sql.Timestamp upload_date;
    private java.sql.Date upload_ddate;
    private java.sql.Timestamp last_update;
    private String filename;
    private String cover;
    private boolean active;

    @ComtorForeingField(referencesClass = MusicGenre.class, foreingColumn = "name", referencesColumn = "music_genre")
    private String music_genre_name;

    @ComtorField(insertable = false, updatable = false, selectable = false, findable = false)
    private String original_filename;

    public Song() {
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

    public long getMusic_genre() {
        return music_genre;
    }

    public void setMusic_genre(long music_genre) {
        this.music_genre = music_genre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
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

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMusic_genre_name() {
        return music_genre_name;
    }

    public void setMusic_genre_name(String music_genre_name) {
        this.music_genre_name = music_genre_name;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }

    public String getStatus() {
        return active ? "Disponible" : "No disponible";
    }

    @Override
    public String toString() {
        return "Song{"
                + "code=" + code
                + ", title=" + title
                + ", music_genre=" + music_genre
                + ", album=" + album
                + ", artist=" + artist
                + ", release_year=" + release_year
                + ", upload_date=" + upload_date
                + ", last_update=" + last_update
                + ", filename=" + filename
                + ", cover=" + cover
                + ", active=" + active
                + '}';
    }

}
