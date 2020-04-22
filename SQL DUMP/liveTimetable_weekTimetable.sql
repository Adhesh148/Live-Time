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
-- Table structure for table `weekTimetable`
--

DROP TABLE IF EXISTS `weekTimetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weekTimetable` (
  `Sno` int(5) NOT NULL AUTO_INCREMENT,
  `batchNo` int(3) DEFAULT NULL,
  `slotNo` int(2) DEFAULT NULL,
  `courseCode` varchar(12) DEFAULT NULL,
  `facultyCode` varchar(5) DEFAULT NULL,
  `hallNo` varchar(5) DEFAULT NULL,
  `dayNo` int(2) DEFAULT NULL,
  PRIMARY KEY (`Sno`),
  UNIQUE KEY `slotNo` (`slotNo`,`batchNo`,`dayNo`),
  UNIQUE KEY `slotNo_2` (`slotNo`,`dayNo`,`hallNo`),
  KEY `batchNo` (`batchNo`),
  KEY `courseCode` (`courseCode`),
  KEY `facultyCode` (`facultyCode`),
  KEY `hallNo` (`hallNo`),
  KEY `dayNo` (`dayNo`),
  CONSTRAINT `weekTimetable_ibfk_1` FOREIGN KEY (`batchNo`) REFERENCES `batch` (`batchNo`),
  CONSTRAINT `weekTimetable_ibfk_2` FOREIGN KEY (`slotNo`) REFERENCES `slot` (`slotNo`),
  CONSTRAINT `weekTimetable_ibfk_3` FOREIGN KEY (`courseCode`) REFERENCES `course` (`courseCode`),
  CONSTRAINT `weekTimetable_ibfk_4` FOREIGN KEY (`facultyCode`) REFERENCES `faculty` (`facultyCode`),
  CONSTRAINT `weekTimetable_ibfk_5` FOREIGN KEY (`hallNo`) REFERENCES `hall` (`hallNo`),
  CONSTRAINT `weekTimetable_ibfk_6` FOREIGN KEY (`dayNo`) REFERENCES `day` (`dayNo`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weekTimetable`
--

LOCK TABLES `weekTimetable` WRITE;
/*!40000 ALTER TABLE `weekTimetable` DISABLE KEYS */;
INSERT INTO `weekTimetable` VALUES (1,1,1,'COM212T','JKL','H15',1),(3,1,3,'MAN202T','SVR','H25',1),(4,1,4,'MAN202T','SVR','H25',1),(8,1,8,'DES203T','JBI','H25',1),(9,1,9,'DES203T','JBI','H25',1),(10,1,1,'COM212T','JKL','H15',2),(12,1,3,'MAT205T','SMA','H15',2),(19,1,1,'COM212T','JKL','H15',3),(29,1,2,'COM212P','JKL','L209',4),(30,1,3,'COM212P','JKL','L209',4),(31,1,4,'COM212P','JKL','L209',4),(32,1,5,'MAT205T','SMA','H15',4),(34,1,7,'COM210P','NSG','L209',4),(35,1,8,'COM210P','NSG','L209',4),(36,1,9,'COM210P','NSG','L209',4),(38,1,2,'COM211T','TSN','H15',5),(39,1,3,'COM209T','NSG','H15',5),(40,1,4,'MAT205T','SMA','H15',5),(41,1,5,'COM211T','TSN','H15',5),(43,1,7,'COM211T','TSN','H15',5),(44,1,8,'COM211P','TSN','L209',5),(45,1,9,'COM211P','TSN','L209',5),(58,1,2,'COM209T','NSG','H15',1),(59,1,2,'COM209T','NSG','H15',2),(61,2,1,'MAN202T','SVR','H25',1),(62,2,2,'MAN202T','SVR','H25',1),(64,2,1,'COM212T','JKL','H23',2),(65,2,2,'COM212T','JKL','H23',2),(66,2,2,'COM211P','APK','H22',5),(67,2,3,'COM211P','APK','H22',5),(68,2,4,'COM211P','APK','H22',5),(69,1,3,'COM209T','NSG','H15',3),(70,2,3,'COM210P','NSG','H22',1),(71,2,1,'COM211P','ASK','H21',3);
/*!40000 ALTER TABLE `weekTimetable` ENABLE KEYS */;
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
