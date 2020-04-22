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







 

