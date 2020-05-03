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
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpgbqt6dnai52x55o1qvsx1dfn` (`company_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` VALUES (4,'gabrielle.patel@pathwayelectronics.com','Gabrielle','Patel','Customer',1),(5,'brian.robinson@etechmanagement.com','Brian','Robinson','Contacted',2),(6,'eduardo.haugen@pathetechmanagement.com','Eduardo','Haugen','Customer',3),(7,'koen.johansen@pathetechmanagement.com','Koen','Johansen','NotContacted',3),(8,'alejandro.macdonald@pathwayelectronics.com','Alejandro','Macdonald','ClosedLost',1),(9,'angel.karlsson@pathetechmanagement.com','Angel','Karlsson','Contacted',3),(10,'yahir.gustavsson@pathetechmanagement.com','Yahir','Gustavsson','Contacted',3),(11,'haiden.svensson@pathetechmanagement.com','Haiden','Svensson','ClosedLost',3),(12,'emily.stewart@etechmanagement.com','Emily','Stewart','ImportedLead',2),(13,'corinne.davis@pathetechmanagement.com','Corinne','Davis','ImportedLead',3),(14,'ryann.davis@etechmanagement.com','Ryann','Davis','Customer',2),(15,'yurem.jackson@pathetechmanagement.com','Yurem','Jackson','Contacted',3),(16,'kelly.gustavsson@pathetechmanagement.com','Kelly','Gustavsson','ImportedLead',3),(17,'eileen.walker@pathwayelectronics.com','Eileen','Walker','Contacted',1),(18,'katelyn.martin@pathwayelectronics.com','Katelyn','Martin','Customer',1),(19,'israel.carlsson@pathetechmanagement.com','Israel','Carlsson','ImportedLead',3),(20,'quinn.hansson@pathetechmanagement.com','Quinn','Hansson','Contacted',3),(21,'makena.smith@pathetechmanagement.com','Makena','Smith','Contacted',3),(22,'danielle.watson@pathwayelectronics.com','Danielle','Watson','Customer',1),(23,'leland.harris@etechmanagement.com','Leland','Harris','ImportedLead',2),(24,'gunner.karlsen@pathetechmanagement.com','Gunner','Karlsen','ImportedLead',3),(25,'jamar.olsson@pathetechmanagement.com','Jamar','Olsson','ImportedLead',3),(26,'lara.martin@etechmanagement.com','Lara','Martin','NotContacted',2),(27,'ann.andersson@etechmanagement.com','Ann','Andersson','NotContacted',2),(28,'remington.andersson@etechmanagement.com','Remington','Andersson','Contacted',2),(29,'rene.carlsson@etechmanagement.com','Rene','Carlsson','Contacted',2),(30,'elvis.olsen@pathetechmanagement.com','Elvis','Olsen','NotContacted',3),(31,'solomon.olsen@pathwayelectronics.com','Solomon','Olsen','NotContacted',1),(32,'jaydan.jackson@etechmanagement.com','Jaydan','Jackson','Contacted',2),(33,'bernard.nilsen@pathwayelectronics.com','Bernard','Nilsen','Contacted',1);
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-01 11:50:56
