-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.10-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.5036
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for straalbetaal
CREATE DATABASE IF NOT EXISTS `straalbetaal` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `straalbetaal`;


-- Dumping structure for table straalbetaal.betaalgeschiedenis
CREATE TABLE IF NOT EXISTS `betaalgeschiedenis` (
  `userID` char(50) NOT NULL,
  `af_bij_geschreven` int(11) DEFAULT NULL,
  `datum` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table straalbetaal.betaalgeschiedenis: ~0 rows (approximately)
/*!40000 ALTER TABLE `betaalgeschiedenis` DISABLE KEYS */;
/*!40000 ALTER TABLE `betaalgeschiedenis` ENABLE KEYS */;


-- Dumping structure for table straalbetaal.saldo
CREATE TABLE IF NOT EXISTS `saldo` (
  `userID` varchar(50) NOT NULL,
  `userSaldo` int(11) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table straalbetaal.saldo: ~0 rows (approximately)
/*!40000 ALTER TABLE `saldo` DISABLE KEYS */;
INSERT INTO `saldo` (`userID`, `userSaldo`) VALUES
	('jasonPhillips69', 1000);
/*!40000 ALTER TABLE `saldo` ENABLE KEYS */;


-- Dumping structure for table straalbetaal.users
CREATE TABLE IF NOT EXISTS `users` (
  `userID` char(50) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `userPassword` smallint(4) NOT NULL,
  `registeredDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table straalbetaal.users: ~1 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`userID`, `firstName`, `lastName`, `userPassword`, `registeredDate`) VALUES
	('jasonPhillips69', 'Jason', 'Phillips', 6663, '2016-03-06 15:15:07');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
