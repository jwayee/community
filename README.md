## 码匠社区

## 资料
[spring 文档](https://spring.io/guides)
[spring web](https://spring.io/guides/gs/serving-web-content/)
[es 社区](https://elasticsearch.cn/explore)
[bootstap](https://v3.bootcss.com/getting-started)
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
## 工具
[git](https://git-scm.com/download)
[Visual Paradigm(画流程图)](https://www.visual-paradigm.com)
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)
[lombok](https://www.projectlombok.org/)

##脚本
```sql
CREATE TABLE USER(
    ID INT auto_increment primary key NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN CHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
)
```
```sql
CREATE TABLE comment
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL,
    type int NOT NULL,
    commentator int NOT NULL,
    gmt_create BIGINT NOT NULL,
    gmt_modified BIGINT NOT NULL,
    like_count BIGINT DEFAULT 0
);
```

```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
