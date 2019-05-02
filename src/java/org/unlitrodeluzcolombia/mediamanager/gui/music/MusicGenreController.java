package org.unlitrodeluzcolombia.mediamanager.gui.music;

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
import org.unlitrodeluzcolombia.mediamanager.element.MusicGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Song;
import org.unlitrodeluzcolombia.mediamanager.facade.SongDAOFacade;
import org.unlitrodeluzcolombia.mediamanager.web.facade.MusicGenreWebFacade;
import web.global.LitroDeLuzImages;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class MusicGenreController
        extends AbstractComtorFacadeAdministratorControllerI18n<MusicGenre, Long> {

    private static final Logger LOG = Logger.getLogger(MusicGenreController.class.getName());

    @Override
    public String getEntityName() {
        return "musicgenre.entityname";
    }

    @Override
    public String getLogModule() {
        return "musicgenre.logmodule";
    }

    @Override
    protected String getTitleImgPath() {
        return LitroDeLuzImages.MUSIC_GENRE_CONTROLLER;
    }

    @Override
    public WebLogicFacade<MusicGenre, Long> getLogicFacade() {
        return new MusicGenreWebFacade();
    }

    @Override
    public void initForm(AdministrableForm form, MusicGenre genre)
            throws BusinessLogicException {
        if (genre != null) {
            final long musicGenreId = genre.getId();

            form.addInputHidden("id", musicGenreId);

            HtmlText id = new HtmlText(musicGenreId + "");
            form.addField("musicgenre.field.id", id, null);
        }

        HtmlInputText name = new HtmlInputText("name", 32, 128);
        form.addField("musicgenre.field.name", name, null, true);
    }

    @Override
    public HtmlElement getViewForm(Long key, HttpServletMixedRequest request,
            HttpServletResponse response) throws ComtorDaoException {
        AdministrableForm form = new DivFormI18n(getBaseUrl(),
                AdministrableForm.METHOD_POST, request);
        form.setTitle(getViewFormLabel());
        form.setFormName(getFormName());

        try {
            MusicGenre genre = getLogicFacade().find(key);

            final long id = genre.getId();

            HtmlText text;

            text = new HtmlText(id);
            form.addField("musicgenre.field.id", text, null);

            text = new HtmlText(genre.getName());
            form.addField("musicgenre.field.name", text, null);

            LinkedList<Song> songs;

            songs = new SongDAOFacade().findAllByProperty("music_genre", id);

            if (songs.isEmpty()) {
                HtmlDiv alert = new HtmlDiv("", "alert alert-info");
                alert.addString("No hay canciones asociadas a esta categoría");

                form.addRowInOneCell(alert);
            } else {
                HtmlDiv table_container = new HtmlDiv("", "table_container");

                BigTable table = new BigTable("#", "Título", "Ver");
                table.setClazz("big_table");

                HtmlLink link;
                int pos = 1;

                for (Song song : songs) {
                    String deviceURL = ComtorGlobal.getLink(SongAdmin.class)
                            + "&action=viewform&key=" + song.getCode();

                    link = new HtmlLink("", deviceURL, ActionIcon.VIEW_24);

                    table.addRow(pos++, song.getTitle(), link.getHtml());
                }

                table_container.addElement(table);

                form.addRowInOneCell(table_container);
            }
        } catch (ComtorDaoException | BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        form.addButton("ok", getLabelContinue());

        return form;
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_MUSIC_GENRE";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_MUSIC_GENRE";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_MUSIC_GENRE";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_MUSIC_GENRE";
    }

    @Override
    public String getFormName() {
        return "music_genre_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("name", "Nombre");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(MusicGenre genre) {
        LinkedList<Object> row = new LinkedList<>();
        row.add(genre.getId());
        row.add(genre.getName());

        return row;
    }

    @Override
    public String getAddNewObjectLabel() {
        return translate("musicgenre.add.title");
    }

    @Override
    public String getAddFormLabel() {
        return translate("musicgenre.new.title");
    }

    @Override
    public String getEditFormLabel() {
        return translate("musicgenre.edit.title");
    }

    @Override
    public String getViewFormLabel() {
        return translate("musicgenre.view.title");
    }

    @Override
    public String getAddedMessage(MusicGenre genre) {
        return formatI18nMessage("musicgenre.message.added", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getUpdatedMessage(MusicGenre genre) {
        return formatI18nMessage("musicgenre.message.updated", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getConfirmDeleteMessage(MusicGenre genre) {
        return formatI18nMessage("musicgenre.message.confirmdelete", (genre.getId() + ""),
                genre.getName());
    }

    @Override
    public String getDeletedMessage(MusicGenre genre) {
        return formatI18nMessage("musicgenre.message.deleted", (genre.getId() + ""),
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

}
