INSERT INTO `users` (`username`, `email`, `password_hash`)
VALUE
-- pass is 12345
('Admin', 'adminmail@somecompany.com','$2a$10$uW3wXutdYv5qJLO/9jPv5O6nwt3d6qh3ugz8mVIfdUrORqDzINhsq');

INSERT INTO `roles` (`role`)
VALUES
('ADMIN'),
('USER');

INSERT INTO `users_roles` (`user_id`, `role_id`)
VALUES
(1, 1),
(1, 2);

INSERT INTO `languages` (`name`)
VALUES
('JAVA'),
('JAVASCRIPT');