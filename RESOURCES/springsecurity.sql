create database spring;
use liveTimetable;
create table user(
id int primary key,
active BOOLEAN,
password varchar(30) not null,
roles varchar(20),
userName varchar(30) not null);

insert into user values(1,true,"pass","ROLE_USER","user");

select * from user;