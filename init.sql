DROP SCHEMA IF EXISTS projetPAE CASCADE;
CREATE SCHEMA projetPAE;

CREATE TABLE projetPAE.adresses
(
    id_adresse  SERIAL PRIMARY KEY,
    rue         VARCHAR(100) NOT NULL,
    numero      INTEGER      NOT NULL,
    boite       VARCHAR(10),
    code_postal INTEGER      NOT NULL,
    ville       VARCHAR(100) NOT NULL
);

CREATE TABLE projetPAE.membres
(
    id_membre        SERIAL PRIMARY KEY,
    pseudo           VARCHAR(100) NOT NULL,
    nom              VARCHAR(100) NOT NULL,
    prenom           VARCHAR(100),
    admin            BOOLEAN      NOT NULL,
    telephone        VARCHAR(10),
    etat_inscription VARCHAR(100) NOT NULL,
    raison_refus     VARCHAR(200),
    id_adresse       INTEGER      NOT NULL REFERENCES projetPAE.adresses (id_adresse),
    mot_de_passe     VARCHAR(100) NOT NULL
);

CREATE TABLE projetPAE.types
(
    id_type SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE projetPAE.photos
(
    nom_photo VARCHAR(100) PRIMARY KEY,
    id_photo  SERIAL
);

CREATE TABLE projetPAE.objets
(
    id_objet       SERIAL PRIMARY KEY,
    type           INTEGER      NOT NULL REFERENCES projetPAE.types (id_type),
    titre          VARCHAR(100) NOT NULL,
    description    VARCHAR(100) NOT NULL,
    photo          VARCHAR(100) REFERENCES projetPAE.photos (nom_photo),
    date           TIMESTAMP,
    plage_horaire  VARCHAR(100) NOT NULL,
    membre_offreur INTEGER      NOT NULL REFERENCES projetPAE.membres (id_membre),
    etat           VARCHAR(100) NOT NULL,
    nbr_interesse  INTEGER      NOT NULL
);

CREATE TABLE projetPAE.interets_choisis
(
    id_interet_choisi SERIAL PRIMARY KEY,
    membre            INTEGER NOT NULL REFERENCES projetPAE.membres (id_membre),
    objet             INTEGER NOT NULL REFERENCES projetPAE.objets (id_objet),
    etat_transaction  VARCHAR(100)
);

CREATE TABLE projetPAE.interets
(
    id_interet         SERIAL PRIMARY KEY,
    membre             INTEGER NOT NULL REFERENCES projetPAE.membres (id_membre),
    objet              INTEGER NOT NULL REFERENCES projetPAE.objets (id_objet),
    date_disponibilite VARCHAR(100),
    appel              BOOLEAN NOT NULL
);

CREATE TABLE projetPAE.notifications
(
    id_notif   SERIAL PRIMARY KEY,
    libelle    VARCHAR(255) NOT NULL,
    notif_vue  BOOLEAN      NOT NULL,
    date_notif TIMESTAMP    NOT NULL,
    id_membre  INTEGER      NOT NULL REFERENCES projetPAE.membres (id_membre),
    type       VARCHAR(100) NOT NULL
);

CREATE TABLE projetPAE.evaluations
(
    id_evaluation SERIAL PRIMARY KEY,
    note          INTEGER NOT NULL,
    commentaire   INTEGER NOT NULL,
    objet         INTEGER NOT NULL REFERENCES projetPAE.objets (id_objet),
    redacteur     INTEGER NOT NULL REFERENCES projetPAE.membres (id_membre)
);

-- TYPES
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Accessoires pour animaux domestiques');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Accessoires pour voiture');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Décoration');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Jouets');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Literie');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Matériel de cuisine');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Matériel de jardinage');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Meuble');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Plantes');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Produits cosmétiques');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Vélo, trotinette');
INSERT INTO projetPAE.types(id_type, libelle)
VALUES (DEFAULT, 'Vêtements');

-- PHOTOS
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'christmas-1869533_640.png');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'dog-4118585_640.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'BureauEcolier-7.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'cadre-cottage-1178704_640.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'pots-daisy-181905_640.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'pots-plants-6520443_640.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'tableau.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'table-bistro.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'table-bistro-carree-bleue.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'table-jardin.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'tasse-garden-5037113_640.jpg');
INSERT INTO projetPAE.photos(id_photo, nom_photo)
VALUES (DEFAULT, 'wheelbarrows-4566619_640.jpg');

-- ADRESSES
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue de l Eglise', 11, 'B1', 4987, 'Stoumont');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue de Renkin', 7, null, 4800, 'Verviers');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue Haute Folie', 6, 'A103', 4800, 'Verviers');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Haute-Vinâve', 13, null, 4845, 'Jalhay');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue de Renkin', 7, null, 4800, 'Verviers');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue de Verviers', 47, null, 4000, 'Liège');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue du salpêtré', 789, 'Bis', 1040, 'Bruxelles');
INSERT INTO projetPAE.adresses(id_adresse, rue, numero, boite, code_postal, ville)
VALUES (DEFAULT, 'Rue des Minières', 45, 'Ter', 4800, 'Verviers');


-- MEMBRES
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'caro', 'Line', 'Caroline', true, null, 'Confirmé', null, 1,
        '$2a$12$AD2L6hhn6Xb/RTFHxB6PO.425TA9UBf5Gzitd/SdK.qxiDs3ndTfi'); -- MDP: Mdpuser.1
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'achil', 'Ile', 'Achille', false, null, 'Refusé',
        'L’application n’est pas encore ouverte à tous.', 2,
        '$2a$12$AD2L6hhn6Xb/RTFHxB6PO.425TA9UBf5Gzitd/SdK.qxiDs3ndTfi'); -- MDP: Mdpuser.1
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'bazz', 'Ile', 'Basile', false, null, 'Confirmé', null, 3,
        '$2a$12$AD2L6hhn6Xb/RTFHxB6PO.425TA9UBf5Gzitd/SdK.qxiDs3ndTfi'); -- MDP: Mdpuser.1
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'bri', 'Lehmann', 'Brigitte', true, null, 'Confirmé', null, 4,
        '$2a$12$eL9E.nyZRwln/kIU3gwzOuBhuySe8YgcUCi44Vi/R6wkNnBEJctCm'); -- MDP: Rad;293
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'test3', 'test', 'test', true, default, 'Confirmé', default, 1,
        '$2a$12$6XRm2keSZVJBzTza.Ran9uN7GBGt7hvFIJxyEczb.2VbWxODQg1Hu'); -- MDP: test3
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'theo', 'Ile', 'Théophile', false, null, 'Confirmé', null, 5,
        '$2a$10$oP4jgGib4LEsb/ATaWDNb.hJlT.9IeW9bLoIQE8ZWXXq4woKEzlLK'); -- MDP: mdpuser.3
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'emi', 'Ile', 'Emile', false, null, 'Refusé',
        'L’application n’est pas encore ouverte à tous.', 6,
        '$2a$10$SYymQQzv6xl003yrkvYxfeIEc9M2VCesoGhAvqSaFm06jb1MnMR5.'); -- MDP: mdpuser.5
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'cora', 'Line', 'Coralie', false, null, 'Refusé',
        'Vous devez encore attendre quelques jours.', 7,
        '$2a$10$//PSCCnRU0ekf4bGfB/HVuZO3kxnOBh.anqEDs6617HerEOSOTedO'); -- MDP: mdpuser.6
INSERT INTO projetPAE.membres(id_membre, pseudo, nom, prenom, admin, telephone, etat_inscription,
                              raison_refus, id_adresse, mot_de_passe)
VALUES (DEFAULT, 'charline', 'Line', 'Charles', false, null, 'Inscrit', null, 8,
        '$2a$12$nu8FkL6QvNQR2JXRbsniLeuBGJya8H8rQ4IOoG1664ExAOP9ggIUC');
-- MDP: mdpusr.4

-- OBJETS
INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 3, 'Décorations de Noël de couleur rouge', 'Décorations de Noël de couleur rouge',
        'christmas-1869533_640.png', '2022-03-21 00:00:01', 'Mardi de 17h à 22h', 3, 'Annulé', 0);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 3, 'Cadre représentant un chien noir sur un fond noir',
        'Cadre représentant un chien noir sur un fond noir', 'dog-4118585_640.jpg',
        '2022-03-25 12:00:01', 'Lundi de 18h à 22h', 3, 'Offert', 0);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 8, 'Ancien bureau d écolier', 'Ancien bureau d écolier', 'BureauEcolier-7.jpg',
        '2022-03-25 00:00:02', 'Tous les jours de 15h à 18h', 4, 'Interet marqué', 2);


INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 7,
        'Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos',
        'Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos',
        'wheelbarrows-4566619_640.jpg',
        '2022-03-28 00:00:01', 'Tous les matins avant 11h30', 6, 'Interet marqué', 3);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 7, 'Scie sur perche Gardena', 'Scie sur perche Gardena', null,
        '2022-03-28 00:00:02',
        'Tous les matins avant 11h30', 6, 'Offert', 0);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 8, 'Table jardin et deux chaises en bois', 'Table jardin et deux chaises en bois',
        'table-jardin.jpg',
        '2022-03-29 00:00:01', 'En semaine, de 20h à 21h', 6, 'Offert', 0);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 8, 'Table bistro', 'Table bistro', 'table-bistro.jpg',
        '2022-03-30 00:00:01', 'Lundi de 18h à 20h', 6, 'Offert', 0);


INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 8, 'Table bistro ancienne de couleur bleue',
        'Table bistro ancienne de couleur bleue',
        'table-bistro-carree-bleue.jpg',
        '2022-04-14 00:00:01', 'Samedi en journée', 1, 'Interet marqué', 2);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 4, 'Tableau noir pour enfant', 'Tableau noir pour enfant', 'tableau.jpg',
        '2022-04-14 00:00:02', 'Lundi de 18h à 20h', 6, 'Receveur Choisi', 1);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 3, 'Cadre cottage naïf', 'Cadre cottage naïf', 'cadre-cottage-1178704_640.jpg',
        '2022-04-21 00:00:01', 'Lundi de 18h à 20h', 6, 'Interet marqué', 3);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 6, 'Tasse de couleur claire rose & mauve', 'Tasse de couleur claire rose & mauve',
        'tasse-garden-5037113_640.jpg',
        '2022-04-21 00:00:02', 'Lundi de 18h à 20h', 6, 'Interet marqué', 2);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 9, 'Pâquerettes dans pots rustiques', 'Pâquerettes dans pots rustiques',
        'pots-daisy-181905_640.jpg',
        '2022-04-21 00:00:03', 'Samedi en journée', 1, 'Receveur Choisi', 1);

INSERT INTO projetPAE.objets(id_objet, type, titre, description, photo, date, plage_horaire,
                             membre_offreur, etat, nbr_interesse)
VALUES (DEFAULT, 9, 'Pots en grès pour petites plantes', 'Pots en grès pour petites plantes',
        'pots-plants-6520443_640.jpg',
        '2022-04-21 00:00:04', 'Samedi en journée', 1, 'Offert', 0);


--INTERETS
INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 6, 3, '16 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT, 'Le membre theo a liker votre objet : Ancien bureau d écolier ', false,
        '2022-05-16 00:00:01', 4, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 3, 3, '17 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT, 'Le membre bazz a liker votre objet : Ancien bureau d écolier ', false,
        '2022-05-17 00:00:01', 4, 'interet');



INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 1, 4, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre caro a liker votre objet : Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos ',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 3, 4, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bazz a liker votre objet : Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos ',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 4, 4, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bri a liker votre objet : Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos ',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 6, 8, '14 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre theo a liker votre objet : Table bistro ancienne de couleur bleue ',
        false, '2022-05-14 00:00:01', 1, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 4, 8, '14 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bri a liker votre objet : Table bistro ancienne de couleur bleue ',
        false, '2022-05-14 00:00:01', 1, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 1, 9, '16 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre caro a liker votre objet : Tableau noir pour enfant',
        false, '2022-05-16 00:00:01', 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 1, 10, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre caro a liker votre objet : Cadre cottage naïf',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 3, 10, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bazz a liker votre objet : Cadre cottage naïf',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 4, 10, '12 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bri a liker votre objet : Cadre cottage naïf',
        false, now(), 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 1, 11, '16 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre caro a liker votre objet : Tasse de couleur claire rose & mauve ',
        false, '2022-05-16 00:00:01', 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 3, 11, '16 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bazz a liker votre objet : Tasse de couleur claire rose & mauve ',
        false, '2022-05-16 00:00:02', 6, 'interet');


INSERT INTO projetPAE.interets(id_interet, membre, objet, date_disponibilite, appel)
VALUES (DEFAULT, 3, 12, '16 mai', false);
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Le membre bazz a liker votre objet : Pâquerettes dans pots rustiques ',
        false, '2022-05-16 00:00:01', 1, 'interet');

--INTERET CHOISIS
INSERT INTO projetPAE.interets_choisis(id_interet_choisi, membre, objet, etat_transaction)
VALUES (DEFAULT, 1, 9, 'En attente');
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Vous avez été désigné comme receveur pour l''objet: Tableau noir pour enfant ',
        false, now(), 1, 'interet choisis');


INSERT INTO projetPAE.interets_choisis(id_interet_choisi, membre, objet, etat_transaction)
VALUES (DEFAULT, 3, 12, 'En attente');
INSERT INTO projetPAE.notifications(id_notif, libelle, notif_vue, date_notif, id_membre, type)
VALUES (DEFAULT,
        'Vous avez été désigné comme receveur pour l''objet: Pâquerettes dans pots rustiques ',
        false, now(), 3, 'interet choisis');



--DEMO
--INSERT INTO projetPAE.types(id_type, libelle) VALUES (DEFAULT, 'Potager');

--SELECT m.id_membre, m.pseudo, m.admin, m.etat_inscription, m.raison_refus  FROM projetPAE.membres m ORDER BY m.admin DESC, m.etat_inscription;

--SELECT o.description, t.libelle, o.etat, o.date FROM projetPAE.objets o, projetPAE.types t WHERE o.type = t.id_type ORDER BY o.date DESC;

--SELECT m.nom, count(o.id_objet)FROM projetPAE.membres m, projetPAE.objets o WHERE o.membre_offreur = m.id_membre group by m.nom;

--SELECT ic.objet, o.description, t.libelle, o.etat, ic.etat_transaction, m.pseudo FROM projetPAE.interets_choisis ic, projetPAE.objets o, projetPAE.membres m, projetPAE.types t WHERE ic.membre = m.id_membre AND ic.objet = o.id_objet AND o.type = t.id_type;

--SELECT o.etat, count(o.id_objet)FROM projetPAE.objets o group by o.etat;







