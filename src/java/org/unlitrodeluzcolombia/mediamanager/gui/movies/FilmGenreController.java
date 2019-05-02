package org.unlitrodeluzcolombia.mediamanager.gui.movies;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.ActionIcon;
import net.comtor.advanced.html.BigTable;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.global.ComtorGlobal;
import net.comtor.framework.logic.facade.WebLogicFacade;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlLink;
import net.comtor.html.HtmlText;
import net.comtor.html.form.HtmlInputText;
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.i18n.html.DivFormI18n;
import org.unlitrodeluzcolombia.mediamanager.element.FilmGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Movie;
import org.unlitrodeluzcolombia.mediamanager.facade.MovieDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.web.facade.FilmGenreWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class FilmGenreController
        extends AbstractComtorFacadeAdministratorControllerI18n<FilmGenre, Long> {

    private static final Logger LOG = Logger.getLogger(FilmGenreController.class.getName());

    @Override
    public String getEntityName() {
        return "G�neros de Pel�cula";
    }

    @Override
    public String getLogModule() {
        return "G�neros de Pel�cula";
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.FILM_GENRE_CONTROLLER;
    }

    @Override
    public WebLogicFacade<FilmGenre, Long> getLogicFacade() {
        return new FilmGenreWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, FilmGenre genre)
            throws BusinessLogicException {
        if (genre != null) {
            form.addInputHidden("id", genre.getId());
        }

        HtmlInputText name = new HtmlInputText("name", 32, 128);
        form.addField("Nombre", name, null, true);
    }

    @Override
    public HtmlElement getViewForm(Long key, HttpServletMixedRequest request,
            HttpServletResponse response) throws ComtorDaoException {
        AdministrableForm form = new DivFormI18n(getBaseUrl(),
                AdministrableForm.METHOD_POST, request);
        form.setTitle(getViewFormLabel());
        form.setFormName(getFormName());

        try {
            FilmGenre genre = getLogicFacade().find(key);

            final long id = genre.getId();

            HtmlText text = new HtmlText(genre.getName());
            form.addField("Nombre", text, null);

            List<Movie> movies = new MovieDAOFacade()
                    .findAllByProperty("film_genre", id);

            form.addRowInOneCell(getMoviesList(movies));
        } catch (ComtorDaoException | BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        form.addButton("ok", getLabelContinue());

        return form;
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_FILM_GENRE";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_FILM_GENRE";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_FILM_GENRE";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_FILM_GENRE";
    }

    @Override
    public String getFormName() {
        return "film_genre_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(FilmGenre genre) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(genre.getId());
        row.add(genre.getName());

        return row;
    }

    @Override
    public String getAddNewObjectLabel() {
        return "Nuevo G�nero";
    }

    @Override
    public String getAddFormLabel() {
        return "Crear G�nero de Pel�cula";
    }

    @Override
    public String getEditFormLabel() {
        return "Editar G�nero de Pel�cula";
    }

    @Override
    public String getViewFormLabel() {
        return "Ver G�nero de Pel�cula";
    }

    @Override
    public String getConfirmDeleteMessage(FilmGenre genre) {
        return "�Est� seguro que desea el g�nero <b>[" + genre.getId()
                + "] " + genre.getName() + "</b>?";
    }

    @Override
    public String getAddedMessage(FilmGenre genre) {
        return "El g�nero <b>[" + genre.getId() + "] " + genre.getName()
                + "</b> ha sido creado.";
    }

    @Override
    public String getDeletedMessage(FilmGenre genre) {
        return "El g�nero <b>[" + genre.getId() + "] " + genre.getName()
                + "</b> ha sido eliminado.";
    }

    @Override
    public String getUpdatedMessage(FilmGenre genre) {
        return "El g�nero <b>[" + genre.getId() + "] " + genre.getName()
                + "</b> ha sido actualizado.";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este m�dulo.";
    }

    private HtmlDiv getMoviesList(List<Movie> movies) {
        if (movies.isEmpty()) {
            HtmlDiv alert = new HtmlDiv("", "alert alert-info");
            alert.addString("No hay pel�culas asociadas a esta categor�a");

            return alert;
        } else {
            HtmlDiv container = new HtmlDiv("", "table_container");

            BigTable table = new BigTable("#", "T�tulo", "Ver");
            table.setClazz("big_table");

            HtmlLink link;
            int pos = 1;

            for (Movie movie : movies) {
                String deviceURL = ComtorGlobal.getLink(MovieAdmin.class)
                        + "&action=viewform&key=" + movie.getCode();

                link = new HtmlLink("", deviceURL, ActionIcon.VIEW_24);

                table.addRow(pos++, movie.getTitle(), link.getHtml());
            }

            container.addElement(table);

            return container;
        }
    }

}
