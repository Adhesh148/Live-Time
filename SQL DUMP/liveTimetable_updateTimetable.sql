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
-- Table structure for table `updateTimetable`
--

DROP TABLE IF EXISTS `updateTimetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `updateTimetable` (
  `Sno` int(5) NOT NULL AUTO_INCREMENT,
  `batchNo` int(3) NOT NULL,
  `slotNo` int(2) NOT NULL,
  `courseCode` varchar(12) NOT NULL,
  `facultyCode` varchar(5) NOT NULL,
  `hallNo` varchar(5) NOT NULL,
  `date` date NOT NULL,
  `flag` varchar(2) NOT NULL,
  `rescheduleId` int(5) DEFAULT NULL,
  `postedDate` date DEFAULT NULL,
  PRIMARY KEY (`Sno`),
  KEY `batchNo` (`batchNo`),
  KEY `slotNo` (`slotNo`),
  KEY `courseCode` (`courseCode`),
  KEY `facultyCode` (`facultyCode`),
  KEY `hallNo` (`hallNo`),
  KEY `rescheduleId` (`rescheduleId`),
  CONSTRAINT `updateTimetable_ibfk_1` FOREIGN KEY (`batchNo`) REFERENCES `batch` (`batchNo`),
  CONSTRAINT `updateTimetable_ibfk_2` FOREIGN KEY (`slotNo`) REFERENCES `slot` (`slotNo`),
  CONSTRAINT `updateTimetable_ibfk_3` FOREIGN KEY (`courseCode`) REFERENCES `course` (`courseCode`),
  CONSTRAINT `updateTimetable_ibfk_4` FOREIGN KEY (`facultyCode`) REFERENCES `faculty` (`facultyCode`),
  CONSTRAINT `updateTimetable_ibfk_5` FOREIGN KEY (`hallNo`) REFERENCES `hall` (`hallNo`),
  CONSTRAINT `updateTimetable_ibfk_6` FOREIGN KEY (`rescheduleId`) REFERENCES `updateTimetable` (`Sno`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `updateTimetable`
--

LOCK TABLES `updateTimetable` WRITE;
/*!40000 ALTER TABLE `updateTimetable` DISABLE KEYS */;
INSERT INTO `updateTimetable` VALUES (26,1,1,'COM209T','NSG','H23','2020-05-07','S',NULL,'2020-05-05'),(27,1,2,'COM212P','JKL','L209','2020-05-07','CW',NULL,'2020-05-05'),(28,1,1,'COM210P','APK','H21','2020-05-08','S',NULL,'2020-05-05'),(30,3,3,'COM211P','BRJ','H16','2020-05-11','S',NULL,'2020-05-09'),(31,2,3,'COM211P','APK','H22','2020-05-08','CW',NULL,'2020-05-09'),(32,2,4,'COM211P','APK','H22','2020-05-08','CW',NULL,'2020-05-09'),(33,2,3,'COM210P','NSG','H22','2020-05-04','CW',NULL,'2020-05-09'),(34,3,1,'DES203T','JBI','H22','2020-05-12','S',NULL,'2020-05-09'),(35,2,2,'MAN202T','SVR','H25','2020-05-04','CW',NULL,'2020-05-09'),(36,2,2,'COM212T','JKL','H23','2020-05-05','CW',NULL,'2020-05-09'),(37,2,1,'MAN202T','SVR','H25','2020-05-04','CW',NULL,'2020-05-09'),(38,2,1,'COM211P','ASK','H21','2020-05-06','CW',NULL,'2020-05-09'),(39,2,2,'COM211P','APK','H22','2020-05-08','CW',NULL,'2020-05-09'),(40,2,1,'COM212T','JKL','H23','2020-05-05','CW',NULL,'2020-05-09'),(41,1,9,'COM211P','TSN','L209','2020-05-08','CW',NULL,'2020-05-09'),(42,1,1,'COM212T','JKL','H15','2020-05-11','CW',NULL,'2020-05-09'),(43,2,2,'MAN202T','SVR','H25','2020-05-11','CW',NULL,'2020-05-09'),(44,1,1,'COM212T','JKL','H15','2020-05-18','CW',NULL,'2020-05-09'),(45,1,5,'COM212T','JKL','H15','2020-05-11','S',NULL,'2020-05-09'),(46,2,7,'MAN202T','SVR','H13','2020-05-15','S',NULL,'2020-05-10'),(47,1,2,'COM211T','TSN','H15','2020-05-15','CW',NULL,'2020-05-13');
/*!40000 ALTER TABLE `updateTimetable` ENABLE KEYS */;
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

-- Dump completed on 2020-05-21 22:58:51
