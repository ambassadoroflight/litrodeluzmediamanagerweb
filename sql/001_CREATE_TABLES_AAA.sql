-- -----------------------------------------------------------------------------
-- COMTOR MEDIA MANAGER
-- TABLES AAA
-- Version 1.0.0
-- Jan 23, 2019
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Tabla privilege
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS privilege;

CREATE TABLE privilege (
    code           VARCHAR(64)     NOT NULL,
    description    VARCHAR(256)    DEFAULT '',
    category       VARCHAR(128)    DEFAULT '',

    PRIMARY KEY (code)
);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ALL', 'Todos los privilegios', 'Configuracion');

-- -----------------------------------------------------------------------------
-- Tabla users
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    login       VARCHAR(64)     NOT NULL,
    password    VARCHAR(256)    NOT NULL,
    salt        VARCHAR(64)     NOT NULL,
    name        VARCHAR(256)    NOT NULL,
    email       VARCHAR(128)    DEFAULT '',
    phone       VARCHAR(32)     DEFAULT '',
    active      INT             NOT NULL        DEFAULT 1,

    PRIMARY KEY (login)
);

INSERT INTO 
    users (login, password, salt, name, email, phone, active)
VALUES 
    ('comtor', '199766ce7c04cc060172f1c187a8ee743eb73d7f6ecdaa1dcf228d561093dac6', 'COMTOR', 'Administrador COMTOR', 'comtor@comtor.net', '4631118', 1);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_USER', 'Agregar usuario', 'Configuracion'), 
    ('EDIT_USER', 'Editar usuario', 'Configuracion'),
    ('VIEW_USER', 'Ver usuario', 'Configuracion'),
    ('DELETE_USER', 'Eliminar usuario', 'Configuracion');

-- -----------------------------------------------------------------------------
-- Tabla profile
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS profile;

CREATE TABLE profile (
    id          INT(11)         NOT NULL    AUTO_INCREMENT,
    name        VARCHAR(128)    NOT NULL,
    editable    INT             DEFAULT 1,
    
    PRIMARY KEY (id)
);

CREATE INDEX profile_name_idx ON profile (name);

INSERT INTO 
    profile (id, name, editable)
VALUES
    (1, 'Administrador', 0);

INSERT INTO 
    privilege (code, description, category)
VALUES
    ('ADD_PROFILE', 'Agregar perfil', 'Configuracion'), 
    ('VIEW_PROFILE', 'Ver perfil', 'Configuracion'), 
    ('EDIT_PROFILE', 'Editar perfil', 'Configuracion'), 
    ('DELETE_PROFILE', 'Eliminar perfil', 'Configuracion');

-- -----------------------------------------------------------------------------
-- Tabla profile_x_privilege
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS profile_x_privilege;

CREATE TABLE profile_x_privilege (
    profile      INT(11)        NOT NULL    DEFAULT 0,
    privilege    VARCHAR(64)    NOT NULL    DEFAULT ' ',
  
    PRIMARY KEY (profile, privilege)
);

INSERT INTO
    profile_x_privilege (profile, privilege)
VALUES 
    (1, 'ALL');

-- -----------------------------------------------------------------------------
-- Tabla user_x_profile
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS user_x_profile;

CREATE TABLE user_x_profile (
    profile    INT(11)        NOT NULL    DEFAULT 0,
    login      VARCHAR(64)    NOT NULL    DEFAULT ' ',
    
    PRIMARY KEY (login, profile)
);

INSERT INTO 
    user_x_profile (profile, login)
VALUES 
    (1, 'comtor');