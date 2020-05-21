use liveTimetable;

select * from weekTimetable;

select distinct w.courseCode,c.courseName,f.facultyName,w.hallNo from weekTimetable w
inner join course c
on c.courseCode = w.courseCode
inner join faculty f
on w.facultyCode = f.facultyCode
where c.courseCode = "COM209T";

desc course;

create table courseRequirements(
courseCode varchar(12),
requirements varchar(50),
foreign key(courseCode) references course(CourseCode));

desc courseRequirements;

select * from course;

insert into courseRequirements values("COM210P","Laptop");
insert into courseRequirements values("COM212P","Laptop");
insert into courseRequirements values("COM212P","MySQL Software");

select * from courseRequirements;

select * from faculty;

use liveTimetable;

show tables;
desc weekTimetable;

show create table weekTimetable;

create table updateTimetable(
Sno int(5) primary key auto_increment,
batchNo int(3) not null,
slotNo int(2) not null,
courseCode varchar(12) not null,
facultyCode varchar(5) not null,
hallNo varchar(5) not null,
`date` date not null,
flag varchar(2) not null,
rescheduleId int(5),
foreign key(batchNo) references batch(batchNo),
foreign key(slotNo) references slot(slotNo),
foreign key(courseCode) references course(courseCode),
foreign key(facultyCode) references faculty(facultyCode),
foreign key(hallNo) references hall(hallNo),
foreign key(rescheduleId) references updateTimetable(Sno));

use liveTimetable;
desc updateTimetable;

select * from updateTimetable;

