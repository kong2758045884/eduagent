-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: training_backend
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `training_backend`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `training_backend` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `training_backend`;

--
-- Table structure for table `ai_call_log`
--

DROP TABLE IF EXISTS `ai_call_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_call_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `scenario` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '调用场景',
  `model` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模型',
  `prompt_chars` int DEFAULT '0' COMMENT '提示词字符数',
  `response_chars` int DEFAULT '0' COMMENT '响应字符数',
  `elapsed_ms` bigint DEFAULT '0' COMMENT '耗时毫秒',
  `success` tinyint NOT NULL DEFAULT '1' COMMENT '是否成功',
  `error_message` varchar(600) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_ai_call_created` (`created_at`,`scenario`,`success`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 调用日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_call_log`
--

LOCK TABLES `ai_call_log` WRITE;
/*!40000 ALTER TABLE `ai_call_log` DISABLE KEYS */;
INSERT INTO `ai_call_log` VALUES (1,'chat','qwen-plus',538,1188,14838,1,NULL,'2026-06-01 09:28:30'),(3,'ocr','qwen-vl-ocr-latest',14410,134,527,1,NULL,'2026-06-01 09:29:08'),(4,'chat','qwen-plus',447,574,8953,1,NULL,'2026-06-01 09:29:17'),(5,'chat','qwen-plus',600,1253,17921,1,NULL,'2026-06-01 09:49:43'),(6,'chat','qwen-plus',600,1275,17841,1,NULL,'2026-06-01 09:50:38'),(7,'speech','qwen3-asr-flash',275580,64,637,1,NULL,'2026-06-01 09:53:20'),(8,'ocr','qwen-vl-ocr-latest',15346,169,639,1,NULL,'2026-06-01 09:54:12'),(9,'chat','qwen-plus',497,548,9837,1,NULL,'2026-06-01 09:54:22');
/*!40000 ALTER TABLE `ai_call_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_file`
--

DROP TABLE IF EXISTS `app_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '上传/生成用户 ID',
  `original_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始文件名',
  `content_type` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容类型',
  `relative_path` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '相对路径',
  `public_url` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问 URL',
  `size` bigint NOT NULL DEFAULT '0' COMMENT '文件大小',
  `biz_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务类型',
  `biz_id` bigint DEFAULT NULL COMMENT '业务 ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_file_user_biz` (`user_id`,`biz_type`,`biz_id`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件中心表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_file`
--

LOCK TABLES `app_file` WRITE;
/*!40000 ALTER TABLE `app_file` DISABLE KEYS */;
INSERT INTO `app_file` VALUES (1,3,'Smoke Growth Report.pdf','application/pdf','reports/2026/05/5ec9e341-a723-437d-8a76-60cb39541819.pdf','/uploads/reports/2026/05/5ec9e341-a723-437d-8a76-60cb39541819.pdf',39483,'growth_report',NULL,'2026-05-31 23:38:55'),(3,9,'CodexSmokeExperience.pdf','application/pdf','reports/2026/06/6c334ee5-b174-4aaf-8893-cf3fd847cae3.pdf','/uploads/reports/2026/06/6c334ee5-b174-4aaf-8893-cf3fd847cae3.pdf',41957,'experience_book',NULL,'2026-06-01 09:49:43'),(4,10,'CodexSmokeExperience.pdf','application/pdf','reports/2026/06/fa8f920d-9996-4e60-980e-3f813184be61.pdf','/uploads/reports/2026/06/fa8f920d-9996-4e60-980e-3f813184be61.pdf',42007,'experience_book',NULL,'2026-06-01 09:50:38'),(5,13,'Codex Experience Report.pdf','application/pdf','reports/2026/06/69521a47-71b7-4bcb-a8ed-6963b2c38d65.pdf','/uploads/reports/2026/06/69521a47-71b7-4bcb-a8ed-6963b2c38d65.pdf',36180,'experience_book',NULL,'2026-06-01 09:52:42'),(6,13,'Codex Assessment Package.docx','application/vnd.openxmlformats-officedocument.wordprocessingml.document','reports/2026/06/fce3efe2-c53d-43d3-943e-7f541c73ec69.docx','/uploads/reports/2026/06/fce3efe2-c53d-43d3-943e-7f541c73ec69.docx',2457,'assessment_package',NULL,'2026-06-01 09:52:42'),(7,13,'Codex Growth Report.pdf','application/pdf','reports/2026/06/ec58fd75-6132-4397-abfc-b97150c57b71.pdf','/uploads/reports/2026/06/ec58fd75-6132-4397-abfc-b97150c57b71.pdf',40788,'growth_report',NULL,'2026-06-01 09:52:43'),(8,14,'speech-test.wav','audio/wav','speech/2026/06/3e82d3b1-6411-4a82-8297-0ef53dd23564.wav','/uploads/speech/2026/06/3e82d3b1-6411-4a82-8297-0ef53dd23564.wav',275580,'smoke',NULL,'2026-06-01 09:53:20');
/*!40000 ALTER TABLE `app_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_notification`
--

DROP TABLE IF EXISTS `app_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '?? ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `source_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `source_id` bigint DEFAULT NULL COMMENT '?? ID',
  `read_status` tinyint NOT NULL DEFAULT '0' COMMENT '????',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_notification_user_read` (`user_id`,`read_status`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_notification`
--

LOCK TABLES `app_notification` WRITE;
/*!40000 ALTER TABLE `app_notification` DISABLE KEYS */;
INSERT INTO `app_notification` VALUES (1,4,'资源已提交审核','《????????》已进入县域资源审核队列。','audit','resource',3,0,'2026-06-01 09:28:30'),(2,4,'资源审核结果','《????????》审核状态：approved','audit','resource',3,0,'2026-06-01 09:28:30'),(3,9,'资源已提交审核','《Codex resource》已进入县域资源审核队列。','audit','resource',4,0,'2026-06-01 09:49:43'),(4,9,'资源审核结果','《Codex resource》审核状态：approved','audit','resource',4,0,'2026-06-01 09:49:43'),(5,9,'资源已提交审核','《Codex shared experience》已进入县域资源审核队列。','audit','resource',5,0,'2026-06-01 09:49:43'),(6,10,'资源已提交审核','《Codex resource》已进入县域资源审核队列。','audit','resource',6,0,'2026-06-01 09:50:38'),(7,10,'资源审核结果','《Codex resource》审核状态：approved','audit','resource',6,0,'2026-06-01 09:50:38'),(8,10,'资源已提交审核','《Codex shared experience》已进入县域资源审核队列。','audit','resource',7,0,'2026-06-01 09:50:38');
/*!40000 ALTER TABLE `app_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classroom_asset`
--

DROP TABLE IF EXISTS `classroom_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classroom_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `asset_key` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??????',
  `title` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `asset_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `subject` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '??' COMMENT '??',
  `grade` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '??',
  `tags` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `version` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT '1.0.0' COMMENT '??',
  `demo_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??????',
  `offline_package_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '?????',
  `file_size` bigint DEFAULT '0' COMMENT '?????',
  `offline_ready` tinyint NOT NULL DEFAULT '1' COMMENT '??????',
  `use_count` int NOT NULL DEFAULT '0' COMMENT '????',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_classroom_asset_key` (`asset_key`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classroom_asset`
--

LOCK TABLES `classroom_asset` WRITE;
/*!40000 ALTER TABLE `classroom_asset` DISABLE KEYS */;
INSERT INTO `classroom_asset` VALUES (1,'fraction-pie','分数饼图分割','math-animation','数学','三年级','用于演示分数含义、等分和合并。','分数,等分','1.0.0','/assets/classroom/fraction-pie/index.html','/assets/classroom/fraction-pie-offline.zip',2048000,1,2,'2026-06-01 09:49:25','2026-06-01 09:50:20'),(2,'cylinder-net','圆柱体展开','webgl-model','数学','六年级','用于演示圆柱侧面展开与表面积。','圆柱,表面积','1.0.0','/assets/classroom/cylinder-net/index.html','/assets/classroom/cylinder-net-offline.zip',2048000,1,0,'2026-06-01 09:49:25','2026-06-01 09:49:25'),(3,'line-segment','线段图关系分析','h5-tool','数学','五年级','用于分数应用题和数量关系建模。','线段图,应用题','1.0.0','/assets/classroom/line-segment/index.html','/assets/classroom/line-segment-offline.zip',2048000,1,0,'2026-06-01 09:49:25','2026-06-01 09:49:25'),(4,'chicken-rabbit','鸡兔同笼操作板','h5-tool','数学','五年级','用于枚举、假设和方程思想演示。','鸡兔同笼,枚举','1.0.0','/assets/classroom/chicken-rabbit/index.html','/assets/classroom/chicken-rabbit-offline.zip',2048000,1,0,'2026-06-01 09:49:25','2026-06-01 09:49:25');
/*!40000 ALTER TABLE `classroom_asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosis_report`
--

DROP TABLE IF EXISTS `diagnosis_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosis_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '创建用户 ID',
  `student_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学生姓名',
  `class_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '班级',
  `subject` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '数学' COMMENT '学科',
  `topic` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '知识点',
  `question_text` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目/OCR 文本',
  `answer_text` text COLLATE utf8mb4_unicode_ci COMMENT '学生答案/错误表现',
  `image_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错题图片地址',
  `report_json` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'AI 诊断 JSON',
  `root_cause` text COLLATE utf8mb4_unicode_ci COMMENT '根因分析',
  `interventions` text COLLATE utf8mb4_unicode_ci COMMENT '干预策略',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_diagnosis_user_created` (`user_id`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学情诊断报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosis_report`
--

LOCK TABLES `diagnosis_report` WRITE;
/*!40000 ALTER TABLE `diagnosis_report` DISABLE KEYS */;
INSERT INTO `diagnosis_report` VALUES (1,8,'王晨','六年级1班','数学','除法含义','OCR识别内容：\nMath mistake sample:\n\nQuestion: $12 \\div 3 = ?$\n\nStudent answer: $12 \\div 4 = 3$\n\nProblem: divisor and dividend relationship confusion',NULL,'/uploads/diagnosis/2026/06/a25970e4-e90f-4b25-ba91-b9713b7918b2.png','{\n  \"errorSteps\": [\"混淆除数与被除数的位置关系\", \"错误地将除号右侧数字当作被除数\", \"用12除以4而非题目要求的3\"],\n  \"rootCause\": \"未建立除法算式中‘被除数 ÷ 除数 = 商’的结构化表征，缺乏对运算符号两侧数角色的功能性理解\",\n  \"interventions\": [\"用实物分组活动：发12根小棒，每次分给3人（强调‘分给3人’即除数是3），直观体验‘12÷3’的含义\", \"引入‘除法三兄弟’角色卡：被除数（总数）、除数（每份几个/分给几份）、商（能分几份/每份几个），配图+动作强化\", \"设计‘找位置’练习：给出算式12÷□=□，让学生用卡片填入3或4，并说明为什么不能填4在除数位置\"],\n  \"teacherScript\": \"我们一起来当小侦探：题目写的是‘12 ÷ 3’，这个‘3’站在除号后面，它不是要被分的数，而是告诉我们‘要平均分成3份’或者‘每份分3个’——它叫除数。你写的‘12 ÷ 4’里，4跑来当了除数，可题目明明请3来当除数呀！我们用手势记住：左手是被除数（举高12），右手是除数（平伸3），中间‘÷’是分东西的动作。\",\n  \"followUpExercise\": \"有15颗糖，要平均分给5个小朋友，每人分几颗？请写出正确的除法算式，并圈出哪个数是除数。\"\n}','未建立除法算式中‘被除数 ÷ 除数 = 商’的结构化表征，缺乏对运算符号两侧数角色的功能性理解','用实物分组活动：发12根小棒，每次分给3人（强调‘分给3人’即除数是3），直观体验‘12÷3’的含义\n引入‘除法三兄弟’角色卡：被除数（总数）、除数（每份几个/分给几份）、商（能分几份/每份几个），配图+动作强化\n设计‘找位置’练习：给出算式12÷□=□，让学生用卡片填入3或4，并说明为什么不能填4在除数位置','2026-06-01 09:29:17'),(2,15,'Codex Student','Grade 5 Class 1','Math','Division meaning','OCR识别内容：\nMath mistake sample\n\nQuestion: $12 \\div 3 = ?$\n\nStudent answer: $12 \\div 4 = 3$\n\nProblem: divisor and dividend relationship confusion\n\nPlease diagnose the misconception.',NULL,'/uploads/diagnosis/2026/06/e9edd309-f6e1-461e-8604-a1f9b84f16d7.png','{\n  \"errorSteps\": [\"混淆了除数与被除数的位置\", \"错误地将原题12÷3写成12÷4\", \"用错误算式12÷4得出结果3，未验证是否符合原题\"],\n  \"rootCause\": \"学生未建立除法算式中\'被除数÷除数=商\'的结构化理解，对除号前后数字所代表的实际意义（总数÷份数=每份量）缺乏具象经验支撑\",\n  \"interventions\": [\"用实物分组活动：发12根小棒，让学生尝试平均分成3份（正确）和分成4份（对比），观察结果差异\", \"画图建模：引导学生画12个圆圈，用圈一圈方式分别表示\'分成3组\'和\'分成4组\'，标注算式并比较\", \"语言锚定训练：反复强调\'12是总数，3是分成的份数，结果是每份有几个\'，配合手势（左手摊开表总数，右手比3指表份数）\"],\n  \"teacherScript\": \"我们一起来当分糖果的小老师——你有12颗糖，要分给3个小朋友，每人几颗？（停顿）如果错分给了4个小朋友，每人就变少了。看，题目写的是\'分给3个\'，不是\'分给4个\'，除号后面的数字说的是\'分给几个人\'，不能随便改哦！\",\n  \"followUpExercise\": \"有15个苹果，要平均分给5个小朋友，每人分几个？请先画圈分一分，再写出正确的算式和答案。\"\n}','学生未建立除法算式中\'被除数÷除数=商\'的结构化理解，对除号前后数字所代表的实际意义（总数÷份数=每份量）缺乏具象经验支撑','用实物分组活动：发12根小棒，让学生尝试平均分成3份（正确）和分成4份（对比），观察结果差异\n画图建模：引导学生画12个圆圈，用圈一圈方式分别表示\'分成3组\'和\'分成4组\'，标注算式并比较\n语言锚定训练：反复强调\'12是总数，3是分成的份数，结果是每份有几个\'，配合手势（左手摊开表总数，右手比3指表份数）','2026-06-01 09:54:22');
/*!40000 ALTER TABLE `diagnosis_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expert_appointment`
--

DROP TABLE IF EXISTS `expert_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expert_appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '???? ID',
  `expert_id` bigint NOT NULL COMMENT '?? ID',
  `topic_id` bigint DEFAULT NULL COMMENT '???? ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `question` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `appointment_time` datetime DEFAULT NULL COMMENT '????',
  `status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '??',
  `meeting_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `reply_note` text COLLATE utf8mb4_unicode_ci COMMENT '????/??',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_appointment_user_created` (`user_id`,`created_at`),
  KEY `idx_appointment_expert_time` (`expert_id`,`appointment_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expert_appointment`
--

LOCK TABLES `expert_appointment` WRITE;
/*!40000 ALTER TABLE `expert_appointment` DISABLE KEYS */;
INSERT INTO `expert_appointment` VALUES (1,5,1,NULL,'??????','????????????','2026-06-04 09:28:30','pending','https://meeting.example.local/expert-1-2026-06-04',NULL,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(2,11,1,NULL,'Fraction teaching consultation','How should I scaffold equivalent fractions?','2026-06-04 09:51:57','confirmed','https://meeting.example.com/smoke','Confirmed by smoke test','2026-06-01 09:51:58','2026-06-01 09:51:58');
/*!40000 ALTER TABLE `expert_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expert_profile`
--

DROP TABLE IF EXISTS `expert_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expert_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `organization` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `field` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `tags` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `introduction` text COLLATE utf8mb4_unicode_ci COMMENT '??',
  `available_slots` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '?????',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '???1 ???0 ??',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_expert_field` (`field`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expert_profile`
--

LOCK TABLES `expert_profile` WRITE;
/*!40000 ALTER TABLE `expert_profile` DISABLE KEYS */;
INSERT INTO `expert_profile` VALUES (1,'刘明华','教授','省师范大学数学教育研究中心','数学课程与教学论','课题设计,错题研究,乡村数学','长期指导县域数学教研课题，擅长把错题数据转化为研究问题。','周二 19:00-20:00；周四 19:30-20:30',1,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(2,'陈思远','高级教师','县教师发展中心','课堂诊断与教学评价','听评课,课堂观察,考核材料','熟悉本地教育局考核材料要求，可指导职称申报佐证材料整理。','周二 19:00-20:00；周四 19:30-20:30',1,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(3,'徐雅琴','教研员','市教育科学研究院','教育技术与人机协同','AI教学,数字资源,成果转化','关注 AI 工具在乡村课堂中的稳定落地和成果转化。','周二 19:00-20:00；周四 19:30-20:30',1,'2026-06-01 09:28:30','2026-06-01 09:28:30');
/*!40000 ALTER TABLE `expert_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `growth_event`
--

DROP TABLE IF EXISTS `growth_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `growth_event` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `event_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'manual' COMMENT '事件类型',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事件标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '事件内容',
  `source_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint DEFAULT NULL COMMENT '来源 ID',
  `event_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_growth_user_time` (`user_id`,`event_time`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成长档案事件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `growth_event`
--

LOCK TABLES `growth_event` WRITE;
/*!40000 ALTER TABLE `growth_event` DISABLE KEYS */;
INSERT INTO `growth_event` VALUES (1,3,'lesson_practice','First lesson practice','Finished a local math lesson.',NULL,NULL,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(2,3,'resource_shared','分享教研资源：Smoke resource','Smoke summary','resource',2,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(3,3,'resource_commented','评论教研资源','Good local example.','resource',2,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(4,3,'classroom_asset_used','使用课堂辅助资源：分数饼图分割','用于演示分数含义、等分和合并。','classroom_asset',NULL,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(5,4,'lesson_saved','保存教案：用村里石磨学圆的周长','通过测量真实石磨的直径和周长，引导学生理解圆周率意义并掌握圆的周长计算方法。','lesson',1,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(6,4,'resource_shared','分享教研资源：????????','????','resource',3,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(7,5,'expert_appointment_created','预约专家咨询：??????','????????????','expert_appointment',1,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(8,6,'course_enrolled','报名课程：乡村教学场景AI适配课','本土教法库、在线答疑和成长档案袋实操。','course',3,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(9,6,'qa_question_created','发布课堂答疑：????','?????????????','qa_question',1,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(10,6,'growth_feedback_mentor_comment','????','??????,?????????','growth_feedback',1,'2026-06-01 09:28:31','2026-06-01 09:28:31'),(11,9,'classroom_asset_used','使用课堂辅助资源：分数饼图分割','用于演示分数含义、等分和合并。','classroom_asset',1,'2026-06-01 09:49:25','2026-06-01 09:49:25'),(12,9,'lesson_saved','保存教案：市场里的分数：买菜分斤两','通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。','lesson',2,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(13,9,'reflection_created','记录课后反思','Students understood equivalent fractions after using market examples. Need more practice on simplification.','reflection',1,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(14,9,'resource_shared','分享教研资源：Codex resource','Smoke resource summary','resource',4,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(15,9,'resource_favorited','收藏资源：Codex resource','Smoke resource summary','resource',4,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(16,9,'resource_watched','观看资源：Codex resource','Smoke resource summary','resource',4,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(17,9,'resource_commented','评论教研资源','Smoke comment','resource',4,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(18,9,'resource_shared','分享教研资源：Codex shared experience','Smoke shared experience','resource',5,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(19,10,'classroom_asset_used','使用课堂辅助资源：分数饼图分割','用于演示分数含义、等分和合并。','classroom_asset',1,'2026-06-01 09:50:20','2026-06-01 09:50:20'),(20,10,'lesson_saved','保存教案：市场里的分数：买菜分份子','通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。','lesson',3,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(21,10,'reflection_created','记录课后反思','Students understood equivalent fractions after using market examples. Need more practice on simplification.','reflection',2,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(22,10,'resource_shared','分享教研资源：Codex resource','Smoke resource summary','resource',6,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(23,10,'resource_favorited','收藏资源：Codex resource','Smoke resource summary','resource',6,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(24,10,'resource_watched','观看资源：Codex resource','Smoke resource summary','resource',6,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(25,10,'resource_commented','评论教研资源','Smoke comment','resource',6,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(26,10,'resource_shared','分享教研资源：Codex shared experience','Smoke shared experience','resource',7,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(27,11,'expert_appointment_created','预约专家咨询：Fraction teaching consultation','How should I scaffold equivalent fractions?','expert_appointment',2,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(28,11,'expert_appointment_updated','更新专家咨询：Fraction teaching consultation','confirmed','expert_appointment',2,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(29,12,'lesson_saved','保存教案：Smoke saved lesson','Saved without AI for downstream tests','lesson',4,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(30,12,'reflection_created','记录课后反思','Smoke reflection for downstream reports.','reflection',3,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(31,12,'course_enrolled','报名课程：AI教学入门速成课','语音备课、随堂反思录制和一键导出经验册。','course',1,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(32,12,'course_progress_updated','更新课程进度：AI教学入门速成课','进度：60%','course',1,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(33,13,'lesson_saved','保存教案：Novice smoke lesson','Saved without AI for novice tests','lesson',5,'2026-06-01 09:52:41','2026-06-01 09:52:41'),(34,13,'reflection_created','记录课后反思','Novice smoke reflection for downstream reports.','reflection',4,'2026-06-01 09:52:41','2026-06-01 09:52:41'),(35,13,'qa_question_created','发布课堂答疑：Fraction lesson','How can I diagnose fraction misconception?','qa_question',2,'2026-06-01 09:52:42','2026-06-01 09:52:42'),(36,13,'qa_reply_created','回复课堂答疑','Ask students to explain the unit whole first.','qa_question',2,'2026-06-01 09:52:42','2026-06-01 09:52:42'),(37,13,'qa_question_forwarded','转发问题给名师','How can I diagnose fraction misconception?','qa_question',2,'2026-06-01 09:52:42','2026-06-01 09:52:42'),(38,13,'lesson','Smoke growth event','Completed backend smoke test lesson','lesson',5,'2026-06-01 10:00:00','2026-06-01 09:52:42'),(39,13,'growth_feedback_mentor_comment','Smoke feedback','Keep improving diagnosis workflow.','growth_feedback',2,'2026-06-01 09:52:42','2026-06-01 09:52:42'),(40,15,'diagnosis_archived','归档学生诊断：Codex Student','Smoke archive','diagnosis',2,'2026-06-01 09:54:41','2026-06-01 09:54:41');
/*!40000 ALTER TABLE `growth_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `growth_feedback`
--

DROP TABLE IF EXISTS `growth_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `growth_feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '?? ID',
  `feedback_type` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'mentor_comment/student_feedback/method_case',
  `source` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '??',
  `source_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `source_id` bigint DEFAULT NULL COMMENT '?? ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_growth_feedback_user_type` (`user_id`,`feedback_type`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `growth_feedback`
--

LOCK TABLES `growth_feedback` WRITE;
/*!40000 ALTER TABLE `growth_feedback` DISABLE KEYS */;
INSERT INTO `growth_feedback` VALUES (1,6,'mentor_comment','???','????','??????,?????????',NULL,NULL,'2026-06-01 09:28:30'),(2,13,'mentor_comment','system','Smoke feedback','Keep improving diagnosis workflow.','growth',38,'2026-06-01 09:52:42');
/*!40000 ALTER TABLE `growth_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_draft`
--

DROP TABLE IF EXISTS `lesson_draft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson_draft` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '创建用户 ID',
  `title` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '教案标题',
  `summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摘要',
  `requirement` text COLLATE utf8mb4_unicode_ci COMMENT '原始备课需求',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Markdown/文本教案内容',
  `content_json` longtext COLLATE utf8mb4_unicode_ci COMMENT 'AI 结构化 JSON',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_lesson_user_updated` (`user_id`,`updated_at`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教案草稿表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_draft`
--

LOCK TABLES `lesson_draft` WRITE;
/*!40000 ALTER TABLE `lesson_draft` DISABLE KEYS */;
INSERT INTO `lesson_draft` VALUES (1,4,'用村里石磨学圆的周长','通过测量真实石磨的直径和周长，引导学生理解圆周率意义并掌握圆的周长计算方法。','???????,????????,??????','# 用村里石磨学圆的周长\n\n## 一、基本信息\n- 年级：六年级\n- 学科：数学\n- 知识点：圆的周长\n- 乡土案例：村里石磨/磨盘：测量磨盘直径和周长，理解圆周率和周长公式。\n\n## 二、教学目标\n- 能指出圆的直径和周长，会用软尺测量实物圆形物体的周长\n- 通过实验发现周长与直径的比值接近3.14，初步感知圆周率\n- 能用C=πd或C=2πr计算简单圆的周长，并解决石磨边沿彩带装饰等实际问题\n\n## 三、重难点\n- 圆周率π是固定比值，约等于3.14\n- 圆的周长公式C=πd和C=2πr的推导与应用\n- 测量中误差分析：为什么几次测量结果略有不同？\n\n## 四、教学过程\n### 情境导入\n- 时间：5分钟\n- 活动：\n\n### 动手测量\n- 时间：15分钟\n- 活动：\n\n### 发现规律\n- 时间：10分钟\n- 活动：\n\n### 公式应用\n- 时间：10分钟\n- 活动：\n\n## 五、评价设计\n- 课堂观察：学生能否正确使用软尺测量圆形物体\n- 小组记录表：数据是否完整、计算过程是否合理\n- 随堂小练：计算晒谷场竹围栏（圆形）所需竹条长度（已知半径3.5米）\n\n## 六、课后延伸\n- 回家测量家中锅盖、簸箕等圆形物品的直径，估算周长\n- 采访老人：过去怎么不用尺子估摸磨盘周长？了解传统估算法\n- 拓展思考：为什么石磨要做成圆形？结合周长与面积谈实用性\n\n## 七、原始需求\n???????,????????,??????\n','{\n  \"title\": \"用村里石磨学圆的周长\",\n  \"summary\": \"通过测量真实石磨的直径和周长，引导学生理解圆周率意义并掌握圆的周长计算方法。\",\n  \"grade\": \"六年级\",\n  \"subject\": \"数学\",\n  \"knowledgePoint\": \"圆的周长\",\n  \"localScenario\": \"村里石磨/磨盘：测量磨盘直径和周长，理解圆周率和周长公式。\",\n  \"objectives\": [\n    \"能指出圆的直径和周长，会用软尺测量实物圆形物体的周长\",\n    \"通过实验发现周长与直径的比值接近3.14，初步感知圆周率\",\n    \"能用C=πd或C=2πr计算简单圆的周长，并解决石磨边沿彩带装饰等实际问题\"\n  ],\n  \"keyPoints\": [\n    \"圆周率π是固定比值，约等于3.14\",\n    \"圆的周长公式C=πd和C=2πr的推导与应用\",\n    \"测量中误差分析：为什么几次测量结果略有不同？\"\n  ],\n  \"process\": [\n    {\n      \"name\": \"情境导入\",\n      \"duration\": \"5分钟\",\n      \"activities\": [\"展示村口老石磨照片；提问：给磨盘边沿钉一圈彩带，要多长？\"]\n    },\n    {\n      \"name\": \"动手测量\",\n      \"duration\": \"15分钟\",\n      \"activities\": [\"分组用软尺测量本村两台石磨的直径和周长（提前联系村民同意）；记录数据填入表格\"]\n    },\n    {\n      \"name\": \"发现规律\",\n      \"duration\": \"10分钟\",\n      \"activities\": [\"计算每台磨盘‘周长÷直径’的商；观察结果都接近3.14；引出π的概念\"]\n    },\n    {\n      \"name\": \"公式应用\",\n      \"duration\": \"10分钟\",\n      \"activities\": [\"计算：若新修磨盘直径1.2米，周长多少米？（保留一位小数）；讨论彩带预留接头长度\"]\n    }\n  ],\n  \"assessment\": [\n    \"课堂观察：学生能否正确使用软尺测量圆形物体\",\n    \"小组记录表：数据是否完整、计算过程是否合理\",\n    \"随堂小练：计算晒谷场竹围栏（圆形）所需竹条长度（已知半径3.5米）\"\n  ],\n  \"extension\": [\n    \"回家测量家中锅盖、簸箕等圆形物品的直径，估算周长\",\n    \"采访老人：过去怎么不用尺子估摸磨盘周长？了解传统估算法\",\n    \"拓展思考：为什么石磨要做成圆形？结合周长与面积谈实用性\"\n  ]\n}','2026-06-01 09:28:30','2026-06-01 09:28:30'),(2,9,'市场里的分数：买菜分斤两','通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。','Generate a concise Grade 5 math lesson about fractions using a local market scenario.','# 市场里的分数：买菜分斤两\n\n## 一、基本信息\n- 年级：5\n- 学科：数学\n- 知识点：分数的初步认识（几分之一、几分之几；同分母分数加减）\n- 乡土案例：村口小集市卖菜：李奶奶把1千克青菜平均分成4份卖，每份是1/4千克；王叔叔卖西瓜，切开后按1/3、1/3、1/3份卖；学生帮摊主算‘买1/4千克青菜和2/4千克青菜一共多少’。\n\n## 二、教学目标\n- 能结合集市实物说出1/2、1/4、3/4等分数的实际含义\n- 会读写简单分数，并用图示或实物模型表示分数\n- 能计算同分母分数的简单加减（如1/4 + 2/4），并解释结果在买菜情境中的意思\n\n## 三、重难点\n- 分数表示‘整体被平均分成几份，取其中几份’\n- 分母相同才能直接相加减，结果分母不变，分子相加减\n- 集市中‘半斤’‘四分之一斤’‘三份之一筐’都是生活中的分数\n\n## 四、教学过程\n### 情境导入\n- 时间：5分钟\n- 活动：\n\n### 探究活动\n- 时间：15分钟\n- 活动：\n\n### 应用练习\n- 时间：10分钟\n- 活动：\n\n## 五、评价设计\n- 观察学生能否用青菜捆实物指出‘这是3/4千克’\n- 检查练习题正确率，重点关注是否混淆分子分母含义\n- 随机抽取2名学生口头解释‘1/4 + 2/4 = 3/4’在卖菜中的意思\n\n## 六、课后延伸\n- 回家记录家里一次买菜时听到的分数说法（如‘半只鸡’‘一勺糖的三分之一’）\n- 为自家小菜园设计‘浇水分工表’：把一天浇水量平均分给3人，每人负责几分之几？\n\n## 七、原始需求\nGenerate a concise Grade 5 math lesson about fractions using a local market scenario.\n','{\n  \"title\": \"市场里的分数：买菜分斤两\",\n  \"summary\": \"通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。\",\n  \"grade\": 5,\n  \"subject\": \"数学\",\n  \"knowledgePoint\": \"分数的初步认识（几分之一、几分之几；同分母分数加减）\",\n  \"localScenario\": \"村口小集市卖菜：李奶奶把1千克青菜平均分成4份卖，每份是1/4千克；王叔叔卖西瓜，切开后按1/3、1/3、1/3份卖；学生帮摊主算‘买1/4千克青菜和2/4千克青菜一共多少’。\",\n  \"objectives\": [\n    \"能结合集市实物说出1/2、1/4、3/4等分数的实际含义\",\n    \"会读写简单分数，并用图示或实物模型表示分数\",\n    \"能计算同分母分数的简单加减（如1/4 + 2/4），并解释结果在买菜情境中的意思\"\n  ],\n  \"keyPoints\": [\n    \"分数表示‘整体被平均分成几份，取其中几份’\",\n    \"分母相同才能直接相加减，结果分母不变，分子相加减\",\n    \"集市中‘半斤’‘四分之一斤’‘三份之一筐’都是生活中的分数\"\n  ],\n  \"process\": [\n    {\n      \"name\": \"情境导入\",\n      \"duration\": \"5分钟\",\n      \"activities\": [\"展示村小集市照片（青菜捆、切西瓜、竹筐装豆角）\", \"提问：‘半斤青菜是1斤的几分之几？’‘一筐豆角分给3户，每户得几分之几？’\"]\n    },\n    {\n      \"name\": \"探究活动\",\n      \"duration\": \"15分钟\",\n      \"activities\": [\"分组操作：用纸条模拟1千克青菜，折出1/4、2/4、3/4；贴黑板对比\", \"用西瓜图片剪拼：1/3 + 1/3 = ? 引导学生说‘两份三分之一就是三分之二’\"]\n    },\n    {\n      \"name\": \"应用练习\",\n      \"duration\": \"10分钟\",\n      \"activities\": [\"完成‘集市小账本’练习题：李奶奶卖出1/5筐辣椒，又卖出2/5筐，共卖多少？\", \"同桌互编一道‘买菜分数题’并交换解答\"]\n    }\n  ],\n  \"assessment\": [\n    \"观察学生能否用青菜捆实物指出‘这是3/4千克’\",\n    \"检查练习题正确率，重点关注是否混淆分子分母含义\",\n    \"随机抽取2名学生口头解释‘1/4 + 2/4 = 3/4’在卖菜中的意思\"\n  ],\n  \"extension\": [\n    \"回家记录家里一次买菜时听到的分数说法（如‘半只鸡’‘一勺糖的三分之一’）\",\n    \"为自家小菜园设计‘浇水分工表’：把一天浇水量平均分给3人，每人负责几分之几？\"\n  ]\n}','2026-06-01 09:49:43','2026-06-01 09:49:43'),(3,10,'市场里的分数：买菜分份子','通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。','Generate a concise Grade 5 math lesson about fractions using a local market scenario.','# 市场里的分数：买菜分份子\n\n## 一、基本信息\n- 年级：5\n- 学科：数学\n- 知识点：分数的初步认识（几分之一、几分之几；同分母分数加减）\n- 乡土案例：村里赶集日，王奶奶用竹篮装了12个鸡蛋，分给3个邻居，每人得几分之几？李叔叔卖西瓜，把一个西瓜平均切成8块，卖了3块，还剩几分之几？\n\n## 二、教学目标\n- 能结合生活情境说出几分之一和几分之几的含义\n- 会读写简单分数，如1/3、5/8\n- 能计算同分母分数的简单加减（如2/8 + 3/8）\n\n## 三、重难点\n- 分数表示‘整体被平均分成几份，取其中几份’\n- 分母表示总份数，分子表示所取份数\n- 同分母分数相加减，分母不变，分子相加减\n\n## 四、教学过程\n### 情境导入\n- 时间：5分钟\n- 活动：\n\n### 探究新知\n- 时间：15分钟\n- 活动：\n\n### 乡土练习\n- 时间：10分钟\n- 活动：\n\n### 巩固应用\n- 时间：8分钟\n- 活动：\n\n## 五、评价设计\n- 课堂观察：学生能否指着西瓜图准确说出‘3/8’的含义\n- 练习卡第①题正确率\n- 小组活动中是否使用‘平均分’‘几份中的几份’等关键词描述\n\n## 六、课后延伸\n- 回家帮家长算：一篮青菜15棵，做汤用了5棵，用了几分之几？\n- 采访一位摊主：他今天卖的菜，哪样是按‘半份’（1/2）或‘四分之一份’（1/4）卖的？记下来明天分享\n\n## 七、原始需求\nGenerate a concise Grade 5 math lesson about fractions using a local market scenario.\n','{\n  \"title\": \"市场里的分数：买菜分份子\",\n  \"summary\": \"通过乡村集市买菜分量的情境，帮助五年级学生理解分数的意义、读写及简单同分母加减。\",\n  \"grade\": 5,\n  \"subject\": \"数学\",\n  \"knowledgePoint\": \"分数的初步认识（几分之一、几分之几；同分母分数加减）\",\n  \"localScenario\": \"村里赶集日，王奶奶用竹篮装了12个鸡蛋，分给3个邻居，每人得几分之几？李叔叔卖西瓜，把一个西瓜平均切成8块，卖了3块，还剩几分之几？\",\n  \"objectives\": [\n    \"能结合生活情境说出几分之一和几分之几的含义\",\n    \"会读写简单分数，如1/3、5/8\",\n    \"能计算同分母分数的简单加减（如2/8 + 3/8）\"\n  ],\n  \"keyPoints\": [\n    \"分数表示‘整体被平均分成几份，取其中几份’\",\n    \"分母表示总份数，分子表示所取份数\",\n    \"同分母分数相加减，分母不变，分子相加减\"\n  ],\n  \"process\": [\n    {\n      \"name\": \"情境导入\",\n      \"duration\": \"5分钟\",\n      \"activities\": [\"展示赶集照片+竹篮鸡蛋、切西瓜实物图；提问：‘12个鸡蛋分给3人，每人得多少？能用‘份’来说吗？’\"]\n    },\n    {\n      \"name\": \"探究新知\",\n      \"duration\": \"15分钟\",\n      \"activities\": [\"用彩纸折一折：把一张纸平均折成4份，涂1份→引出1/4；再折成8份，涂3份→引出3/8；结合西瓜图讲解分母与分子含义\"]\n    },\n    {\n      \"name\": \"乡土练习\",\n      \"duration\": \"10分钟\",\n      \"activities\": [\"完成‘集市分数卡’：①张伯家卖豆腐，一板豆腐切6块，卖了2块，剩下几分之几？②小卖部一包糖有10颗，阿明买了3颗，吃了1颗，还剩几分之几？\"]\n    },\n    {\n      \"name\": \"巩固应用\",\n      \"duration\": \"8分钟\",\n      \"activities\": [\"小组活动：用粉笔在地上画‘虚拟菜摊’，用石子当鸡蛋、树叶当菜叶，互相出题‘分几分之几’并列式\"]\n    }\n  ],\n  \"assessment\": [\n    \"课堂观察：学生能否指着西瓜图准确说出‘3/8’的含义\",\n    \"练习卡第①题正确率\",\n    \"小组活动中是否使用‘平均分’‘几份中的几份’等关键词描述\"\n  ],\n  \"extension\": [\n    \"回家帮家长算：一篮青菜15棵，做汤用了5棵，用了几分之几？\",\n    \"采访一位摊主：他今天卖的菜，哪样是按‘半份’（1/2）或‘四分之一份’（1/4）卖的？记下来明天分享\"\n  ]\n}','2026-06-01 09:50:38','2026-06-01 09:50:38'),(4,12,'Smoke saved lesson','Saved without AI for downstream tests','Smoke requirement','Smoke lesson content','{}','2026-06-01 09:51:58','2026-06-01 09:51:58'),(5,13,'Novice smoke lesson','Saved without AI for novice tests','Smoke requirement','Smoke novice lesson content','{}','2026-06-01 09:52:41','2026-06-01 09:52:41');
/*!40000 ALTER TABLE `lesson_draft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_reflection`
--

DROP TABLE IF EXISTS `lesson_reflection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lesson_reflection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '创建用户 ID',
  `lesson_id` bigint NOT NULL COMMENT '关联教案 ID',
  `text` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课后反思原文',
  `ai_summary` text COLLATE utf8mb4_unicode_ci COMMENT '自动摘要/批注',
  `shared` tinyint NOT NULL DEFAULT '0' COMMENT '是否共享：1 是，0 否',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_reflection_user_lesson` (`user_id`,`lesson_id`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课后反思表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_reflection`
--

LOCK TABLES `lesson_reflection` WRITE;
/*!40000 ALTER TABLE `lesson_reflection` DISABLE KEYS */;
INSERT INTO `lesson_reflection` VALUES (1,9,2,'Students understood equivalent fractions after using market examples. Need more practice on simplification.','课堂反思已关联到教案：Students understood equivalent fractions...',1,'2026-06-01 09:49:43'),(2,10,3,'Students understood equivalent fractions after using market examples. Need more practice on simplification.','课堂反思已关联到教案：Students understood equivalent fractions...',1,'2026-06-01 09:50:38'),(3,12,4,'Smoke reflection for downstream reports.','课堂反思已关联到教案：Smoke reflection for downstream reports.',1,'2026-06-01 09:51:58'),(4,13,5,'Novice smoke reflection for downstream reports.','课堂反思已关联到教案：Novice smoke reflection for downstream r...',1,'2026-06-01 09:52:41');
/*!40000 ALTER TABLE `lesson_reflection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `local_teaching_case`
--

DROP TABLE IF EXISTS `local_teaching_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `local_teaching_case` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `subject` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '??' COMMENT '??',
  `grade` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `knowledge_point` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '???',
  `local_scenario` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `resource_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT 'scenario' COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `tags` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `county` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `school` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `usage_count` int NOT NULL DEFAULT '0' COMMENT '????',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_case_search` (`subject`,`grade`,`knowledge_point`,`usage_count`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='????????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `local_teaching_case`
--

LOCK TABLES `local_teaching_case` WRITE;
/*!40000 ALTER TABLE `local_teaching_case` DISABLE KEYS */;
INSERT INTO `local_teaching_case` VALUES (1,'水稻产量算百分比','数学','五年级','百分比','本地水稻亩产','scenario','用今年和去年水稻亩产对比，设计百分比变化和增产率问题。','百分比,水稻,产量','演示县','桥头小学',3,'2026-06-01 09:28:15','2026-06-01 09:50:20'),(2,'鸡鸭数量讲倍数','数学','三年级','倍的认识','村里家禽养殖','scenario','用鸡鸭数量关系理解一份数、几份数和倍数表达。','倍数,家禽,一份数','演示县','桥头小学',3,'2026-06-01 09:28:15','2026-06-01 09:50:20'),(3,'磨盘讲圆的周长','数学','六年级','圆的周长','村里石磨/磨盘','scenario','测量磨盘直径和周长，理解圆周率和周长公式。','圆,周长,磨盘','演示县','桥头小学',3,'2026-06-01 09:28:15','2026-06-01 09:50:20'),(4,'田埂测量讲周长','数学','四年级','长方形周长','田埂和菜地','scenario','用菜地围栏长度估算长方形周长，联系实际测量。','周长,田埂,菜地','演示县','桥头小学',0,'2026-06-01 09:28:15','2026-06-01 09:28:15'),(5,'集市称重讲单位换算','数学','四年级','质量单位','乡镇集市','scenario','用斤、千克和袋装大米建立量感，解决单位换算。','单位换算,斤,千克','演示县','桥头小学',0,'2026-06-01 09:28:15','2026-06-01 09:28:15');
/*!40000 ALTER TABLE `local_teaching_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qa_question`
--

DROP TABLE IF EXISTS `qa_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qa_question` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '???? ID',
  `topic` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'open' COMMENT 'open/answered/forwarded',
  `mentor_user_id` bigint DEFAULT NULL COMMENT '??????? ID',
  `source_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `source_id` bigint DEFAULT NULL COMMENT '?? ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_qa_question_user_created` (`user_id`,`created_at`),
  KEY `idx_qa_question_status` (`status`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='???????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_question`
--

LOCK TABLES `qa_question` WRITE;
/*!40000 ALTER TABLE `qa_question` DISABLE KEYS */;
INSERT INTO `qa_question` VALUES (1,6,'????','?????????????','open',NULL,NULL,NULL,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(2,13,'Fraction lesson','How can I diagnose fraction misconception?','forwarded',NULL,'lesson',5,'2026-06-01 09:52:42','2026-06-01 09:52:42');
/*!40000 ALTER TABLE `qa_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qa_reply`
--

DROP TABLE IF EXISTS `qa_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qa_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `question_id` bigint NOT NULL COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '???? ID?0 ????',
  `role` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_qa_reply_question_created` (`question_id`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='???????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_reply`
--

LOCK TABLES `qa_reply` WRITE;
/*!40000 ALTER TABLE `qa_reply` DISABLE KEYS */;
INSERT INTO `qa_reply` VALUES (1,1,0,'平台助理','已收到问题，建议先补充学段、班级特点和已尝试方法。','2026-06-01 09:28:30'),(2,2,0,'平台助理','已收到问题，建议先补充学段、班级特点和已尝试方法。','2026-06-01 09:52:42'),(3,2,13,'mentor','Ask students to explain the unit whole first.','2026-06-01 09:52:42');
/*!40000 ALTER TABLE `qa_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `research_topic`
--

DROP TABLE IF EXISTS `research_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `research_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '创建用户 ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课题名称',
  `meta` text COLLATE utf8mb4_unicode_ci COMMENT '选题价值/推荐依据',
  `extra` text COLLATE utf8mb4_unicode_ci COMMENT '研究计划/专家信息',
  `sources` text COLLATE utf8mb4_unicode_ci COMMENT '来源材料',
  `application_draft` longtext COLLATE utf8mb4_unicode_ci COMMENT '申报书初稿',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_research_user_updated` (`user_id`,`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课题研究表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `research_topic`
--

LOCK TABLES `research_topic` WRITE;
/*!40000 ALTER TABLE `research_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `research_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource_comment`
--

DROP TABLE IF EXISTS `resource_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resource_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `resource_id` bigint NOT NULL COMMENT '资源 ID',
  `user_id` bigint NOT NULL COMMENT '评论用户 ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_comment_resource_time` (`resource_id`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource_comment`
--

LOCK TABLES `resource_comment` WRITE;
/*!40000 ALTER TABLE `resource_comment` DISABLE KEYS */;
INSERT INTO `resource_comment` VALUES (1,2,3,'Good local example.','2026-05-31 23:38:55'),(2,4,9,'Smoke comment','2026-06-01 09:49:43'),(3,6,10,'Smoke comment','2026-06-01 09:50:38');
/*!40000 ALTER TABLE `resource_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teaching_resource`
--

DROP TABLE IF EXISTS `teaching_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teaching_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '分享用户 ID',
  `title` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源标题',
  `summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '资源正文',
  `resource_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'lesson' COMMENT 'lesson/reflection/diagnosis/video 等',
  `subject` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '数学' COMMENT '学科',
  `grade` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '年级',
  `county` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '县域',
  `school` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学校',
  `cover_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '?????',
  `media_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??/????',
  `duration` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `uploader` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??????',
  `tags` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `source_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint DEFAULT NULL COMMENT '来源 ID',
  `audit_status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'approved' COMMENT '审核状态',
  `likes` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `view_count` int NOT NULL DEFAULT '0' COMMENT '??/???',
  `favorite_count` int NOT NULL DEFAULT '0' COMMENT '???',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_resource_filter` (`county`,`resource_type`,`subject`,`grade`,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='县域教研资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teaching_resource`
--

LOCK TABLES `teaching_resource` WRITE;
/*!40000 ALTER TABLE `teaching_resource` DISABLE KEYS */;
INSERT INTO `teaching_resource` VALUES (1,2,'烟测教案资源','接口烟测','测试内容','lesson','数学','三年级','演示县','桥头小学',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'approved',0,0,0,'2026-05-31 23:17:44','2026-05-31 23:17:44'),(2,3,'Smoke resource','Smoke summary','test content','lesson','Math','Grade 3','DemoCounty','BridgeSchool',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'approved',1,0,0,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(3,4,'????????','????','??????????','lesson','??','???','???','????',NULL,NULL,NULL,NULL,'?,??',NULL,NULL,'approved',0,0,0,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(4,9,'Codex resource','Smoke resource summary','A reusable teaching resource','lesson','Math','Grade 5','DemoCounty','DemoSchool',NULL,NULL,NULL,NULL,'smoke,test','lesson',2,'approved',1,1,1,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(5,9,'Codex shared experience','Smoke shared experience','# 市场里的分数：买菜分斤两\n\n# 市场里的分数：买菜分斤两\n\n## 一、基本信息\n- 年级：5\n- 学科：数学\n- 知识点：分数的初步认识（几分之一、几分之几；同分母分数加减）\n- 乡土案例：村口小集市卖菜：李奶奶把1千克青菜平均分成4份卖，每份是1/4千克；王叔叔卖西瓜，切开后按1/3、1/3、1/3份卖；学生帮摊主算‘买1/4千克青菜和2/4千克青菜一共多少’。\n\n## 二、教学目标\n- 能结合集市实物说出1/2、1/4、3/4等分数的实际含义\n- 会读写简单分数，并用图示或实物模型表示分数\n- 能计算同分母分数的简单加减（如1/4 + 2/4），并解释结果在买菜情境中的意思\n\n## 三、重难点\n- 分数表示‘整体被平均分成几份，取其中几份’\n- 分母相同才能直接相加减，结果分母不变，分子相加减\n- 集市中‘半斤’‘四分之一斤’‘三份之一筐’都是生活中的分数\n\n## 四、教学过程\n### 情境导入\n- 时间：5分钟\n- 活动：\n\n### 探究活动\n- 时间：15分钟\n- 活动：\n\n### 应用练习\n- 时间：10分钟\n- 活动：\n\n## 五、评价设计\n- 观察学生能否用青菜捆实物指出‘这是3/4千克’\n- 检查练习题正确率，重点关注是否混淆分子分母含义\n- 随机抽取2名学生口头解释‘1/4 + 2/4 = 3/4’在卖菜中的意思\n\n## 六、课后延伸\n- 回家记录家里一次买菜时听到的分数说法（如‘半只鸡’‘一勺糖的三分之一’）\n- 为自家小菜园设计‘浇水分工表’：把一天浇水量平均分给3人，每人负责几分之几？\n\n## 七、原始需求\nGenerate a concise Grade 5 math lesson about fractions using a local market scenario.\n\n## 课后心得\nStudents understood equivalent fractions after using market examples. Need more practice on simplification.\n\n## AI 摘要\n课堂反思已关联到教案：Students understood equivalent fractions...','experience','Math','Grade 5','DemoCounty','DemoSchool',NULL,NULL,NULL,NULL,NULL,'reflection',1,'pending',0,0,0,'2026-06-01 09:49:43','2026-06-01 09:49:43'),(6,10,'Codex resource','Smoke resource summary','A reusable teaching resource','lesson','Math','Grade 5','DemoCounty','DemoSchool',NULL,NULL,NULL,NULL,'smoke,test','lesson',3,'approved',1,1,1,'2026-06-01 09:50:38','2026-06-01 09:50:38'),(7,10,'Codex shared experience','Smoke shared experience','# 市场里的分数：买菜分份子\n\n# 市场里的分数：买菜分份子\n\n## 一、基本信息\n- 年级：5\n- 学科：数学\n- 知识点：分数的初步认识（几分之一、几分之几；同分母分数加减）\n- 乡土案例：村里赶集日，王奶奶用竹篮装了12个鸡蛋，分给3个邻居，每人得几分之几？李叔叔卖西瓜，把一个西瓜平均切成8块，卖了3块，还剩几分之几？\n\n## 二、教学目标\n- 能结合生活情境说出几分之一和几分之几的含义\n- 会读写简单分数，如1/3、5/8\n- 能计算同分母分数的简单加减（如2/8 + 3/8）\n\n## 三、重难点\n- 分数表示‘整体被平均分成几份，取其中几份’\n- 分母表示总份数，分子表示所取份数\n- 同分母分数相加减，分母不变，分子相加减\n\n## 四、教学过程\n### 情境导入\n- 时间：5分钟\n- 活动：\n\n### 探究新知\n- 时间：15分钟\n- 活动：\n\n### 乡土练习\n- 时间：10分钟\n- 活动：\n\n### 巩固应用\n- 时间：8分钟\n- 活动：\n\n## 五、评价设计\n- 课堂观察：学生能否指着西瓜图准确说出‘3/8’的含义\n- 练习卡第①题正确率\n- 小组活动中是否使用‘平均分’‘几份中的几份’等关键词描述\n\n## 六、课后延伸\n- 回家帮家长算：一篮青菜15棵，做汤用了5棵，用了几分之几？\n- 采访一位摊主：他今天卖的菜，哪样是按‘半份’（1/2）或‘四分之一份’（1/4）卖的？记下来明天分享\n\n## 七、原始需求\nGenerate a concise Grade 5 math lesson about fractions using a local market scenario.\n\n## 课后心得\nStudents understood equivalent fractions after using market examples. Need more practice on simplification.\n\n## AI 摘要\n课堂反思已关联到教案：Students understood equivalent fractions...','experience','Math','Grade 5','DemoCounty','DemoSchool',NULL,NULL,NULL,NULL,NULL,'reflection',2,'pending',0,0,0,'2026-06-01 09:50:38','2026-06-01 09:50:38');
/*!40000 ALTER TABLE `teaching_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_course`
--

DROP TABLE IF EXISTS `training_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `audience` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'senior/mid/novice/student',
  `category` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `summary` text COLLATE utf8mb4_unicode_ci COMMENT '??',
  `cover_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??',
  `hours` int DEFAULT '0' COMMENT '???',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '??',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  KEY `idx_course_audience` (`audience`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_course`
--

LOCK TABLES `training_course` WRITE;
/*!40000 ALTER TABLE `training_course` DISABLE KEYS */;
INSERT INTO `training_course` VALUES (1,'AI教学入门速成课','senior','AI基础','语音备课、随堂反思录制和一键导出经验册。',NULL,8,1,'2026-06-01 09:28:30','2026-06-06 20:21:47'),(2,'AI精准教学与科研提升课','mid','科研提效','错题数据解读、课题导航和考核材料整理。',NULL,16,1,'2026-06-01 09:28:30','2026-06-06 20:21:47'),(3,'乡村教学场景AI适配课','novice','成长陪跑','本土教法库、在线答疑和成长档案袋实操。',NULL,12,1,'2026-06-01 09:28:30','2026-06-06 20:21:47'),(4,'乡村AI教学工具实操与应用','student','师范生课程','麦克风、拍摄仪、备课和诊断功能实训。',NULL,24,1,'2026-06-01 09:28:30','2026-06-06 20:21:47');
/*!40000 ALTER TABLE `training_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_course_enrollment`
--

DROP TABLE IF EXISTS `training_course_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_course_enrollment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `user_id` bigint NOT NULL COMMENT '?? ID',
  `course_id` bigint NOT NULL COMMENT '?? ID',
  `progress` int NOT NULL DEFAULT '0' COMMENT '???? 0-100',
  `status` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'learning' COMMENT '????',
  `rating` int DEFAULT NULL COMMENT '??',
  `feedback` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `enrolled_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_enrollment` (`user_id`,`course_id`),
  KEY `idx_course_enrollment_user` (`user_id`,`updated_at`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='?????????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_course_enrollment`
--

LOCK TABLES `training_course_enrollment` WRITE;
/*!40000 ALTER TABLE `training_course_enrollment` DISABLE KEYS */;
INSERT INTO `training_course_enrollment` VALUES (1,6,3,0,'learning',NULL,NULL,'2026-06-01 09:28:30','2026-06-01 09:28:30'),(2,12,1,60,'learning',5,'Useful course','2026-06-01 09:51:58','2026-06-01 09:51:58');
/*!40000 ALTER TABLE `training_course_enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_course_lesson`
--

DROP TABLE IF EXISTS `training_course_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_course_lesson` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '?? ID',
  `course_id` bigint NOT NULL COMMENT '?? ID',
  `title` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '????',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `video_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `duration` int DEFAULT '0' COMMENT '????',
  `sort_order` int DEFAULT '0' COMMENT '??',
  PRIMARY KEY (`id`),
  KEY `idx_course_lesson` (`course_id`,`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='???????';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_course_lesson`
--

LOCK TABLES `training_course_lesson` WRITE;
/*!40000 ALTER TABLE `training_course_lesson` DISABLE KEYS */;
INSERT INTO `training_course_lesson` VALUES (1,1,'功能入口与场景理解','认识本课程对应的平台功能和乡村课堂任务。','/uploads/mock/videos/course-1.mp4',12,1),(2,1,'真实案例演练','基于本地案例完成一次完整操作。','/uploads/mock/videos/course-2.mp4',12,2),(3,1,'成果沉淀与反馈','将操作成果保存到档案并提交课程反馈。','/uploads/mock/videos/course-3.mp4',12,3),(4,2,'功能入口与场景理解','认识本课程对应的平台功能和乡村课堂任务。','/uploads/mock/videos/course-1.mp4',12,1),(5,2,'真实案例演练','基于本地案例完成一次完整操作。','/uploads/mock/videos/course-2.mp4',12,2),(6,2,'成果沉淀与反馈','将操作成果保存到档案并提交课程反馈。','/uploads/mock/videos/course-3.mp4',12,3),(7,3,'功能入口与场景理解','认识本课程对应的平台功能和乡村课堂任务。','/uploads/mock/videos/course-1.mp4',12,1),(8,3,'真实案例演练','基于本地案例完成一次完整操作。','/uploads/mock/videos/course-2.mp4',12,2),(9,3,'成果沉淀与反馈','将操作成果保存到档案并提交课程反馈。','/uploads/mock/videos/course-3.mp4',12,3),(10,4,'功能入口与场景理解','认识本课程对应的平台功能和乡村课堂任务。','/uploads/mock/videos/course-1.mp4',12,1),(11,4,'真实案例演练','基于本地案例完成一次完整操作。','/uploads/mock/videos/course-2.mp4',12,2),(12,4,'成果沉淀与反馈','将操作成果保存到档案并提交课程反馈。','/uploads/mock/videos/course-3.mp4',12,3);
/*!40000 ALTER TABLE `training_course_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `role` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER' COMMENT '角色',
  `teacher_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'senior' COMMENT '教师端类型：senior/mid/novice',
  `county` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '县域',
  `school` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学校',
  `avatar_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `real_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `subject` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '??' COMMENT '????',
  `grade` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '??/??',
  `teaching_years` int DEFAULT NULL COMMENT '??',
  `bio` text COLLATE utf8mb4_unicode_ci COMMENT '????',
  `expertise_tags` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '????',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '用户状态：1 正常，0 禁用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username_teacher_type` (`username`,`teacher_type`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test_20260526233412','$2a$10$hFpX1G5edTzOwd9sN0FmCuJVCUtrqUwQXQqlvbtwZKmfZRimt1mMW','测试用户','USER','senior',NULL,NULL,NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-05-26 23:34:13','2026-05-26 23:34:13'),(2,'smoke_1780240663','$2a$10$y5zq3C9A4e5AtL4tnU5LkeB90xVon8ZCDXVpadfbDM8b.uOKVQZam','烟测用户','USER','mid','演示县','桥头小学',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-05-31 23:17:44','2026-05-31 23:17:44'),(3,'smoke_1780241933','$2a$10$yYhg4.a27hoJxaXCGo4LseRAa.7C7POwlh02z34MInuDleL94ebYC','Smoke','USER','novice','DemoCounty','BridgeSchool',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-05-31 23:38:55','2026-05-31 23:38:55'),(4,'codex_senior_20260601092813','$2a$10$D6V3oilxCcoLj3dO/VacSO8/f2EQzbhEbTox9oC3SucgjcI4iY77C','Senior','USER','senior','???','????',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-06-01 09:28:15','2026-06-01 09:28:15'),(5,'codex_mid_20260601092813','$2a$10$C1/5zwzHtSOSqdj2o5JHrOChtDd3g/Wexfltm.mLQNQZS1SVXy51S','Mid','USER','mid','???','????',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-06-01 09:28:15','2026-06-01 09:28:15'),(6,'codex_novice_20260601092813','$2a$10$bxN3KZxrVe4EqlwVQGMIlezrj3FWFvXOVkOZgsDNXCpnXYgSheDri','Novice','USER','novice','???','????',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-06-01 09:28:15','2026-06-01 09:28:15'),(7,'codex_ai_senior_20260601092906','$2a$10$M/CuDoSCS5y.xdI79QCPMeklqRcwn4AcoMpuySqeDYpR.i/oiKa8a','SeniorAI','USER','senior','???','????',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-06-01 09:29:07','2026-06-01 09:29:07'),(8,'codex_ai_mid_20260601092906','$2a$10$iRqmm9g/RJM3HUN0qogQieHIqApX58YMeFPpGkMEC6wNZT/z.pEXS','MidAI','USER','mid','???','????',NULL,NULL,NULL,'??',NULL,NULL,NULL,NULL,NULL,1,'2026-06-01 09:29:07','2026-06-01 09:29:07'),(9,'codex_core_20260601094923','$2a$10$LzUv9zeq7ligdZYl1g9gGOUXCH/cl1x7/GRY0C9Su.hqBNA30lLEG','Codex Core','USER','senior','DemoCounty','DemoSchool',NULL,'Core Smoke','13800000000','Math','Grade 5','Senior Teacher',12,'Smoke test profile','lesson,diagnosis',1,'2026-06-01 09:49:24','2026-06-01 09:49:24'),(10,'codex_core2_20260601095019','$2a$10$cGI6UfjLEkS/j.4yJCxppeSjUPKit8VxiGxSfY.gsF24Fdyt2KAWW','Codex Core2','USER','senior','DemoCounty','DemoSchool',NULL,'Core Smoke','13800000000','Math','Grade 5','Senior Teacher',12,'Smoke test profile','lesson,diagnosis',1,'2026-06-01 09:50:20','2026-06-01 09:50:20'),(11,'codex_mid_20260601095157638','$2a$10$1SiUmPxKc.sdK0etzbyciOmO8fJaZkp4ajP2vWWrvGSrOFCMigmKu','Codex mid','USER','mid','DemoCounty','DemoSchool',NULL,NULL,NULL,'Math','Grade 5',NULL,NULL,NULL,NULL,1,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(12,'codex_senior_20260601095158022','$2a$10$4pGdvTvEw2IWHkrAXgii5OaSuJlLfIO0vJapnVVaA7NKiQDd1suZS','Codex senior','USER','senior','DemoCounty','DemoSchool',NULL,NULL,NULL,'Math','Grade 5',NULL,NULL,NULL,NULL,1,'2026-06-01 09:51:58','2026-06-01 09:51:58'),(13,'codex_novice_20260601095241221','$2a$10$hEXmC.LEVlD8caVHj2UqgODzhQhqqcct1A5AYB9na8IigfVk8bfCa','Codex novice','USER','novice','DemoCounty','DemoSchool',NULL,NULL,NULL,'Math','Grade 5',NULL,NULL,NULL,NULL,1,'2026-06-01 09:52:41','2026-06-01 09:52:41'),(14,'codex_speech_20260601095319312','$2a$10$1tzixeBBd52ry5ZILTFxP.q6EQQU9mD6E1OgUNOntLChm.3pK8Iv2','Speech Smoke','USER','novice','DemoCounty','DemoSchool',NULL,NULL,NULL,'Math','Grade 5',NULL,NULL,NULL,NULL,1,'2026-06-01 09:53:19','2026-06-01 09:53:19'),(15,'codex_ocr_20260601095411449','$2a$10$xcJtf7gj2Mm5KcDUyCU1LO19FtudmyvRYzhoUQgkQ4.A8TNEpJH0.','OCR Smoke','USER','mid','DemoCounty','DemoSchool',NULL,NULL,NULL,'Math','Grade 5',NULL,NULL,NULL,NULL,1,'2026-06-01 09:54:12','2026-06-01 09:54:12');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-06 20:25:09
