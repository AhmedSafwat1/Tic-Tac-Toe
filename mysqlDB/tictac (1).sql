-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 12, 2019 at 10:08 PM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tictac`
--

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `id` int(11) NOT NULL,
  `winner` varchar(255) NOT NULL,
  `player1` text NOT NULL,
  `player2` text NOT NULL,
  `againt` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `game`
--

INSERT INTO `game` (`id`, `winner`, `player1`, `player2`, `againt`, `user_id`) VALUES
(78, 'No-One', 'x1,x5,x4,x8,x3', 'o6,o2,o7,o9', 'PC', 3),
(79, 'hind', 'x1,x5,x9', 'o4,o3', 'Safwat', 7),
(80, 'ali', 'x1,x5,x6,x8,x3', 'o2,o4,o9,o7', 'ali', 3),
(81, 'ali', 'x1,x5,x4', 'o6,o3,o9', 'PC', 1),
(83, 'ali', 'x3,x6,x9', 'o2,o5', 'safwat', 3),
(84, 'ali', 'x1,x4,x7', 'o2,o5', 'PC', 3),
(85, 'ali', 'x2,x4,x9,x1,x7', 'o5,o8,o3,o6', 'PC', 3),
(86, 'ddd', 'x2,x5,x3', 'o1,o4,o7', 'ddd', 3),
(87, 'ali', 'x2,x5,x8', 'o1,o4', 'ddd', 3),
(88, 'ali', 'x1,x5,x9', 'o6,o2', 'PC', 1),
(89, 'ali', 'x2,x5,x8', 'o6,o4', 'nagiab', 1),
(90, 'ali', 'x2,x5,x8', 'o1,o7', 'PC', 1),
(91, 'ali', 'X1,X5,X9', 'O4,O8', 'ahmed', 3),
(92, 'ali', 'X1,X4,X7', 'O5,O9', 'safwat', 3),
(93, 'ahmed', 'X1,X7,X9,X4', 'O5,O8,O3', 'safwat', 1),
(94, 'ali', 'x2,x4,x6,x9,x3', 'o5,o1,o8,o7', 'aa', 10);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `points` int(11) NOT NULL DEFAULT '0',
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `status`, `points`, `password`) VALUES
(1, 'ahmed', 1, 0, '123'),
(2, 'ahemd', 1, 0, 'as'),
(3, 'ali', 1, 0, '123'),
(4, 'ahmed', 1, 0, '123'),
(7, 'hind', 1, 0, 'hindhind'),
(8, 'hind', 1, 0, 'hind123'),
(9, 'ali', 1, 0, '123'),
(10, 'safwat', 1, 0, '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_game_fk` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=95;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `game`
--
ALTER TABLE `game`
  ADD CONSTRAINT `user_game_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
