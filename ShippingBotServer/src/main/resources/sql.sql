-- create table lover (id bigint not null unique ,
-- name varchar(30) not null ,
-- gender varchar(30) not null ,
-- preference varchar(10) default 'ALL' not null ,
-- description text default 'Описания нет' not null, primary key (id));
--
--
-- create table likes (one bigint, two bigint, primary key (one, two),
-- foreign key (one) references lover(id) on delete cascade on update no action,
-- foreign key (two) references lover(id) on delete cascade on update no action );

-- create table dislikes (whoId bigint, targetId bigint, primary key (whoId, targetId),
--                        foreign key (whoId) references lover(id) on delete cascade on update no action,
--                        foreign key (targetId) references lover(id) on delete cascade on update no action );

-- select one, two, l.name as who_liked, l2.name as was_liked from likes inner join lover l on l.id = likes.one
-- inner join lover l2 on l2.id = likes.two

-- insert into lover (id, name, gender, preference, description) values (310384389, 'Антон', 'Man', 'Girl', 'Clever boy');

-- select * from lover