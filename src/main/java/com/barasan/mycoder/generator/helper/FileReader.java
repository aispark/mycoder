package com.barasan.mycoder.generator.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * 파일 내용을 읽기 위한 클래스 입니다.
 */
@Component
public class FileReader {

  @Autowired
  BoilerplateProperties properties;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  /**
   * Template 내용을 가져 온다.
   * 
   * @param fileNm 파일명
   * @return Template 내용
   * @throws Exception
   */
  public String getTemplateContents(String fileNm) {
    return getFileContent(getTemplateFile(fileNm));
  }

  /**
   * Template 파일을 가져 온다.
   * 
   * @param fileNm 파일명
   * @return Template 파일
   * @throws Exception
   */
  public File getTemplateFile(String fileNm) {
    Resource resource = new PathMatchingResourcePatternResolver()
        .getResource("classpath:generatortemplate/" + activeProfile + "/" + fileNm);

    File templateFile = null;
    try {
      templateFile = resource.getFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return templateFile;
  }

  /**
   * 경로에 해당하는 내용을 읽어 옵니다.
   * 
   * @param filePath 파일 경로
   * @return 파일 내용
   */
  public String getFileContent(String filePath) {
    return getFileContent(new File(filePath));
  }

  /**
   * 파일 내용를 읽어 옵니다.
   * 
   * @param file 파일
   * @return 파일 내용
   */
  public String getFileContent(File file) {
    final StringBuilder sb = new StringBuilder();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

      String line;

      while ((line = br.readLine()) != null) {
        sb.append(line);
        sb.append('\n');
      }
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null)
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    return sb.toString();
  }

  /**
   * paramMap의 key가 '변수명' 일 경우 source의 ${변수명}을 찾아 치환 합니다.
   * 
   * @param source   템플릿 내용
   * @param paramMap 파라메터
   * @return
   */
  public String bindingVariable(final String source, final Map<String, String> paramMap) {
    final Pattern pattern = Pattern.compile("\\$\\{\\w+?\\}");
    final Matcher m = pattern.matcher(source);

    final StringBuffer sb = new StringBuffer();

    while (m.find()) {
      if (m.group() != null) {
        final String key = m.group().replaceAll("\\$\\{(\\w+?)\\}", "$1");
        if (paramMap.containsKey(key) && paramMap.get(key) != null) {
          m.appendReplacement(sb, paramMap.get(key));
        } else {
          // m.appendReplacement(sb, "");
        }
      }
    }

    m.appendTail(sb);
    return sb.toString();
  }
}