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
-- Table structure for table `projectAssign`
--

DROP TABLE IF EXISTS `projectAssign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectAssign` (
  `Sno` int(40) NOT NULL AUTO_INCREMENT,
  `postedDate` date NOT NULL,
  `courseCode` varchar(12) NOT NULL,
  `facultyCode` varchar(5) NOT NULL,
  `title` varchar(2000) DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `batchNo` int(3) NOT NULL,
  `marks` int(4) DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `dueTime` time DEFAULT NULL,
  `topic` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`Sno`),
  KEY `batchNo` (`batchNo`),
  KEY `courseCode` (`courseCode`),
  KEY `facultyCode` (`facultyCode`),
  CONSTRAINT `projectAssign_ibfk_1` FOREIGN KEY (`batchNo`) REFERENCES `batch` (`batchNo`),
  CONSTRAINT `projectAssign_ibfk_2` FOREIGN KEY (`courseCode`) REFERENCES `course` (`courseCode`),
  CONSTRAINT `projectAssign_ibfk_3` FOREIGN KEY (`facultyCode`) REFERENCES `faculty` (`facultyCode`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectAssign`
--

LOCK TABLES `projectAssign` WRITE;
/*!40000 ALTER TABLE `projectAssign` DISABLE KEYS */;
INSERT INTO `projectAssign` VALUES (4,'2020-04-24','COM209T','APK','Sample 2','With Attachment',1,123,'2020-04-24','06:00:00','ATA'),(5,'2020-04-24','COM211P','ASK','Attacment Download','Downloadable attachment',3,123,'2020-04-29','02:00:00','ATA'),(6,'2020-04-24','COM209T','APK','Test a pdf','Testing a pdf ata',1,123,'2020-04-29','00:00:00','ATA');
/*!40000 ALTER TABLE `projectAssign` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-01 11:50:49
