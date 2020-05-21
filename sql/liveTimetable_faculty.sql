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
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faculty` (
  `facultyCode` varchar(5) NOT NULL,
  `facultyName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`facultyCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculty`
--

LOCK TABLES `faculty` WRITE;
/*!40000 ALTER TABLE `faculty` DISABLE KEYS */;
INSERT INTO `faculty` VALUES ('AKR','Y. Ashok Kumar Reddy.'),('APK','Anushree P Khandale'),('ASK','AsutoshKar'),('BJK','Binsu J Kailath'),('BM','Banshidhar Majhi'),('BRJ','Raja B'),('BSS','Sivaselvan B'),('CBB','Chitti Babu B'),('CGN','Gurunathan C'),('CSD','Charles Suresh David (External)'),('DJM','Dony J Muttath  (PhD Scholar)'),('DKN','Deepak Kumar Nayak'),('DYK','Dhayalakumar (PhD Scholar)'),('GSN','Gowthaman Swaminathan'),('JBI','Jayachandra Bingi'),('JBK','Jayabal K'),('JKL','Jagadeesh Kakarla'),('KBN','Kirubakaran S (PhD Scholar)'),('KLP','Kalpana P'),('KNN','Karthic Narayanan'),('KPK','Premkumar K.'),('KPP','Kumar Prasannajith Pradhan'),('KSJ','Selvajyothi K'),('KSK','Senthilkumaran K'),('KVK','Vijayakumar K.'),('MDS','Selvaraj M. D.'),('MLN','Mahalakshmi Niroo (External)'),('MNS','Munesh Singh'),('MRG','Muralidhar G (PhD Scholar)'),('MSK','Sreekumar M'),('NKM','Nachiketa Mishra'),('NKV','Naveen Kumar Vats'),('NMM','Noor Mahammad'),('NSG','Sadagopan N'),('PDD','Damodharan P'),('PKK','Priyanka Kokil'),('PPK','P. Prasanna Kumar (PhD Scholar)'),('PPN','Pandithevan P'),('PSS','Prerna Saxena'),('PVS','Pandiarasan Veluswamy'),('RRM','Raguraman M'),('SBC','Shubhankar Chakraborty'),('SBP','Sriram Bhaskar P M'),('SHK','Shahul Hamid Khan'),('SJV','Jayavel S'),('SKM','Sathish Kumar M. (PhD Scholar)'),('SMA','Shalu M A'),('SNN','Narayanan S'),('SPV','Venkateshan SP'),('SRP','Pandian S. R.'),('SVK','Vijayakumar S'),('SVN','Shankaran Vaidyanathan (External)'),('SVR','Sudhir Varadarajan'),('TPS','Tapas Sil'),('TSN','Harinarayanan T S'),('UMJ','Umarani J'),('VJK','Dr. Vijayakumar K'),('VMM','Masilamani V'),('VNS','Vani Sudhakar (Lab Engineer)'),('VTM','Venkata Timmaraju Mallina'),('VVK','Vivek Kumar'),('WGN','William Gnanasekaran (External)');
/*!40000 ALTER TABLE `faculty` ENABLE KEYS */;
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

-- Dump completed on 2020-05-21 22:58:57
