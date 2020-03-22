## 码匠社区

## 部署
###依赖
- git
- JDK
- Maven
- MySQL
## 步骤
yum update:更新资源
yum install git
mkdir App
cd App
#下载代码
git clone https://github.com/Jway12580/community.git
#安装maven
yum install maven
#查看版本
mvn -v  
java -version
#编译打包
mvn compile package
cp src/main/resources/application.properties src/main/resources/application-production.properties
#打包新的配置
mvn package
#运行备份文件production.properties
java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar

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
[markdown插件](http://editor.md.ipandao.com/)
[aliyun java sdk](https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.4.1.698d59aaCZEa3o)
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
CREATE TABLE notification
  (
      id bigint AUTO_INCREMENT PRIMARY KEY,
      notifier bigint NOT NULL,
      notifier_name VARCHAR(100) NOT NULL,
      receiver bigint NOT NULL,
      out_id bigint NOT NULL,
      out_title varchar(256) NOT NULL,
      type int DEFAULT 0,
      gmt_create bigint DEFAULT 0
  );

```

```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
