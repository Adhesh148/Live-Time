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

select * from userLog;
desc batch;

drop table studentRecord;
create table studentRecord(
rollNo varchar(11) primary key,
loginId int(11),
phone bigint,
emailId varchar(50),
DOB date,
foreign key(loginId) references user(id)
);

desc studentRecord;

insert into studentRecord values("COE18B001",2,9011114765,null,null);

select * from studentRecord;

select * from userLog;

select count(*) as cnt from user where username = 'COE18B01';

desc course;

create table courseAbbreviation(
sno int(11) primary key,
userId int(11) not null,
courseCode varchar(12) not null,
abbrv varchar(20) not null,
foreign key(userId) references user(id),
foreign key(courseCode) references course(courseCode)
);
alter table courseAbbreviation modify column sno int(11) auto_increment;
desc courseAbbreviation;

insert into courseAbbreviation (userId,courseCode,abbrv) values(3,"COM209T","DAA");

select (@n:=@n+1) as sno, courseCode,courseName,abbrv from 
(select distinct w.courseCode,p.courseName,a.abbrv from weekTimetable w
inner join course p
on p.courseCode = w.courseCode
left join courseAbbreviation a
on a.courseCode = p.courseCode and a.userId = 2
where batchNo = 1) t1
join (select @n:=0) n;

select count(*) as cnt from courseAbbreviation where courseCode = "COM209T" and userId = 3;

select ifnull(abbrv,''),courseCode as abbrv from courseAbbreviation where userId = 3;

show tables;

