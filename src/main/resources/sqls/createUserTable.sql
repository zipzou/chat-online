create database dbchatroom;/*创建数据库*/

--创建用户表
create User(username CHAR(20) PRIMARY KEY, password NOT NULL, nickname VARCHAR(20), sex CHAR(2));