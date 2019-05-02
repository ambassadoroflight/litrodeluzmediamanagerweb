package org.unlitrodeluzcolombia.mediamanager.utils;

import java.io.File;
import net.comtor.advanced.administrable.AdministrableForm;
import net.comtor.html.HtmlSpan;
import net.comtor.html.form.HtmlOption;
import net.comtor.html.form.HtmlSelect;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 21, 2019
 */
public final class FormUtils {

    public static void getFilesList(final AdministrableForm form, final String name, 
            final String directory, final String hint) {
        HtmlSelect select = new HtmlSelect(name);

        File uploadsDir = new File(directory);

        File[] files = uploadsDir.listFiles();

        if (files.length == 0) {
            form.addRowInOneCell(new HtmlSpan("", ("No se encontraron archivos en el directorio "
                    + directory), "alert alert-danger"));
            
            return;
        }

        HtmlOption disabledOption = new HtmlOption("", "Seleccionar archivo", "true");
        disabledOption.addAttribute("disabled", "");

        select.addElement(disabledOption);

        for (File file : files) {
            select.addOption(file.getName(), file.getName());
        }

        form.addField("Archivo", select, hint, true);

    }

}
