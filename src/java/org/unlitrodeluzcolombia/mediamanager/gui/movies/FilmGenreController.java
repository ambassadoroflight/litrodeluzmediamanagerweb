package org.unlitrodeluzcolombia.mediamanager.gui.movies;

import java.util.LinkedHashMap;
import java.util.LinkedList;
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
        return "filmgenre.entityname";
    }

    @Override
    public String getLogModule() {
        return "filmgenre.logmodule";
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
            final long filmGenreId = genre.getId();

            form.addInputHidden("id", filmGenreId);

            HtmlText id = new HtmlText(filmGenreId + "");
            form.addField("filmgenre.field.id", id, null);
        }

        HtmlInputText name = new HtmlInputText("name", 32, 128);
        form.addField("filmgenre.field.name", name, null, true);
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

            HtmlText text;

            text = new HtmlText(id);
            form.addField("musicgenre.field.id", text, null);

            text = new HtmlText(genre.getName());
            form.addField("musicgenre.field.name", text, null);

            LinkedList<Movie> movies = new MovieDAOFacade()
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
        headers.put("id", "filmgenre.field.id");
        headers.put("name", "filmgenre.field.name");

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
        return translate("filmgenre.add.title");
    }

    @Override
    public String getAddFormLabel() {
        return translate("filmgenre.new.title");
    }

    @Override
    public String getEditFormLabel() {
        return translate("filmgenre.edit.title");
    }

    @Override
    public String getViewFormLabel() {
        return translate("filmgenre.view.title");
    }

    @Override
    public String getAddedMessage(FilmGenre genre) {
        return formatI18nMessage("filmgenre.message.added", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getUpdatedMessage(FilmGenre genre) {
        return formatI18nMessage("filmgenre.message.updated", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getConfirmDeleteMessage(FilmGenre genre) {
        return formatI18nMessage("filmgenre.message.confirmdelete", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getDeletedMessage(FilmGenre genre) {
        return formatI18nMessage("filmgenre.message.deleted", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getAddPrivilegeMsg() {
        return "controller.message.noprivileges";
    }

    @Override
    public String getEditPrivilegeMsg() {
        return "controller.message.noprivileges";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "controller.message.noprivileges";
    }

    @Override
    public String getDeletePrivilegeMsg() {
        return "controller.message.noprivileges";
    }

    private HtmlDiv getMoviesList(LinkedList<Movie> movies) {
        if (movies.isEmpty()) {
            HtmlDiv alert = new HtmlDiv("", "alert alert-info");
            alert.addString("No hay películas asociadas a esta categoría");

            return alert;
        } else {
            HtmlDiv container = new HtmlDiv("", "table_container");

            BigTable table = new BigTable("#", "Título", "Ver");
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
