-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: liveTimetable
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.18.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `updateTimetable`
--

LOCK TABLES `updateTimetable` WRITE;
/*!40000 ALTER TABLE `updateTimetable` DISABLE KEYS */;
INSERT INTO `updateTimetable` VALUES (2,1,1,'COM209T','NSG','H15','2020-04-02','S',NULL),(3,1,2,'MAN202T','SVR','H25','2020-04-01','S',NULL),(4,2,1,'COM210P','APK','H22','2020-04-23','S',NULL),(5,2,3,'COM210P','ASK','H22','2020-04-21','S',NULL),(6,1,1,'COM209T','NSG','H12','2020-04-23','S',NULL);
/*!40000 ALTER TABLE `updateTimetable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-21 10:49:34
