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

 Date: 07/08/2019 15:09:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_project_crane_alarm
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_alarm`;
CREATE TABLE `t_project_crane_alarm`  (
  `id` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `crane_id` int(11) NULL DEFAULT NULL COMMENT '塔吊id',
  `detail_id` int(11) NULL DEFAULT NULL COMMENT '实时数据ID',
  `alarm_id` int(11) NULL DEFAULT NULL COMMENT '报警id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `alarm_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报警内容',
  `is_del` int(11) NULL DEFAULT NULL COMMENT '删除标志',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `create_user` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人（处理人id）',
  `modify_user_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理意见',
  `is_handle` int(255) NULL DEFAULT NULL COMMENT '是否处理',
  `alarm_time` datetime(0) NULL DEFAULT NULL COMMENT '报警时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_cyclic_work_duration
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_cyclic_work_duration`;
CREATE TABLE `t_project_crane_cyclic_work_duration`  (
  `id` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `crane_id` int(11) NULL DEFAULT NULL COMMENT '主表id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '设备编号',
  `begin_height` decimal(12, 3) NULL DEFAULT NULL COMMENT '起始高度',
  `end_height` decimal(12, 3) NULL DEFAULT NULL COMMENT '终止高度',
  `begin_range` decimal(12, 3) NULL DEFAULT NULL COMMENT '起始幅度',
  `end_range` decimal(12, 3) NULL DEFAULT NULL COMMENT '终止幅度',
  `begin_moment` decimal(12, 3) NULL DEFAULT NULL COMMENT '起始力矩',
  `weight` decimal(12, 3) NULL DEFAULT NULL COMMENT '起始重量',
  `safety_weight` decimal(12, 3) NULL DEFAULT NULL COMMENT '安全吊重',
  `wind_speed` decimal(12, 3) NULL DEFAULT NULL COMMENT '风速',
  `begin_angle` decimal(12, 3) NULL DEFAULT NULL COMMENT '起始角度',
  `end_angle` decimal(12, 3) NULL DEFAULT NULL COMMENT '终止角度',
  `moment_percentage` decimal(12, 3) NULL DEFAULT NULL COMMENT '力矩百分比',
  `multiple_rate` decimal(12, 3) NULL DEFAULT NULL COMMENT '吊绳倍率',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束日期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `alarm_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `begin_rope_length` decimal(12, 3) NULL DEFAULT NULL,
  `end_rope_length` decimal(12, 3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_project_crane_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_project_crane_detail`;
CREATE TABLE `t_project_crane_detail`  (
  `id` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `crane_id` int(11) NULL DEFAULT NULL COMMENT '塔吊id',
  `device_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `driver` int(11) NULL DEFAULT NULL COMMENT '司机',
  `device_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `weight` decimal(12, 3) NULL DEFAULT NULL COMMENT '重量',
  `safety_weight` decimal(12, 3) NULL DEFAULT NULL COMMENT '安全重量',
  `moment` decimal(12, 3) NULL DEFAULT NULL COMMENT '力矩',
  `safety_moment` decimal(12, 3) NULL DEFAULT NULL COMMENT '安全力矩',
  `height` decimal(12, 3) NULL DEFAULT NULL COMMENT '高度',
  `range` decimal(12, 3) NULL DEFAULT NULL COMMENT '幅度',
  `moment_percentage` decimal(12, 3) NULL DEFAULT NULL COMMENT '力矩百分比',
  `rotary_angle` decimal(12, 3) NULL DEFAULT NULL COMMENT '回转角度',
  `tilt_angle` decimal(12, 3) NULL DEFAULT NULL COMMENT '倾斜角度',
  `wind_speed` decimal(12, 3) NULL DEFAULT NULL COMMENT '风速',
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
  `modify_user` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `crane_height` decimal(12, 3) NULL DEFAULT NULL,
  `rope_length` decimal(12, 3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
