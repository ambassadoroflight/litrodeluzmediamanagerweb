package org.unlitrodeluzcolombia.mediamanager.utils;

import java.io.File;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlSpan;
import net.comtor.html.form.HtmlOption;
import net.comtor.html.form.HtmlSelect;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 21, 2019
 */
public final class FormUtils {

    public static HtmlElement getFilesList(String name, String directory) {
        HtmlSelect select = new HtmlSelect(name);

        File uploadsDir = new File(directory);

        File[] files = uploadsDir.listFiles();

        if (files.length == 0) {
            return new HtmlSpan("", ("No se encontraron archivos en el directorio "
                    + directory), "alert alert-danger");
        }

        HtmlOption disabledOption = new HtmlOption("", "Seleccionar archivo", "true");
        disabledOption.addAttribute("disabled", "");

        select.addElement(disabledOption);

        for (File file : files) {
            select.addOption(file.getName(), file.getName());
        }

        return select;

    }

}
