-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server versie:                5.7.9-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Versie:              9.3.0.5009
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Databasestructuur van straalbetaal wordt geschreven
CREATE DATABASE IF NOT EXISTS `straalbetaal` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `straalbetaal`;


-- Structuur van  tabel straalbetaal.betaalgeschiedenis wordt geschreven
CREATE TABLE IF NOT EXISTS `betaalgeschiedenis` (
  `cardID` char(50) NOT NULL,
  `af_bij_geschreven` int(11) DEFAULT NULL,
  `datum` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cardID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporteren was gedeselecteerd


-- Structuur van  tabel straalbetaal.cards wordt geschreven
CREATE TABLE IF NOT EXISTS `cards` (
  `cardID` char(50) NOT NULL,
  `userID` char(50) NOT NULL,
  `pincode` char(4) NOT NULL,
  PRIMARY KEY (`cardID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporteren was gedeselecteerd


-- Structuur van  tabel straalbetaal.saldo wordt geschreven
CREATE TABLE IF NOT EXISTS `saldo` (
  `cardID` char(50) NOT NULL,
  `cardSaldo` int(11) NOT NULL,
  PRIMARY KEY (`cardID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporteren was gedeselecteerd


-- Structuur van  tabel straalbetaal.users wordt geschreven
CREATE TABLE IF NOT EXISTS `users` (
  `userID` char(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `registeredDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporteren was gedeselecteerd
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
