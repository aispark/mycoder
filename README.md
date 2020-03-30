# 개발 스펙

### jdk 1.8 이상

- spring boot 2.x 부터는 jdk 1.8 이상 지원
- functional interface와 stream api 사용으로 jdk 1.8 이상 필수

### spring boot 2.2.5

- spring boot로 빠른 setup

### maven

- build 및 dependency 관리

# mycoder

spring mvc boilerplate code를 생성해 주는 도구 입니다.  
자동생성할 template을 작성하여야 합니다.
template의 \${변수}는 application-{profile}.properties에 정의한 데이터로 치환됩니다.

# resources

application.properties 공통 프로퍼티

```
# profile 명
spring.profiles.active=gsipa

# spring boot properties
server.port=5000
spring.datasource.hikari.maximum-pool-size=4
spring.mvc.view.prefix=/WEB-INF/views
spring.mvc.view.suffix=.jsp

# logging
logging.level.com.barasan.mycoder=info

# 로그 구문강조 기능
spring.output.ansi.enabled=ALWAYS
```

application-{profile}.properties 치환 되어야 할 프로러티를 정의 합니다.

```
# db connection 정보
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

#패키지명, 메소드명, class명을 정의할 때 사용 된다.
boilerplate.package-nm=

# 자동생성 루트 경로
boilerplate.generate-root-path=

# dbms 종류 예) oracle, postgre, mysql 등등
boilerplate.dbms=

# 테이블 메타 데이터 조회시 조건으로 사용 된다.
boilerplate.database-nm=

# 지정한 테이블의 crud query를 생성한다.
boilerplate.table-nm=

# unique key를 조회하기 위한 시퀀스
boilerplate.sequence-nm=

# mybatis파일이 생성 될 root 경로
boilerplate.mybatis-path=

# java 파일이 생성 될 root 경로
boilerplate.java-path=

# jsp 파일이 생성 될 root 경로
boilerplate.jsp-path=

# java 파일의 prefix package
boilerplate.project-package=
```

# test

DemoApplicationTests.java로 test 목록을 확인해 주세요.  
실제로 개발중인 경로에 boilerplate 생성하지 마세요.  
동일 파일이 있을 경우 **덮어쓰기** 됩니다.  
소스를 날리는 상항이 발생하지 않길
