package com.barasan.mycoder.generator.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 소스 파일을 생성 합니다.
 */
@Component
public class FileWriter {

  Logger logger = LoggerFactory.getLogger(FileWriter.class);

  @Autowired
  BoilerplateProperties properties;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  /**
   * 데이터를 파일로 생성 합니다.
   * 
   * @param dest            생성 될 경로
   * @param bindingContents 내용
   */
  public void write(String destination, String bindingContents) {

    destination = properties.getGenerateRootPath() + "/" + activeProfile + destination;
    File f = new File(destination);

    // 경로를 생성 한다.
    f.getParentFile().mkdirs();

    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8));
      bw.write(bindingContents);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        bw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    logger.info("파일 생성 경로: " + destination);
  }
}