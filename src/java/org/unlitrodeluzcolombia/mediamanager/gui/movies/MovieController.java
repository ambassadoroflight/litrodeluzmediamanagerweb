package org.unlitrodeluzcolombia.mediamanager.gui.movies;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.HtmlFinder;
import net.comtor.advanced.html.form.HtmlInputNumber;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlImg;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlCheckbox;
import net.comtor.html.form.HtmlInputFile;
import net.comtor.html.form.HtmlInputText;
import net.comtor.html.form.HtmlTextArea;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.util.StringUtil;
import org.unlitrodeluzcolombia.mediamanager.element.FilmGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Movie;
import org.unlitrodeluzcolombia.mediamanager.gui.finder.FilmGenreFinder;
import org.unlitrodeluzcolombia.mediamanager.utils.FormUtils;
import org.unlitrodeluzcolombia.mediamanager.web.facade.FilmGenreWebFacade;
import org.unlitrodeluzcolombia.mediamanager.web.facade.MovieWebFacade;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MovieController
        extends AbstractComtorFacadeAdministratorControllerI18n<Movie, String> {

    private static final Logger LOG = Logger.getLogger(MovieController.class.getName());

    @Override
    public String getEntityName() {
        return "Película";
    }

    @Override
    public WebLogicFacade<Movie, String> getLogicFacade() {
        return new MovieWebFacade();
    }

    @Override
    public void initFormAdvancedSearch(AdministrableForm form, HttpServletMixedRequest request) {
        HtmlInputText code = new HtmlInputText("code", 32, 32);
        form.addField("Código", code, null);

        HtmlInputText title = new HtmlInputText("title", 32, 256);
        form.addField("Título", title, null);

        HtmlFinder film_genre = new HtmlFinder("film_genre", FilmGenreFinder.class, "", 32);
        form.addField("Género/Categoría", film_genre, null);

        HtmlInputNumber release_year = new HtmlInputNumber("release_year", 4, 4,
                HtmlInputNumber.Type.INTEGER);
        form.addField("Año", release_year, null);

        //TODO: AGREGAR BUSCADOR DE FECHA DE CARGA
        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        form.addField("Activo", active, null);
    }

    @Override
    public void initForm(AdministrableForm form, Movie movie)
            throws BusinessLogicException {
        form.setEnctype(AdministrableForm.ENCTYPE_MULTIPART_FORM_DATA);

        if (movie != null) {
            final String movieCode = movie.getCode();
            final java.sql.Timestamp uploadDate = movie.getUpload_date();
            final java.sql.Timestamp lastUpdate = movie.getLast_update();

            form.addInputHidden("code", movieCode);
            form.addInputHidden("upload_date", uploadDate);
            form.addInputHidden("upload_ddate", movie.getUpload_ddate());
            form.addInputHidden("filename", movie.getFilename());
            form.addInputHidden("poster", movie.getPoster());

            HtmlText code = new HtmlText(movieCode);
            form.addField("Código", code, null);

            HtmlText upload_date = new HtmlText(uploadDate);
            form.addField("Fecha de Carga", upload_date, null);

            if (!uploadDate.equals(lastUpdate)) {
                HtmlText last_update = new HtmlText(lastUpdate);
                form.addField("Última Actualización", last_update, null);
            }
        }

        HtmlInputText title = new HtmlInputText("title", 32, 256);
        form.addField("Título", title, null, true);

        HtmlFinder film_genre = getFilmGenreFinder(movie);
        form.addField("Categoría", film_genre, null, true);

        HtmlInputNumber release_year = new HtmlInputNumber("release_year", 4, 4,
                HtmlInputNumber.Type.INTEGER);
        form.addField("Año", release_year, null);

        HtmlTextArea synopsis = new HtmlTextArea("synopsis", 32, 5, 2048);
        synopsis.addAttribute("placeholder", "Máx. 2048 caracteres");
        form.addField("Sinopsis", synopsis, "Ingrese una breve sinopsis o resumen "
                + "de la película.");

        /*
         Los archivos no se suben a través del formulario. Ya están en el servidor,
         aquí símplemente se indica cuál es el archivo correspondiente para moverlo
         a la carpeta correspondiente y renombrar el archivo con el código.
         */
        String uploadsDir = GlobalWeb.UPLOADS_MOVIES_PATH;

        form.addField("Directorio de Películas", new HtmlText(uploadsDir), null);

        HtmlElement filename = FormUtils.getFilesList("original_filename", uploadsDir);
        form.addField("Archivo", filename, "Seleccione del listado de los archivos"
                + " que se encuentran cargados en el servidor el que corresponde a"
                + " la canción.", true);

        if ((movie != null) && (StringUtil.isValid(movie.getFilename()))) {
            HtmlImg cover = new HtmlImg("ImagesServlet?code=" + movie.getCode());
            form.addRowInOneCell(cover);
        }

        HtmlInputFile poster_file = new HtmlInputFile("poster_file");
        form.addField("Poster", poster_file, "Cargue la imagen del poster (portada) "
                + "de la película. Esta imagen será vista en la plataforma.");

        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        active.checked((movie == null) ? true : movie.isActive());
        form.addField("Activo", active, "Si marca la casilla, la película estará "
                + "disponible para verse en la plataforma.");
    }

    @Override
    public String getViewFormLabel() {
        return "Ver Canción";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este módulo.";
    }

    @Override
    public void initFormView(AdministrableForm form, Movie movie) {
        final String code = movie.getCode();

        HtmlImg cover = new HtmlImg("ImagesServlet?code=" + code);
        form.addRowInOneCell(cover);

        HtmlText text;

        text = new HtmlText(code);
        form.addField("Código", text, null);

        text = new HtmlText(movie.getTitle());
        form.addField("Título", text, null);

        text = new HtmlText(movie.getFilm_genre_name());
        form.addField("Género/Categoría", text, null);

        text = new HtmlText(movie.getRelease_year());
        form.addField("Año", text, null);

        text = new HtmlText(movie.getSynopsis());
        form.addField("Sinopsis", text, null);

        text = new HtmlText(movie.getUpload_date());
        form.addField("Fecha de Carga", text, null);

        text = new HtmlText(movie.getLast_update());
        form.addField("Última Actualización", text, null);

        text = new HtmlText(movie.getFilename());
        form.addField("Archivo", text, null);

        text = new HtmlText(movie.getStatus());
        form.addField("Estado", text, null);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_MOVIE";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_MOVIE";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_MOVIE";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_MOVIE";
    }

    @Override
    public String getFormName() {
        return "movie_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("poster", "Poster");
        headers.put("title", "Título");
        headers.put("film_genre", "Género/Categoría");
        headers.put("active", "Estado");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Movie movie) {
        LinkedList<Object> row = new LinkedList<>();
        HtmlImg poster = new HtmlImg("ImagesServlet?code=" + movie.getCode());

        row.add(poster.getHtml());
        row.add(movie.getTitle());
        row.add(movie.getFilm_genre_name());
        row.add(movie.getStatus());

        return row;
    }

    private HtmlFinder getFilmGenreFinder(Movie movie) {
        FilmGenre genre = null;
        String valueToShow = "";

        try {
            genre = ((movie == null)
                    ? null
                    : new FilmGenreWebFacade().find(movie.getFilm_genre()));
            valueToShow = ((genre == null)
                    ? ""
                    : new FilmGenreFinder().getValueToShow(genre));

            return new HtmlFinder("film_genre", FilmGenreFinder.class, valueToShow, 32);
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }
    }

}
