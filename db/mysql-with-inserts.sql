DROP DATABASE IF EXISTS `letta`;
CREATE DATABASE `letta`;

CREATE TABLE `letta`.`users`
(
    `login`    varchar(100) NOT NULL,
    `password` varchar(64)  NOT NULL,
    `role`     varchar(10)  NOT NULL,
    PRIMARY KEY (`login`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`people`
(
    `person_id`      int          NOT NULL AUTO_INCREMENT,
    `person_name`    varchar(50)  NOT NULL,
    `person_surname` varchar(100) NOT NULL,
    `login`          varchar(100) NOT NULL references users (login),
    PRIMARY KEY (`person_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`mod`
(
    `mod_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`mod_id`),
    FOREIGN KEY (mod_id) references people (person_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`admin`
(
    `admin_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`admin_id`),
    FOREIGN KEY (admin_id) references people (person_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`admin_people`
(
    `admin_id`  int NOT NULL references admin (admin_id),
    `person_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`admin_id`, `person_id`),
    FOREIGN KEY (`admin_id`) references admin (admin_id),
    FOREIGN KEY (`person_id`) references people (person_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`promoter`
(
    `promoter_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`promoter_id`),
    FOREIGN KEY (promoter_id) REFERENCES people (person_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`campaign`
(
    `campaign_id`   int         NOT NULL AUTO_INCREMENT,
    `campaign_name` varchar(50) NOT NULL,
    `promoter_id`   int         NOT NULL references promoter (promoter_id),
    PRIMARY KEY (`campaign_id`),
    FOREIGN KEY (`promoter_id`) references promoter (promoter_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`rrpp`
(
    `rrpp_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`rrpp_id`),
    FOREIGN KEY (rrpp_id) references people (person_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`events`
(
    `event_id`          int          NOT NULL AUTO_INCREMENT,
    `event_location`    varchar(100) NOT NULL,
    `event_name`        varchar(20)  NOT NULL,
    `event_description` varchar(100) NOT NULL,
    `event_date`        DATE         NOT NULL,
    `image`             varchar(500)                 ,
    `organizer_id`      int          NOT NULL references people (person_id),
    PRIMARY KEY (`event_id`),
    FOREIGN KEY (organizer_id) REFERENCES people (person_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`send_events`
(
    `rrpp_id`    int NOT NULL references rrpp (rrpp_id),
    `event_id`   int NOT NULL references events (event_id),
    `destiny_id` int NOT NULL,
    PRIMARY KEY (`rrpp_id`, `event_id`, `destiny_id`),
    FOREIGN KEY (rrpp_id) references rrpp (rrpp_id),
    FOREIGN KEY (event_id) references events (event_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`people_events`
(
    `person_id` int NOT NULL references people (person_id),
    `event_id`  int NOT NULL references events (event_id),
    PRIMARY KEY (`person_id`, `event_id`),
    FOREIGN KEY (`person_id`) references people (person_id) ON DELETE CASCADE,
    FOREIGN KEY (`event_id`) references events (event_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`groups`
(
    `group_id`   int         NOT NULL AUTO_INCREMENT,
    `group_name` varchar(45) NOT NULL,
    `creator_id` int         NOT NULL,
    PRIMARY KEY (`group_id`),
    FOREIGN KEY (`creator_id`) references people (person_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`people_groups`
(
    `person_id` int NOT NULL references people (person_id),
    `group_id`  int NOT NULL references `groups` (group_id),
    PRIMARY KEY (`person_id`, `group_id`),
    FOREIGN KEY (`person_id`) references people (person_id) ON DELETE CASCADE,
    FOREIGN KEY (`group_id`) references `groups` (group_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`groups_events`
(
    `event_id` int NOT NULL references events (event_id),
    `group_id` int NOT NULL references `groups` (group_id),
    PRIMARY KEY (`event_id`, `group_id`),
    FOREIGN KEY (`event_id`) references events (event_id) ON DELETE CASCADE,
    FOREIGN KEY (`group_id`) references `groups` (group_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `letta`.`friends`
(
    `friend1_id` int NOT NULL references people (person_id),
    `friend2_id` int NOT NULL references people (person_id),
    PRIMARY KEY (`friend1_id`, `friend2_id`),
    FOREIGN KEY (`friend1_id`) references people (person_id),
    FOREIGN KEY (`friend2_id`) references people (person_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE USER IF NOT EXISTS 'letta'@'localhost' IDENTIFIED WITH mysql_native_password BY 'letta';
GRANT ALL ON `letta`.* TO 'letta'@'localhost';

INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Antón', 'Pérez', 'anton');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Manuel', 'Martínez', 'manuel');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Laura', 'Reboredo', 'laura');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Perico', 'Palotes', 'perico');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Ana', 'María', 'ana');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'María', 'Nuevo', 'maria');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Alba', 'Fernández', 'alba');
INSERT INTO `letta`.`people` (`person_id`, `person_name`, `person_surname`, `login`)
VALUES (0, 'Asunción', 'Jiménez', 'asuncion');

-- The password for each user is its role suffixed with "pass". For example, user "admin" has the password "adminpass".
-- only for the 8 firsts users
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('anton', '713bfda78870bf9d1b261f565286f85e97ee614efe5f0faf7c34e7ca4f65baca', 'ADMIN');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('manuel', '05d49692b755f99c4504b510418efeeeebfd466892540f27acf9a31a326d6504', 'ADMIN');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('laura', '8969af198f252991021b588f3151a8921b7b2f299b62293bd4ab844c52cc5a2c', 'MOD');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('perico', '8969af198f252991021b588f3151a8921b7b2f299b62293bd4ab844c52cc5a2c', 'MOD');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('ana', 'a611303135ca2134d5a85a50047bb9479ac64637ae15702a415fa5338f091621', 'RRPP');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('maria', 'a611303135ca2134d5a85a50047bb9479ac64637ae15702a415fa5338f091621', 'RRPP');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('alba', '931b5d3c4d2e4589afa4032b593e8a3991539df580bf8cda911b0b5ff6384f88', 'PROMOTER');
INSERT INTO `letta`.`users` (`login`, `password`, `role`)
VALUES ('asuncion', '05d49692b755f99c4504b510418efeeeebfd466892540f27acf9a31a326d6504', 'USER');

INSERT INTO `letta`.`admin`(`admin_id`)
VALUES (1);
INSERT INTO `letta`.`admin`(`admin_id`)
VALUES (2);

INSERT INTO `letta`.`mod`(`mod_id`)
VALUES (3);
INSERT INTO `letta`.`mod`(`mod_id`)
VALUES (4);

INSERT INTO `letta`.`rrpp`(`rrpp_id`)
VALUES (5);
INSERT INTO `letta`.`rrpp`(`rrpp_id`)
VALUES (6);

INSERT INTO `letta`.`promoter`(`promoter_id`)
VALUES (7);
INSERT INTO `letta`.`promoter`(`promoter_id`)
VALUES (8);

INSERT INTO `letta`.`events` (`event_id`, `event_location`, `event_name`, `event_description`, `event_date`,
                              `organizer_id`)
VALUES (null, 'Ourense', 'Libros', 'Lectura de libros en el parque de San Lázaro', '2022-05-11', 3);
INSERT INTO `letta`.`events` (`event_id`, `event_location`, `event_name`, `event_description`, `event_date`,
                              `organizer_id`)
VALUES (null, 'Vigo', 'Concierto', 'Concierto Yung Beef', '2022-04-08', 5);
INSERT INTO `letta`.`events` (`event_id`, `event_location`, `event_name`, `event_description`, `event_date`,
                              `organizer_id`)
VALUES (null, 'Coruña', 'ClubAutosCoruña', 'Quedada de coches clasicos en el centro', '2022-07-15', 1);

INSERT INTO `letta`.`send_events` (rrpp_id, event_id, destiny_id)
VALUES (5, 1, 8);

INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (1, 1);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (8, 1);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (1, 2);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (2, 2);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (6, 2);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (5, 3);
INSERT INTO `letta`.people_events(`person_id`, `event_id`)
VALUES (8, 3);

INSERT INTO `letta`.`groups`(group_id, group_name, creator_id)
VALUES (0, 'Club Nipón Ourense', 2);
INSERT INTO `letta`.`groups`(group_id, group_name, creator_id)
VALUES (0, 'New Wave', 1);
INSERT INTO `letta`.`groups`(group_id, group_name, creator_id)
VALUES (0, 'MACBA', 2);
INSERT INTO `letta`.`groups`(group_id, group_name, creator_id)
VALUES (0, 'Club Lectura El Cercano', 7);

INSERT INTO `letta`.`groups_events`(event_id, group_id)
VALUES (1, 1);
INSERT INTO `letta`.`groups_events`(event_id, group_id)
VALUES (1, 2);
INSERT INTO `letta`.`groups_events`(event_id, group_id)
VALUES (2, 3);
INSERT INTO `letta`.`groups_events`(event_id, group_id)
VALUES (3, 4);

INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (1, 1);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (2, 1);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (3, 1);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (4, 1);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (5, 2);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (6, 2);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (7, 2);
INSERT INTO `letta`.`people_groups`(person_id, group_id)
VALUES (8, 2);


