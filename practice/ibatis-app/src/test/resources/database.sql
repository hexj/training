CREATE DATABASE hexj;
USE hexj;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `userid` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `users` WRITE;

INSERT INTO `users` VALUES (1,'Jeff'),(2,'Jonathan'),(3,'DDD'),(4,'Tom'),(5,'Lox'),(6,'Dave'),(7,'abc'),(8,'abc'),(9,'Ben');

UNLOCK TABLES;
