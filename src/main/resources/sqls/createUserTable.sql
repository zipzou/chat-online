create database dbchatroom;/*创建数据库*/

--创建用户表
create table ChatUser(username CHAR(20) PRIMARY KEY, password CHAR(20) NOT NULL, nickname VARCHAR(20), sex CHAR(2));
