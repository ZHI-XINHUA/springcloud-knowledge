/*
Navicat MySQL Data Transfer

Source Server         : 192.168.3.108(mysql)
Source Server Version : 50720
Source Host           : 192.168.3.108:3306
Source Database       : microserver

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-04-03 08:29:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for my_mood
-- ----------------------------
DROP TABLE IF EXISTS `my_mood`;
CREATE TABLE `my_mood` (
  `id` varchar(64) NOT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `praise_num` int(10) DEFAULT NULL,
  `publish_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of my_mood
-- ----------------------------
INSERT INTO `my_mood` VALUES ('ae8aebe7-6c44-4847-a26d-d1df7a409414', '今天天气很好', 'e98b0351-9fa9-4de7-92e9-cf5c79c663ef', '101', '2019-03-01 00:00:00');
INSERT INTO `my_mood` VALUES ('e47db91c-45f3-48de-884a-e57938308f64', '卧槽，坑爹', 'e98b0351-9fa9-4de7-92e9-cf5c79c663ef', '99', '2019-03-01 00:00:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userid` varchar(64) NOT NULL,
  `usercode` varchar(30) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `telephone` varchar(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `state` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('e98b0351-9fa9-4de7-92e9-cf5c79c663ef', 'zs', '张三', 'xxvcvc', 'dfdf@dd.com', null, null, null, '0');
INSERT INTO `user` VALUES ('fb0abbba-525a-46f3-9cd0-12adf3299203', 'lisi', '李四', 'xxvcvc', 'dfdf@dd.com', null, null, null, null);
