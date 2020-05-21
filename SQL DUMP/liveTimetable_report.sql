-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com    Database: liveTimetable
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED='';

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `report` varchar(10000) NOT NULL,
  `flag` char(2) NOT NULL,
  `postedDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `report_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (4,3,'Date added','C','2020-05-03 15:16:24'),(5,3,'This is a comment.','C','2020-05-03 17:39:03'),(6,2,'Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit','B','2020-05-08 13:27:19'),(7,2,'A question is an utterance which typically functions as a request for information, which is expected to be provided in the form of an answer.','Q','2020-05-08 14:20:37'),(8,12,'Awesome','C','2020-05-10 05:07:36'),(9,12,'Awesome!','C','2020-05-10 05:07:44'),(10,12,'It\"s Awesome!','C','2020-05-10 05:07:55'),(11,11,'Unable to submit report','B','2020-05-10 05:11:51'),(12,11,'Hi','Q','2020-05-10 05:11:59'),(13,11,'D.b.m.s','B','2020-05-10 05:13:07'),(14,11,'F\"','B','2020-05-10 05:13:18'),(15,10,'the single quotation is a problem','B','2020-05-10 05:14:04'),(16,10,'tHIS IS A SECRET ERROR\n','B','2020-05-10 05:21:42'),(17,14,'Website is really awesome , you guys did a great job.\nOne Small Suggestion\nPlease add an Show password Checkbox to toggle password visibility at login form.\n                                       \n','C','2020-05-11 02:04:26');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-21 22:58:59
