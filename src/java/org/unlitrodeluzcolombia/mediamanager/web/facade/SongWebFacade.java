package org.unlitrodeluzcolombia.mediamanager.web.facade;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import org.unlitrodeluzcolombia.mediamanager.element.Song;
import org.unlitrodeluzcolombia.mediamanager.enums.FilePrefix;
import org.unlitrodeluzcolombia.mediamanager.facade.SongDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.utils.EntityUtils;
import static org.unlitrodeluzcolombia.mediamanager.utils.EntityUtils.generateUniqueCode;
import org.unlitrodeluzcolombia.mediamanager.utils.FileUtils;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class SongWebFacade extends AbstractWebLogicFacade<Song, String, SongDAOFacade> {

    private static final Logger LOG = Logger.getLogger(SongWebFacade.class.getName());
//TODO: VALIDAR CUANDO EL ARCHIVO NO EXISTA
    @Override
    public void insert(Song song) throws BusinessLogicException {
        try {
            song.setCode(generateUniqueCode(FilePrefix.MUSIC));

            File srcFile = new File(GlobalWeb.UPLOADS_MUSIC_PATH + File.separator
                    + song.getOriginal_filename());

            String filename = EntityUtils.getFilename(song.getCode(), srcFile);

            File desFile = new File(GlobalWeb.MUSIC_DIRECTORY_PATH + File.separator
                    + filename);

            boolean renameTo = srcFile.renameTo(desFile);

            if (!renameTo) {
                throw new BusinessLogicException("Hubo un error en la copia...");
            }

            song.setFilename(filename);

            //FIXME: MEJORAR ESTA PARTE DE LA LÓGICA
            File cover_file = getRequest().getMparser().getFile("cover_file");

            if (cover_file == null) {
                Song other = getDaoFacade().findFromSameAlbum(song.getAlbum());

                if (other != null) {
                    song.setCover(other.getCover());
                    song.setRelease_year(other.getRelease_year());
                }
            } else {
                String cover = EntityUtils.getFilename(song.getCode(), cover_file);

                if (StringUtil.isValid(song.getAlbum())) {
                    cover = EntityUtils.getFilenameFromAlbum(song.getCode(), song.getAlbum(), cover_file);
                } else {
                    cover = EntityUtils.getFilename(song.getCode(), cover_file);
                }

                FileUtils.createPhotoThumbnail(cover_file, cover);

                song.setCover(cover);
            }

            super.insert(song);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex.getMessage(), ex);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }

    }

    @Override
    public void update(Song song) throws BusinessLogicException {
        try {
            if (StringUtil.isValid(song.getOriginal_filename())) {
                File srcFile = new File(GlobalWeb.UPLOADS_MUSIC_PATH + File.separator
                        + song.getOriginal_filename());

                String filename;

                filename = EntityUtils.getFilename(song.getCode(), srcFile);

                File desFile = new File(GlobalWeb.MUSIC_DIRECTORY_PATH + File.separator
                        + filename);

                boolean renameTo = srcFile.renameTo(desFile);

                if (!renameTo) {
                    throw new BusinessLogicException("Hubo un error en la copia...");
                }
            }

            File cover_file = getRequest().getMparser().getFile("cover_file");

            if (cover_file == null) {
                Song other = getDaoFacade().findFromSameAlbum(song.getAlbum());

                if (other != null) {
                    song.setCover(other.getCover());
                    song.setRelease_year(other.getRelease_year());
                }
            } else {
                String cover = EntityUtils.getFilename(song.getCode(), cover_file);

                if (StringUtil.isValid(song.getAlbum())) {
                    cover = EntityUtils.getFilenameFromAlbum(song.getCode(), song.getAlbum(), cover_file);
                } else {
                    cover = EntityUtils.getFilename(song.getCode(), cover_file);
                }

                FileUtils.createPhotoThumbnail(cover_file, cover);

                song.setCover(cover);
            }

            super.update(song);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex.getMessage(), ex);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public boolean delete(String key) throws BusinessLogicException {
        try {
            Song song = find(key);

            if (song != null) {
                String songPath = GlobalWeb.MUSIC_DIRECTORY_PATH + File.separator
                        + song.getFilename();
                String coverPath = GlobalWeb.IMAGES_DIRECTORY_PATH
                        + File.separator + song.getCover();

                boolean deleted = FileUtils.deleteFiles(songPath, coverPath);

                getDaoFacade().remove(song);

                return true && deleted;
            }
        } catch (IOException ex) {
            throw new BusinessLogicException(ex);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }

        return false;
    }

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
                                + "     song.title LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME)
                        && (filter.getValue() != null)) {
                    // Si hay finder con parámetro
                } else if (filter.getType() == ComtorObjectListFilter.TYPE_EQUALS) {
                    // Si hay filtro avanzado
                    switch (filter.getId()) {
                        case "song.code":
                            where += " AND song.code LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "song.title":
                            where += " AND song.title LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "song.music_genre":
                            where += " AND song.music_genre = ? \n";
                            params.add(value);
                            break;
                        case "song.album":
                            where += " AND song.album LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "song.artist":
                            where += " AND song.artist LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "song.release_year":
                            where += " AND movie.release_year = ? \n";
                            params.add(value);
                            break;
                        case "song.upload_ddate":
                            where += " AND song.upload_ddate = ? \n";
                            params.add(value);
                            break;
                        case "song.active":
                            where += " AND song.active = ? \n";
                            params.add(value);
                            break;
                    }
                } else {
                    where += " AND " + id + " = ? \n";
                    params.add(value);
                }
            }
        }

        return where;
    }

    @Override
    public String getOrder(ComtorObjectCriterions criterions) {
        return "\n"
                + " ORDER BY \n"
                + "     song.title ASC \n";
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Song song) {
        return validate(song);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Song song) {
        return validate(song);
    }

    private LinkedList<ObjectValidatorException> validate(Song song) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (!StringUtil.isValid(song.getOriginal_filename())
                && !StringUtil.isValid(song.getFilename())) {
            exceptions.add(new ObjectValidatorException("original_filename",
                    "Debe indicar el archivo de la canción"));
        }

        if (!StringUtil.isValid(song.getTitle())) {
            exceptions.add(new ObjectValidatorException("title", "Debe indicar el"
                    + " título de la canción."));
        }

        if (song.getMusic_genre() <= 0) {
            exceptions.add(new ObjectValidatorException("music_genre",
                    "Debe indicar la categoría de la canción."));
        }

        if (!StringUtil.isValid(song.getArtist())) {
            exceptions.add(new ObjectValidatorException("artist", "Debe indicar el"
                    + " artista de la canción."));
        }

        File cover_file = request.getMparser().getFile("cover_file");

        if (cover_file != null) {
            if (!FileUtils.hasValidFormat(cover_file, FileUtils.Type.IMG)) {
                exceptions.add(new ObjectValidatorException("cover_file",
                        "Debe subir una imagen en formato <b><i>png o jpeg.</i></b>"));
            } else if (!FileUtils.isValidImageFile(cover_file)) {
                exceptions.add(new ObjectValidatorException("cover_file",
                        "La imagen que está intentando cargar tiene un perfil "
                        + "no soportado para la carga. Intente guardar "
                        + "la imagen en otro formato."));
            }
        }

        return exceptions;
    }

}
