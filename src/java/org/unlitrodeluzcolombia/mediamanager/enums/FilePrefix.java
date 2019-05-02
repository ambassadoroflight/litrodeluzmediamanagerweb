package org.unlitrodeluzcolombia.mediamanager.enums;

/**
 *
 * @author juandiego@comtor.net
 * @since Feb 18, 2019
 */
public enum FilePrefix {

    MOVIE("MV"),
    MUSIC("MS");

    private String code;

    private FilePrefix(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
