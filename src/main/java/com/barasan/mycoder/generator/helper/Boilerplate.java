package com.barasan.mycoder.generator.helper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Template 파일을 읽어와서 parameter mapping 처리 후 대상 경로에 boilerplate 파일을 생성 합니다.
 */
@Component
public class Boilerplate {

  Logger logger = LoggerFactory.getLogger(Boilerplate.class);

  @Autowired
  FileReader fileReader;

  @Autowired
  FileWriter fileWriter;

  /**
   * Template 파일을 읽어와서 parameter mapping 처리 후 대상 경로에 boilerplate 파일을 생성 합니다.
   * 
   * @param template    템플릿 파일명
   * @param paramMap    파라메터
   * @param destination 파일이 생성 될 경로
   */
  public void generate(String template, Map<String, String> paramMap, String destination) {
    // Template 내용을 읽어 온다.
    String contents = fileReader.getTemplateContents(template);

    // paramMap과 치환한다.
    String bindingContents = fileReader.bindingVariable(contents, paramMap);

    // 파일 생성
    fileWriter.write(destination, bindingContents);
  }

}