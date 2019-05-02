package org.unlitrodeluzcolombia.mediamanager.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import web.global.GlobalWeb;

/**
 * Clase utilitaria para manipulación de archivos.
 *
 * @author juandiego@comtor.net
 * @since Feb 20, 2019
 */
public final class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());

    private static final int MAX_SIZE = 500; //px

    private static final String[] VALID_VIDEO_FORMATS = {
        ".mp4",
        ".ogg",
        ".webm"
    };

    private static final String[] VALID_IMAGE_FORMATS = {
        ".png",
        ".jpeg",
        ".jpg"
    };

    private static final String[] VALID_AUDIO_FORMATS = {
        ".mp3",
        ".ogg",
        ".acc",
        ".wav"
    };

    public static enum Type {

        IMG,
        AUDIO,
        VIDEO
    }

    public static boolean uploadFile(String targetPath, String filename, File srcFile)
            throws IOException {
        File targetDirectory = getDirectory(targetPath);

        File targetFile = new File(targetDirectory.getPath() + File.separator + filename);

        byte[] bytes = Files.readAllBytes(srcFile.toPath());

        Files.write(targetFile.toPath(), bytes);

        return targetFile.exists();
    }

    public static boolean moveFile(String targetPath, String filename, File srcFile)
            throws IOException {
        File targetDirectory = getDirectory(targetPath);

        File targetFile = new File(targetDirectory.getPath() + File.separator + filename);

        return srcFile.renameTo(targetFile);
    }

    public static boolean deleteFiles(String... filePaths) throws IOException {
        boolean resp = true;
        File file;

        for (String filePath : filePaths) {
            file = new File(filePath);
            Files.delete(file.toPath());

            resp &= !file.exists();
        }

        return resp;
    }

    public static String getExtension(final File file) {
        final String name = file.getName();
        final String extension = name.substring(name.lastIndexOf('.') + 1);

        return extension;
    }

    public static boolean hasValidFormat(final File file, Type type) {
        String[] array = null;

        if (type.equals(Type.IMG)) {
            array = VALID_IMAGE_FORMATS;
        } else if (type.equals(Type.AUDIO)) {
            array = VALID_AUDIO_FORMATS;
        } else if (type.equals(Type.VIDEO)) {
            array = VALID_VIDEO_FORMATS;
        }

        for (String format : array) {
            if (file.getName().endsWith(format)) {
                return true;
            }
        }

        return false;
    }

    public static File getDirectory(String directoryPath) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }

        return directory;
    }

    public static boolean createPhotoThumbnail(File srcFile, String filename) throws IOException {
        BufferedImage bi = null;

        bi = ImageIO.read(srcFile);

        int bfType = ((bi.getType() == 0)
                ? BufferedImage.TYPE_INT_ARGB
                : bi.getType());

        int width = bi.getWidth();
        int height = bi.getHeight();

        if ((width > MAX_SIZE) || (height > MAX_SIZE)) {
            if (width >= height) {
                width = MAX_SIZE;
                height = ((MAX_SIZE * height) / width);
            } else {
                width = ((MAX_SIZE * width / height));
                height = MAX_SIZE;
            }
        }

        File desFile = new File(GlobalWeb.IMAGES_DIRECTORY_PATH + File.separator + filename);

        String extention = FileUtils.getExtension(desFile);

        ImageIO.write(resizeImage(bi, bfType, width, height), extention, desFile);

        srcFile.delete();

        return srcFile.exists();
    }

    private static BufferedImage resizeImage(BufferedImage bi, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D graphic = resizedImage.createGraphics();
        graphic.drawImage(bi, 0, 0, width, height, null);
        graphic.dispose();

        return resizedImage;
    }

    public static boolean isValidImageFile(File photo) {
        try {
            ImageIO.read(photo);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Imagen no apta para redimensión\t" + ex.getMessage());

            return false;
        }

        return true;
    }

}
