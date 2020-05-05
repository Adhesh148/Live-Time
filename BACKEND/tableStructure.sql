show databases;

create database liveTimetable;
use liveTimetable;

-- create table department(
-- departmentCode varchar(5) primary key,
-- departmentName varchar(50) unique not null);

-- drop table department;

-- desc department;

create table faculty(
facultyCode varchar(5) primary key,
facultyName varchar(50));


desc faculty;

create table hall(
hallNo varchar(5) primary key,
hallStrength int(4));

desc hall;

create table slot(
slotNo int(2) primary key,
`from` time,
`to` time);

desc slot;

create table `day`(
dayNo int(2) primary key,
`day` varchar(10));

desc day;

create table course(
courseCode varchar(12) primary key,
courseName varchar(70) not null);

desc course;

create table batch(
batchNo int(3) primary key,
batchCode varchar(5) not null,
batchName varchar(70) not null,
`year` year, 
specialization varchar(50));

alter table batch add unique key(batchCode);

desc batch;

create table weekTimetable(
Sno int(5) primary key,
batchNo int(3),
slotNo int(2),
courseCode varchar(12),
facultyCode varchar(5),
hallNo varchar(5),
dayNo int(2),
foreign key(batchNo) references batch(batchNo),
foreign key(slotNo) references slot(slotNo),
foreign key(courseCode) references course(courseCode),
foreign key(facultyCode) references faculty(facultyCode),
foreign key(hallNo) references hall(hallNo),
foreign key(dayNo) references `day`(dayNo));

desc weekTimetable;

show tables;
desc hall;

show tables;
desc weekTimetable;

-- Adding unique values in weekTimetable table to prevent overlapping of slots ..
alter  table weekTimetable
add unique key(slotNo,batchNo,dayNo);

alter  table weekTimetable
add unique key(slotNo,dayNo,hallNo);

-- Adding auto increment to serial no in weekTimetable table
select * from weekTimetable; 

desc weekTimetable;

alter table weekTimetable change Sno Sno int(5) auto_increment;

use liveTimetable;

create table projectAssign(
Sno int(40) primary key auto_increment,
postedDate date not null,
courseCode varchar(12) not null,
facultyCode varchar(5) not null,
title varchar(2000),
description varchar(10000),
batchNo int(3) not null,
marks int(4),
dueDate date,
dueTime time,
topic varchar(1000),
foreign key(batchNo) references batch(batchNo),
foreign key(courseCode) references course(courseCode),
foreign key(facultyCode) references faculty(facultyCode));

alter table projectAssign change Sno Sno int(40) auto_increment;


desc projectAssign;

-- Create a new table to store blob attachments of the projects
create table attachment(
Sno int(40) primary key auto_increment,
Pno int(40) not null,
attach BLOB,
foreign key(Pno) references projectAssign(Sno));

insert into attachment (Pno) values(1);
select last_insert_id() as auto;

select * from attachment;

use liveTimetable;

-- Changing structure of attachment table to store the format of the attachment as well.alter
alter table attachment
add `format` varchar(10);

desc attachment;

update attachment set `format` = "pdf" where Sno = 6; 
update attachment set `format` = "jpg" where Sno = 5;
update attachment set `format` = "png" where Sno = 4;

use liveTimetable;

create table mailingList(
Sno int primary key auto_increment,
name varchar(50) not null,
email varchar(50) not null,
`group` varchar(50));

desc mailingList;

insert into mailingList values(1,"Adhesh R","coe18b001@iiitdm.ac.in","COE18");

select * from mailingList;

alter table mailingList add unique(email);

select distinct `group` from mailingList;
 
select distinct email from mailingList where `group` like '%COE18%' or `group` like '%CED15%';

desc batch;
desc `user`;
alter table `user` add column batchNo int(3);

alter table user add
foreign key(batchNo) references batch(batchNo);

desc user;
alter table user drop primary key;
alter table user add primary key(userName);

alter table user add unique(id);

select * from user;

insert into user values(3,1,"123","USER","COE18B001",1);

alter table user
alter batchNo drop default;

alter table user
add unique(userName);

drop table userLog;

create table userLog(
loginId int(10) primary key auto_increment,
userId int(11) not null,
loginTime datetime not null,
foreign key(userId) references user(id)
);

desc userLog;	

select * from userLog;

alter table userLog drop column logOutTime;

insert into userLog (userName,loginTime) values('Adhesh','');

use liveTimetable;

desc user;

alter table user drop primary key;
alter table user add primary key(id);

drop table report;

create table report(
id int(11) primary key auto_increment,
userId int(11) not null,
report varchar(10000) not null,
flag char(2) not null,
foreign key(userId) references user(id));

desc report;

select * from report;

use liveTimetable;

select * from user;	

select * from report;

alter table report add column postedDate datetime;

select * from courseAbbreviation;

use liveTimetable;

select * from updateTimetable order by Date desc;

select courseCode,slotNo from updateTimetable where batchNo = 1 and date = "2020-04-02" and flag = 'S' order by slotNo asc;

select * from weekTimetable;

select * from updateTimetable ;

alter table updateTimetable add column postedDate date;

select count(*) as cnt from updateTimetable where flag = "S" and postedDate >= "2020-05-04"and postedDate <= "2020-05-09";

select count(*) as cnt from userLog where loginTime >= "2020-05-04"and loginTime <= "2020-05-09";

alter table projectAssign add column teamSize int(11);


select * from projectAssign;

select * from attachment; 

