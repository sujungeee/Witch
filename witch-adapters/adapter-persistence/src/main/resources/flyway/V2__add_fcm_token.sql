CREATE TABLE `fcm_token`
(
    `fcm_token_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      VARCHAR(36)  NOT NULL,
    `fcm_token`    VARCHAR(300) NOT NULL
);
