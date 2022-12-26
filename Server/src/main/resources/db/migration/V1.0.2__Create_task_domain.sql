# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE `descriptions`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `url`        VARCHAR(20)  NOT NULL UNIQUE,
    `created_at` TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `tasks`
(
    `id`             INT PRIMARY KEY AUTO_INCREMENT,
    `name`           VARCHAR(60)  NOT NULL UNIQUE,
    `description_id` INT          NOT NULL,
    `answer_url`     VARCHAR(30)  NOT NULL,
    `created_at`     TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_tasks_descriptions` FOREIGN KEY (`description_id`)
        REFERENCES `descriptions` (`id`)
);

CREATE TABLE `language_categories`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       ENUM ('JAVA', 'JAVASCRIPT') NOT NULL,
    `created_at` TIMESTAMP(3)                NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `submissions`
(
    `id`                   INT PRIMARY KEY AUTO_INCREMENT,
    `task_id`              INT                         NOT NULL,
    `user_id`              INT                         NOT NULL,
    `result`               ENUM ('SOLVED', 'UNSOLVED') NOT NULL,
    `language_category_id` INT                         NOT NULL,
    `execution_time`       DOUBLE,
    `created_at`           TIMESTAMP(3)                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_submissions_tasks` FOREIGN KEY (`task_id`)
        REFERENCES `tasks` (`id`),
    CONSTRAINT `fk_submissions_users` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`),
    CONSTRAINT `fk_submissions_language_categories` FOREIGN KEY (`language_category_id`)
        REFERENCES `language_categories` (`id`)
);