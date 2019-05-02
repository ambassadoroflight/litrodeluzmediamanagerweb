package org.unlitrodeluzcolombia.mediamanager.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.unlitrodeluzcolombia.mediamanager.enums.FilePrefix;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public final class EntityUtils {

    public static String generateUniqueCode(FilePrefix prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "");

        return prefix.getCode() + uuid;
    }

    public static String getFilename(String code, File srcFile) {
        return code + '.' + FileUtils.getExtension(srcFile);
    }

    public static String getFilenameFromAlbum(String code, String album, File srcFile) {
        album = album.toLowerCase().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        return getFilename(code + "_" + album, srcFile);
    }

    public static String moveFile(String srcPath, String srcFilename, String targetPath, String filename)
            throws RuntimeException, IOException {
        if (srcFilename != null) {
            File srcFile = new File(srcPath + File.separator + srcFilename);

            filename += '.' + FileUtils.getExtension(srcFile);

            FileUtils.moveFile(targetPath, filename, srcFile);

            return filename;
        }

        return "";
    }
}
