package com.barasan.mycoder.generator.domain;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * application.properties의 boilerplate로 시작되는 property를 VO로 생성합니다.
 */
@Component
@ConfigurationProperties("boilerplate") // prefix 지정자를 받음
@Validated
@Data
public class BoilerplateProperties {

  // DBMS oracle, postgre, mysql, mariadb
  @NotEmpty
  private String dbms;

  // 데이터베이스명
  @NotEmpty
  private String databaseNm;

  // 업무별 구분자
  @NotEmpty
  private String packageNm;

  // 대상 테이블 crud query을 생성한다.
  @NotEmpty
  private String tableNm;

  // unique key를 조회하기 위한 시퀀스
  @NotEmpty
  private String sequenceNm;

  // mybatis파일이 생성 될 root 경로
  @NotEmpty
  private String mybatisPath;

  // java 파일이 생성 될 root 경로
  @NotEmpty
  private String javaPath;

  // java 파일의 prefix package
  @NotEmpty
  private String projectPackage;

  // jsp 파일이 생성 될 root 경로
  @NotEmpty
  private String jspPath;

  // 자동생성 루트 경로
  @NotEmpty
  private String generateRootPath;

}
