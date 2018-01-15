create database dbchatroom;/*创建数据库*/

--创建用户表
create table ChatUser(username CHAR(20) PRIMARY KEY, password CHAR(64) NOT NULL, nickname VARCHAR(20), sex CHAR(2));

create view ViewUserToChat as select username, password, nickname, sex from ChatUser;
