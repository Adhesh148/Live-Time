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
-- Table structure for table `studentRecord`
--

DROP TABLE IF EXISTS `studentRecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `studentRecord` (
  `rollNo` varchar(11) NOT NULL,
  `loginId` int(11) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `emailId` varchar(50) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`rollNo`),
  UNIQUE KEY `loginId` (`loginId`),
  CONSTRAINT `studentRecord_ibfk_1` FOREIGN KEY (`loginId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studentRecord`
--

LOCK TABLES `studentRecord` WRITE;
/*!40000 ALTER TABLE `studentRecord` DISABLE KEYS */;
INSERT INTO `studentRecord` VALUES ('1001',14,NULL,NULL,NULL,'Guest'),('COE18B001',2,'9011114765',NULL,NULL,'Adhesh'),('COE18B003',9,NULL,NULL,NULL,'Akshun'),('COE18B004',10,'9826122012',NULL,'2000-01-03','Anant'),('COE18B005',11,NULL,NULL,NULL,'Aparajith'),('COE18B006',12,NULL,NULL,NULL,'Ashwin');
/*!40000 ALTER TABLE `studentRecord` ENABLE KEYS */;
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

-- Dump completed on 2020-05-21 22:59:09
