# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE `users`
(
    `id`            INT PRIMARY KEY AUTO_INCREMENT,
    `username`      VARCHAR(40)  NOT NULL UNIQUE,
    `password_hash` CHAR(60)     NOT NULL,
    `email`         VARCHAR(254) NOT NULL UNIQUE,
    `created_at`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `roles`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `role` ENUM ('USER', 'ADMIN') NOT NULL UNIQUE
);

CREATE TABLE `users_roles`
(
    `user_id` INT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_users_roles_users` FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`),
    CONSTRAINT `fk_users_roles_roles` FOREIGN KEY (`role_id`)
        REFERENCES `roles` (`id`)
);