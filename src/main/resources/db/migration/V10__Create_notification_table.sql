CREATE TABLE notification
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    notifier bigint NOT NULL,
    receiver bigint NOT NULL,
    out_id bigint NOT NULL,
    gmt_create bigint NOT NULL,
    type int NOT NULL,
    status int DEFAULT 0
);