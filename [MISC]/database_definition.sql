begin;

drop table if exists movies;
create table movies
	(movieID integer primary key autoincrement,
	movieTitle varchar(255),
	isRented char(1) check(isRented = 'y' or isRented = 'n'));

insert into movies (movieTitle, isRented)
values('The Matrix', 'n');

insert into movies (movieTitle, isRented)
values('The Matrix Reloaded', 'n');

insert into movies (movieTitle, isRented)
values('The Matrix Revolutions', 'n');

commit;
