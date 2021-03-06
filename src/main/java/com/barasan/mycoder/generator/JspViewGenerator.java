package com.barasan.mycoder.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;
import com.barasan.mycoder.generator.helper.Boilerplate;
import com.barasan.mycoder.generator.helper.StringUtil;
import com.barasan.mycoder.web.service.MycoderService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Jsp 상세 파일을 생성 합니다.
 */
@Component
public class JspViewGenerator implements Generator {

  @Autowired
  Boilerplate boilerplate;

  @Autowired
  MycoderService mycoderService;

  @Autowired
  BoilerplateProperties properties;

  public void build() throws Exception {

    // VO를 Map 타입으로 변환한다.
    Map<String, String> paramMap = BeanUtils.describe(properties);
    paramMap.put("packageNm", properties.getPackageNm()); // 업무명
    paramMap.put("classNm", StringUtil.upperFirstLetter(properties.getPackageNm())); // 업무명 첫글자는 대문자로

    String primaryColumn = mycoderService.selectPrimaryColumn(paramMap); // primary 컬럼
    paramMap.put("primaryColumn", primaryColumn); // primary 컬럼
    paramMap.put("primaryId", StringUtil.camelCase(primaryColumn)); // primary 컬럼 camel case

    String tableComment = mycoderService.selectTableComment(paramMap);
    paramMap.put("tableComment", tableComment); // 테이블 comment

    List<Map<String, String>> columnList = mycoderService.selectColumnList(paramMap);
    String columns = columnList.stream().map(item -> {
      String html = "\t\t\t\t\t<tr>\n"
          + String.format("\t\t\t\t\t\t<th scope=\"col\">%s</th>\n", item.get("columnComment")) + String
              .format("\t\t\t\t\t\t<td colspan=\"3\" id=\"%s\"></td>\n", StringUtil.camelCase(item.get("columnName")))
          + "\t\t\t\t\t</tr>";
      return html;
    }).collect(Collectors.joining("\n"));
    paramMap.put("columns", columns); // 컬럼

    // template 파일명
    String template = "JspView-Template.jsp";

    // boilerplate 생성 경로
    String destination = properties.getJspPath() + "/" + properties.getPackageNm() + "/select"
        + StringUtil.upperFirstLetter(properties.getPackageNm()) + "View.jsp";

    // boilerplate 생성
    boilerplate.generate(template, paramMap, destination);
  }

}