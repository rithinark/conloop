USE conloop;
-- 1. Roles table:

CREATE TABLE `roles` (
   `role_id` varchar(255) NOT NULL,
   `role_name` varchar(50) NOT NULL,
   PRIMARY KEY (`role_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. Users table:

CREATE TABLE `users` (
   `user_id` varchar(255) NOT NULL,
   `username` varchar(255) NOT NULL,
   `email` varchar(50) NOT NULL,
   `password` varchar(255) NOT NULL,
   `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `is_enabled` tinyint(1) NOT NULL DEFAULT '1',
   `is_credentials_non_expired` tinyint(1) NOT NULL DEFAULT '1',
   `is_account_non_locked` tinyint(1) NOT NULL DEFAULT '1',
   `is_account_non_expired` tinyint(1) NOT NULL DEFAULT '1',
   PRIMARY KEY (`user_id`),
   UNIQUE KEY `username` (`username`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. User_roles table:

CREATE TABLE `user_roles` (
   `user_id` varchar(255) NOT NULL,
   `role_id` varchar(255) NOT NULL,
   PRIMARY KEY (`user_id`,`role_id`),
   KEY `role_id` (`role_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. User_details table:
CREATE TABLE `user_details` (
   `user_details_id` varchar(255) NOT NULL,
   `user_id` varchar(255) NOT NULL,
   `bio` varchar(255) DEFAULT NULL,
   `profile_picture` varchar(255) DEFAULT NULL,
   `location` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`user_details_id`),
   KEY `user_id` (`user_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

 -- Insert roles

INSERT INTO conloop.roles (role_id, role_name) VALUES (UUID(), 'ROLE_ADMIN');
INSERT INTO conloop.roles (role_id, role_name) VALUES (UUID(), 'ROLE_USER');
INSERT INTO conloop.roles (role_id, role_name) VALUES (UUID(), 'ROLE_GROUP_ADMIN');
INSERT INTO conloop.roles (role_id, role_name) VALUES (UUID(), 'ROLE_GROUP_MEMBER');