create table question
(
    id bigint auto_increment primary key,
    title varchar(50),
    description text,
    gmt_create bigint default 0,
    gmt_modified bigint default 0,
    creator bigint not null,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    tag varchar(256)
);