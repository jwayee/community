create table notification
  (
      id bigint auto_increment primary key,
      notifier bigint not null,
      notifier_name varchar(100) not null,
      receiver bigint not null,
      out_id bigint not null,
      out_title varchar(256) not null,
      type int default 0,
      gmt_create bigint default 0,
      status int default 0
  );