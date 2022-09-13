DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `cart_item`;

CREATE TABLE IF NOT EXISTS `user` (
        `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `email` varchar(255) NOT NULL,
        `first_name` varchar(255) NOT NULL,
        `last_name` varchar(255) NOT NULL,
        `password` varchar(255) NOT NULL,
        `phone` varchar(255) NOT NULL,
        `user_type` varchar(255) NOT NULL,
        UNIQUE KEY `UK_email` (`email`)
);

CREATE TABLE IF NOT EXISTS `product` (
        `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `available_quantity` int NOT NULL,
        `description` varchar(255) NOT NULL,
        `name` varchar(255) NOT NULL,
        `price` double NOT NULL
);

CREATE TABLE IF NOT EXISTS `address` (
        `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `city` varchar(255) NOT NULL,
        `country` varchar(255) NOT NULL,
        `state` varchar(255) NOT NULL,
        `street` varchar(255) NOT NULL,
        `zipcode` varchar(255) NOT NULL,
        `user_id` int NOT NULL,
        CONSTRAINT `FK_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `order` (
        `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        `order_date` datetime NOT NULL,
        `order_status` varchar(255) NOT NULL,
        `address_id` int NOT NULL,
        `user_id` int NOT NULL,
        CONSTRAINT `FK_order_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
        CONSTRAINT `FK_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `order_item` (
       `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
       `price` double NOT NULL,
       `quantity` int NOT NULL,
       `order_id` int NOT NULL,
       `product_id` int NOT NULL,
       CONSTRAINT `FK_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
       CONSTRAINT `FK_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
) ;

CREATE TABLE IF NOT EXISTS `cart` (
       `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
       `user_id` int NOT NULL,
       UNIQUE KEY `UK_user` (`user_id`),
       CONSTRAINT `FK_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `cart_item` (
       `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
       `quantity` int NOT NULL,
       `cart_id` int NOT NULL,
       `product_id` int NOT NULL,
       CONSTRAINT `FK_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`),
       CONSTRAINT `FK_cart_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);


