package com.barasan.mycoder.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;
import com.barasan.mycoder.generator.helper.StringUtil;
import com.barasan.mycoder.generator.helper.Boilerplate;
import com.barasan.mycoder.web.service.MycoderService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * VO를 생성하기 위한 클래스 입니다.
 */
@Component
public class ValueObjectGenerator implements Generator {

  @Autowired
  Boilerplate boilerplate;

  @Autowired
  MycoderService mycoderService;

  @Autowired
  BoilerplateProperties properties;

  public void build() throws Exception {

    // type은 class명, 변수명, boilerplate를 가져오는데 사용된다.
    // packageType은 class의 package를 정의하는데 사용된다.
    String type = "VO";
    String packageType = "service";

    // VO를 Map 타입으로 변환한다.
    Map<String, String> paramMap = BeanUtils.describe(properties);

    // controller 생성
    paramMap.put("classPackage", properties.getPackageNm() + "." + packageType); // 예) package
                                                                                 // com.barasan.boilerplate.web;
    paramMap.put("classNm", StringUtil.upperFirstLetter(properties.getPackageNm()));

    List<Map<String, String>> list = mycoderService.selectColumnList(paramMap);

    // 검색 조건 추가
    List<Map<String, String>> searchColumnList = mycoderService.selectSearchConditionList(paramMap);
    searchColumnList.stream().forEach(item -> {
      item.put("columnName", item.get("searchColumnName"));
      list.add(item);
    });

    // vo properties
    String columns = list.stream().map(item -> "\tprivate String " + StringUtil.camelCase(item.get("columnName")) + ";")
        .collect(Collectors.joining("\n"));
    paramMap.put("columns", columns);

    // setter, getter 메소드
    String setterGetter = list.stream()
        .map(item -> getterTemplate(item.get("columnName")) + "\n" + setterTemplate(item.get("columnName")))
        .collect(Collectors.joining("\n"));
    paramMap.put("setterGetter", setterGetter);

    // template 파일명
    String template = StringUtil.upperFirstLetter(type) + "-Template.java";

    // boilerplate 생성 경로
    String destination = properties.getJavaPath() + "/" + properties.getProjectPackage().replace(".", "/") + "/"
        + properties.getPackageNm() + "/" + packageType + "/" + StringUtil.upperFirstLetter(properties.getPackageNm())
        + StringUtil.upperFirstLetter(type) + ".java";

    // boilerplate 생성
    boilerplate.generate(template, paramMap, destination);
  }

  private String setterTemplate(String item) {
    String camelCaseItem = StringUtil.camelCase(item);
    return "\tpublic void set" + StringUtil.upperFirstLetter(camelCaseItem) + "(String " + camelCaseItem
        + ") {\n\t\tthis." + camelCaseItem + " = " + camelCaseItem + ";\n\t}";
  }

  private String getterTemplate(String item) {
    String camelCaseItem = StringUtil.camelCase(item);
    return "\tpublic String get" + StringUtil.upperFirstLetter(camelCaseItem) + "() {\n\t\treturn " + camelCaseItem
        + ";\n\t}";
  }
}