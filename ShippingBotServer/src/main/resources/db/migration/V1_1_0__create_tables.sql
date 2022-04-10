create table lover
(
    id          bigint                             not null unique,
    name        varchar(30)                        not null,
    gender      varchar(30)                        not null,
    preference  varchar(10) default 'all'          not null,
    description text        default 'Описания нет' not null,
    primary key (id)
);


create table likes
(
    one bigint,
    two bigint,
    primary key (one, two),
    foreign key (one) references lover (id) on delete cascade on update no action,
    foreign key (two) references lover (id) on delete cascade on update no action
);

create table dislikes
(
    whoId    bigint,
    targetId bigint,
    primary key (whoId, targetId),
    foreign key (whoId) references lover (id) on delete cascade on update no action,
    foreign key (targetId) references lover (id) on delete cascade on update no action
);

create table watch_lover
(
    id      bigint unique,
    counter bigint,
    primary key (id),
    foreign key (id) references lover (id) on delete cascade on update no action
);