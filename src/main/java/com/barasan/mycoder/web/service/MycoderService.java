package com.barasan.mycoder.web.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.barasan.mycoder.web.mapper.MycoderMapper;

import org.springframework.stereotype.Service;

/**
 * MycoderService
 */
@Service
public class MycoderService {
  @Resource
  MycoderMapper mycoderMapper;

  /**
   * 컬럼 목록을 조회 합니다. (postgresql)
   * 
   * @param paramMap
   * @return
   * @throws Exception
   */
  public List<Map<String, String>> selectColumnList(Map<String, String> paramMap) throws Exception {
    return mycoderMapper.selectColumnList(paramMap);
  }

  /**
   * primary column을 조회 합니다.
   * 
   * @param paramMap
   * @return
   * @throws Exception
   */
  public String selectPrimaryColumn(Map<String, String> paramMap) throws Exception {
    return mycoderMapper.selectPrimaryColumn(paramMap);
  }

  /**
   * 테이블 comment를 조회 한다.
   * 
   * @param paramMap
   * @return
   * @throws Exception
   */
  public String selectTableComment(Map<String, String> paramMap) throws Exception {
    return mycoderMapper.selectTableComment(paramMap);
  }

  /**
   * 조회 조건 컬럼을 조회 합니다. 컬럼에 prefix로 'search'을 추가 합니다.
   * 
   * @param paramMap
   * @return
   * @throws Exception
   */
  public List<Map<String, String>> selectSearchConditionList(Map<String, String> paramMap) throws Exception {
    return mycoderMapper.selectSearchConditionList(paramMap);
  }

}