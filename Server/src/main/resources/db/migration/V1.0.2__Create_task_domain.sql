# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE `documents`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(50)  NOT NULL UNIQUE,
    `url`        VARCHAR(60)  NOT NULL UNIQUE,
    `difficulty` ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    `is_test`    TINYINT(1)   NOT NULL,
    `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `tasks`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL UNIQUE,
    `document_id` INT          NOT NULL,
    `answer_url`  VARCHAR(60)  NOT NULL,
    `created_at`  TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_tasks_descriptions` FOREIGN KEY (`document_id`)
        REFERENCES `documents` (`id`)
);

CREATE TABLE `code_languages`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       ENUM ('JAVA', 'JAVASCRIPT') NOT NULL,
    `created_at` TIMESTAMP(3)                NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `submissions`
(
    `id`               INT PRIMARY KEY AUTO_INCREMENT,
    `task_id`          INT                         NOT NULL,
    `user_id`          INT                         NOT NULL,
    `result`           ENUM ('SOLVED', 'UNSOLVED') NOT NULL,
    `code_language_id` INT                         NOT NULL,
    `execution_time`   VARCHAR(10)                 NOT NULL,
    `created_at`       TIMESTAMP(3)                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_submissions_tasks` FOREIGN KEY (`task_id`)
        REFERENCES `tasks` (`id`),
    CONSTRAINT `fk_submissions_users` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`),
    CONSTRAINT `fk_submissions_languages` FOREIGN KEY (`code_language_id`)
        REFERENCES `code_languages` (`id`)
);