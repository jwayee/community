CREATE TABLE notification
  (
      id bigint AUTO_INCREMENT PRIMARY KEY,
      notifier bigint NOT NULL,
      notifier_name VARCHAR(100) NOT NULL,
      receiver bigint NOT NULL,
      out_id bigint NOT NULL,
      out_title varchar(256) NOT NULL,
      type int DEFAULT 0,
      gmt_create bigint DEFAULT 0,
      status int default 0
  );