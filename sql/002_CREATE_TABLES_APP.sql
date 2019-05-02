-- -----------------------------------------------------------------------------
-- COMTOR MEDIA MANAGER
-- TABLES APP
-- Version 1.0.0
-- Jan 23, 2019
-- -----------------------------------------------------------------------------

-- CREATE DATABASE litro_de_luz_radius2;
-- USE litro_de_luz_radius2;
-- GRANT ALL PRIVILEGES ON litro_de_luz_radius2.* TO litrodeluz@localhost IDENTIFIED BY 'litrodeluzradius2';
-- FLUSH PRIVILEGES;

-- -----------------------------------------------------------------------------
-- Tabla film_genre
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS film_genre;

CREATE TABLE film_genre (
    id          INT                 NOT NULL 		AUTO_INCREMENT,
    name        VARCHAR(128)        NOT NULL,

    PRIMARY KEY(id)
);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_FILM_GENRE', 'Agregar género de película', 'Películas'), 
    ('VIEW_FILM_GENRE', 'Ver género de película', 'Películas'), 
    ('EDIT_FILM_GENRE', 'Editar género de película', 'Películas'), 
    ('DELETE_FILM_GENRE', 'Eliminar género de película', 'Películas');

-- -----------------------------------------------------------------------------
-- Tabla movie
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
    code                VARCHAR(64)         NOT NULL,
    title               VARCHAR(256)        NOT NULL,
    film_genre          INT                 NOT NULL,
    release_year        INT                 DEFAULT 0,
    synopsis            VARCHAR(2048)       DEFAULT '',
    upload_date         DATETIME,
    upload_ddate        DATE,
    last_update         DATETIME,
    filename            VARCHAR(1024)       NOT NULL,
    poster              VARCHAR(1024)       DEFAULT '',
    active              TINYINT(1)          DEFAULT 1,

    PRIMARY KEY (code)
);

CREATE INDEX movie_title_idx ON movie(title);
CREATE INDEX movie_film_genre_idx ON movie(film_genre);
CREATE INDEX movie_release_year_idx ON movie(release_year);
CREATE INDEX movie_upload_ddate_idx ON movie(upload_ddate);
CREATE INDEX movie_active_idx ON movie(active);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_MOVIE', 'Agregar película', 'Películas'), 
    ('VIEW_MOVIE', 'Ver película', 'Películas'), 
    ('EDIT_MOVIE', 'Editar película', 'Películas'), 
    ('DELETE_MOVIE', 'Eliminar película', 'Películas');

-- -----------------------------------------------------------------------------
-- Tabla music_genre
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS music_genre;

CREATE TABLE music_genre (
    id          INT                 NOT NULL        AUTO_INCREMENT,
    name        VARCHAR(128)        NOT NULL,

    PRIMARY KEY(id)
);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_MUSIC_GENRE', 'Agregar género musical', 'Música'), 
    ('VIEW_MUSIC_GENRE', 'Ver género musical', 'Música'), 
    ('EDIT_MUSIC_GENRE', 'Editar género musical', 'Música'), 
    ('DELETE_MUSIC_GENRE', 'Eliminar género musical', 'Música');

-- -----------------------------------------------------------------------------
-- Tabla song
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS song;

CREATE TABLE song (
    code            VARCHAR(64)         NOT NULL,
    title           VARCHAR(256)        NOT NULL,
    music_genre     INT                 NOT NULL,
    album           VARCHAR(256)        DEFAULT '',
    artist          VARCHAR(256)        NOT NULL,
    release_year    INT                 DEFAULT 0,
    upload_date     DATETIME,
    upload_ddate    DATE,
    last_update     DATETIME,
    filename        VARCHAR(1024)       NOT NULL,
    cover           VARCHAR(1024)       DEFAULT '',
    active          TINYINT(1)          DEFAULT 1,
	
    PRIMARY KEY (code)
);

CREATE INDEX song_title_idx ON song(title);
CREATE INDEX song_music_genre_idx ON song(music_genre);
CREATE INDEX song_album_idx ON song(album);
CREATE INDEX song_artist_idx ON song(artist);
CREATE INDEX song_release_year_idx ON song(artist);
CREATE INDEX song_upload_ddate_idx ON song(upload_ddate);
CREATE INDEX song_active_idx ON song(active);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_SONG', 'Agregar canción', 'Música'), 
    ('VIEW_SONG', 'Ver canción', 'Música'), 
    ('EDIT_SONG', 'Editar canción', 'Música'), 
    ('DELETE_SONG', 'Eliminar canción', 'Música');