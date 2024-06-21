CREATE DATABASE chat;

CREATE TABLE [chat].[dbo].[chat_box](
	ID int ,
	Name_chat Nvarchar(225),
	ID_User_in_chat int,
	User_name Nvarchar(225),
	Permission Nvarchar(225)
);

CREATE TABLE [chat].[dbo].[chat_his](
	Chat_his_ID int IDENTITY(1,1) primary key,
	chat_id int,
	user_id int,
	user_name Nvarchar(225),
	message  NVARCHAR(225),
	TIME_send datetime
);

CREATE TABLE [chat].[dbo].[user](
	ID int IDENTITY(1,1) primary key,
	Name NVARCHAR(225),
	Email NVARCHAR(225),
	Pass  NVARCHAR(225),
);
