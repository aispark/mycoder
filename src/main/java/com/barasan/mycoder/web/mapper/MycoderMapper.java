package com.barasan.mycoder.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 테이블의 컬럼 정보 및 릴레이션을 조회하기 위한 Class 입니다.
 */
@Mapper
public interface MycoderMapper {
  // 컬럼 정보와 데이터 타입을 조회 한다.
  public List<Map<String, String>> selectColumnList(Map<String, String> paramMap) throws Exception;

  // primary key column을 조회한다.
  public String selectPrimaryColumn(Map<String, String> paramMap) throws Exception;

  // 테이블 comment를 조회 한다.
  public String selectTableComment(Map<String, String> paramMap) throws Exception;

  // 조회조건으로 사용할 컬럼을 조회 한다.
  public List<Map<String, String>> selectSearchConditionList(Map<String, String> paramMap) throws Exception;

}