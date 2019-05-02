package org.unlitrodeluzcolombia.mediamanager.gui.music;

import java.io.File;
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
import net.comtor.i18n.html.AbstractComtorFacadeAdministratorControllerI18n;
import net.comtor.util.StringUtil;
import org.unlitrodeluzcolombia.mediamanager.element.MusicGenre;
import org.unlitrodeluzcolombia.mediamanager.element.Song;
import org.unlitrodeluzcolombia.mediamanager.gui.finder.MusicGenreFinder;
import org.unlitrodeluzcolombia.mediamanager.utils.FormUtils;
import org.unlitrodeluzcolombia.mediamanager.web.facade.MusicGenreWebFacade;
import org.unlitrodeluzcolombia.mediamanager.web.facade.SongWebFacade;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public class SongController
        extends AbstractComtorFacadeAdministratorControllerI18n<Song, String> {

    private static final Logger LOG = Logger.getLogger(SongController.class.getName());

    @Override
    public String getEntityName() {
        return "Canci�n";
    }

    @Override
    public WebLogicFacade<Song, String> getLogicFacade() {
        return new SongWebFacade();
    }

    @Override
    public void initFormAdvancedSearch(AdministrableForm form, HttpServletMixedRequest request) {
        HtmlInputText code = new HtmlInputText("code", 32, 32);
        form.addField("C�digo", code, null);

        HtmlInputText title = new HtmlInputText("title", 32, 256);
        form.addField("T�tulo", title, null);

        HtmlFinder music_genre = new HtmlFinder("music_genre", MusicGenreFinder.class, "", 32);
        form.addField("G�nero/Categor�a", music_genre, null);

        HtmlInputText album = new HtmlInputText("album", 32, 256);
        form.addField("�lbum", album, null);

        HtmlInputText artist = new HtmlInputText("artist", 32, 256);
        form.addField("Artista", artist, null);

        HtmlInputNumber release_year = new HtmlInputNumber("release_year", 4, 4,
                HtmlInputNumber.Type.INTEGER);
        form.addField("A�o", release_year, null);

        //TODO: AGREGAR BUSCADOR DE FECHA DE CARGA
        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        form.addField("Activo", active, null);
    }

    @Override
    public void initForm(AdministrableForm form, Song song)
            throws BusinessLogicException {
        form.setEnctype(AdministrableForm.ENCTYPE_MULTIPART_FORM_DATA);

        if (song != null) {
            final String movieCode = song.getCode();
            final java.sql.Timestamp uploadDate = song.getUpload_date();
            final java.sql.Timestamp lastUpdate = song.getLast_update();

            form.addInputHidden("code", movieCode);
            form.addInputHidden("upload_date", uploadDate);
            form.addInputHidden("upload_ddate", song.getUpload_ddate());
            form.addInputHidden("filename", song.getFilename());
            form.addInputHidden("cover", song.getCover());

            HtmlText code = new HtmlText(movieCode);
            form.addField("C�digo", code, null);

            HtmlText upload_date = new HtmlText(uploadDate);
            form.addField("Fecha de Carga", upload_date, null);

            if (!uploadDate.equals(lastUpdate)) {
                HtmlText last_update = new HtmlText(lastUpdate);
                form.addField("�ltima Actualizaci�n", last_update, null);
            }
        }

        HtmlInputText title = new HtmlInputText("title", 32, 256);
        form.addField("T�tulo", title, null, true);

        HtmlInputText artist = new HtmlInputText("artist", 32, 256);
        form.addField("Artista", artist, null, true);

        HtmlInputText album = new HtmlInputText("album", 32, 256);
        form.addField("�lbum", album, null);

        HtmlFinder music_genre = getMusicGenreFinder(song);
        form.addField("G�nero/Categor�a", music_genre, null, true);

        HtmlInputNumber release_year = new HtmlInputNumber("release_year", 4, 4,
                HtmlInputNumber.Type.INTEGER);
        form.addField("A�o", release_year, "Si ya existe una canci�n del mismo"
                + " �lbum, se le asignar� el a�o de �sta.");

        /*
         Los archivos no se suben a trav�s del formulario. Ya est�n en el servidor,
         aqu� s�mplemente se indica cu�l es el archivo correspondiente para moverlo
         a la carpeta correspondiente y renombrar el archivo con el c�digo.
         */
        String uploadsDir = GlobalWeb.UPLOADS_MUSIC_PATH;

        form.addField("Directorio de Canciones", new HtmlText(uploadsDir), null);

        HtmlElement filename = FormUtils.getFilesList("original_filename", uploadsDir);
        form.addField("Archivo", filename, "Seleccione del listado de los archivos"
                + " que se encuentran cargados en el servidor el que corresponde a"
                + " la canci�n.", true);

        if ((song != null) && (StringUtil.isValid(song.getCover()))) {
            HtmlImg cover = new HtmlImg("ImagesServlet?code=" + song.getCode());
            form.addRowInOneCell(cover);
        }

        HtmlInputFile cover_file = new HtmlInputFile("cover_file");
        form.addField("Portada", cover_file, "Cargue la imagen de la portada "
                + "de la canci�n o �lbum. Esta imagen ser� vista en la plataforma."
                + "Si ya existe una canci�n del mismo �lbum con portada, se le"
                + "asignar� la misma si no carga una imagen.");

        HtmlCheckbox active = new HtmlCheckbox("active", "active");
        active.checked((song == null) ? true : song.isActive());
        form.addField("Activo", active, "Si marca la casilla, la canci�n estar� "
                + "disponible en la plataforma.");
    }

    @Override
    public String getViewFormLabel() {
        return "Ver Canci�n";
    }

    @Override
    public String getViewPrivilegeMsg() {
        return "Ud. no tiene permisos para ingresar a este m�dulo.";
    }

    @Override
    public void initFormView(AdministrableForm form, Song song) {
        HtmlImg cover = new HtmlImg("ImagesServlet?code=" + song.getCode());
        form.addRowInOneCell(cover);

        HtmlText input;

        input = new HtmlText(song.getCode());
        form.addField("C�digo", input, null);

        input = new HtmlText(song.getTitle());
        form.addField("T�tulo", input, null);

        input = new HtmlText(song.getArtist());
        form.addField("Artista", input, null);

        input = new HtmlText(song.getAlbum());
        form.addField("�lbum", input, null);

        input = new HtmlText(song.getMusic_genre_name());
        form.addField("G�nero/Categor�a", input, null);

        input = new HtmlText(song.getRelease_year());
        form.addField("A�o", input, null);

        input = new HtmlText(song.getUpload_date());
        form.addField("Fecha de Carga", input, null);

        input = new HtmlText(GlobalWeb.MUSIC_DIRECTORY_PATH
                .replace(GlobalWeb.BASE_PATH, "") + File.separator + song.getFilename());
        form.addField("Ubicaci�n de Archivo Canci�n", input, null);

        input = new HtmlText(GlobalWeb.IMAGES_DIRECTORY_PATH
                .replace(GlobalWeb.BASE_PATH, "") + File.separator + song.getCover());

        form.addField("Ubicaci�n de Archivo Portada", input, null);

        input = new HtmlText(song.getStatus());
        form.addField("Estado", input, null);
    }

    @Override
    public String getAddPrivilege() {
        return "ADD_SONG";
    }

    @Override
    public String getEditPrivilege() {
        return "EDIT_SONG";
    }

    @Override
    public String getDeletePrivilege() {
        return "DELETE_SONG";
    }

    @Override
    public String getViewPrivilege() {
        return "VIEW_SONG";
    }

    @Override
    public String getFormName() {
        return "song_form";
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("poster", "Portada");
        headers.put("title", "T�tulo");
        headers.put("artist", "Artista");
        headers.put("album", "�lbum");
        headers.put("film_genre", "G�nero/Categor�a");
        headers.put("active", "Estado");

        return headers;
    }

    @Override
    public LinkedList<Object> getRow(Song song) {
        HtmlImg cover = new HtmlImg("ImagesServlet?code=" + song.getCode());

        LinkedList<Object> row = new LinkedList<>();
        row.add(cover.getHtml());
        row.add(song.getTitle());
        row.add(song.getArtist());
        row.add(song.getAlbum());
        row.add(song.getMusic_genre_name());
        row.add(song.getStatus());

        return row;
    }

    private HtmlFinder getMusicGenreFinder(Song song) {
        MusicGenre genre = null;
        String value = "";

        try {
            genre = ((song == null)
                    ? null
                    : new MusicGenreWebFacade().find(song.getMusic_genre()));
            value = ((genre == null)
                    ? ""
                    : new MusicGenreFinder().getValueToShow(genre));

            return new HtmlFinder("music_genre", MusicGenreFinder.class, value, 32);
        } catch (BusinessLogicException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return null;
        }
    }

}
