DROP DATABASE IF EXISTS `letta`;
CREATE DATABASE `letta`;

CREATE TABLE `letta`.`people`
(
    `person_id`      int          NOT NULL AUTO_INCREMENT,
    `person_name`    varchar(50)  NOT NULL,
    `person_surname` varchar(100) NOT NULL,
    `login`          varchar(100) NOT NULL,
    `password`       varchar(64)  NOT NULL,
    `role`           varchar(10)  NOT NULL,
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