CREATE TABLE comment
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  parent_id BIGINT NOT NULL,
  type int NOT NULL,
  commentator int NOT NULL,
  content varchar(1024),
  gmt_create BIGINT NOT NULL,
  gmt_modified BIGINT NOT NULL,
  like_count BIGINT DEFAULT 0

);