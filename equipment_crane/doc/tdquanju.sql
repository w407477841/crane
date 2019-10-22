/*
 Navicat Premium Data Transfer

 Source Server         : 118.31.69.25
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : 118.31.69.25:3306
 Source Schema         : demon

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 07/08/2019 15:05:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_project_announcement
-- ----------------------------
DROP TABLE IF EXISTS `t_project_announcement`;
CREATE TABLE `t_project_announcement`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告编号',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_announcement_file
-- ----------------------------
DROP TABLE IF EXISTS `t_project_announcement_file`;
CREATE TABLE `t_project_announcement_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `announcement_id` int(11) NULL DEFAULT NULL COMMENT 'announcement_id',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件编码',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `author` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档编写人',
  `path` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保存路径',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织机构',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_application_config
-- ----------------------------
DROP TABLE IF EXISTS `t_project_application_config`;
CREATE TABLE `t_project_application_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台名称',
  `topic` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'topic',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `org_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_area
-- ----------------------------
DROP TABLE IF EXISTS `t_project_area`;
CREATE TABLE `t_project_area`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NULL DEFAULT NULL,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane`;
CREATE TABLE `t_project_crane`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '工程名称',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '黑匣子编号',
  `model` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '黑匣子型号',
  `assemble_date` datetime(0) NULL DEFAULT NULL COMMENT '黑匣子安装日期',
  `gprs` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'GPRS',
  `identifier` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '产权编号（备案编号）',
  `specification` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '规格型号（塔机型号）',
  `owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '产权单位',
  `manufactor` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '制造厂家',
  `licence` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '制造许可证',
  `assemble_unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '安装单位',
  `production_date` datetime(0) NULL DEFAULT NULL COMMENT '设备出场日期',
  `test_date` datetime(0) NULL DEFAULT NULL COMMENT '检测日期',
  `crane_assemble_date` datetime(0) NULL DEFAULT NULL COMMENT '安装日期',
  `disassemble_date` datetime(0) NULL DEFAULT NULL COMMENT '预计拆除日期',
  `max_range` decimal(12, 2) NULL DEFAULT NULL COMMENT '最大幅度',
  `max_weight` decimal(12, 2) NULL DEFAULT NULL COMMENT '最大载重量',
  `standard_height` decimal(12, 2) NULL DEFAULT NULL COMMENT '标准高度(独立高度)',
  `current_height` decimal(12, 2) NULL DEFAULT NULL COMMENT '当前高度',
  `fix_moment` decimal(12, 2) NULL DEFAULT NULL COMMENT '额定力矩(最大力矩/起重量)',
  `wind_speed` decimal(12, 2) NULL DEFAULT NULL COMMENT '风速',
  `tilt_angle` decimal(12, 2) NULL DEFAULT NULL COMMENT '倾角',
  `turn` int(11) NULL DEFAULT NULL COMMENT '附着道数',
  `alarm_status` int(11) NULL DEFAULT NULL COMMENT '报警状态（正常0,1预警,2报警,3违章）',
  `is_online` int(11) NULL DEFAULT NULL COMMENT '在线状态',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `place_point` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '位置',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  `firmware_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '固件版本',
  `upgrade_time` datetime(0) NULL DEFAULT NULL COMMENT '固件升级时间',
  `crane_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '塔机编号',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名称',
  `multiple_rate` int(11) NULL DEFAULT NULL COMMENT '倍率',
  `function_config` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '功能配置',
  `recognition_config` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '识别配置',
  `device_manufactor` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `time_sum` bigint(20) NULL DEFAULT NULL COMMENT '累计工作时长',
  PRIMARY KEY (`id`, `recognition_config`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_calibration_log
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_calibration_log`;
CREATE TABLE `t_project_crane_calibration_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `command` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属命令:\r\n0x01登录,   0x02心跳,   0x03同步时间,   0x04上传监控数据,   0x05请求升级,   0x06传输升级数据,   0x07升级文件发送完毕,   0x08上传异常情况(报警),   0x09远程重启,   0x0A塔机基本信息设置上传服务器,   0x0B服务器下传塔机基本设置信息,   0x0C塔机幅度校准信息设置上传服务器,   0x0D塔机高度校准信息设置上传服务器,   0x0E塔机角度校准信息设置上传服务器,   0x0F塔机起重量校准信息设置上传服务器,   0x10单机防碰撞区域设置上传服务器,\r\n',
  `project_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目编号',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '塔吊校准设置履历' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_data_model
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_data_model`;
CREATE TABLE `t_project_crane_data_model`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `specification` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `manufactor` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '制造厂家',
  `driver` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机',
  `device_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `weight` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '重量',
  `moment` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '力矩',
  `height` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '高度',
  `range` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '幅度',
  `moment_percentage` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '力矩百分比',
  `rotary_angle` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回转角度',
  `tilt_angle` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '倾斜角度',
  `wind_speed` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风速',
  `key1` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key2` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key3` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key4` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key5` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key6` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key7` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key8` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key9` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `key10` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留的键值',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `alarm` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_file
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_file`;
CREATE TABLE `t_project_crane_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `info_id` int(11) NULL DEFAULT NULL COMMENT 'announcement_id',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件编码',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `author` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档编写人',
  `path` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保存路径',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织机构',
  `crane_id` int(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_heartbeat
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_heartbeat`;
CREATE TABLE `t_project_crane_heartbeat`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `crane_id` int(11) NULL DEFAULT NULL COMMENT '主表id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态 1 受控 0未受控',
  `reason` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `end_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_message
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_message`;
CREATE TABLE `t_project_crane_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model` int(11) NULL DEFAULT NULL COMMENT '短信模板',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信标题',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `related_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定人',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_muti_collision_avoidance_set
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_muti_collision_avoidance_set`;
CREATE TABLE `t_project_crane_muti_collision_avoidance_set`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '设备编号',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目编号',
  `sequence` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改序号',
  `crane_no` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '塔机编号',
  `arm_length` decimal(12, 2) NULL DEFAULT NULL COMMENT '塔机臂长',
  `relative_distance` decimal(12, 2) NULL DEFAULT NULL COMMENT '相对距离',
  `relative_angle` decimal(12, 2) NULL DEFAULT NULL COMMENT '相对角度',
  `relative_height` decimal(12, 2) NULL DEFAULT NULL COMMENT '相对高度',
  `current_angle` decimal(12, 2) NULL DEFAULT NULL COMMENT '当前角度',
  `current_height` decimal(12, 2) NULL DEFAULT NULL COMMENT '当前高度',
  `status` int(11) NULL DEFAULT NULL COMMENT '设置状态',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_original_data
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_original_data`;
CREATE TABLE `t_project_crane_original_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `original_data` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `device_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_single_collision_avoidance_set
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_single_collision_avoidance_set`;
CREATE TABLE `t_project_crane_single_collision_avoidance_set`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目编号',
  `start_angle` decimal(12, 1) NULL DEFAULT NULL COMMENT '起始角度',
  `end_angle` decimal(12, 1) NULL DEFAULT NULL COMMENT '终止角度',
  `start_height` decimal(12, 1) NULL DEFAULT NULL COMMENT '起始高度',
  `end_height` decimal(12, 1) NULL DEFAULT NULL COMMENT '终止高度',
  `start_range` decimal(12, 1) NULL DEFAULT NULL COMMENT '起始幅度',
  `end_range` decimal(12, 1) NULL DEFAULT NULL COMMENT '终止幅度',
  `status` int(2) NULL DEFAULT NULL COMMENT '设置状态 0 未设置，1 已设置',
  `page_range` int(2) NULL DEFAULT NULL COMMENT '区域编号',
  `is_del` int(1) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '塔吊单机防碰撞设置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_statistics_daily
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_statistics_daily`;
CREATE TABLE `t_project_crane_statistics_daily`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '工程id',
  `project_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '工程名称',
  `builder` int(11) NULL DEFAULT NULL COMMENT '施工单位',
  `owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '产权单位',
  `identifier` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备案编号',
  `crane_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '设备编号',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '黑匣子编号',
  `lift_frequency` int(11) NULL DEFAULT NULL COMMENT '吊重次数',
  `percentage0` int(11) NULL DEFAULT NULL COMMENT '40以下',
  `percentage40` int(11) NULL DEFAULT NULL COMMENT '40-60',
  `percentage60` int(11) NULL DEFAULT NULL COMMENT '60-80',
  `percentage80` int(11) NULL DEFAULT NULL COMMENT '80-90',
  `percentage90` int(11) NULL DEFAULT NULL COMMENT '90-110',
  `percentage110` int(11) NULL DEFAULT NULL COMMENT '110-120',
  `percentage120` int(11) NULL DEFAULT NULL COMMENT '120以上',
  `weight_warn` int(11) NULL DEFAULT NULL COMMENT '重量预警',
  `range_warn` int(11) NULL DEFAULT NULL COMMENT '幅度预警',
  `limit_warn` int(11) NULL DEFAULT NULL COMMENT '高度预警',
  `moment_warn` int(11) NULL DEFAULT NULL COMMENT '力矩预警',
  `collision_warn` int(11) NULL DEFAULT NULL COMMENT '碰撞预警',
  `weight_alarm` int(11) NULL DEFAULT NULL COMMENT '重量报警',
  `range_alarm` int(11) NULL DEFAULT NULL COMMENT '幅度报警',
  `limit_alarm` int(11) NULL DEFAULT NULL COMMENT '高度报警',
  `moment_alarm` int(11) NULL DEFAULT NULL COMMENT '力矩报警',
  `collision_alarm` int(11) NULL DEFAULT NULL COMMENT '碰撞报警',
  `wind_speed_alarm` int(11) NULL DEFAULT NULL COMMENT '风速报警',
  `tilt_alarm` int(11) NULL DEFAULT NULL COMMENT '倾斜报警',
  `work_date` datetime(0) NULL DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_upgrade_record
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_upgrade_record`;
CREATE TABLE `t_project_crane_upgrade_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '工程名',
  `version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `upgrade_time` datetime(0) NULL DEFAULT NULL COMMENT '升级时间',
  `flag` int(11) NULL DEFAULT NULL COMMENT '是否升级成功',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_video
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_video`;
CREATE TABLE `t_project_crane_video`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `crane_id` int(11) NULL DEFAULT NULL COMMENT '塔吊id',
  `login_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `ip_address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `port` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '端口号',
  `platform_type` int(11) NULL DEFAULT NULL COMMENT '平台区分',
  `tunnel` int(11) NULL DEFAULT NULL COMMENT '通道',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  `type` int(11) NULL DEFAULT NULL,
  `appkey` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'appkey,萤石云视频专用',
  `secret` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥,萤石云视频专用',
  `area` int(11) NULL DEFAULT NULL COMMENT '区域',
  `http_address` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '乐橙视屏专用存储地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_dev_error
-- ----------------------------
DROP TABLE IF EXISTS `t_project_dev_error`;
CREATE TABLE `t_project_dev_error`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NULL DEFAULT NULL COMMENT '1：起重机 2：考勤机 3：绿色施工监控 4：升降电梯 5：摄像头',
  `no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pid` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device`;
CREATE TABLE `t_project_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '工程id',
  `current_flag` int(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '是否是最新绑定数据(0:当前绑定,1:代表是绑定履历数据)',
  `status` int(11) NULL DEFAULT NULL COMMENT '启用状态:1启用,0未启用',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `type` int(1) NULL DEFAULT NULL COMMENT '设备类型 1安全帽 2基站 3标签 4扬尘 5塔吊 6升降机 7电表 8水表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '安全帽' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device_error_log
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device_error_log`;
CREATE TABLE `t_project_device_error_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `content` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常信息',
  `type` int(11) NULL DEFAULT NULL COMMENT '所属模块(1扬尘、2塔吊、3升降机)',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '所属项目id',
  `error_time` datetime(0) NULL DEFAULT NULL COMMENT '异常时间',
  `is_del` int(11) NULL DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '硬件功能错误日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device_restart_record
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device_restart_record`;
CREATE TABLE `t_project_device_restart_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(2) NULL DEFAULT NULL,
  `project_id` int(11) NULL DEFAULT NULL,
  `restart_time` datetime(0) NULL DEFAULT NULL,
  `is_del` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_user` int(1) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备重启履历' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device_stock`;
CREATE TABLE `t_project_device_stock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_no` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `type` int(11) NULL DEFAULT NULL COMMENT '设备类型 1安全帽 2基站 3标签 4扬尘 5塔吊 6升降机 7电表 8水表',
  `status` int(11) NULL DEFAULT NULL COMMENT '库存状态: 0 在库 1:已出库',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device_upgrade_package
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device_upgrade_package`;
CREATE TABLE `t_project_device_upgrade_package`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '升级包名',
  `type` int(255) NOT NULL COMMENT '设备类型:数据字典',
  `version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '版本号',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '路径',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NOT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_user` int(1) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_device_worker_record
-- ----------------------------
DROP TABLE IF EXISTS `t_project_device_worker_record`;
CREATE TABLE `t_project_device_worker_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `id_card_type` int(11) NULL DEFAULT NULL COMMENT '证件类型',
  `id_card_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件编号',
  `name` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_error_data
-- ----------------------------
DROP TABLE IF EXISTS `t_project_error_data`;
CREATE TABLE `t_project_error_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数据',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '错误数据\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_info
-- ----------------------------
DROP TABLE IF EXISTS `t_project_info`;
CREATE TABLE `t_project_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开工日期',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束日期',
  `position` int(11) NULL DEFAULT NULL COMMENT '项目区域',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目地址',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型（1、房建 2、市政）',
  `introduction` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目简介',
  `builder` int(11) NULL DEFAULT NULL COMMENT '施工单位',
  `surveyor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '勘察单位',
  `building_size` decimal(12, 2) NULL DEFAULT NULL COMMENT '建筑面积',
  `supervisor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监理单位',
  `construction_unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建设单位',
  `manager` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目经理',
  `manager_phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `fix_days` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定额工期',
  `place_point` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目经纬度',
  `project_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目范围',
  `ichnography` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '项目平面图',
  `uuid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'uuid',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织机构',
  `topic` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅主题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_master_cetificate_type
-- ----------------------------
DROP TABLE IF EXISTS `t_project_master_cetificate_type`;
CREATE TABLE `t_project_master_cetificate_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `call_times` int(11) NULL DEFAULT NULL COMMENT '占用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '证书类别' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_master_device_type
-- ----------------------------
DROP TABLE IF EXISTS `t_project_master_device_type`;
CREATE TABLE `t_project_master_device_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `call_times` int(11) NULL DEFAULT NULL COMMENT '占用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '1塔吊、2升降机,3扬尘、' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_master_protocol_config
-- ----------------------------
DROP TABLE IF EXISTS `t_project_master_protocol_config`;
CREATE TABLE `t_project_master_protocol_config`  (
  `id` int(11) NOT NULL,
  `type` int(11) NULL DEFAULT NULL COMMENT '1:塔吊 2:升降机 3:扬尘',
  `head` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '协议头',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user` int(11) NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `modify_user` int(11) NULL DEFAULT NULL,
  `is_del` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_message
-- ----------------------------
DROP TABLE IF EXISTS `t_project_message`;
CREATE TABLE `t_project_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model` int(11) NULL DEFAULT NULL COMMENT '短信模板编号',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信标题',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `related_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定人',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_message_device_error
-- ----------------------------
DROP TABLE IF EXISTS `t_project_message_device_error`;
CREATE TABLE `t_project_message_device_error`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_ids` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信内容',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '所属项目id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '硬件错误信息推送' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_message_model
-- ----------------------------
DROP TABLE IF EXISTS `t_project_message_model`;
CREATE TABLE `t_project_message_model`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信编号',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '短信标题',
  `device_type` int(11) NULL DEFAULT NULL COMMENT '设备类型',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '预计开始发送时间',
  `type` int(11) NULL DEFAULT NULL COMMENT '短信类型',
  `status` int(11) NULL DEFAULT NULL COMMENT '启用',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `related_user` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定人',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '短信模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_message_user_device_error_log
-- ----------------------------
DROP TABLE IF EXISTS `t_project_message_user_device_error_log`;
CREATE TABLE `t_project_message_user_device_error_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `device_type` int(11) NULL DEFAULT NULL COMMENT '设备类型',
  `user_ids` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日志发送用户一览' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_target_set_crane
-- ----------------------------
DROP TABLE IF EXISTS `t_project_target_set_crane`;
CREATE TABLE `t_project_target_set_crane`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `specification` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `manufactor` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '制造厂家',
  `max_range` decimal(12, 2) NULL DEFAULT NULL COMMENT '最大幅度',
  `max_weight` decimal(12, 2) NULL DEFAULT NULL COMMENT '最大载重量',
  `standard_height` decimal(12, 2) NULL DEFAULT NULL COMMENT '标准高度',
  `fix_moment` decimal(12, 2) NULL DEFAULT NULL COMMENT '额定力矩',
  `wind_speed` decimal(12, 2) NULL DEFAULT NULL COMMENT '风速',
  `tilt_angle` decimal(12, 2) NULL DEFAULT NULL COMMENT '倾角',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织结构',
  `call_times` int(11) NULL DEFAULT 0 COMMENT '占用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_top_up
-- ----------------------------
DROP TABLE IF EXISTS `t_project_top_up`;
CREATE TABLE `t_project_top_up`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `project_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '项目名称',
  `crane_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '塔吊id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '设备编号',
  `gprs` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'SIM卡号',
  `charge_time` int(11) NULL DEFAULT NULL COMMENT '充值时间',
  `charge_money` decimal(12, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `status` int(11) NULL DEFAULT NULL COMMENT '处理状态0：未处理，1：已处理',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `out_trade_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_user
-- ----------------------------
DROP TABLE IF EXISTS `t_project_user`;
CREATE TABLE `t_project_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目名称',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `identity_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `certificate_type` int(11) NULL DEFAULT NULL COMMENT '证书类型',
  `certificate_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证书名称',
  `certificate_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证书编号',
  `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束日期',
  `status` int(11) NULL DEFAULT NULL,
  `comments` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '组织机构',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_operation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_operation`;
CREATE TABLE `t_sys_operation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '等级',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `permission` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限字符',
  `type` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 301 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_operation
-- ----------------------------
INSERT INTO `t_sys_operation` VALUES (1, 0, 'PC端', 1, '/', '/', '0', NULL, '111', 0, NULL, NULL, '2019-06-27 16:01:52', 1);
INSERT INTO `t_sys_operation` VALUES (2, 0, '手机端', 1, '/', '/', '1', NULL, '222333', 0, NULL, NULL, '2018-09-29 09:23:52', 1);
INSERT INTO `t_sys_operation` VALUES (3, 0, 'WEB端', 1, '/', '/', '2', NULL, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (5, 1, '工程管理', 2, NULL, 'project:menu', '0', NULL, 'eeeeeeeeeeeeeeee2222222222222222222222222222222222222222', 0, '2018-09-06 17:20:29', 1, '2018-09-18 18:09:06', 1);
INSERT INTO `t_sys_operation` VALUES (6, 1, '信息管理', 2, NULL, 'information:menu', '0', NULL, NULL, 0, '2018-09-06 17:21:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (7, 1, '系统管理', 2, NULL, 'system:menu', '0', NULL, NULL, 0, '2018-09-06 17:23:24', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (10, 5, '工程信息', 3, NULL, 'project:info:menu', '0', NULL, NULL, 0, '2018-09-06 17:27:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (11, 6, '实时监控(塔吊)', 3, NULL, 'information:realTower:menu', '0', NULL, NULL, 1, '2018-09-06 17:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (12, 7, '用户管理', 3, NULL, 'system:user:menu', '0', NULL, NULL, 0, '2018-09-06 17:29:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (13, 7, '角色管理', 3, NULL, 'system:role:menu', '0', NULL, NULL, 0, '2018-09-06 17:30:14', 1, '2018-09-06 17:56:01', 1);
INSERT INTO `t_sys_operation` VALUES (14, 7, '资源管理', 3, NULL, 'system:operation:menu', '0', NULL, NULL, 0, '2018-09-06 17:30:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (23, 10, '新增', 4, NULL, 'project:info:insert', '0', NULL, NULL, 0, '2018-09-06 18:09:03', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (24, 10, '编辑', 4, NULL, 'project:info:update', '0', NULL, NULL, 0, '2018-09-06 18:09:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (25, 10, '删除', 4, NULL, 'project:info:delete', '0', NULL, NULL, 0, '2018-09-06 18:09:43', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (26, 10, '查看', 4, NULL, 'project:info:view', '0', NULL, NULL, 0, '2018-09-06 18:10:03', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (27, 11, '查看', 4, NULL, 'information:realTower:view', '0', NULL, NULL, 1, '2018-09-06 18:10:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (28, 12, '新增', 4, NULL, 'system:user:insert', '0', NULL, NULL, 0, '2018-09-06 18:11:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (29, 12, '编辑', 4, NULL, 'system:user:update', '0', NULL, NULL, 0, '2018-09-06 18:11:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (30, 12, '删除', 4, NULL, 'system:user:delete', '0', NULL, NULL, 0, '2018-09-06 18:11:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (31, 12, '查看', 4, NULL, 'system:user:view', '0', NULL, NULL, 0, '2018-09-06 18:11:51', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (32, 13, '新增', 4, NULL, 'system:role:insert', '0', NULL, NULL, 0, '2018-09-06 18:12:10', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (33, 13, '编辑', 4, NULL, 'system:role:update', '0', NULL, NULL, 0, '2018-09-06 18:12:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (34, 13, '删除', 4, NULL, 'system:role:delete', '0', NULL, NULL, 0, '2018-09-06 18:12:46', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (35, 13, '查看', 4, NULL, 'system:role:view', '0', NULL, NULL, 0, '2018-09-06 18:12:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (36, 14, '新增', 4, NULL, 'system:operation:insert', '0', NULL, NULL, 0, '2018-09-06 18:13:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (37, 14, '编辑', 4, NULL, 'system:operation:update', '0', NULL, NULL, 0, '2018-09-06 18:13:43', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (38, 14, '删除', 4, NULL, 'system:operation:delete', '0', NULL, NULL, 0, '2018-09-06 18:14:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (39, 14, '查看', 4, NULL, 'system:operation:view', '0', NULL, NULL, 0, '2018-09-06 18:14:28', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (40, 7, '部门管理', 3, NULL, 'system:org:menu', '0', NULL, NULL, 0, '2018-09-12 15:57:46', 1, '2018-09-12 15:58:01', 1);
INSERT INTO `t_sys_operation` VALUES (41, 40, '新增', 4, NULL, 'system:org:insert', '0', NULL, NULL, 0, '2018-09-12 15:58:42', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (42, 40, '编辑', 4, NULL, 'system:org:update', '0', NULL, NULL, 0, '2018-09-12 15:59:08', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (43, 40, '删除', 4, NULL, 'system:org:delete', '0', NULL, NULL, 0, '2018-09-12 15:59:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (44, 40, '查看', 4, NULL, 'system:org:view', '0', NULL, NULL, 0, '2018-09-12 16:00:11', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (46, 5, '人员信息', 3, NULL, 'project:user:menu', '0', NULL, NULL, 0, '2018-09-14 17:30:49', 1, '2018-09-14 18:04:51', 1);
INSERT INTO `t_sys_operation` VALUES (47, 5, '设备类型', 3, NULL, 'project:deviceType:menu', '0', NULL, NULL, 0, '2018-09-14 17:31:25', 1, '2018-09-14 18:05:25', 1);
INSERT INTO `t_sys_operation` VALUES (48, 46, '新增', 4, NULL, 'project:user:insert', '0', NULL, NULL, 0, '2018-09-14 17:33:37', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (49, 46, '编辑', 4, NULL, 'project:user:update', '0', NULL, NULL, 0, '2018-09-14 17:33:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (50, 46, '删除', 4, NULL, 'project:user:delete', '0', NULL, NULL, 0, '2018-09-14 17:34:49', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (51, 46, '查看', 4, NULL, 'project:user:view', '0', NULL, NULL, 0, '2018-09-14 17:35:14', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (52, 47, '新增', 4, NULL, 'project:device:insert', '0', NULL, NULL, 0, '2018-09-14 17:36:45', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (53, 47, '编辑', 4, NULL, 'project:device:update', '0', NULL, NULL, 0, '2018-09-14 17:36:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (54, 47, '删除', 4, NULL, 'project:device:delete', '0', NULL, NULL, 0, '2018-09-14 17:37:12', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (55, 47, '查看', 4, NULL, 'project:device:view', '0', NULL, NULL, 0, '2018-09-14 17:37:26', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (56, 1, '升降机管理', 2, NULL, 'lift:menu', '0', NULL, NULL, 0, '2018-09-17 11:12:05', 1, '2018-09-17 11:12:29', 1);
INSERT INTO `t_sys_operation` VALUES (57, 56, '实时监控', 3, NULL, 'lift:monitorMain:menu', '0', NULL, NULL, 1, '2018-09-17 11:13:47', 1, '2018-09-20 14:21:33', 1);
INSERT INTO `t_sys_operation` VALUES (58, 1, '扬尘管理', 2, NULL, 'dust:menu', '0', NULL, NULL, 0, '2018-09-17 11:16:18', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (59, 58, '信息管理', 3, NULL, 'dust:infoMain:menu', '0', NULL, NULL, 1, '2018-09-17 11:17:45', 1, '2018-09-17 13:37:32', 1);
INSERT INTO `t_sys_operation` VALUES (60, 59, '通知公告', 4, NULL, 'dust:infoMain:notice:menu', '0', NULL, NULL, 1, '2018-09-17 13:38:20', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (64, 6, '通知公告', 3, NULL, 'information:notice:menu', '0', NULL, NULL, 0, '2018-09-18 09:26:04', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (65, 6, '短信推送', 3, NULL, 'information:message:menu', '0', NULL, NULL, 0, '2018-09-18 09:26:34', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (66, 64, '新增', 4, NULL, 'information:notice:insert', '0', NULL, NULL, 0, '2018-09-18 09:27:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (67, 64, '编辑', 4, NULL, 'information:notice:update', '0', NULL, NULL, 0, '2018-09-18 09:28:04', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (68, 64, '删除', 4, NULL, 'information:notice:delete', '0', NULL, NULL, 0, '2018-09-18 09:28:48', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (69, 64, '查看', 4, NULL, 'information:notice:view', '0', NULL, NULL, 0, '2018-09-18 09:29:27', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (70, 65, '新增', 4, NULL, 'information:message:insert', '0', NULL, NULL, 0, '2018-09-18 09:34:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (71, 65, '删除', 4, NULL, 'information:message:delete', '0', NULL, NULL, 0, '2018-09-18 09:37:45', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (72, 65, '查看', 4, NULL, 'information:message:view', '0', NULL, NULL, 0, '2018-09-18 09:38:12', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (73, 65, '编辑', 4, NULL, 'information:message:update', '0', NULL, NULL, 0, '2018-09-18 09:40:53', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (74, 65, '启用', 4, NULL, 'information:message:use', '0', NULL, NULL, 0, '2018-09-18 09:43:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (75, 58, '地图管理', 2, NULL, 'dust:map:menu', '0', NULL, NULL, 0, '2018-09-18 16:18:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (76, 75, '扬尘电子地图', 3, NULL, 'dust:map:monitor:menu', '0', NULL, NULL, 0, '2018-09-18 16:19:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (77, 58, '设备管理', 2, NULL, 'dust:monitor:menu', '0', NULL, NULL, 0, '2018-09-18 16:22:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (78, 77, '绿色施工监控', 3, NULL, 'dust:monitor:device:menu', '0', NULL, NULL, 0, '2018-09-18 16:29:53', 1, '2018-09-18 17:08:59', 1);
INSERT INTO `t_sys_operation` VALUES (79, 78, '新增', 4, NULL, 'dust:monitor:device:insert', '0', NULL, NULL, 0, '2018-09-18 16:34:16', 1, '2018-09-18 17:09:26', 1);
INSERT INTO `t_sys_operation` VALUES (80, 78, '编辑', 4, NULL, 'dust:monitor:device:update', '0', NULL, NULL, 0, '2018-09-18 16:34:42', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (81, 1, '塔基管理', 2, NULL, 'crane:menu', '0', NULL, NULL, 0, '2018-09-18 16:34:49', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (82, 78, '查看', 4, NULL, 'dust:monitor:device:view', '0', NULL, NULL, 0, '2018-09-18 16:35:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (83, 78, '删除', 4, NULL, 'dust:monitor:device:delete', '0', NULL, NULL, 0, '2018-09-18 16:35:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (84, 78, '启用', 4, NULL, 'dust:monitor:device:open', '0', NULL, NULL, 0, '2018-09-18 16:36:04', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (85, 81, '地图管理', 3, NULL, 'crane:map:menu', '0', NULL, NULL, 0, '2018-09-18 16:47:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (87, 81, '设备管理', 3, NULL, 'crane:device:menu', '0', NULL, NULL, 0, '2018-09-19 08:55:05', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (88, 85, '塔吊电子地图', 3, NULL, 'crane:map:crane:menu', '0', NULL, NULL, 0, '2018-09-19 08:58:36', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (89, 87, '塔吊', 3, NULL, 'crane:device:crane:menu', '0', NULL, NULL, 0, '2018-09-19 08:59:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (90, 89, '新增', 4, NULL, 'crane:device:crane:insert', '0', NULL, NULL, 0, '2018-09-19 08:59:46', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (91, 89, '编辑', 4, NULL, 'crane:device:crane:update', '0', NULL, NULL, 0, '2018-09-19 09:00:48', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (92, 89, '查看', 4, NULL, 'crane:device:crane:view', '0', NULL, NULL, 0, '2018-09-19 09:01:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (93, 89, '删除', 4, NULL, 'crane:device:crane:delete', '0', NULL, NULL, 0, '2018-09-19 09:01:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (95, 58, '实时监控', 2, NULL, 'dust:oftenMonitor:menu', '0', NULL, NULL, 0, '2018-09-20 08:56:06', 1, '2018-09-20 15:29:57', 1);
INSERT INTO `t_sys_operation` VALUES (96, 3, '5555', 2, '5', '555555', '0', NULL, '55', 1, '2018-09-20 13:55:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (97, 57, '实时监控(升降机)', 4, NULL, 'lift:monitorMain:realTimeLift:menu', '0', NULL, NULL, 1, '2018-09-20 14:22:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (98, 56, '实时监控', 2, NULL, 'lift:monitorMain:menu', '0', NULL, NULL, 0, '2018-09-20 14:24:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (99, 98, '实时监控(升降机)', 3, NULL, 'lift:monitorMain:realTimeLift:menu', '0', NULL, NULL, 0, '2018-09-20 14:25:20', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (100, 56, '地图管理', 2, NULL, 'lift:mapMain:menu', '0', NULL, NULL, 0, '2018-09-20 14:32:56', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (101, 100, '升降机电子地图', 3, NULL, 'lift:mapMain:projectLiftMap:menu', '0', NULL, NULL, 0, '2018-09-20 14:33:56', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (102, 56, '设备管理', 2, NULL, 'lift:deviceMain:menu', '0', NULL, NULL, 0, '2018-09-20 14:34:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (103, 102, '升降机', 3, NULL, 'lift:deviceMain:projectLift:menu', '0', NULL, NULL, 0, '2018-09-20 14:35:22', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (104, 56, '信息管理', 2, NULL, 'lift:infoMain:menu', '0', NULL, NULL, 0, '2018-09-20 14:36:29', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (105, 104, '升降机数据模板', 3, NULL, 'lift:infoMain:liftDataModel:menu', '0', NULL, NULL, 0, '2018-09-20 14:37:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (106, 104, '升降机指标设置', 3, NULL, 'lift:infoMain:targetSetLift:menu', '0', NULL, NULL, 0, '2018-09-20 14:38:24', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (107, 59, '扬尘数据模板', 4, NULL, 'dust:infoMain:data', '0', NULL, NULL, 1, '2018-09-25 11:04:24', 1, '2018-09-25 11:04:44', 1);
INSERT INTO `t_sys_operation` VALUES (108, 58, '信息管理', 2, NULL, 'dust:infoMain:menu', '0', NULL, NULL, 0, '2018-09-25 11:06:24', 1, '2018-09-25 11:06:45', 1);
INSERT INTO `t_sys_operation` VALUES (109, 108, '通知公告', 3, NULL, 'dust:infoMain:notice:menu', '0', NULL, NULL, 1, '2018-09-25 11:07:22', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (110, 108, '扬尘数据模板', 3, NULL, 'dust:infoMain:data:menu', '0', NULL, NULL, 0, '2018-09-25 11:08:00', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (111, 110, '新增', 4, NULL, 'dust:infoMain:data:insert', '0', NULL, NULL, 0, '2018-09-25 11:09:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (112, 110, '编辑', 4, NULL, 'dust:infoMain:data:update', '0', NULL, NULL, 0, '2018-09-25 11:09:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (113, 110, '删除', 4, NULL, 'dust:infoMain:data:delete', '0', NULL, NULL, 0, '2018-09-25 11:10:09', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (114, 110, '查看', 4, NULL, 'dust:infoMain:data:view', '0', NULL, NULL, 0, '2018-09-25 11:10:29', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (115, 81, '实时监控', 3, NULL, 'crane:real:menu', '0', NULL, NULL, 1, '2018-09-26 09:25:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (116, 115, '实时监控(塔吊)', 4, NULL, 'crane:real:tower:menu', '0', NULL, NULL, 1, '2018-09-26 09:28:12', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (117, 81, '实时监控', 2, NULL, 'crane:real:menu', '0', NULL, NULL, 0, '2018-09-26 09:29:04', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (118, 117, '实时监控(塔吊)', 3, NULL, 'crane:real:tower:menu', '0', NULL, NULL, 0, '2018-09-26 09:29:43', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (119, 118, '查看', 4, NULL, 'crane:real:tower:view', '0', NULL, NULL, 0, '2018-09-26 09:30:16', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (120, 3, 'test', 2, '444', '4444', '2', NULL, '5555555555', 0, '2018-09-26 11:20:09', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (121, 120, 'shuju', 4, '234', '4445t55', '2', NULL, '4433', 1, '2018-09-26 11:20:28', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (122, 95, '实时监控(扬尘)', 3, NULL, 'dust:dustMonitor:menu', '0', NULL, NULL, 0, '2018-09-26 14:53:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (123, 122, '监控状态', 4, NULL, 'dust:monitorStatus:menu', '0', NULL, NULL, 0, '2018-09-26 14:54:26', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (124, 122, '运行数据', 4, NULL, 'dust:operationData:menu', '0', NULL, NULL, 0, '2018-09-26 14:55:46', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (125, 122, '报警信息', 4, NULL, 'dust:warningMessage:menu', '0', NULL, NULL, 0, '2018-09-26 14:56:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (126, 58, '扬尘首页', 3, NULL, 'dust:home:menu', '0', NULL, NULL, 1, '2018-09-26 15:08:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (127, 58, '扬尘首页', 2, NULL, 'dust:home:menu', '0', NULL, NULL, 0, '2018-09-26 15:11:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (128, 10, '绑定', 4, NULL, 'project:info:binding', '0', NULL, NULL, 0, '2018-09-26 17:00:27', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (129, 46, '加入', 4, NULL, 'project:user:putIn', '0', NULL, NULL, 0, '2018-09-26 17:01:05', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (130, 106, '新增', 4, NULL, 'lift:infoMain:targetSetLift:insert', '0', NULL, NULL, 0, '2018-09-26 17:09:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (131, 106, '编辑', 4, NULL, 'lift:infoMain:targetSetLift:update', '0', NULL, NULL, 0, '2018-09-26 17:09:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (132, 106, '删除', 4, NULL, 'lift:infoMain:targetSetLift:delete', '0', NULL, NULL, 0, '2018-09-26 17:10:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (133, 106, '查看', 4, NULL, 'lift:infoMain:targetSetLift:view', '0', NULL, NULL, 0, '2018-09-26 17:12:44', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (134, 103, '新增', 4, NULL, 'lift:deviceMain:projectLift:insert', '0', NULL, NULL, 0, '2018-09-26 17:23:20', 1, '2018-09-26 17:24:08', 1);
INSERT INTO `t_sys_operation` VALUES (135, 103, '编辑', 4, NULL, 'lift:deviceMain:projectLift:update', '0', NULL, NULL, 0, '2018-09-26 17:24:33', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (136, 103, '删除', 4, NULL, 'lift:deviceMain:projectLift:delete', '0', NULL, NULL, 0, '2018-09-26 17:24:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (137, 103, '查看', 4, NULL, 'lift:deviceMain:projectLift:view', '0', NULL, NULL, 0, '2018-09-26 17:25:12', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (138, 81, '信息管理', 2, NULL, 'crane:infoMain:menu', '0', NULL, NULL, 0, '2018-09-26 17:32:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (139, 108, '扬尘指标设置', 3, NULL, 'dust:infoMain:targetSet:menu', '0', NULL, NULL, 0, '2018-09-26 17:36:33', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (140, 138, '塔吊数据模板', 3, NULL, 'crane:infoMain:craneDataModel:menu', '0', NULL, NULL, 0, '2018-09-26 17:36:44', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (141, 139, '新增', 4, NULL, 'dust:infoMain:targetSet:insert', '0', NULL, NULL, 0, '2018-09-26 17:36:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (142, 139, '编辑', 4, NULL, 'dust:infoMain:targetSet:update', '0', NULL, NULL, 0, '2018-09-26 17:37:07', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (143, 138, '塔吊指标设置', 3, NULL, 'crane:infoMain:targetSetCrane:menu', '0', NULL, NULL, 0, '2018-09-26 17:37:28', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (144, 139, '删除', 4, NULL, 'dust:infoMain:targetSet:delete', '0', NULL, NULL, 0, '2018-09-26 17:37:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (145, 139, '查看', 4, NULL, 'dust:infoMain:targetSet:view', '0', NULL, NULL, 0, '2018-09-26 17:37:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (146, 105, '新增', 4, NULL, 'lift:infoMain:liftDataModel:insert', '0', NULL, NULL, 0, '2018-09-26 17:38:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (147, 105, '编辑', 4, NULL, 'lift:infoMain:liftDataModel:update', '0', NULL, NULL, 0, '2018-09-26 17:39:18', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (148, 105, '删除', 4, NULL, 'lift:infoMain:liftDataModel:delete', '0', NULL, NULL, 0, '2018-09-26 17:39:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (149, 105, '查看', 4, NULL, 'lift:infoMain:liftDataModel:view', '0', NULL, NULL, 0, '2018-09-26 17:39:58', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (150, 140, '新增', 4, NULL, 'crane:infoMain:craneDataModel:insert', '0', NULL, NULL, 0, '2018-09-26 17:44:45', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (151, 140, '编辑', 4, NULL, 'crane:infoMain:craneDataModel:update', '0', NULL, NULL, 0, '2018-09-26 17:45:06', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (152, 140, '查看', 4, NULL, 'crane:infoMain:craneDataModel:view', '0', NULL, NULL, 0, '2018-09-26 17:45:40', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (153, 140, '删除', 4, NULL, 'crane:infoMain:craneDataModel:delete', '0', NULL, NULL, 0, '2018-09-26 17:46:05', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (154, 143, '新增', 4, NULL, 'crane:infoMain:targetSetCrane:insert', '0', NULL, NULL, 0, '2018-09-26 17:46:40', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (155, 143, '编辑', 4, NULL, 'crane:infoMain:targetSetCrane:update', '0', NULL, NULL, 0, '2018-09-26 17:47:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (156, 143, '查看', 4, NULL, 'crane:infoMain:targetSetCrane:view', '0', NULL, NULL, 0, '2018-09-26 17:47:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (157, 143, '删除', 4, NULL, 'crane:infoMain:targetSetCrane:delete', '0', NULL, NULL, 0, '2018-09-26 17:47:45', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (158, 1, '水电管理', 2, NULL, 'electric:menu', '0', NULL, NULL, 0, '2018-09-27 19:31:11', 1, '2018-10-12 09:52:48', 1);
INSERT INTO `t_sys_operation` VALUES (159, 158, '地图管理', 2, NULL, 'electric:map:menu', '0', NULL, NULL, 0, '2018-09-27 19:32:00', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (160, 158, '设备管理', 2, NULL, 'electric:device:menu', '0', NULL, NULL, 0, '2018-09-27 19:32:53', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (161, 158, '信息管理', 2, NULL, 'electric:infoMain:menu', '0', NULL, NULL, 0, '2018-09-27 19:33:32', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (162, 159, '电表电子地图', 4, NULL, 'electric:map:device:menu', '0', NULL, NULL, 0, '2018-09-27 19:34:33', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (163, 160, '电表设备监控', 3, NULL, 'electric:device:device:menu', '0', NULL, NULL, 0, '2018-09-27 19:35:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (164, 161, '电表指标设置', 4, NULL, 'electric:infoMain:targetSet:menu', '0', NULL, NULL, 1, '2018-09-27 19:36:20', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (165, 161, '电表指标设置', 4, NULL, 'electric:infoMain:targetSet:menu', '0', NULL, NULL, 1, '2018-09-27 19:37:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (166, 161, '电表指标设置', 3, NULL, 'electric:infoMain:targetSet:menu', '0', NULL, NULL, 0, '2018-09-27 19:41:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (167, 166, '新增', 4, NULL, 'electric:infoMain:targetSet:insert', '0', NULL, NULL, 0, '2018-09-27 19:43:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (168, 166, '编辑', 4, NULL, 'electric:infoMain:targetSet:update', '0', NULL, NULL, 0, '2018-09-27 19:43:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (169, 166, '删除', 4, NULL, 'electric:infoMain:targetSet:delete', '0', NULL, NULL, 0, '2018-09-27 19:44:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (170, 166, '查看', 4, NULL, 'electric:infoMain:targetSet:view', '0', NULL, NULL, 0, '2018-09-27 19:44:32', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (171, 163, '新增', 4, NULL, 'electric:device:device:insert', '0', NULL, NULL, 0, '2018-09-27 19:46:09', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (172, 163, '编辑', 4, NULL, 'electric:device:device:update', '0', NULL, NULL, 0, '2018-09-27 19:46:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (173, 163, '删除', 4, NULL, 'electric:device:device:delete', '0', NULL, NULL, 0, '2018-09-27 19:46:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (174, 163, '查看', 4, NULL, 'electric:device:device:view', '0', NULL, NULL, 0, '2018-09-27 19:47:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (175, 163, '启用', 4, NULL, 'electric:device:device:open', '0', NULL, NULL, 0, '2018-09-27 19:47:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (176, 1, '水电管理（废弃）', 2, NULL, 'water:menu', '0', NULL, NULL, 1, '2018-09-29 14:39:18', 1, '2018-10-12 09:53:03', 1);
INSERT INTO `t_sys_operation` VALUES (177, 176, '实时监控', 2, NULL, 'water:monitor:menu', '0', NULL, NULL, 1, '2018-09-29 14:42:03', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (178, 177, '实时监控(水表)', 3, NULL, 'water:monitor:water:menu', '0', NULL, NULL, 1, '2018-09-29 14:44:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (179, 178, '查看', 4, NULL, 'water:monitor:water:view', '0', NULL, NULL, 1, '2018-09-29 14:45:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (180, 158, '实时监控', 2, NULL, 'electric:oftenDevice:menu', '0', NULL, NULL, 0, '2018-09-29 15:26:29', 1, '2018-09-29 15:27:00', 1);
INSERT INTO `t_sys_operation` VALUES (181, 180, '实时监控(电表)', 3, NULL, 'electric:electricDevice:menu', '0', NULL, NULL, 0, '2018-09-29 15:27:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (182, 181, '监控状态', 4, NULL, 'electric:deviceStatus:menu', '0', NULL, NULL, 0, '2018-09-29 15:28:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (183, 181, '运行数据', 4, NULL, 'electric:operationData:menu', '0', NULL, NULL, 0, '2018-09-29 15:28:42', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (184, 181, '报警信息', 4, NULL, 'electric:warningMessage:menu', '0', NULL, NULL, 0, '2018-09-29 15:29:06', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (185, 176, '水电首页', 2, NULL, 'hydropower:home:menu', '0', NULL, NULL, 1, '2018-10-08 09:47:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (186, 56, '升降机首页', 3, NULL, 'lift:home:menu', '0', NULL, NULL, 0, '2018-10-09 16:22:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (187, 81, '塔吊首页', 3, NULL, 'crane:home:menu', '0', NULL, NULL, 0, '2018-10-09 17:00:22', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (188, 176, '设备管理', 3, NULL, 'water:equipment:menu', '0', NULL, NULL, 1, '2018-10-10 11:19:41', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (189, 176, '设备管理', 2, NULL, 'water:equipment:menu', '0', NULL, NULL, 1, '2018-10-10 11:20:48', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (190, 189, '水表', 3, NULL, 'water:equipment:water:menu', '0', NULL, NULL, 1, '2018-10-10 11:22:16', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (191, 190, '新增', 4, NULL, 'water:equipment:water:insert', '0', NULL, NULL, 1, '2018-10-10 11:22:40', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (192, 190, '编辑', 4, NULL, 'water:equipment:water:update', '0', NULL, NULL, 1, '2018-10-10 11:23:01', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (193, 190, '删除', 4, NULL, 'water:equipment:water:delete', '0', NULL, NULL, 1, '2018-10-10 11:23:48', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (194, 190, '查看', 4, NULL, 'water:equipment:water:view', '0', NULL, NULL, 1, '2018-10-10 11:25:58', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (195, 190, '启用', 4, NULL, 'water:equipment:water:inUse', '0', NULL, NULL, 1, '2018-10-10 11:26:33', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (196, 10, '审批', 4, NULL, 'project:info:approve', '0', NULL, NULL, 0, '2018-10-11 14:21:28', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (197, 158, '水电首页', 3, NULL, 'hydropower:home:menu', '0', NULL, NULL, 0, '2018-10-12 09:53:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (198, 180, '实时监控(水表)', 3, NULL, 'electric:oftenDevice:water:menu', '0', NULL, NULL, 0, '2018-10-12 10:26:27', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (199, 198, '查看', 4, NULL, 'electric:oftenDevice:water:view', '0', NULL, NULL, 0, '2018-10-12 10:27:37', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (200, 160, '水表', 4, NULL, 'electric:device:water:menu', '0', NULL, NULL, 1, '2018-10-12 10:28:41', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (201, 160, '水表', 3, NULL, 'electric:device:water:menu', '0', NULL, NULL, 0, '2018-10-12 10:32:18', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (202, 201, '新增', 4, NULL, 'electric:device:water:insert', '0', NULL, NULL, 0, '2018-10-12 10:32:42', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (203, 201, '编辑', 4, NULL, 'electric:device:water:update', '0', NULL, NULL, 0, '2018-10-12 10:33:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (204, 201, '删除', 4, NULL, 'electric:device:water:delete', '0', NULL, NULL, 0, '2018-10-12 10:33:30', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (205, 201, '查看', 4, NULL, 'electric:device:water:view', '0', NULL, NULL, 0, '2018-10-12 10:33:58', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (206, 201, '启用', 4, NULL, 'electric:device:water:setUse', '0', NULL, NULL, 0, '2018-10-12 10:35:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (207, 159, '水表电子地图', 3, NULL, 'electric:map:water:menu', '0', NULL, NULL, 0, '2018-10-15 09:23:22', 1, '2018-10-15 09:25:05', 1);
INSERT INTO `t_sys_operation` VALUES (208, 161, '水表指标设置', 3, NULL, 'electric:infoMain:targetSetWater:menu', '0', NULL, NULL, 0, '2018-10-15 14:23:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (209, 208, '新增', 4, NULL, 'electric:infoMain:targetSetWater:insert', '0', NULL, NULL, 0, '2018-10-15 14:27:02', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (210, 208, '编辑', 4, NULL, 'electric:infoMain:targetSetWater:update', '0', NULL, NULL, 0, '2018-10-15 14:27:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (211, 208, '删除', 4, NULL, 'electric:infoMain:targetSetWater:delete', '0', NULL, NULL, 0, '2018-10-15 14:27:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (212, 208, '查看', 4, NULL, 'electric:infoMain:targetSetWater:view', '0', NULL, NULL, 0, '2018-10-15 14:28:06', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (213, 1, '远程设置', 2, NULL, 'setting:menu', '0', NULL, NULL, 0, '2018-10-15 15:58:07', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (214, 213, '升级包管理', 3, NULL, 'setting:index:menu', '0', NULL, NULL, 0, '2018-10-15 15:58:56', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (215, 214, '新增', 4, NULL, 'setting:index:insert', '0', NULL, NULL, 0, '2018-10-15 16:16:58', 1, '2018-10-15 16:18:03', 1);
INSERT INTO `t_sys_operation` VALUES (216, 214, '删除', 4, NULL, 'setting:index:delete', '0', NULL, NULL, 0, '2018-10-15 16:18:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (217, 214, '升级', 4, NULL, 'setting:index:upgrade', '0', NULL, NULL, 0, '2018-10-15 18:10:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (218, 213, '日志发送用户', 2, NULL, 'logUser:index:menu', '0', NULL, NULL, 1, '2018-10-22 11:30:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (219, 213, '日志发送用户', 3, NULL, 'logUser:index:menu', '0', NULL, NULL, 0, '2018-10-22 11:31:45', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (220, 213, '日志管理', 3, NULL, 'logManagement:index:menu', '0', NULL, NULL, 0, '2018-10-22 11:32:26', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (221, 219, '新增', 4, NULL, 'logUser:index:insert', '0', NULL, NULL, 0, '2018-10-22 13:22:37', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (222, 219, '删除', 4, NULL, 'logUser:index:delete', '0', NULL, NULL, 0, '2018-10-22 13:23:11', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (223, 219, '查看', 4, NULL, 'logUser:index:view', '0', NULL, NULL, 0, '2018-10-22 13:24:24', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (224, 219, '编辑', 4, NULL, 'logUser:index:update', '0', NULL, NULL, 0, '2018-10-22 13:25:03', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (225, 89, '启用', 4, NULL, 'crane:device:crane:open', '0', NULL, NULL, 0, '2018-10-28 10:06:19', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (226, 103, '启用', 4, NULL, 'lift:deviceMain:projectLift:handleOpen', '0', NULL, NULL, 0, '2018-11-14 09:59:42', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (227, 201, '停用', 4, NULL, 'electric:device:water:setDis', '0', NULL, NULL, 0, '2018-11-14 18:05:10', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (228, 89, '停用', 4, NULL, 'crane:device:crane:close', '0', NULL, NULL, 0, '2018-11-15 11:21:39', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (229, 78, '停用', 4, NULL, 'dust:monitor:device:close', '0', NULL, NULL, 0, '2018-11-15 15:35:26', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (230, 103, '停用', 4, NULL, 'lift:deviceMain:projectLift:close', '0', NULL, NULL, 0, '2018-11-16 13:49:23', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (231, 1, '安全帽管理', 2, NULL, 'helmet:menu', '0', NULL, NULL, 0, '2018-12-04 13:38:01', 1, '2018-12-04 13:39:26', 1);
INSERT INTO `t_sys_operation` VALUES (232, 231, '设备管理', 2, NULL, 'helmet:device:manage', '0', NULL, NULL, 0, '2018-12-04 13:41:06', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (233, 232, '安全帽管理', 3, NULL, 'helmet:device:manage:menu', '0', NULL, NULL, 0, '2018-12-04 13:41:38', 1, '2018-12-07 11:08:12', 1);
INSERT INTO `t_sys_operation` VALUES (234, 233, '项目调拨', 4, NULL, 'helmet:device:manage:projectTransfers', '0', NULL, NULL, 0, '2018-12-04 13:44:34', 1, '2018-12-07 11:22:20', 1);
INSERT INTO `t_sys_operation` VALUES (235, 233, '人员调拨', 4, NULL, 'helmet:device:manage:personTransfers', '0', NULL, NULL, 0, '2018-12-04 13:44:54', 1, '2018-12-07 11:23:19', 1);
INSERT INTO `t_sys_operation` VALUES (236, 233, '删除', 4, NULL, 'helmet:device:manage:delete', '0', NULL, NULL, 1, '2018-12-04 13:45:18', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (237, 233, '启用', 4, NULL, 'helmet:device:manage:open', '0', NULL, NULL, 0, '2018-12-04 13:45:58', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (238, 233, '关闭', 4, NULL, 'helmet:device:manage:close', '0', NULL, NULL, 0, '2018-12-04 13:46:29', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (239, 233, '查看', 4, NULL, 'helmet:device:manage:view', '0', NULL, NULL, 1, '2018-12-04 13:48:03', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (240, 231, '信息管理', 3, NULL, 'helmet:info:manage', '0', NULL, NULL, 0, '2018-12-05 11:19:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (241, 240, '实时监控', 4, NULL, 'helmet:info:manage:menu', '0', NULL, NULL, 0, '2018-12-05 13:45:47', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (242, 232, '安全帽入库', 2, NULL, 'helmet:device:stock', '0', NULL, NULL, 1, '2018-12-07 11:07:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (243, 242, '入库', 3, NULL, 'helmet:device:stock:putStock', '0', NULL, NULL, 1, '2018-12-07 11:09:52', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (244, 242, '出库', 3, NULL, 'helmet:device:stock:putStock', '0', NULL, NULL, 1, '2018-12-07 11:10:11', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (245, 232, '安全帽入库', 3, NULL, 'helmet:device:stock', '0', NULL, NULL, 0, '2018-12-07 11:11:45', 1, '2018-12-07 11:18:06', 1);
INSERT INTO `t_sys_operation` VALUES (246, 245, '入库', 4, NULL, 'helmet:device:stock:putStock', '0', NULL, NULL, 0, '2018-12-07 11:13:11', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (247, 245, '出库', 4, NULL, 'helmet:device:stock:outStock', '0', NULL, NULL, 0, '2018-12-07 11:13:43', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (248, 245, '模板下载', 4, NULL, 'helmet:device:stock:templateExport', '0', NULL, NULL, 0, '2018-12-07 11:14:59', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (249, 245, '删除', 4, NULL, 'helmet:device:stock:delete', '0', NULL, NULL, 0, '2018-12-07 11:15:37', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (250, 213, '应用管理', 3, NULL, 'remote:application:menu', '0', NULL, NULL, 0, '2019-01-18 13:09:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (251, 250, '新增', 4, NULL, 'remote:application:insert', '0', NULL, NULL, 0, '2019-01-18 13:10:00', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (252, 250, '编辑', 4, NULL, 'remote:application:update', '0', NULL, NULL, 0, '2019-01-18 13:10:13', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (253, 250, '删除', 4, NULL, 'remote:application:delete', '0', NULL, NULL, 0, '2019-01-18 13:10:29', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (254, 250, '查看', 4, NULL, 'remote:application:view', '0', NULL, NULL, 0, '2019-01-18 13:10:48', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (255, 1, '出厂调试', 2, NULL, 'factory:menu', '0', NULL, NULL, 0, '2019-01-21 15:40:31', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (256, 255, '出厂调试', 3, NULL, 'factory:debugging:menu', '0', NULL, NULL, 0, '2019-01-21 15:41:25', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (257, 256, '新增', 4, NULL, 'factory:debugging:insert', '0', NULL, NULL, 0, '2019-01-21 15:43:32', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (258, 256, '查看', 4, NULL, 'factory:debugging:view', '0', NULL, NULL, 0, '2019-01-21 15:43:51', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (259, 256, '删除', 4, NULL, 'factory:debugging:delete', '0', NULL, NULL, 0, '2019-01-21 15:44:08', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (260, 256, '连接', 4, NULL, 'factory:debugging:link', '0', NULL, NULL, 0, '2019-01-21 15:44:33', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (261, 10, '修改组织', 4, NULL, 'project:info:change', '0', NULL, NULL, 0, '2019-01-28 11:19:10', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (262, 1, '库存管理', 2, NULL, 'inventory:menu', '0', NULL, NULL, 0, '2019-03-27 08:58:20', 1, '2019-03-27 09:48:08', 1);
INSERT INTO `t_sys_operation` VALUES (263, 262, '库存管理', 2, NULL, 'inventory:inventory:manage', '0', NULL, NULL, 1, '2019-03-27 09:08:38', 1, '2019-03-27 09:25:26', 1);
INSERT INTO `t_sys_operation` VALUES (264, 262, '库存管理', 3, NULL, 'inventory:inventory:manage', '0', NULL, NULL, 1, '2019-03-27 09:29:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (265, 262, '库存管理', 2, NULL, 'inventory:inventory:manage', '0', NULL, NULL, 1, '2019-03-27 09:30:44', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (266, 262, '库存管理', 3, NULL, 'inventory:inventory:manage', '0', NULL, NULL, 0, '2019-03-27 10:38:57', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (267, 1, '基站管理', 2, NULL, 'station:menu', '0', NULL, NULL, 0, '2019-04-01 11:03:25', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (268, 267, '平面图配置', 3, NULL, 'station:map:config:menu', '0', NULL, NULL, 0, '2019-04-01 11:07:53', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (269, 267, '配置基站', 3, NULL, 'station:config:menu', '0', NULL, NULL, 0, '2019-04-01 11:09:10', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (270, 269, '配置基站一览', 4, NULL, 'station:config:view:menu', '0', NULL, NULL, 0, '2019-04-01 11:14:05', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (271, 78, '绑定', 4, NULL, 'dust:monitor:device:bind', '0', NULL, NULL, 0, '2019-04-03 10:28:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (272, 266, '入库', 4, NULL, 'inventory:device:manage:putStock', '0', NULL, NULL, 0, '2019-04-17 18:25:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (273, 266, '出库', 4, NULL, 'inventory:device:manage:outStock', '0', NULL, NULL, 0, '2019-04-17 18:26:47', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (274, 266, '删除', 4, NULL, 'inventory:device:manage:delete', '0', NULL, NULL, 0, '2019-04-17 18:27:43', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (275, 266, '模板下载', 4, NULL, 'inventory:device:manage:templateExport', '0', NULL, NULL, 0, '2019-04-17 18:28:20', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (276, 77, '喷淋管理', 2, NULL, 'dust:spray:device:menu', '0', NULL, NULL, 0, '2019-05-08 09:53:24', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (277, 276, '新增', 3, NULL, 'dust:spray:device:insert', '0', NULL, NULL, 0, '2019-05-08 09:58:37', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (278, 276, '删除', 3, NULL, 'dust:spray:device:delete', '0', NULL, NULL, 0, '2019-05-08 09:59:12', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (279, 276, '更新', 3, NULL, 'dust:spray:device:update', '0', NULL, NULL, 0, '2019-05-08 10:04:22', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (280, 276, '重启', 2, NULL, 'dust:spray:device:reboot', '0', NULL, NULL, 0, '2019-05-08 10:05:05', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (281, 276, '开启', 3, NULL, 'dust:spray:device:open', '0', NULL, NULL, 0, '2019-05-08 10:05:54', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (282, 276, '关闭', 3, NULL, 'dust:spray:device:close', '0', NULL, NULL, 0, '2019-05-08 10:06:38', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (283, 276, '日志', 3, NULL, 'dust:spray:device:log', '0', NULL, NULL, 0, '2019-05-08 10:07:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (284, 276, '查看', 3, NULL, 'dust:spray:device:view', '0', NULL, NULL, 0, '2019-05-08 10:09:35', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (285, 89, '塔吊首页', 4, '', 'crane:home:menu', '0', NULL, NULL, 0, '2019-06-17 10:00:41', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (286, 1, '报表统计', 2, '', 'statisticsreport:menu', '0', NULL, '', 0, '2019-06-28 19:34:07', 1, '2019-06-28 19:35:18', 1);
INSERT INTO `t_sys_operation` VALUES (287, 286, '设备在线统计', 3, '', 'deviceonline:menu', '0', NULL, '', 0, '2019-06-28 19:37:04', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (288, 286, '设备离线统计', 3, '', 'deviceoffline:device', '0', NULL, '', 0, '2019-06-28 19:37:57', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (289, 286, '设备台账', 3, '', 'deviceparameter:menu', '0', NULL, '', 0, '2019-06-28 19:38:48', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (290, 286, '设备工作（分时段）统计', 3, '', 'devicework:menu', '0', NULL, '', 0, '2019-06-28 19:40:16', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (291, 286, '设备流量统计', 3, '', 'deviceflow:menu', '0', NULL, '', 0, '2019-06-28 19:40:51', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (292, 286, '设备循环工作时长', 3, '', 'devicecycleworkingtime:menu', '0', NULL, '', 0, '2019-06-28 19:42:03', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (293, 286, '预警信息一览', 3, '', 'warninformation:menu', '0', NULL, '', 0, '2019-06-28 19:42:45', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (294, 286, '报警信息一览', 3, '', 'alarminformation:menu', '0', NULL, '', 0, '2019-06-28 19:43:37', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (295, 286, '违章信息一览', 3, '', 'illegalinformation:menu', '0', NULL, '', 0, '2019-06-28 19:48:20', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (296, 286, '工作等级统计', 3, '', 'worklevel:menu', '0', NULL, '', 0, '2019-06-28 19:49:14', 1, '2019-06-28 19:35:18', NULL);
INSERT INTO `t_sys_operation` VALUES (297, 87, '充值', 4, NULL, 'crane:device:recharge:menu', '0', NULL, NULL, 1, '2019-07-02 17:25:50', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (298, 1, '充值', 2, NULL, 'recharge:menu', '0', NULL, NULL, 0, '2019-07-02 17:36:55', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (299, 298, '处理', 3, NULL, 'recharge:update', '0', NULL, NULL, 0, '2019-07-02 17:37:21', 1, NULL, NULL);
INSERT INTO `t_sys_operation` VALUES (300, 298, '查看', 3, NULL, 'recharge:view', '0', NULL, NULL, 0, '2019-07-02 17:37:39', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization`;
CREATE TABLE `t_sys_organization`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '组织等级',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT 'PID',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '所属集团',
  `introduction` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业描述',
  `flag` int(11) NULL DEFAULT NULL COMMENT '来源标记',
  `relation_org_id` int(11) NULL DEFAULT NULL COMMENT '关联组织id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_organization
-- ----------------------------
INSERT INTO `t_sys_organization` VALUES (1, '运营商', 1, 0, 1, NULL, 1, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (4, '星云', 2, 1, 1, '星云', NULL, NULL, NULL, NULL, 0, '2018-09-11 16:23:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (5, '集团', 2, 1, 1, '集团', NULL, NULL, NULL, NULL, 0, '2018-09-18 17:42:01', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (6, 'lalalal', 2, 1, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-18 17:58:36', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (7, 'test', 3, 1, 1, '233333333333333333333333333333333333333333333333333333333333333333333', NULL, NULL, NULL, NULL, 1, '2018-09-18 18:04:27', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (8, '公司1', 3, 1, 1, '公司1', NULL, NULL, NULL, NULL, 1, '2018-09-19 08:59:33', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (9, 'test', 4, 8, 1, '23333', NULL, NULL, NULL, NULL, 0, '2018-09-19 09:55:29', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (10, '公司1', 3, 5, 1, '公司1', NULL, NULL, NULL, NULL, 0, '2018-09-19 16:34:06', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (11, '公司2', 3, 5, 1, '22', NULL, NULL, NULL, NULL, 0, '2018-09-19 16:34:21', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (12, '项目1', 4, 10, 1, '22211', NULL, NULL, NULL, NULL, 0, '2018-09-19 16:35:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (13, '项目2', 4, 11, 1, '22', NULL, NULL, NULL, NULL, 0, '2018-09-21 10:04:44', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (14, '1', 4, 7, 1, '1', NULL, NULL, NULL, NULL, 0, '2018-09-21 14:41:11', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (15, '6', 4, 11, 1, '6', NULL, NULL, NULL, NULL, 1, '2018-09-21 14:43:49', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (16, '大集团', 2, 1, 1, '222', NULL, NULL, NULL, NULL, 1, '2018-09-21 14:44:09', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (17, '公司1', 3, 16, 1, '2', NULL, NULL, NULL, NULL, 0, '2018-09-21 14:44:19', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (18, '公司2', 3, 16, 1, NULL, NULL, NULL, NULL, NULL, 0, '2018-09-21 14:44:35', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (19, '1', 4, 17, 1, '1', NULL, NULL, NULL, NULL, 0, '2018-09-21 14:44:42', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (20, '测试1', 2, 1, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-25 16:27:41', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (21, '测试2', 3, 20, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-25 16:27:48', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (22, '测试3', 4, 21, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-25 16:27:55', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (23, 'test', 3, 4, 1, '333', NULL, NULL, NULL, NULL, 0, '2018-09-26 09:59:12', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (24, '44444', 4, 23, 1, '44444444444', NULL, NULL, NULL, NULL, 1, '2018-09-26 09:59:21', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (25, '项目11', 4, 10, 1, '项目111', NULL, NULL, NULL, NULL, 0, '2018-09-27 17:19:03', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (26, '2', 3, 4, 1, '22222222', NULL, NULL, NULL, NULL, 0, '2018-09-27 22:13:31', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (27, '11', 3, 4, 1, '22', NULL, NULL, NULL, NULL, 1, '2018-09-27 22:13:54', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (28, '1', 3, 4, 1, '1', NULL, NULL, NULL, NULL, 1, '2018-09-27 22:14:30', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (29, '1', 3, 4, 1, '111111', NULL, NULL, NULL, NULL, 1, '2018-09-27 22:14:43', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (30, '1', 3, 4, 1, '1', NULL, NULL, NULL, NULL, 0, '2018-09-27 22:15:34', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (31, '施工单位', 4, 23, 1, '11', NULL, NULL, NULL, NULL, 0, '2018-09-27 22:18:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (32, '1', 4, 11, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-27 22:19:40', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (33, '1', 4, 11, 1, NULL, NULL, NULL, NULL, NULL, 0, '2018-09-27 22:20:25', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (34, '111', 4, 11, 1, NULL, NULL, NULL, NULL, NULL, 0, '2018-09-27 22:21:56', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (35, '11111', 4, 11, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-27 22:30:13', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (36, '11', 4, 26, 1, '11', NULL, NULL, NULL, NULL, 0, '2018-09-28 09:29:44', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (37, '22', 4, 23, 1, '222222', NULL, NULL, NULL, NULL, 1, '2018-09-29 09:39:02', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (38, '住建局', 2, 1, 1, NULL, NULL, NULL, NULL, NULL, 1, '2018-09-29 09:46:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (39, '住建局', 2, 1, 1, NULL, NULL, NULL, NULL, NULL, 0, '2018-09-29 09:46:32', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (40, '星云智能公司', 3, 39, 1, NULL, NULL, NULL, NULL, NULL, 0, '2018-09-29 09:50:07', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (41, '软件部门', 4, 40, 1, '阿萨撒大声大大撒阿萨德阿萨撒大声大', NULL, NULL, NULL, NULL, 0, '2018-09-29 09:51:02', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (42, '10.11', 3, 4, 1, '33', NULL, NULL, NULL, NULL, 0, '2018-10-11 11:12:09', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (43, '出厂调试', 2, 1, 1, '出厂调试', NULL, NULL, NULL, NULL, 0, '2019-01-23 13:48:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (44, '塔吊部门1111111111111111', 2, 1, 1, '11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111', NULL, NULL, NULL, NULL, 1, '2019-06-21 09:33:05', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (45, '塔吊公司', 2, 1, 1, '', NULL, NULL, NULL, NULL, 1, '2019-06-21 09:33:27', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (46, '塔吊集团', 2, 1, 1, '', NULL, NULL, NULL, NULL, 0, '2019-06-21 09:35:46', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (47, '塔吊公司', 3, 46, 1, '', NULL, NULL, NULL, NULL, 0, '2019-06-21 09:35:58', 1, NULL, NULL);
INSERT INTO `t_sys_organization` VALUES (48, '塔吊部门', 4, 47, 1, '', NULL, NULL, NULL, NULL, 0, '2019-06-21 09:36:08', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_organization_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization_user`;
CREATE TABLE `t_sys_organization_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '所属部门',
  `group_id` int(11) NULL DEFAULT NULL COMMENT '所属集团',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_organization_user
-- ----------------------------
INSERT INTO `t_sys_organization_user` VALUES (1, 1, 1, 1, NULL, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (2, 30, 1, 1, NULL, NULL, 0, '2018-09-12 09:33:49', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (3, 2, 1, 1, NULL, NULL, 0, '2018-09-12 09:33:49', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (5, 31, 1, 1, NULL, NULL, 0, '2018-09-12 10:19:24', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (6, 35, 4, 1, NULL, NULL, 0, '2018-09-17 14:04:23', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (7, 38, 4, 1, NULL, NULL, 1, '2018-09-18 14:49:40', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (8, 31, 4, 1, NULL, NULL, 1, '2018-09-18 14:53:23', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (9, 30, 4, 1, NULL, NULL, 1, '2018-09-18 14:54:52', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (10, 2, 4, 1, NULL, NULL, 1, '2018-09-18 14:57:34', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (11, 38, 4, 1, NULL, NULL, 0, '2018-09-18 16:37:08', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (12, 31, 9, 1, NULL, NULL, 0, '2018-09-19 10:00:17', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (13, 31, 8, 1, NULL, NULL, 0, '2018-09-19 10:39:07', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (14, 43, 8, 1, NULL, NULL, 0, '2018-09-19 10:49:31', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (15, 44, 12, 1, NULL, NULL, 0, '2018-09-19 16:36:46', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (16, 46, 12, 1, NULL, NULL, 1, '2018-09-21 14:02:29', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (17, 47, 13, 1, NULL, NULL, 1, '2018-09-25 15:13:18', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (18, 43, 24, 1, NULL, NULL, 0, '2018-09-26 10:01:25', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (19, 31, 24, 1, NULL, NULL, 1, '2018-09-26 10:01:25', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (20, 48, 24, 1, NULL, NULL, 0, '2018-09-26 14:53:43', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (21, 49, 1, 1, NULL, NULL, 0, '2018-09-26 15:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (22, 50, 1, 1, NULL, NULL, 0, '2018-09-26 19:09:42', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (23, 46, 12, 1, NULL, NULL, 0, '2018-09-27 13:47:34', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (24, 47, 13, 1, NULL, NULL, 0, '2018-09-27 13:49:32', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (25, 51, 13, 1, NULL, NULL, 1, '2018-09-27 13:54:57', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (26, 52, 1, 1, NULL, NULL, 1, '2018-09-27 13:58:54', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (27, 52, 13, 1, NULL, NULL, 1, '2018-09-27 14:00:05', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (28, 52, 1, 1, NULL, NULL, 1, '2018-09-27 14:15:27', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (29, 52, 1, 1, NULL, NULL, 1, '2018-09-27 14:16:17', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (30, 52, 1, 1, NULL, NULL, 1, '2018-09-27 14:20:19', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (31, 53, 10, 1, NULL, NULL, 0, '2018-09-27 17:18:21', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (32, 54, 11, 1, NULL, NULL, 0, '2018-09-27 17:18:33', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (33, 46, 25, 1, NULL, NULL, 1, '2018-09-27 17:19:12', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (34, 55, 13, 1, NULL, NULL, 0, '2018-09-27 20:12:35', 53, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (35, 56, 13, 1, NULL, NULL, 0, '2018-09-27 20:21:31', 54, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (36, 57, 13, 1, NULL, NULL, 0, '2018-09-27 20:24:11', 56, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (37, 58, 12, 1, NULL, NULL, 0, '2018-09-27 22:11:21', 46, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (38, 58, 11, 1, NULL, NULL, 0, '2018-09-27 22:15:53', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (39, 59, 39, 1, NULL, NULL, 0, '2018-09-29 10:46:41', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (40, 60, 40, 1, NULL, NULL, 0, '2018-09-29 10:56:47', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (41, 61, 41, 1, NULL, NULL, 0, '2018-09-29 10:57:58', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (42, 63, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (43, 62, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (44, 61, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (45, 60, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (46, 59, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (47, 58, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (48, 57, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (49, 56, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (50, 55, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (51, 54, 1, 1, NULL, NULL, 0, '2018-09-29 15:14:20', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (52, 68, 42, 1, NULL, NULL, 0, '2018-10-11 11:28:01', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (53, 62, 42, 1, NULL, NULL, 0, '2018-10-11 11:28:01', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (54, 47, 33, 1, NULL, NULL, 1, '2018-10-12 16:42:44', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (55, 66, 12, 1, NULL, NULL, 0, '2018-10-22 15:35:58', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (56, 67, 41, 1, NULL, NULL, 0, '2018-12-17 13:24:06', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (57, 71, 41, 1, NULL, NULL, 0, '2018-12-17 13:28:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (58, 70, 41, 1, NULL, NULL, 0, '2018-12-17 13:28:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (59, 69, 41, 1, NULL, NULL, 0, '2018-12-17 13:28:14', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (60, 72, 43, 1, NULL, NULL, 0, '2019-01-23 13:48:27', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (61, 1, 48, 1, NULL, NULL, 1, '2019-06-21 09:37:41', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (62, 1, 48, 1, NULL, NULL, 1, '2019-06-21 09:38:02', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (63, 76, 48, 1, NULL, NULL, 0, '2019-06-26 08:58:59', 1, NULL, NULL);
INSERT INTO `t_sys_organization_user` VALUES (64, 77, 4, 1, NULL, NULL, 0, '2019-07-02 17:40:19', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `instroction` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '所属组织',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES (2, '系统管理', '0002', NULL, 1, NULL, NULL, 0, '2018-09-10 09:55:07', 1, NULL, NULL);
INSERT INTO `t_sys_role` VALUES (45, '测试', '1000', '12345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678898912345678', 1, NULL, '', 1, '2019-06-21 09:15:18', 1, '2019-06-21 09:18:39', 1);
INSERT INTO `t_sys_role` VALUES (46, '11', '11', NULL, 1, NULL, '', 1, '2019-06-21 09:19:05', 1, NULL, NULL);
INSERT INTO `t_sys_role` VALUES (47, '测试', 'ceshi', NULL, 1, NULL, '', 0, '2019-06-21 09:20:12', 1, NULL, NULL);
INSERT INTO `t_sys_role` VALUES (48, '商务部', '0003', '根据收款信息，操作激活SIM卡', 1, NULL, '', 0, '2019-07-02 17:24:43', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_role_operation
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_operation`;
CREATE TABLE `t_sys_role_operation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `oper_id` int(11) NULL DEFAULT NULL COMMENT '所属部门',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20940 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role_operation
-- ----------------------------
INSERT INTO `t_sys_role_operation` VALUES (19654, 2, 1, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19655, 2, 5, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19656, 2, 10, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19657, 2, 23, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19658, 2, 24, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19659, 2, 25, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19660, 2, 26, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19661, 2, 128, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19662, 2, 196, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19663, 2, 261, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19664, 2, 46, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19665, 2, 48, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19666, 2, 49, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19667, 2, 50, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19668, 2, 51, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19669, 2, 129, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19670, 2, 47, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19671, 2, 52, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19672, 2, 53, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19673, 2, 54, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19674, 2, 55, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19675, 2, 6, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19676, 2, 64, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19677, 2, 66, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19678, 2, 67, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19679, 2, 68, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19680, 2, 69, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19681, 2, 65, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19682, 2, 70, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19683, 2, 71, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19684, 2, 72, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19685, 2, 73, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19686, 2, 74, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19687, 2, 7, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19688, 2, 12, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19689, 2, 28, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19690, 2, 29, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19691, 2, 30, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19692, 2, 31, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19693, 2, 13, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19694, 2, 32, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19695, 2, 33, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19696, 2, 34, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19697, 2, 35, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19698, 2, 14, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19699, 2, 36, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19700, 2, 37, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19701, 2, 38, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19702, 2, 39, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19703, 2, 40, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19704, 2, 41, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19705, 2, 42, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19706, 2, 43, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19707, 2, 44, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19708, 2, 56, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19709, 2, 98, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19710, 2, 99, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19711, 2, 100, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19712, 2, 101, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19713, 2, 102, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19714, 2, 103, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19715, 2, 134, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19716, 2, 135, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19717, 2, 136, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19718, 2, 137, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19719, 2, 226, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19720, 2, 230, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19721, 2, 104, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19722, 2, 105, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19723, 2, 146, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19724, 2, 147, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19725, 2, 148, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19726, 2, 149, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19727, 2, 106, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19728, 2, 130, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19729, 2, 131, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19730, 2, 132, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19731, 2, 133, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19732, 2, 186, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19733, 2, 58, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19734, 2, 75, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19735, 2, 76, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19736, 2, 77, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19737, 2, 78, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19738, 2, 79, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19739, 2, 80, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19740, 2, 82, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19741, 2, 83, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19742, 2, 84, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19743, 2, 229, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19744, 2, 271, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19745, 2, 276, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19746, 2, 277, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19747, 2, 278, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19748, 2, 279, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19749, 2, 280, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19750, 2, 281, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19751, 2, 282, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19752, 2, 283, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19753, 2, 284, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19754, 2, 95, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19755, 2, 122, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19756, 2, 123, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19757, 2, 124, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19758, 2, 125, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19759, 2, 108, NULL, NULL, 1, '2019-05-08 10:11:19', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19760, 2, 110, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19761, 2, 111, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19762, 2, 112, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19763, 2, 113, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19764, 2, 114, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19765, 2, 139, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19766, 2, 141, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19767, 2, 142, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19768, 2, 144, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19769, 2, 145, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19770, 2, 127, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19771, 2, 81, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19772, 2, 85, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19773, 2, 88, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19774, 2, 87, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19775, 2, 89, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19776, 2, 90, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19777, 2, 91, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19778, 2, 92, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19779, 2, 93, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19780, 2, 225, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19781, 2, 228, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19782, 2, 117, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19783, 2, 118, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19784, 2, 119, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19785, 2, 138, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19786, 2, 140, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19787, 2, 150, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19788, 2, 151, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19789, 2, 152, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19790, 2, 153, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19791, 2, 143, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19792, 2, 154, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19793, 2, 155, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19794, 2, 156, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19795, 2, 157, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19796, 2, 187, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19797, 2, 158, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19798, 2, 159, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19799, 2, 162, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19800, 2, 207, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19801, 2, 160, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19802, 2, 163, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19803, 2, 171, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19804, 2, 172, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19805, 2, 173, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19806, 2, 174, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19807, 2, 175, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19808, 2, 201, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19809, 2, 202, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19810, 2, 203, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19811, 2, 204, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19812, 2, 205, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19813, 2, 206, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19814, 2, 227, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19815, 2, 161, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19816, 2, 166, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19817, 2, 167, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19818, 2, 168, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19819, 2, 169, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19820, 2, 170, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19821, 2, 208, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19822, 2, 209, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19823, 2, 210, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19824, 2, 211, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19825, 2, 212, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19826, 2, 180, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19827, 2, 181, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19828, 2, 182, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19829, 2, 183, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19830, 2, 184, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19831, 2, 198, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19832, 2, 199, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19833, 2, 197, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19834, 2, 213, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19835, 2, 214, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19836, 2, 215, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19837, 2, 216, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19838, 2, 217, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19839, 2, 219, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19840, 2, 221, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19841, 2, 222, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19842, 2, 223, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19843, 2, 224, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19844, 2, 220, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19845, 2, 250, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19846, 2, 251, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19847, 2, 252, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19848, 2, 253, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19849, 2, 254, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19850, 2, 231, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19851, 2, 232, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19852, 2, 233, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19853, 2, 234, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19854, 2, 235, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19855, 2, 237, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19856, 2, 238, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19857, 2, 245, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19858, 2, 246, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19859, 2, 247, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19860, 2, 248, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19861, 2, 249, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19862, 2, 240, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19863, 2, 241, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19864, 2, 255, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19865, 2, 256, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19866, 2, 257, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19867, 2, 258, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19868, 2, 259, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19869, 2, 260, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19870, 2, 262, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19871, 2, 266, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19872, 2, 272, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19873, 2, 273, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19874, 2, 274, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19875, 2, 275, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19876, 2, 267, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19877, 2, 268, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19878, 2, 269, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19879, 2, 270, NULL, NULL, 1, '2019-05-08 10:11:20', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19880, 2, 1, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19881, 2, 5, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19882, 2, 10, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19883, 2, 23, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19884, 2, 24, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19885, 2, 25, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19886, 2, 26, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19887, 2, 128, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19888, 2, 46, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19889, 2, 48, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19890, 2, 49, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19891, 2, 50, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19892, 2, 51, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19893, 2, 129, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19894, 2, 47, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19895, 2, 52, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19896, 2, 53, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19897, 2, 54, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19898, 2, 55, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19899, 2, 6, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19900, 2, 64, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19901, 2, 66, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19902, 2, 67, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19903, 2, 68, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19904, 2, 69, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19905, 2, 65, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19906, 2, 70, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19907, 2, 71, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19908, 2, 72, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19909, 2, 73, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19910, 2, 74, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19911, 2, 7, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19912, 2, 12, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19913, 2, 28, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19914, 2, 29, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19915, 2, 30, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19916, 2, 31, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19917, 2, 13, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19918, 2, 32, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19919, 2, 33, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19920, 2, 34, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19921, 2, 35, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19922, 2, 14, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19923, 2, 36, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19924, 2, 37, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19925, 2, 38, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19926, 2, 39, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19927, 2, 40, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19928, 2, 41, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19929, 2, 42, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19930, 2, 43, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19931, 2, 44, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19932, 2, 81, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19933, 2, 85, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19934, 2, 88, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19935, 2, 87, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19936, 2, 89, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19937, 2, 90, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19938, 2, 91, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19939, 2, 92, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19940, 2, 93, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19941, 2, 117, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19942, 2, 118, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19943, 2, 119, NULL, NULL, 1, '2019-06-17 09:55:08', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19944, 2, 1, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19945, 2, 5, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19946, 2, 10, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19947, 2, 23, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19948, 2, 24, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19949, 2, 25, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19950, 2, 26, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19951, 2, 128, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19952, 2, 46, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19953, 2, 48, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19954, 2, 49, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19955, 2, 50, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19956, 2, 51, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19957, 2, 129, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19958, 2, 47, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19959, 2, 52, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19960, 2, 53, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19961, 2, 54, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19962, 2, 55, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19963, 2, 6, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19964, 2, 64, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19965, 2, 66, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19966, 2, 67, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19967, 2, 68, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19968, 2, 69, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19969, 2, 65, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19970, 2, 70, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19971, 2, 71, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19972, 2, 72, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19973, 2, 73, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19974, 2, 74, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19975, 2, 7, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19976, 2, 12, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19977, 2, 28, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19978, 2, 29, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19979, 2, 30, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19980, 2, 31, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19981, 2, 13, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19982, 2, 32, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19983, 2, 33, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19984, 2, 34, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19985, 2, 35, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19986, 2, 14, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19987, 2, 36, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19988, 2, 37, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19989, 2, 38, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19990, 2, 39, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19991, 2, 40, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19992, 2, 41, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19993, 2, 42, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19994, 2, 43, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19995, 2, 44, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19996, 2, 58, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19997, 2, 127, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19998, 2, 81, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (19999, 2, 85, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20000, 2, 88, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20001, 2, 87, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20002, 2, 89, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20003, 2, 90, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20004, 2, 91, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20005, 2, 92, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20006, 2, 93, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20007, 2, 117, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20008, 2, 118, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20009, 2, 119, NULL, NULL, 1, '2019-06-17 09:57:33', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20010, 2, 1, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20011, 2, 5, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20012, 2, 10, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20013, 2, 23, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20014, 2, 24, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20015, 2, 25, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20016, 2, 26, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20017, 2, 128, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20018, 2, 46, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20019, 2, 48, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20020, 2, 49, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20021, 2, 50, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20022, 2, 51, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20023, 2, 129, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20024, 2, 47, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20025, 2, 52, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20026, 2, 53, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20027, 2, 54, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20028, 2, 55, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20029, 2, 6, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20030, 2, 64, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20031, 2, 66, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20032, 2, 67, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20033, 2, 68, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20034, 2, 69, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20035, 2, 65, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20036, 2, 70, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20037, 2, 71, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20038, 2, 72, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20039, 2, 73, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20040, 2, 74, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20041, 2, 7, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20042, 2, 12, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20043, 2, 28, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20044, 2, 29, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20045, 2, 30, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20046, 2, 31, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20047, 2, 13, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20048, 2, 32, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20049, 2, 33, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20050, 2, 34, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20051, 2, 35, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20052, 2, 14, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20053, 2, 36, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20054, 2, 37, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20055, 2, 38, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20056, 2, 39, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20057, 2, 40, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20058, 2, 41, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20059, 2, 42, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20060, 2, 43, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20061, 2, 44, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20062, 2, 81, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20063, 2, 85, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20064, 2, 88, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20065, 2, 87, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20066, 2, 89, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20067, 2, 90, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20068, 2, 91, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20069, 2, 92, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20070, 2, 93, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20071, 2, 225, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20072, 2, 228, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20073, 2, 285, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20074, 2, 117, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20075, 2, 118, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20076, 2, 119, NULL, NULL, 1, '2019-06-18 14:55:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20077, 2, 1, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20078, 2, 5, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20079, 2, 10, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20080, 2, 23, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20081, 2, 24, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20082, 2, 25, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20083, 2, 26, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20084, 2, 128, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20085, 2, 46, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20086, 2, 48, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20087, 2, 49, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20088, 2, 50, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20089, 2, 51, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20090, 2, 129, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20091, 2, 47, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20092, 2, 52, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20093, 2, 53, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20094, 2, 54, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20095, 2, 55, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20096, 2, 6, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20097, 2, 64, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20098, 2, 66, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20099, 2, 67, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20100, 2, 68, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20101, 2, 69, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20102, 2, 65, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20103, 2, 70, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20104, 2, 71, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20105, 2, 72, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20106, 2, 73, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20107, 2, 74, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20108, 2, 7, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20109, 2, 12, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20110, 2, 28, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20111, 2, 29, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20112, 2, 30, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20113, 2, 31, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20114, 2, 13, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20115, 2, 32, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20116, 2, 33, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20117, 2, 34, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20118, 2, 35, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20119, 2, 14, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20120, 2, 36, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20121, 2, 37, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20122, 2, 38, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20123, 2, 39, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20124, 2, 40, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20125, 2, 41, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20126, 2, 42, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20127, 2, 43, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20128, 2, 44, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20129, 2, 81, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20130, 2, 85, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20131, 2, 88, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20132, 2, 87, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20133, 2, 89, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20134, 2, 90, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20135, 2, 91, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20136, 2, 92, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20137, 2, 93, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20138, 2, 225, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20139, 2, 228, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20140, 2, 285, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20141, 2, 117, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20142, 2, 118, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20143, 2, 119, NULL, NULL, 1, '2019-06-20 13:28:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20144, 47, 1, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20145, 47, 5, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20146, 47, 10, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20147, 47, 23, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20148, 47, 24, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20149, 47, 25, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20150, 47, 26, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20151, 47, 128, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20152, 47, 196, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20153, 47, 261, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20154, 47, 46, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20155, 47, 48, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20156, 47, 49, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20157, 47, 50, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20158, 47, 51, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20159, 47, 129, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20160, 47, 47, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20161, 47, 52, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20162, 47, 53, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20163, 47, 54, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20164, 47, 55, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20165, 47, 6, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20166, 47, 64, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20167, 47, 66, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20168, 47, 67, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20169, 47, 68, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20170, 47, 69, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20171, 47, 65, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20172, 47, 70, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20173, 47, 71, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20174, 47, 72, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20175, 47, 73, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20176, 47, 74, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20177, 47, 81, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20178, 47, 85, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20179, 47, 88, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20180, 47, 87, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20181, 47, 89, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20182, 47, 90, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20183, 47, 91, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20184, 47, 92, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20185, 47, 93, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20186, 47, 225, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20187, 47, 228, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20188, 47, 285, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20189, 47, 117, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20190, 47, 118, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20191, 47, 119, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20192, 47, 138, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20193, 47, 140, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20194, 47, 150, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20195, 47, 151, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20196, 47, 152, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20197, 47, 153, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20198, 47, 143, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20199, 47, 154, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20200, 47, 155, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20201, 47, 156, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20202, 47, 157, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20203, 47, 187, NULL, NULL, 1, '2019-06-21 09:20:40', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20204, 47, 1, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20205, 47, 81, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20206, 47, 85, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20207, 47, 88, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20208, 47, 87, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20209, 47, 89, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20210, 47, 90, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20211, 47, 91, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20212, 47, 92, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20213, 47, 93, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20214, 47, 225, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20215, 47, 228, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20216, 47, 285, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20217, 47, 117, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20218, 47, 118, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20219, 47, 119, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20220, 47, 138, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20221, 47, 140, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20222, 47, 150, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20223, 47, 151, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20224, 47, 152, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20225, 47, 153, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20226, 47, 143, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20227, 47, 154, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20228, 47, 155, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20229, 47, 156, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20230, 47, 157, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20231, 47, 187, NULL, NULL, 1, '2019-06-21 13:58:51', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20232, 2, 1, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20233, 2, 5, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20234, 2, 10, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20235, 2, 23, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20236, 2, 24, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20237, 2, 25, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20238, 2, 26, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20239, 2, 128, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20240, 2, 196, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20241, 2, 261, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20242, 2, 46, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20243, 2, 48, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20244, 2, 49, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20245, 2, 50, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20246, 2, 51, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20247, 2, 129, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20248, 2, 47, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20249, 2, 52, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20250, 2, 53, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20251, 2, 54, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20252, 2, 55, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20253, 2, 6, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20254, 2, 64, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20255, 2, 66, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20256, 2, 67, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20257, 2, 68, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20258, 2, 69, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20259, 2, 65, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20260, 2, 70, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20261, 2, 71, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20262, 2, 72, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20263, 2, 73, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20264, 2, 74, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20265, 2, 7, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20266, 2, 12, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20267, 2, 28, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20268, 2, 29, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20269, 2, 30, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20270, 2, 31, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20271, 2, 13, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20272, 2, 32, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20273, 2, 33, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20274, 2, 34, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20275, 2, 35, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20276, 2, 14, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20277, 2, 36, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20278, 2, 37, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20279, 2, 38, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20280, 2, 39, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20281, 2, 40, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20282, 2, 41, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20283, 2, 42, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20284, 2, 43, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20285, 2, 44, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20286, 2, 81, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20287, 2, 85, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20288, 2, 88, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20289, 2, 87, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20290, 2, 89, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20291, 2, 90, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20292, 2, 91, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20293, 2, 92, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20294, 2, 93, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20295, 2, 225, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20296, 2, 228, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20297, 2, 285, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20298, 2, 117, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20299, 2, 118, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20300, 2, 119, NULL, NULL, 1, '2019-06-24 14:36:56', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20301, 2, 1, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20302, 2, 5, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20303, 2, 10, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20304, 2, 23, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20305, 2, 24, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20306, 2, 25, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20307, 2, 26, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20308, 2, 128, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20309, 2, 196, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20310, 2, 261, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20311, 2, 46, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20312, 2, 48, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20313, 2, 49, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20314, 2, 50, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20315, 2, 51, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20316, 2, 129, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20317, 2, 47, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20318, 2, 52, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20319, 2, 53, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20320, 2, 54, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20321, 2, 55, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20322, 2, 6, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20323, 2, 64, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20324, 2, 66, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20325, 2, 67, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20326, 2, 68, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20327, 2, 69, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20328, 2, 65, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20329, 2, 70, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20330, 2, 71, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20331, 2, 72, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20332, 2, 73, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20333, 2, 74, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20334, 2, 7, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20335, 2, 12, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20336, 2, 28, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20337, 2, 29, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20338, 2, 30, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20339, 2, 31, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20340, 2, 13, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20341, 2, 32, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20342, 2, 33, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20343, 2, 34, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20344, 2, 35, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20345, 2, 14, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20346, 2, 36, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20347, 2, 37, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20348, 2, 38, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20349, 2, 39, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20350, 2, 40, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20351, 2, 41, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20352, 2, 42, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20353, 2, 43, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20354, 2, 44, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20355, 2, 81, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20356, 2, 85, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20357, 2, 88, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20358, 2, 87, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20359, 2, 89, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20360, 2, 90, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20361, 2, 91, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20362, 2, 92, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20363, 2, 93, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20364, 2, 225, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20365, 2, 228, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20366, 2, 285, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20367, 2, 117, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20368, 2, 118, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20369, 2, 119, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20370, 2, 213, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20371, 2, 214, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20372, 2, 215, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20373, 2, 216, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20374, 2, 217, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20375, 2, 219, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20376, 2, 221, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20377, 2, 222, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20378, 2, 223, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20379, 2, 224, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20380, 2, 220, NULL, NULL, 1, '2019-06-24 14:40:10', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20381, 47, 1, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20382, 47, 5, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20383, 47, 10, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20384, 47, 23, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20385, 47, 24, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20386, 47, 25, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20387, 47, 26, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20388, 47, 128, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20389, 47, 196, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20390, 47, 261, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20391, 47, 46, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20392, 47, 48, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20393, 47, 49, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20394, 47, 50, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20395, 47, 51, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20396, 47, 129, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20397, 47, 47, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20398, 47, 52, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20399, 47, 53, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20400, 47, 54, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20401, 47, 55, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20402, 47, 6, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20403, 47, 64, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20404, 47, 66, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20405, 47, 67, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20406, 47, 68, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20407, 47, 69, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20408, 47, 65, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20409, 47, 70, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20410, 47, 71, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20411, 47, 72, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20412, 47, 73, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20413, 47, 74, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20414, 47, 7, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20415, 47, 12, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20416, 47, 28, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20417, 47, 29, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20418, 47, 30, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20419, 47, 31, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20420, 47, 13, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20421, 47, 32, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20422, 47, 33, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20423, 47, 34, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20424, 47, 35, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20425, 47, 14, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20426, 47, 36, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20427, 47, 37, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20428, 47, 38, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20429, 47, 39, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20430, 47, 40, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20431, 47, 41, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20432, 47, 42, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20433, 47, 43, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20434, 47, 44, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20435, 47, 81, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20436, 47, 85, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20437, 47, 88, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20438, 47, 87, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20439, 47, 89, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20440, 47, 90, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20441, 47, 91, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20442, 47, 92, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20443, 47, 93, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20444, 47, 225, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20445, 47, 228, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20446, 47, 285, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20447, 47, 117, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20448, 47, 118, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20449, 47, 119, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20450, 47, 187, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20451, 47, 213, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20452, 47, 214, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20453, 47, 215, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20454, 47, 216, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20455, 47, 217, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20456, 47, 219, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20457, 47, 221, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20458, 47, 222, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20459, 47, 223, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20460, 47, 224, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20461, 47, 220, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20462, 47, 250, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20463, 47, 251, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20464, 47, 252, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20465, 47, 253, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20466, 47, 254, NULL, NULL, 1, '2019-06-26 09:03:03', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20467, 2, 1, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20468, 2, 5, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20469, 2, 10, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20470, 2, 23, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20471, 2, 24, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20472, 2, 25, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20473, 2, 26, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20474, 2, 128, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20475, 2, 196, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20476, 2, 261, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20477, 2, 46, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20478, 2, 48, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20479, 2, 49, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20480, 2, 50, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20481, 2, 51, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20482, 2, 129, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20483, 2, 47, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20484, 2, 52, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20485, 2, 53, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20486, 2, 54, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20487, 2, 55, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20488, 2, 6, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20489, 2, 64, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20490, 2, 66, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20491, 2, 67, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20492, 2, 68, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20493, 2, 69, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20494, 2, 65, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20495, 2, 70, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20496, 2, 71, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20497, 2, 72, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20498, 2, 73, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20499, 2, 74, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20500, 2, 7, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20501, 2, 12, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20502, 2, 28, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20503, 2, 29, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20504, 2, 30, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20505, 2, 31, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20506, 2, 13, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20507, 2, 32, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20508, 2, 33, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20509, 2, 34, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20510, 2, 35, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20511, 2, 14, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20512, 2, 36, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20513, 2, 37, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20514, 2, 38, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20515, 2, 39, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20516, 2, 40, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20517, 2, 41, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20518, 2, 42, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20519, 2, 43, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20520, 2, 44, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20521, 2, 81, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20522, 2, 85, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20523, 2, 88, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20524, 2, 87, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20525, 2, 89, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20526, 2, 90, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20527, 2, 91, NULL, NULL, 1, '2019-06-29 11:12:53', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20528, 2, 92, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20529, 2, 93, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20530, 2, 225, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20531, 2, 228, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20532, 2, 285, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20533, 2, 117, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20534, 2, 118, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20535, 2, 119, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20536, 2, 213, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20537, 2, 214, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20538, 2, 215, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20539, 2, 216, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20540, 2, 217, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20541, 2, 219, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20542, 2, 221, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20543, 2, 222, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20544, 2, 223, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20545, 2, 224, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20546, 2, 220, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20547, 2, 286, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20548, 2, 287, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20549, 2, 288, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20550, 2, 289, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20551, 2, 290, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20552, 2, 291, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20553, 2, 292, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20554, 2, 293, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20555, 2, 294, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20556, 2, 295, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20557, 2, 296, NULL, NULL, 1, '2019-06-29 11:12:55', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20558, 2, 1, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20559, 2, 5, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20560, 2, 10, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20561, 2, 23, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20562, 2, 24, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20563, 2, 25, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20564, 2, 26, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20565, 2, 128, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20566, 2, 196, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20567, 2, 261, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20568, 2, 46, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20569, 2, 48, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20570, 2, 49, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20571, 2, 50, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20572, 2, 51, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20573, 2, 129, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20574, 2, 47, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20575, 2, 52, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20576, 2, 53, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20577, 2, 54, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20578, 2, 55, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20579, 2, 6, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20580, 2, 64, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20581, 2, 66, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20582, 2, 67, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20583, 2, 68, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20584, 2, 69, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20585, 2, 65, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20586, 2, 70, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20587, 2, 71, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20588, 2, 72, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20589, 2, 73, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20590, 2, 74, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20591, 2, 7, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20592, 2, 12, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20593, 2, 28, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20594, 2, 29, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20595, 2, 30, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20596, 2, 31, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20597, 2, 13, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20598, 2, 32, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20599, 2, 33, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20600, 2, 34, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20601, 2, 35, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20602, 2, 14, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20603, 2, 36, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20604, 2, 37, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20605, 2, 38, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20606, 2, 39, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20607, 2, 40, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20608, 2, 41, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20609, 2, 42, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20610, 2, 43, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20611, 2, 44, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20612, 2, 81, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20613, 2, 85, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20614, 2, 88, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20615, 2, 87, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20616, 2, 89, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20617, 2, 90, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20618, 2, 91, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20619, 2, 92, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20620, 2, 93, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20621, 2, 225, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20622, 2, 228, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20623, 2, 285, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20624, 2, 117, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20625, 2, 118, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20626, 2, 119, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20627, 2, 213, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20628, 2, 214, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20629, 2, 215, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20630, 2, 216, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20631, 2, 217, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20632, 2, 219, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20633, 2, 221, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20634, 2, 222, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20635, 2, 223, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20636, 2, 224, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20637, 2, 220, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20638, 2, 286, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20639, 2, 287, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20640, 2, 288, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20641, 2, 289, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20642, 2, 290, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20643, 2, 291, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20644, 2, 292, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20645, 2, 293, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20646, 2, 294, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20647, 2, 295, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20648, 2, 296, NULL, NULL, 1, '2019-06-29 11:12:57', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20649, 48, 1, NULL, NULL, 1, '2019-07-02 17:38:22', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20650, 48, 298, NULL, NULL, 1, '2019-07-02 17:38:22', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20651, 48, 299, NULL, NULL, 1, '2019-07-02 17:38:22', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20652, 48, 1, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20653, 48, 81, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20654, 48, 87, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20655, 48, 89, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20656, 48, 90, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20657, 48, 91, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20658, 48, 92, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20659, 48, 93, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20660, 48, 225, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20661, 48, 228, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20662, 48, 285, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20663, 48, 298, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20664, 48, 299, NULL, NULL, 0, '2019-07-02 17:45:26', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20665, 2, 1, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20666, 2, 5, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20667, 2, 10, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20668, 2, 23, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20669, 2, 24, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20670, 2, 25, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20671, 2, 26, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20672, 2, 128, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20673, 2, 196, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20674, 2, 261, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20675, 2, 46, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20676, 2, 48, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20677, 2, 49, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20678, 2, 50, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20679, 2, 51, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20680, 2, 129, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20681, 2, 47, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20682, 2, 52, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20683, 2, 53, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20684, 2, 54, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20685, 2, 55, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20686, 2, 6, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20687, 2, 64, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20688, 2, 66, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20689, 2, 67, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20690, 2, 68, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20691, 2, 69, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20692, 2, 65, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20693, 2, 70, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20694, 2, 71, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20695, 2, 72, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20696, 2, 73, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20697, 2, 74, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20698, 2, 7, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20699, 2, 12, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20700, 2, 28, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20701, 2, 29, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20702, 2, 30, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20703, 2, 31, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20704, 2, 13, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20705, 2, 32, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20706, 2, 33, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20707, 2, 34, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20708, 2, 35, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20709, 2, 14, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20710, 2, 36, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20711, 2, 37, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20712, 2, 38, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20713, 2, 39, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20714, 2, 40, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20715, 2, 41, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20716, 2, 42, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20717, 2, 43, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20718, 2, 44, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20719, 2, 81, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20720, 2, 85, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20721, 2, 88, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20722, 2, 87, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20723, 2, 89, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20724, 2, 90, NULL, NULL, 1, '2019-07-02 17:48:24', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20725, 2, 91, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20726, 2, 92, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20727, 2, 93, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20728, 2, 225, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20729, 2, 228, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20730, 2, 285, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20731, 2, 117, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20732, 2, 118, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20733, 2, 119, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20734, 2, 213, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20735, 2, 214, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20736, 2, 215, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20737, 2, 216, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20738, 2, 217, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20739, 2, 219, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20740, 2, 221, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20741, 2, 222, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20742, 2, 223, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20743, 2, 224, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20744, 2, 220, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20745, 2, 286, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20746, 2, 287, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20747, 2, 288, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20748, 2, 289, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20749, 2, 290, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20750, 2, 291, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20751, 2, 292, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20752, 2, 293, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20753, 2, 294, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20754, 2, 295, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20755, 2, 296, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20756, 2, 298, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20757, 2, 299, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20758, 2, 300, NULL, NULL, 1, '2019-07-02 17:48:25', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20759, 2, 1, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20760, 2, 5, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20761, 2, 10, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20762, 2, 23, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20763, 2, 24, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20764, 2, 25, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20765, 2, 26, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20766, 2, 128, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20767, 2, 196, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20768, 2, 261, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20769, 2, 46, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20770, 2, 48, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20771, 2, 49, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20772, 2, 50, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20773, 2, 51, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20774, 2, 129, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20775, 2, 47, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20776, 2, 52, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20777, 2, 53, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20778, 2, 54, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20779, 2, 55, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20780, 2, 6, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20781, 2, 64, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20782, 2, 66, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20783, 2, 67, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20784, 2, 68, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20785, 2, 69, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20786, 2, 65, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20787, 2, 70, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20788, 2, 71, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20789, 2, 72, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20790, 2, 73, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20791, 2, 74, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20792, 2, 7, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20793, 2, 12, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20794, 2, 28, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20795, 2, 29, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20796, 2, 30, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20797, 2, 31, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20798, 2, 13, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20799, 2, 32, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20800, 2, 33, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20801, 2, 34, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20802, 2, 35, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20803, 2, 14, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20804, 2, 36, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20805, 2, 37, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20806, 2, 38, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20807, 2, 39, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20808, 2, 40, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20809, 2, 41, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20810, 2, 42, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20811, 2, 43, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20812, 2, 44, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20813, 2, 81, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20814, 2, 85, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20815, 2, 88, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20816, 2, 87, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20817, 2, 89, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20818, 2, 90, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20819, 2, 91, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20820, 2, 92, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20821, 2, 93, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20822, 2, 225, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20823, 2, 228, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20824, 2, 285, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20825, 2, 117, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20826, 2, 118, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20827, 2, 119, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20828, 2, 213, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20829, 2, 214, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20830, 2, 215, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20831, 2, 216, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20832, 2, 217, NULL, NULL, 0, '2019-07-02 17:50:46', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20833, 2, 219, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20834, 2, 221, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20835, 2, 222, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20836, 2, 223, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20837, 2, 224, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20838, 2, 220, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20839, 2, 286, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20840, 2, 287, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20841, 2, 288, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20842, 2, 289, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20843, 2, 290, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20844, 2, 291, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20845, 2, 292, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20846, 2, 293, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20847, 2, 294, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20848, 2, 295, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20849, 2, 296, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20850, 2, 298, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20851, 2, 300, NULL, NULL, 0, '2019-07-02 17:50:47', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20852, 47, 1, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20853, 47, 5, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20854, 47, 10, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20855, 47, 23, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20856, 47, 24, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20857, 47, 25, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20858, 47, 26, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20859, 47, 128, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20860, 47, 196, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20861, 47, 261, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20862, 47, 46, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20863, 47, 48, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20864, 47, 49, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20865, 47, 50, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20866, 47, 51, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20867, 47, 129, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20868, 47, 47, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20869, 47, 52, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20870, 47, 53, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20871, 47, 54, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20872, 47, 55, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20873, 47, 6, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20874, 47, 64, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20875, 47, 66, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20876, 47, 67, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20877, 47, 68, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20878, 47, 69, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20879, 47, 65, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20880, 47, 70, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20881, 47, 71, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20882, 47, 72, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20883, 47, 73, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20884, 47, 74, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20885, 47, 7, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20886, 47, 12, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20887, 47, 28, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20888, 47, 29, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20889, 47, 30, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20890, 47, 31, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20891, 47, 13, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20892, 47, 32, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20893, 47, 33, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20894, 47, 34, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20895, 47, 35, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20896, 47, 14, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20897, 47, 36, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20898, 47, 37, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20899, 47, 38, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20900, 47, 39, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20901, 47, 40, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20902, 47, 41, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20903, 47, 42, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20904, 47, 43, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20905, 47, 44, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20906, 47, 81, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20907, 47, 85, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20908, 47, 88, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20909, 47, 87, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20910, 47, 89, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20911, 47, 90, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20912, 47, 91, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20913, 47, 92, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20914, 47, 93, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20915, 47, 225, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20916, 47, 228, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20917, 47, 285, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20918, 47, 117, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20919, 47, 118, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20920, 47, 119, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20921, 47, 187, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20922, 47, 213, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20923, 47, 214, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20924, 47, 215, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20925, 47, 216, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20926, 47, 217, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20927, 47, 219, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20928, 47, 221, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20929, 47, 222, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20930, 47, 223, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20931, 47, 224, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20932, 47, 220, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20933, 47, 250, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20934, 47, 251, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20935, 47, 252, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20936, 47, 253, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20937, 47, 254, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20938, 47, 298, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);
INSERT INTO `t_sys_role_operation` VALUES (20939, 47, 300, NULL, NULL, 0, '2019-07-03 16:51:59', 1, NULL, NULL);

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '所属部门',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户手机',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `flag` int(11) NULL DEFAULT NULL COMMENT '来源标记',
  `relation_user_id` int(11) NULL DEFAULT NULL COMMENT '关联用户id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '用户名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `vender` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '厂家',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 1, 'admin', '13843838438', '管理员', '$2a$10$4NkfgsaSE35HXj0J6YK/B.vPgsAPcXoNzXv.yo1Lo.8Rs4iuSsK4y', NULL, NULL, 1, NULL, 0, '2019-06-21 09:30:12', NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_user` VALUES (74, 1, '123456', '13211111111', '1111112', '$2a$10$DHU6NLiw1jaIndggewHX6ezm0Vk6fH.L27LlpR9TZOFpexumV71NW', NULL, NULL, 1, '', 1, '2019-06-23 09:35:20', 1, NULL, NULL, '');
INSERT INTO `t_sys_user` VALUES (75, 1, 'test', '18736262234', '测试', '$2a$10$8NDsN8dM5gXPbqY7.LkHb.k3yY2q3Bn0gnnjru1idUPbF7m8F4aDe', NULL, NULL, 1, '', 1, '2019-06-26 08:57:25', 1, NULL, NULL, '合肥海智');
INSERT INTO `t_sys_user` VALUES (76, 1, 'test', '18374277234', '测试', '$2a$10$knfTsjjWzLV.EsEvvmL/BucH4d7BFHGy.O/wiJerrBjqcDcFLuada', NULL, NULL, 1, '11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111', 0, '2019-06-26 08:58:10', 1, NULL, NULL, '大连腾屹');
INSERT INTO `t_sys_user` VALUES (77, 4, 'sw001', '13212345678', '商务测试', '$2a$10$3CW.FBgmxFk/TD7I8gIrLunHgIN8tQFxIsiXrmySJtSC3dnyv2EPq', NULL, NULL, 1, '', 0, '2019-07-02 17:34:14', 1, NULL, NULL, '大连腾屹');

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `org_id` int(11) NULL DEFAULT NULL COMMENT '所属组织',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES (62, 1, 2, 1, NULL, NULL, 0, '2018-10-15 18:17:11', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (75, 74, 47, 1, NULL, NULL, 1, '2019-06-23 09:35:20', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (76, 74, 2, 1, NULL, NULL, 1, '2019-06-23 09:35:20', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (77, 74, 47, 1, NULL, NULL, 1, '2019-06-23 09:35:55', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (78, 74, 2, 1, NULL, NULL, 1, '2019-06-23 09:35:55', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (79, 74, 47, 1, NULL, NULL, 0, '2019-06-23 09:38:56', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (80, 75, 47, 1, NULL, NULL, 1, '2019-06-26 08:57:25', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (81, 75, 47, 1, NULL, NULL, 0, '2019-06-26 08:57:47', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (82, 76, 47, 1, NULL, NULL, 1, '2019-06-26 08:58:10', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (83, 76, 47, 1, NULL, NULL, 0, '2019-06-27 15:58:56', 1, NULL, NULL);
INSERT INTO `t_sys_user_role` VALUES (84, 77, 48, 1, NULL, NULL, 0, '2019-07-02 17:34:14', 1, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
