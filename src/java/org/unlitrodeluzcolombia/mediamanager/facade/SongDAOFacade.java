package org.unlitrodeluzcolombia.mediamanager.facade;

import java.util.LinkedList;
import net.comtor.dao.ComtorDaoException;
import net.comtor.dao.generics.ComtorDaoElementLogicFacade;
import org.unlitrodeluzcolombia.mediamanager.element.Song;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class SongDAOFacade extends ComtorDaoElementLogicFacade<Song, String> {

    @Override
    public void create(Song song) throws ComtorDaoException {
        long now = System.currentTimeMillis();

        song.setUpload_date(new java.sql.Timestamp(now));
        song.setUpload_ddate(new java.sql.Date(now));
        song.setLast_update(new java.sql.Timestamp(now));

        super.create(song);
    }

    @Override
    public void edit(Song song) throws ComtorDaoException {
        long now = System.currentTimeMillis();

        song.setLast_update(new java.sql.Timestamp(now));

        super.edit(song);
    }

    public Song findFromSameAlbum(String album) throws ComtorDaoException {
        String query = getFindQuery()
                + " WHERE \n"
                + "     song.album = ? \n"
                + " AND (song.cover IS NOT NULL OR song.cover <> '') \n"
                + " LIMIT 1 \n";

        LinkedList<Song> songs = findAll(query, album);

        if (songs.isEmpty()) {
            return null;
        }

        return songs.getFirst();
    }

}
