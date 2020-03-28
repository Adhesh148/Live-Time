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




 

