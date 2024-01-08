-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: sgu_event_scheduler_app
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `co_so`
--

DROP TABLE IF EXISTS `co_so`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `co_so` (
  `id` int NOT NULL AUTO_INCREMENT,
  `trang_thai` bit(1) DEFAULT NULL,
  `ma_cs` varchar(255) DEFAULT NULL,
  `ten_cs` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `co_so`
--

LOCK TABLES `co_so` WRITE;
/*!40000 ALTER TABLE `co_so` DISABLE KEYS */;
INSERT INTO `co_so` VALUES (1,_binary '','CSC','Cơ sở chính'),(2,_binary '','CS1','Cơ sở 1'),(3,_binary '','CS2','Cơ sở 2'),(6,_binary '\0','CSXX','Cơ sở del'),(7,_binary '\0','CSDEL2','CSDEL2');
/*!40000 ALTER TABLE `co_so` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `giang_vien`
--

DROP TABLE IF EXISTS `giang_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giang_vien` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_tai_khoan` int DEFAULT NULL,
  `ma_gv` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cldc6lwhohm1in94t34v9bsgs` (`id_tai_khoan`),
  CONSTRAINT `FKpkxiu2p4ove634mrbg1fcq4cq` FOREIGN KEY (`id_tai_khoan`) REFERENCES `tai_khoan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `giang_vien`
--

LOCK TABLES `giang_vien` WRITE;
/*!40000 ALTER TABLE `giang_vien` DISABLE KEYS */;
INSERT INTO `giang_vien` VALUES (1,2,'14441');
/*!40000 ALTER TABLE `giang_vien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khoa`
--

DROP TABLE IF EXISTS `khoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khoa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `trang_thai` bit(1) DEFAULT NULL,
  `ma_khoa` varchar(255) DEFAULT NULL,
  `ten_khoa` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khoa`
--

LOCK TABLES `khoa` WRITE;
/*!40000 ALTER TABLE `khoa` DISABLE KEYS */;
INSERT INTO `khoa` VALUES (1,_binary '','CNTT','Công nghệ thông tin'),(2,_binary '','DTVT','Điện tử viễn thông'),(3,_binary '','GD','Giáo dục'),(4,_binary '','GDCT','Giáo dục chính trị'),(5,_binary '','LU','Luật'),(6,_binary '','NN','Ngoại ngữ'),(7,_binary '','TC-KT','Tài chính kế toán'),(8,_binary '','T-UD','Toán - Ứng dụng'),(9,_binary '','QTKD','Quản trị kinh doanh'),(10,_binary '','VHDL','Văn hóa và Du lịch'),(11,_binary '','TV-VP','Thư viện - Văn phòng'),(12,_binary '','SP-KHTN','Sư phạm Khoa học Tự nhiên'),(13,_binary '','SP-KHXH','Sư phạm Khoa học Xã hội'),(14,_binary '','NT','Nghệ thuật'),(15,_binary '','MT','Môi trường'),(16,_binary '','GDTH','Giáo dục Tiểu học'),(17,_binary '','GDMN','Giáo dục Mầm non'),(18,_binary '','GDQP-AN-GDTC','Giáo dục Quốc phòng - An ninh - Giáo dục thể chất');
/*!40000 ALTER TABLE `khoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lop`
--

DROP TABLE IF EXISTS `lop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lop` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_khoa` int DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `ma_lop` varchar(255) DEFAULT NULL,
  `ten_lop` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiy998bs6vwnditexyxqnnj9x7` (`id_khoa`),
  CONSTRAINT `FKiy998bs6vwnditexyxqnnj9x7` FOREIGN KEY (`id_khoa`) REFERENCES `khoa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lop`
--

LOCK TABLES `lop` WRITE;
/*!40000 ALTER TABLE `lop` DISABLE KEYS */;
INSERT INTO `lop` VALUES (1,1,_binary '','DCT1201','ngành Công Nghệ Thông Tin - K20 - lớp 1'),(2,1,_binary '','DCT1202','ngành Công Nghệ Thông Tin - K20 - lớp 2'),(3,1,_binary '','DCT1203','ngành Công Nghệ Thông Tin - K20 - lớp 3'),(4,1,_binary '','DCT1204','ngành Công Nghệ Thông Tin - K20 - lớp 4'),(5,1,_binary '','DCT1205','ngành Công Nghệ Thông Tin - K20 - lớp 5'),(6,1,_binary '','DCT1206','ngành Công Nghệ Thông Tin - K20 - lớp 6'),(7,1,_binary '','DCT1207','ngành Công Nghệ Thông Tin - K20 - lớp 7'),(8,1,_binary '','DCT1208','ngành Công Nghệ Thông Tin - K20 - lớp 8'),(9,1,_binary '','DCT1209','ngành Công Nghệ Thông Tin - K20 - lớp 9'),(10,1,_binary '','DCT12010','ngành Công Nghệ Thông Tin - K20 - lớp 10'),(11,1,_binary '','DKP1201','ngành Kỹ thuật phần mềm - K20 - lớp 1'),(12,1,_binary '','DKP1202','ngành Kỹ thuật phần mềm - K20 - lớp 2'),(13,2,_binary '','DDE1201','ngành Kỹ thuật điện - K20 - lớp 1'),(14,2,_binary '','DDV1201','ngành Kỹ thuật Điện tử-viễn thông - K20 - lớp 1'),(15,2,_binary '','DCV1201','ngành Công nghệ KT Điện tử-viễn thông - K20 - lớp 1'),(16,2,_binary '','DKD1201','ngành Công nghệ Kỹ thuật điện, điện tử - K20 - lớp 1'),(17,3,_binary '\0','GD1202','Lớp GD 1 - k20');
/*!40000 ALTER TABLE `lop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phong_hoc`
--

DROP TABLE IF EXISTS `phong_hoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `phong_hoc` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_co_so` int DEFAULT NULL,
  `trang_thai` bit(1) DEFAULT NULL,
  `ma_phong` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKehqufglbi1p773y5qck0ukacd` (`id_co_so`),
  CONSTRAINT `FKehqufglbi1p773y5qck0ukacd` FOREIGN KEY (`id_co_so`) REFERENCES `co_so` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phong_hoc`
--

LOCK TABLES `phong_hoc` WRITE;
/*!40000 ALTER TABLE `phong_hoc` DISABLE KEYS */;
INSERT INTO `phong_hoc` VALUES (1,1,_binary '','E101'),(2,1,_binary '','E102'),(3,1,_binary '','E103'),(4,1,_binary '','E104'),(5,1,_binary '','E105'),(6,1,_binary '','E201'),(7,1,_binary '','E202'),(8,1,_binary '','E203'),(9,1,_binary '','E204'),(10,1,_binary '','E205'),(11,2,_binary '','A001'),(12,2,_binary '','A002'),(13,2,_binary '','A003'),(14,3,_binary '','A001'),(15,3,_binary '','A002'),(16,3,_binary '','A003'),(28,6,_binary '\0','F101'),(29,7,_binary '\0','PHDEL2');
/*!40000 ALTER TABLE `phong_hoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sinh_vien`
--

DROP TABLE IF EXISTS `sinh_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sinh_vien` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_tai_khoan` int DEFAULT NULL,
  `ma_sv` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_eamilsklld5t1bxxme477cg3j` (`id_tai_khoan`),
  CONSTRAINT `FK74i61muty72dpavi5hqvxcdhg` FOREIGN KEY (`id_tai_khoan`) REFERENCES `tai_khoan` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sinh_vien`
--

LOCK TABLES `sinh_vien` WRITE;
/*!40000 ALTER TABLE `sinh_vien` DISABLE KEYS */;
INSERT INTO `sinh_vien` VALUES (1,3,'3120560088'),(2,4,'3120560089'),(3,5,'3120560090');
/*!40000 ALTER TABLE `sinh_vien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `su_kien`
--

DROP TABLE IF EXISTS `su_kien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `su_kien` (
  `bat_dau` time DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `id_khoa` int DEFAULT NULL,
  `id_lop` int DEFAULT NULL,
  `id_nguoi_tao` int DEFAULT NULL,
  `id_phong` int DEFAULT NULL,
  `ket_thuc` time DEFAULT NULL,
  `ngay` date DEFAULT NULL,
  `sl_cho` int DEFAULT NULL,
  `mo_ta` text,
  `ten_su_kien` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkhn1whcm7ctij2d7vwpjj0wl` (`id_nguoi_tao`),
  KEY `FKhbpmpau1mibot26ypya6yigpx` (`id_lop`),
  KEY `FK5n9is6ksdl5dojsmtoxa6uc3m` (`id_khoa`),
  KEY `FKr3dwbdgvlwsls4f3q7ryrwmte` (`id_phong`),
  CONSTRAINT `FK5n9is6ksdl5dojsmtoxa6uc3m` FOREIGN KEY (`id_khoa`) REFERENCES `khoa` (`id`),
  CONSTRAINT `FKhbpmpau1mibot26ypya6yigpx` FOREIGN KEY (`id_lop`) REFERENCES `lop` (`id`),
  CONSTRAINT `FKkhn1whcm7ctij2d7vwpjj0wl` FOREIGN KEY (`id_nguoi_tao`) REFERENCES `tai_khoan` (`id`),
  CONSTRAINT `FKr3dwbdgvlwsls4f3q7ryrwmte` FOREIGN KEY (`id_phong`) REFERENCES `phong_hoc` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `su_kien`
--

LOCK TABLES `su_kien` WRITE;
/*!40000 ALTER TABLE `su_kien` DISABLE KEYS */;
INSERT INTO `su_kien` VALUES ('07:30:00',1,1,11,2,10,'09:00:00','2023-10-05',50,'Hãy tham gia, đây là bắt buộc','Họp lớp dkp1201 chấm drl'),('09:00:00',2,1,1,2,2,'10:40:00','2023-11-20',45,'Tổ chức bỏ phiếu học chuyên ngành cho năm 3 trở đi','Họp lớp dct1201 chọn chuyên ngành'),('13:15:00',3,2,13,2,12,'14:30:00','2023-10-23',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Ten su kien 123'),('14:00:00',4,2,14,1,13,'15:30:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Ten su kien haha'),('14:00:00',5,1,NULL,1,1,'15:30:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Ten su kien haha12'),('15:00:00',6,1,NULL,1,2,'16:00:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Ten su kien bla'),('16:15:00',7,1,NULL,1,3,'16:30:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Ten su kien b12la'),('16:20:00',8,1,NULL,1,4,'17:00:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','The name'),('17:10:00',9,1,NULL,1,5,'18:30:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','event name'),('18:00:00',10,1,NULL,1,6,'19:00:00','2023-10-29',NULL,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','event title'),('17:00:00',40,2,13,2,15,'18:00:00','2023-10-23',NULL,'FFGG!','wow'),('13:00:00',65,3,NULL,2,1,'13:15:00','2023-11-24',124,'qweqw','delete event123123'),('07:00:00',70,1,7,1,28,'09:00:00','2023-11-29',250,'đáasdssad','F101 Del'),('07:00:00',71,1,10,2,3,'08:30:00','2023-12-13',250,'Việc làm IT FRESHER | 100% lương thử việc | Tăng lương 2 lần/ năm | TopDev - Việc Làm IT Hàng Đầu\r\nCƠ HỘI GỌI TÊN IT FRESHER\r\nhttps://topdev.vn/s/1cRvfW8u\r\n?? 100% Lương thử việc, lương tháng 13, thưởng hàng năm và các khoản trợ cấp, thưởng hấp dẫn, các chuyến du lịch, nghỉ mát, team building, loạt phúc lợi đỉnh,...\r\nQA Tester | HOIIO | 100% lương thử việc, 14 ngày nghỉ có lương/ năm,  lương tháng 13\r\n➤ https://topdev.vn/s/ameVFKa7\r\nJava Develope | TopSquad | Cung cấp Macbook Pro, thời gian làm việc linh hoạt, có thể làm tại nhà, tăng lương 2 lần/ năm\r\n➤ https://topdev.vn/s/FHM2XQXH\r\n Software Developer | TSB VN | Lương tháng 13, thưởng tết, môi trường tốt để luyện tiếng anh giao tiếp\r\n➤ https://topdev.vn/s/W1qPXQha\r\nPHP Developer | DigiBird | Trợ cấp trong khi thực tập, trở thành nhân viên chính thức sau thời gian thực tập\r\n➤ https://topdev.vn/s/Qk382T22\r\nFRONT-END | Bình Anh | Tăng lương 2 lần/ năm, thưởng tháng 13, thưởng doanh thu công ty, thưởng thâm niên, ','long desc event'),('13:00:00',72,2,NULL,2,15,'15:00:00','2023-12-22',120,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','Sự kiện a1'),('07:00:00',73,1,12,2,13,'08:00:00','2023-12-18',250,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','sự kiện b1'),('10:00:00',74,3,NULL,2,10,'10:45:00','2024-01-08',250,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','sự kiện c1'),('07:30:00',75,2,16,2,9,'08:30:00','2024-01-10',250,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','sự kiện d1'),('09:30:00',76,1,6,2,4,'10:15:00','2024-01-03',250,'Lorem ipsum dolor sit amet consectetur adipisicing elit. Corrupti maxime exercitationem, molestias iste molestiae quas hic illum voluptas earum odio quisquam ipsam magnam','sự kiện e1'),('13:30:00',77,2,15,2,7,'14:30:00','2023-12-14',250,'Việc làm IT FRESHER | 100% lương thử việc | Tăng lương 2 lần/ năm | TopDev - Việc Làm IT Hàng Đầu CƠ HỘI GỌI TÊN IT FRESHER https://topdev.vn/s/1cRvfW8u ?? 100% Lương thử việc, lương tháng 13, thưởng hàng năm và các khoản trợ cấp, thưởng hấp dẫn, các chuyến du lịch, nghỉ mát, team building, loạt phúc lợi đỉnh,... QA Tester | HOIIO | 100% lương thử việc, 14 ngày nghỉ có lương/ năm, lương tháng 13 ➤ https://topdev.vn/s/ameVFKa7 Java Develope | TopSquad |','sự kiện a2'),('07:00:00',78,1,8,2,3,'09:00:00','2023-12-09',250,'Việc làm IT FRESHER | 100% lương thử việc | Tăng lương 2 lần/ năm | TopDev - Việc Làm IT Hàng Đầu CƠ HỘI GỌI TÊN IT FRESHER https://topdev.vn/s/1cRvfW8u ?? 100% Lương thử việc, lương tháng 13, thưởng hàng năm và các khoản trợ cấp, thưởng hấp dẫn, các chuyến du lịch, nghỉ mát, team building, loạt phúc lợi đỉnh,... QA Tester | HOIIO | 100% lương thử việc, 14 ngày nghỉ có lương/ năm, lương tháng 13 ➤ https://topdev.vn/s/ameVFKa7 Java Develope | TopSquad |','sự kiện b2'),('14:00:00',79,1,10,2,5,'15:00:00','2023-12-25',250,'Việc làm IT FRESHER | 100% lương thử việc | Tăng lương 2 lần/ năm | TopDev - Việc Làm IT Hàng Đầu CƠ HỘI GỌI TÊN IT FRESHER https://topdev.vn/s/1cRvfW8u ?? 100% Lương thử việc, lương tháng 13, thưởng hàng năm và các khoản trợ cấp, thưởng hấp dẫn, các chuyến du lịch, nghỉ mát, team building, loạt phúc lợi đỉnh,... QA Tester | HOIIO | 100% lương thử việc, 14 ngày nghỉ có lương/ năm, lương tháng 13 ➤ https://topdev.vn/s/ameVFKa7 Java Develope | TopSquad |','sự kiện d3'),('07:00:00',80,2,13,2,11,'09:15:00','2023-12-13',250,'Việc làm IT FRESHER | 100% lương thử việc | Tăng lương 2 lần/ năm | TopDev - Việc Làm IT Hàng Đầu CƠ HỘI GỌI TÊN IT FRESHER https://topdev.vn/s/1cRvfW8u ?? 100% Lương thử việc, lương tháng 13, thưởng hàng năm và các khoản trợ cấp, thưởng hấp dẫn, các chuyến du lịch, nghỉ mát, team building, loạt phúc lợi đỉnh,... QA Tester | HOIIO | 100% lương thử việc, 14 ngày nghỉ có lương/ năm, lương tháng 13 ➤ https://topdev.vn/s/ameVFKa7 Java Develope | TopSquad |','sự kiện e3');
/*!40000 ALTER TABLE `su_kien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sv_sk`
--

DROP TABLE IF EXISTS `sv_sk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sv_sk` (
  `id_sk` int NOT NULL,
  `id_sv` int NOT NULL,
  `diem_danh` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id_sk`,`id_sv`),
  KEY `FKce8aea7qc8no0l9sjg8pw4qwi` (`id_sv`),
  CONSTRAINT `FKce8aea7qc8no0l9sjg8pw4qwi` FOREIGN KEY (`id_sv`) REFERENCES `sinh_vien` (`id`),
  CONSTRAINT `FKpsfg4qnrg58gvd73gegrx1edh` FOREIGN KEY (`id_sk`) REFERENCES `su_kien` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sv_sk`
--

LOCK TABLES `sv_sk` WRITE;
/*!40000 ALTER TABLE `sv_sk` DISABLE KEYS */;
INSERT INTO `sv_sk` VALUES (1,1,_binary ''),(1,2,_binary '\0'),(2,1,_binary '\0'),(3,2,_binary '\0'),(6,1,_binary '\0'),(8,1,_binary '\0'),(71,1,_binary '\0'),(71,2,_binary '\0'),(73,1,_binary '\0'),(75,1,_binary '\0'),(75,2,_binary '\0'),(75,3,_binary '\0'),(79,1,_binary '\0');
/*!40000 ALTER TABLE `sv_sk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tai_khoan`
--

DROP TABLE IF EXISTS `tai_khoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tai_khoan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_vai_tro` int DEFAULT NULL,
  `mat_khau` varchar(255) DEFAULT NULL,
  `ten_dn` varchar(255) DEFAULT NULL,
  `ho_ten` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe5kamlxqi4e8iw7h2asdb9ohy` (`id_vai_tro`),
  CONSTRAINT `FKe5kamlxqi4e8iw7h2asdb9ohy` FOREIGN KEY (`id_vai_tro`) REFERENCES `vai_tro` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tai_khoan`
--

LOCK TABLES `tai_khoan` WRITE;
/*!40000 ALTER TABLE `tai_khoan` DISABLE KEYS */;
INSERT INTO `tai_khoan` VALUES (1,1,'$2a$10$GepJiMm5NqStUrO070sheur7TIeR.eog8LTujcNzY8Fuo0IfGSUJ2','admin','Võ thị C',NULL,NULL),(2,2,'$2a$10$/QW/rxLc8e/dambIFvyutO/QcVA9Ge84KQx/6XokTa5rIyvKwO5PS','teacher','Nguyễn Văn A','teacher@gmail.com',NULL),(3,3,'$2a$10$wpPuRNU4Ypw7im4s2Por.uJNP8FZuJOSJ/AisLATB6sWLcb/pml2e','student','Trần Nguyễn Việt Thái','trannguyenvietthai@gmail.com',NULL),(4,3,'$2a$10$6MM3qUrabi2EF9fL3P/j3OAa3bm7VLSbEBZDOCXJq/kpYIabqIS7a','student2','Trần Dần','ahthikbenhiu@gmail.com',NULL),(5,3,'$2a$10$6MM3qUrabi2EF9fL3P/j3OAa3bm7VLSbEBZDOCXJq/kpYIabqIS7a','student3','Nguyễn Simp','thaiforredvelvet2@gmail.com',NULL);
/*!40000 ALTER TABLE `tai_khoan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vai_tro`
--

DROP TABLE IF EXISTS `vai_tro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vai_tro` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ma_vt` varchar(255) DEFAULT NULL,
  `ten_vt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vai_tro`
--

LOCK TABLES `vai_tro` WRITE;
/*!40000 ALTER TABLE `vai_tro` DISABLE KEYS */;
INSERT INTO `vai_tro` VALUES (1,'ADMIN','Quản trị viên'),(2,'TEACHER','Giảng viên'),(3,'STUDENT','Sinh viên');
/*!40000 ALTER TABLE `vai_tro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-08 21:09:35
