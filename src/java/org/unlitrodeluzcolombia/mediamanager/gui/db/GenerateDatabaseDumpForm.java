package org.unlitrodeluzcolombia.mediamanager.gui.db;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.advanced.html.IndexContainerAndIndexedError;
import net.comtor.exception.ComtorException;
import net.comtor.framework.html.administrable.ComtorMessageHelperI18n;
import net.comtor.framework.request.HttpServletMixedRequest;
import net.comtor.html.HtmlP;
import net.comtor.html.HtmlUl;
import net.comtor.i18n.html.AbstractComtorFormValidateActionI18n;
import net.comtor.i18n.html.DivFormI18n;
import web.global.GlobalWeb;

/**
 *
 * @author juandiego@comtor.net
 * @since 1.8
 * @version Mar 08, 2019
 */
public class GenerateDatabaseDumpForm extends AbstractComtorFormValidateActionI18n {

    private static final Logger LOG = Logger.getLogger(GenerateDatabaseDumpForm.class.getName());

    public GenerateDatabaseDumpForm(Class clazz) {
        super(clazz);
    }

    @Override
    protected String getTitleText() {
        return "Generar Dump de Base de Datos";
    }

    @Override
    protected IndexContainerAndIndexedError createForm(HttpServletMixedRequest request,
            HttpServletResponse response) {
        AdministrableForm form = new DivFormI18n(getOption(),
                AdministrableForm.METHOD_POST, request);
        form.setTitle(getTitleText());
        form.setFormName("generate_db_dump_form");

        form.addRowInOneCell(new HtmlP("Se generará una copia de la base de datos para ser usada en la sincronización hacia los Hotspot."));
        form.addRowInOneCell(new HtmlP("Se copiarán las siguientes tablas:"));

        HtmlUl list = new HtmlUl();
        list.addListElement("Categorías de Películas");
        list.addListElement("Películas");
        list.addListElement("Categorías Musicales");
        list.addListElement("Canciones");

        form.addRowInOneCell(list);

        form.addBasicButtons("Generar", "Cancelar");

        return form;
    }

    @Override
    public String processForm(HttpServletMixedRequest request, HttpServletResponse response)
            throws ComtorException {
        String user = GlobalWeb.getInstance().getDatabaseUser();
        String pass = GlobalWeb.getInstance().getDatabasePassword();

        String commands[] = {"/bin/sh", "-c", "mysqldump -u " + user + " -p"
            + pass + " litro_de_luz_radius2 --complete-insert movie film_genre song "
            + "music_genre > /opt/litro_de_luz/database_dump/litrodeluz_dump.sql "};

        try {
            Process process = Runtime.getRuntime().exec(commands);
            process.waitFor();

            return ComtorMessageHelperI18n.getOkForm(getTitleText(), "index.jsp",
                    "Se ha generado la copia exitosamente.", request).getHtml();
        } catch (IOException | InterruptedException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);

            return ComtorMessageHelperI18n.getErrorForm(getTitleText(),
                    "index.jsp", ex, false, request).getHtml();
        }

    }

}
