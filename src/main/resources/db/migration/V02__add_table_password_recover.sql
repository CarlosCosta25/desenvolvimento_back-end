CREATE TABLE `tb_password_recover` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(100) NOT NULL,
  `email` VARCHAR(200) NOT NULL,
  `expiration` DATETIME NOT NULL,
  PRIMARY KEY (`id`));