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
gsipa 기반 template이 제공 됩니다.  
template을 참고하여 각각의 프로젝트에 맞게 수정 후 사용하세요.

# resources

application.properties 치환 되어야 할 프로러티를 정의 합니다.

```
#template패키지를 나누는 때와 table의 메타 데이터를 조회 한때 사용 된다.
boilerplate.database-nm

#업무명칭 url의 prefix 및 class명에 사용 된다.
boilerplate.package-nm

#boilerplate를 생성할 테이블
boilerplate.table-nm

#대상 테이블의 시퀀스명
boilerplate.sequence-nm

#mybatis boilerplate가 위치할 root 경로
boilerplate.mybatis-path

#java boilerplate가 위치할 root 경로
boilerplate.java-path

#java의 prefix package
boilerplate.project-package

#jsp boilerplate가 위치할 root 경로
boilerplate.jsp-path
```

# test

DemoApplicationTests.java로 test 목록을 확인해 주세요.  
실제로 개발중인 경로에 boilerplate 생성하지 마세요.  
동일 파일이 있을 경우 **덮어쓰기** 됩니다.  
소스를 날리는 상항이 발생하지 않길
