drop table post if exists cascade;
create table post
(
    id    Long,
    content    varchar(100),
    view_cnt integer not null default 0,
    primary key (id)
);
