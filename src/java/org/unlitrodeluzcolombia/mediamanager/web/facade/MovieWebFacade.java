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
import org.unlitrodeluzcolombia.mediamanager.element.Movie;
import org.unlitrodeluzcolombia.mediamanager.enums.FilePrefix;
import org.unlitrodeluzcolombia.mediamanager.facade.MovieDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.utils.EntityUtils;
import static org.unlitrodeluzcolombia.mediamanager.utils.EntityUtils.generateUniqueCode;
import org.unlitrodeluzcolombia.mediamanager.utils.FileUtils;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MovieWebFacade
        extends AbstractWebLogicFacade<Movie, String, MovieDAOFacade> {

    private static final Logger LOG = Logger.getLogger(MovieWebFacade.class.getName());

    @Override
    public void insert(Movie movie) throws BusinessLogicException {
        try {
            movie.setCode(generateUniqueCode(FilePrefix.MOVIE));

            File srcFile = new File(GlobalWeb.UPLOADS_MOVIES_PATH + File.separator
                    + movie.getOriginal_filename());

            String filename = EntityUtils.getFilename(movie.getCode(), srcFile);

            File desFile = new File(GlobalWeb.MOVIES_DIRECTORY_PATH + File.separator
                    + filename);

            boolean renameTo = srcFile.renameTo(desFile);

            if (!renameTo) {
                throw new BusinessLogicException("Hubo un error en la copia...");
            }

            movie.setFilename(filename);

            File poster_file = getRequest().getMparser().getFile("poster_file");

            if (poster_file != null) {
                String poster = EntityUtils.getFilename(movie.getCode(), poster_file);

                FileUtils.createPhotoThumbnail(poster_file, poster);

                movie.setPoster(poster);
            }

            super.insert(movie);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Movie movie) throws BusinessLogicException {
        try {
            if (StringUtil.isValid(movie.getOriginal_filename())) {
                File srcFile = new File(GlobalWeb.UPLOADS_MOVIES_PATH + File.separator
                        + movie.getOriginal_filename());

                File desFile = new File(GlobalWeb.MOVIES_DIRECTORY_PATH + File.separator
                        + movie.getFilename());

                boolean renameTo = srcFile.renameTo(desFile);

                if (!renameTo) {
                    throw new BusinessLogicException("Hubo un error en la copia...");
                }
            }

            File poster_file = getRequest().getMparser().getFile("poster_file");

            if ((poster_file != null)) {
                String poster = EntityUtils.getFilename(movie.getCode(), poster_file);

                FileUtils.createPhotoThumbnail(poster_file, movie.getPoster());

                movie.setPoster(poster);
            }

            super.update(movie);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            throw new BusinessLogicException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean delete(String key) throws BusinessLogicException {
        try {
            Movie movie = find(key);

            if (movie != null) {
                String moviePath = GlobalWeb.MOVIES_DIRECTORY_PATH + File.separator
                        + movie.getFilename();
                String posterPath = GlobalWeb.IMAGES_DIRECTORY_PATH
                        + File.separator + movie.getPoster();

                boolean deleted = FileUtils.deleteFiles(moviePath, posterPath);

                getDaoFacade().remove(movie);

                return true && deleted;
            }
        } catch (IOException ex) {
            Logger.getLogger(MovieWebFacade.class.getName()).log(Level.SEVERE, null, ex);
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
                                + "     movie.title LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME)
                        && (filter.getValue() != null)) {
                    // Si hay finder con parámetro
                } else if (filter.getType() == ComtorObjectListFilter.TYPE_EQUALS) {
                    // Si hay filtro avanzado
                    switch (filter.getId()) {
                        case "movie.code":
                            where += " AND movie.code LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "movie.title":
                            where += " AND movie.title LIKE ? \n";
                            params.add("%" + value + "%");
                            break;
                        case "movie.film_genre":
                            where += " AND movie.film_genre = ? \n";
                            params.add(value);
                            break;
                        case "movie.release_year":
                            where += " AND movie.release_year = ? \n";
                            params.add(value);
                            break;
                        case "movie.upload_ddate":
                            where += " AND movie.upload_ddate = ? \n";
                            params.add(value);
                            break;
                        case "movie.active":
                            where += " AND movie.active = ? \n";
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
                + "     movie.title ASC \n";
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Movie movie) {
        return validate(movie);
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Movie movie) {
        return validate(movie);
    }

    private LinkedList<ObjectValidatorException> validate(Movie movie) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        if (!StringUtil.isValid(movie.getOriginal_filename())
                && !StringUtil.isValid(movie.getFilename())) {
            exceptions.add(new ObjectValidatorException("original_filename",
                    "Debe subir el archivo de la película"));
        }

        if (!StringUtil.isValid(movie.getTitle())) {
            exceptions.add(new ObjectValidatorException("title", "Debe indicar el"
                    + " título de la película."));
        }

        if (movie.getFilm_genre() <= 0) {
            exceptions.add(new ObjectValidatorException("film_genre",
                    "Debe indicar la categoría de la película."));
        }

        File poster_file = request.getMparser().getFile("poster_file");

        if (poster_file != null) {
            if (!FileUtils.hasValidFormat(poster_file, FileUtils.Type.IMG)) {
                exceptions.add(new ObjectValidatorException("poster_file", "Debe subir "
                        + "una imagen en formato <b><i>png o jpeg.</i></b>"));
            } else if (!FileUtils.isValidImageFile(poster_file)) {
                exceptions.add(new ObjectValidatorException("cover_file",
                        "La imagen que está intentando cargar tiene un perfil "
                        + "no soportado para la carga. Intente guardar "
                        + "la imagen en otro formato."));
            }
        }

        return exceptions;
    }

}
