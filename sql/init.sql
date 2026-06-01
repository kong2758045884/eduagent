CREATE DATABASE IF NOT EXISTS training_backend
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE training_backend;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `role` VARCHAR(32) NOT NULL DEFAULT 'USER' COMMENT '角色',
    `teacher_type` VARCHAR(32) NOT NULL DEFAULT 'senior' COMMENT '教师端类型：senior/mid/novice',
    `county` VARCHAR(128) DEFAULT NULL COMMENT '县域',
    `school` VARCHAR(128) DEFAULT NULL COMMENT '学校',
    `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT '头像地址',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
    `subject` VARCHAR(64) DEFAULT '数学' COMMENT '任教学科',
    `grade` VARCHAR(64) DEFAULT NULL COMMENT '任教年级',
    `title` VARCHAR(64) DEFAULT NULL COMMENT '职称/身份',
    `teaching_years` INT DEFAULT NULL COMMENT '教龄',
    `bio` TEXT DEFAULT NULL COMMENT '个人简介',
    `expertise_tags` VARCHAR(512) DEFAULT NULL COMMENT '擅长标签',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：1 正常，0 禁用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

SET @schema_name = DATABASE();
SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'teacher_type'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `teacher_type` VARCHAR(32) NOT NULL DEFAULT ''senior'' COMMENT ''教师端类型：senior/mid/novice'' AFTER `role`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'county'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `county` VARCHAR(128) DEFAULT NULL COMMENT ''县域'' AFTER `teacher_type`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'school'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `school` VARCHAR(128) DEFAULT NULL COMMENT ''学校'' AFTER `county`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'avatar_url'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT ''头像地址'' AFTER `school`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'real_name'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `real_name` VARCHAR(64) DEFAULT NULL COMMENT ''真实姓名'' AFTER `avatar_url`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'phone'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `phone` VARCHAR(32) DEFAULT NULL COMMENT ''联系电话'' AFTER `real_name`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'subject'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `subject` VARCHAR(64) DEFAULT ''数学'' COMMENT ''任教学科'' AFTER `phone`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'grade'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `grade` VARCHAR(64) DEFAULT NULL COMMENT ''任教年级'' AFTER `subject`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'title'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `title` VARCHAR(64) DEFAULT NULL COMMENT ''职称/身份'' AFTER `grade`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'teaching_years'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `teaching_years` INT DEFAULT NULL COMMENT ''教龄'' AFTER `title`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'bio'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `bio` TEXT DEFAULT NULL COMMENT ''个人简介'' AFTER `teaching_years`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'user' AND COLUMN_NAME = 'expertise_tags'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `user` ADD COLUMN `expertise_tags` VARCHAR(512) DEFAULT NULL COMMENT ''擅长标签'' AFTER `bio`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS `lesson_draft` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '创建用户 ID',
    `title` VARCHAR(128) NOT NULL COMMENT '教案标题',
    `summary` VARCHAR(255) DEFAULT NULL COMMENT '摘要',
    `requirement` TEXT DEFAULT NULL COMMENT '原始备课需求',
    `content` LONGTEXT NOT NULL COMMENT 'Markdown/文本教案内容',
    `content_json` LONGTEXT DEFAULT NULL COMMENT 'AI 结构化 JSON',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_lesson_user_updated` (`user_id`, `updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教案草稿表';

CREATE TABLE IF NOT EXISTS `lesson_reflection` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '创建用户 ID',
    `lesson_id` BIGINT NOT NULL COMMENT '关联教案 ID',
    `text` TEXT NOT NULL COMMENT '课后反思原文',
    `ai_summary` TEXT DEFAULT NULL COMMENT '自动摘要/批注',
    `shared` TINYINT NOT NULL DEFAULT 0 COMMENT '是否共享：1 是，0 否',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_reflection_user_lesson` (`user_id`, `lesson_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课后反思表';

CREATE TABLE IF NOT EXISTS `diagnosis_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '创建用户 ID',
    `student_name` VARCHAR(64) DEFAULT NULL COMMENT '学生姓名',
    `class_name` VARCHAR(64) DEFAULT NULL COMMENT '班级',
    `subject` VARCHAR(64) DEFAULT '数学' COMMENT '学科',
    `topic` VARCHAR(128) DEFAULT NULL COMMENT '知识点',
    `question_text` TEXT NOT NULL COMMENT '题目/OCR 文本',
    `answer_text` TEXT DEFAULT NULL COMMENT '学生答案/错误表现',
    `image_url` VARCHAR(512) DEFAULT NULL COMMENT '错题图片地址',
    `report_json` LONGTEXT NOT NULL COMMENT 'AI 诊断 JSON',
    `root_cause` TEXT DEFAULT NULL COMMENT '根因分析',
    `interventions` TEXT DEFAULT NULL COMMENT '干预策略',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_diagnosis_user_created` (`user_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学情诊断报告表';

CREATE TABLE IF NOT EXISTS `teaching_resource` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '分享用户 ID',
    `title` VARCHAR(128) NOT NULL COMMENT '资源标题',
    `summary` VARCHAR(255) DEFAULT NULL COMMENT '简介',
    `content` LONGTEXT DEFAULT NULL COMMENT '资源正文',
    `resource_type` VARCHAR(32) NOT NULL DEFAULT 'lesson' COMMENT 'lesson/reflection/diagnosis/video 等',
    `subject` VARCHAR(64) DEFAULT '数学' COMMENT '学科',
    `grade` VARCHAR(64) DEFAULT NULL COMMENT '年级',
    `county` VARCHAR(128) DEFAULT NULL COMMENT '县域',
    `school` VARCHAR(128) DEFAULT NULL COMMENT '学校',
    `source_type` VARCHAR(32) DEFAULT NULL COMMENT '来源类型',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源 ID',
    `audit_status` VARCHAR(32) NOT NULL DEFAULT 'approved' COMMENT '审核状态',
    `likes` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_resource_filter` (`county`, `resource_type`, `subject`, `grade`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='县域教研资源表';

CREATE TABLE IF NOT EXISTS `research_topic` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '创建用户 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '课题名称',
    `meta` TEXT DEFAULT NULL COMMENT '选题价值/推荐依据',
    `extra` TEXT DEFAULT NULL COMMENT '研究计划/专家信息',
    `sources` TEXT DEFAULT NULL COMMENT '来源材料',
    `application_draft` LONGTEXT DEFAULT NULL COMMENT '申报书初稿',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_research_user_updated` (`user_id`, `updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课题研究表';

CREATE TABLE IF NOT EXISTS `app_file` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '上传/生成用户 ID',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `content_type` VARCHAR(128) DEFAULT NULL COMMENT '内容类型',
    `relative_path` VARCHAR(512) NOT NULL COMMENT '相对路径',
    `public_url` VARCHAR(512) NOT NULL COMMENT '访问 URL',
    `size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小',
    `biz_type` VARCHAR(64) DEFAULT NULL COMMENT '业务类型',
    `biz_id` BIGINT DEFAULT NULL COMMENT '业务 ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_file_user_biz` (`user_id`, `biz_type`, `biz_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件中心表';

CREATE TABLE IF NOT EXISTS `growth_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `event_type` VARCHAR(64) NOT NULL DEFAULT 'manual' COMMENT '事件类型',
    `title` VARCHAR(180) NOT NULL COMMENT '事件标题',
    `content` TEXT DEFAULT NULL COMMENT '事件内容',
    `source_type` VARCHAR(64) DEFAULT NULL COMMENT '来源类型',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源 ID',
    `event_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_growth_user_time` (`user_id`, `event_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成长档案事件表';

CREATE TABLE IF NOT EXISTS `resource_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `resource_id` BIGINT NOT NULL COMMENT '资源 ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户 ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_comment_resource_time` (`resource_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源评论表';

CREATE TABLE IF NOT EXISTS `ai_call_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `scenario` VARCHAR(64) DEFAULT NULL COMMENT '调用场景',
    `model` VARCHAR(64) DEFAULT NULL COMMENT '模型',
    `prompt_chars` INT DEFAULT 0 COMMENT '提示词字符数',
    `response_chars` INT DEFAULT 0 COMMENT '响应字符数',
    `elapsed_ms` BIGINT DEFAULT 0 COMMENT '耗时毫秒',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功',
    `error_message` VARCHAR(600) DEFAULT NULL COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_ai_call_created` (`created_at`, `scenario`, `success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI 调用日志表';

CREATE TABLE IF NOT EXISTS `expert_profile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name` VARCHAR(64) NOT NULL COMMENT '专家姓名',
    `title` VARCHAR(64) DEFAULT NULL COMMENT '职称',
    `organization` VARCHAR(128) DEFAULT NULL COMMENT '单位',
    `field` VARCHAR(128) DEFAULT NULL COMMENT '研究领域',
    `tags` VARCHAR(512) DEFAULT NULL COMMENT '标签',
    `introduction` TEXT DEFAULT NULL COMMENT '简介',
    `available_slots` VARCHAR(512) DEFAULT NULL COMMENT '可预约时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 可用，0 禁用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_expert_field` (`field`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家库表';

CREATE TABLE IF NOT EXISTS `expert_appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '预约用户 ID',
    `expert_id` BIGINT NOT NULL COMMENT '专家 ID',
    `topic_id` BIGINT DEFAULT NULL COMMENT '关联课题 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '咨询主题',
    `question` TEXT NOT NULL COMMENT '咨询问题',
    `appointment_time` DATETIME DEFAULT NULL COMMENT '预约时间',
    `status` VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '状态',
    `reply_note` TEXT DEFAULT NULL COMMENT '专家回复/备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_appointment_user_created` (`user_id`, `created_at`),
    KEY `idx_appointment_expert_time` (`expert_id`, `appointment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家预约表';

CREATE TABLE IF NOT EXISTS `qa_question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '提问用户 ID',
    `topic` VARCHAR(128) DEFAULT NULL COMMENT '问题主题',
    `content` TEXT NOT NULL COMMENT '问题内容',
    `status` VARCHAR(32) NOT NULL DEFAULT 'open' COMMENT 'open/answered/forwarded',
    `mentor_user_id` BIGINT DEFAULT NULL COMMENT '被转发名师用户 ID',
    `source_type` VARCHAR(64) DEFAULT NULL COMMENT '来源类型',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源 ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_qa_question_user_created` (`user_id`, `created_at`),
    KEY `idx_qa_question_status` (`status`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线答疑问题表';

CREATE TABLE IF NOT EXISTS `qa_reply` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `question_id` BIGINT NOT NULL COMMENT '问题 ID',
    `user_id` BIGINT NOT NULL COMMENT '回复用户 ID，0 表示系统',
    `role` VARCHAR(64) DEFAULT NULL COMMENT '回复角色',
    `content` TEXT NOT NULL COMMENT '回复内容',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_qa_reply_question_created` (`question_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线答疑回复表';

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'cover_url'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `cover_url` VARCHAR(512) DEFAULT NULL COMMENT ''封面图地址'' AFTER `school`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'media_url'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `media_url` VARCHAR(512) DEFAULT NULL COMMENT ''视频/媒体地址'' AFTER `cover_url`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'duration'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `duration` VARCHAR(32) DEFAULT NULL COMMENT ''视频时长'' AFTER `media_url`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'uploader'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `uploader` VARCHAR(128) DEFAULT NULL COMMENT ''上传者展示名'' AFTER `duration`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'tags'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `tags` VARCHAR(512) DEFAULT NULL COMMENT ''资源标签'' AFTER `uploader`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'view_count'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `view_count` INT NOT NULL DEFAULT 0 COMMENT ''观看/浏览数'' AFTER `likes`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'teaching_resource' AND COLUMN_NAME = 'favorite_count'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `teaching_resource` ADD COLUMN `favorite_count` INT NOT NULL DEFAULT 0 COMMENT ''收藏数'' AFTER `view_count`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'expert_appointment' AND COLUMN_NAME = 'meeting_url'
);
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE `expert_appointment` ADD COLUMN `meeting_url` VARCHAR(512) DEFAULT NULL COMMENT ''会议链接'' AFTER `status`',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS `local_teaching_case` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '案例标题',
    `subject` VARCHAR(64) DEFAULT '数学' COMMENT '学科',
    `grade` VARCHAR(64) DEFAULT NULL COMMENT '年级',
    `knowledge_point` VARCHAR(128) DEFAULT NULL COMMENT '知识点',
    `local_scenario` VARCHAR(255) DEFAULT NULL COMMENT '乡土场景',
    `resource_type` VARCHAR(64) DEFAULT 'scenario' COMMENT '资源类型',
    `content` TEXT DEFAULT NULL COMMENT '案例内容',
    `tags` VARCHAR(512) DEFAULT NULL COMMENT '标签',
    `county` VARCHAR(128) DEFAULT NULL COMMENT '县域',
    `school` VARCHAR(128) DEFAULT NULL COMMENT '学校',
    `usage_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_case_search` (`subject`, `grade`, `knowledge_point`, `usage_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乡土教学案例库表';

CREATE TABLE IF NOT EXISTS `classroom_asset` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `asset_key` VARCHAR(64) NOT NULL COMMENT '资源包唯一键',
    `title` VARCHAR(128) NOT NULL COMMENT '标题',
    `asset_type` VARCHAR(64) NOT NULL COMMENT '资源类型',
    `subject` VARCHAR(64) DEFAULT '数学' COMMENT '学科',
    `grade` VARCHAR(64) DEFAULT NULL COMMENT '年级',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `tags` VARCHAR(512) DEFAULT NULL COMMENT '标签',
    `version` VARCHAR(32) DEFAULT '1.0.0' COMMENT '版本',
    `demo_url` VARCHAR(512) DEFAULT NULL COMMENT '在线演示地址',
    `offline_package_url` VARCHAR(512) DEFAULT NULL COMMENT '离线包地址',
    `file_size` BIGINT DEFAULT 0 COMMENT '离线包大小',
    `offline_ready` TINYINT NOT NULL DEFAULT 1 COMMENT '是否支持离线',
    `use_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_classroom_asset_key` (`asset_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课堂辅助资源包表';

CREATE TABLE IF NOT EXISTS `app_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '通知标题',
    `content` TEXT DEFAULT NULL COMMENT '通知内容',
    `type` VARCHAR(64) DEFAULT NULL COMMENT '通知类型',
    `source_type` VARCHAR(64) DEFAULT NULL COMMENT '来源类型',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源 ID',
    `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_notification_user_read` (`user_id`, `read_status`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知中心表';

CREATE TABLE IF NOT EXISTS `growth_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `feedback_type` VARCHAR(64) NOT NULL COMMENT 'mentor_comment/student_feedback/method_case',
    `source` VARCHAR(128) DEFAULT NULL COMMENT '来源',
    `title` VARCHAR(180) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `source_type` VARCHAR(64) DEFAULT NULL COMMENT '来源类型',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源 ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_growth_feedback_user_type` (`user_id`, `feedback_type`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成长档案点评反馈表';

CREATE TABLE IF NOT EXISTS `training_course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '课程标题',
    `audience` VARCHAR(64) NOT NULL COMMENT 'senior/mid/novice/student',
    `category` VARCHAR(64) DEFAULT NULL COMMENT '分类',
    `summary` TEXT DEFAULT NULL COMMENT '简介',
    `cover_url` VARCHAR(512) DEFAULT NULL COMMENT '封面',
    `hours` INT DEFAULT 0 COMMENT '课时数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_course_audience` (`audience`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配套课程表';

CREATE TABLE IF NOT EXISTS `training_course_lesson` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `course_id` BIGINT NOT NULL COMMENT '课程 ID',
    `title` VARCHAR(180) NOT NULL COMMENT '课时标题',
    `content` TEXT DEFAULT NULL COMMENT '课时内容',
    `video_url` VARCHAR(512) DEFAULT NULL COMMENT '视频地址',
    `duration` INT DEFAULT 0 COMMENT '时长分钟',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`),
    KEY `idx_course_lesson` (`course_id`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配套课程课时表';

CREATE TABLE IF NOT EXISTS `training_course_enrollment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `course_id` BIGINT NOT NULL COMMENT '课程 ID',
    `progress` INT NOT NULL DEFAULT 0 COMMENT '学习进度 0-100',
    `status` VARCHAR(32) NOT NULL DEFAULT 'learning' COMMENT '学习状态',
    `rating` INT DEFAULT NULL COMMENT '评分',
    `feedback` TEXT DEFAULT NULL COMMENT '课程反馈',
    `enrolled_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_enrollment` (`user_id`, `course_id`),
    KEY `idx_course_enrollment_user` (`user_id`, `updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配套课程报名进度表';
