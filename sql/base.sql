-- À exécuter dans pgAdmin : clic droit sur la base > Query Tool > coller > F5
-- 1) Créer la base (une seule fois, depuis la base "postgres")
-- CREATE DATABASE isg_sousse ENCODING 'UTF8';

-- 2) Dans la base isg_sousse :
CREATE TABLE IF NOT EXISTS candidatures (
    id            SERIAL PRIMARY KEY,
    reference     VARCHAR(20) UNIQUE NOT NULL,
    nom           VARCHAR(80)  NOT NULL,
    prenom        VARCHAR(80)  NOT NULL,
    nom_ar        VARCHAR(160),
    cin           VARCHAR(20)  NOT NULL,
    naissance     DATE         NOT NULL,
    lieu          VARCHAR(80),
    sexe          CHAR(1),
    nationalite   VARCHAR(40),
    adresse       TEXT,
    gouvernorat   VARCHAR(40),
    code_postal   VARCHAR(10),
    telephone     VARCHAR(25)  NOT NULL,
    email         VARCHAR(120) NOT NULL,
    bac_annee     SMALLINT,
    bac_section   VARCHAR(60),
    bac_moyenne   NUMERIC(5,2),
    bac_mention   VARCHAR(20),
    diplome       VARCHAR(60),
    specialite    VARCHAR(120),
    etablissement VARCHAR(160),
    licence_annee SMALLINT,
    licence_moyenne NUMERIC(5,2),
    moyennes_ans  VARCHAR(80),
    session       VARCHAR(30),
    type_master   VARCHAR(40)  NOT NULL,
    choix1        VARCHAR(80)  NOT NULL,
    choix2        VARCHAR(80),
    choix3        VARCHAR(80),
    niveau_fr     VARCHAR(20),
    niveau_en     VARCHAR(20),
    stages        TEXT,
    experience    TEXT,
    motivation    TEXT NOT NULL,
    statut        VARCHAR(20) NOT NULL DEFAULT 'en attente',
    date_depot    TIMESTAMP   NOT NULL DEFAULT NOW(),
    UNIQUE (cin, type_master)
);

CREATE TABLE IF NOT EXISTS candidature_fichiers (
    id              SERIAL PRIMARY KEY,
    candidature_id  INTEGER NOT NULL REFERENCES candidatures(id) ON DELETE CASCADE,
    type_piece      VARCHAR(30) NOT NULL,   -- photo, cin, cv, bac, licence, notes
    nom_fichier     VARCHAR(255) NOT NULL,
    chemin          VARCHAR(400) NOT NULL
);

-- Pour consulter : SELECT * FROM candidatures ORDER BY date_depot DESC;
