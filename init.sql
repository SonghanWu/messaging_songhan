CREATE DATABASE IF NOT EXISTS messaging_test;
USE messaging_test;

CREATE USER IF NOT EXISTS 'messaging'@'localhost' IDENTIFIED BY 'messaging2021';
GRANT ALL PRIVILEGES ON messaging_test.* TO 'messaging'@'localhost';

CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(128) CHARACTER SET utf8mb3  DEFAULT NULL,
    `nickname` varchar(128) CHARACTER SET utf8mb4  DEFAULT NULL,
    `password` varchar(128) CHARACTER SET utf8mb3  DEFAULT NULL,
    `login_token` varchar(128) CHARACTER SET utf8mb3 DEFAULT NULL,
    `register_time` datetime DEFAULT NULL,
    `last_login_time` datetime DEFAULT NULL,
    `gender` varchar(128) DEFAULT NULL,
    `email` varchar(128) DEFAULT NULL,
    `address` varchar(128) DEFAULT NULL,
    `is_valid` tinyint(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_uniq_index` (`username`),
    UNIQUE KEY `email_uniq_index` (`email`),
    KEY `nickname_index` (`nickname`),
    KEY `user_gender` (`gender`),
    KEY `user_login_token_index` (`login_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_validation_code` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int DEFAULT NULL,
    `validation_code` varchar(8) DEFAULT NULL,
    `generated_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_login_token` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` int DEFAULT NULL,
    `login_token` varchar(8) DEFAULT NULL,
    `login_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id_index` (`user_id`),
    KEY `login_token_index` (`login_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `friend_invitation` (
    `id` int NOT NULL AUTO_INCREMENT,
    `sender_user_id` int DEFAULT NULL,
    `receiver_user_id` int DEFAULT NULL,
    `message` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `status` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `sender_user_id_index` (`sender_user_id`),
    KEY `receiver_user_id_index` (`receiver_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
