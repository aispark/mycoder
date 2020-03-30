package com.barasan.mycoder.generator;

import java.util.Map;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;
import com.barasan.mycoder.generator.helper.StringUtil;
import com.barasan.mycoder.generator.helper.Boilerplate;
import com.barasan.mycoder.web.service.MycoderService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SpringMvcGenerator
 */
@Component
public class SpringMvcGenerator implements Generator {

  @Autowired
  Boilerplate boilerplate;

  @Autowired
  MycoderService mycoderService;

  @Autowired
  BoilerplateProperties properties;

  public void build() throws Exception {

    // 컨트롤러 생성
    buildMvc("controller", "web");

    // 서비스 생성
    buildMvc("service", "service");

    // mapper 생성
    buildMvc("mapper", "mapper");

  }

  // type은 class명, 변수명, boilerplate를 가져오는데 사용된다.
  // packageType은 class의 package를 정의하는데 사용된다.
  public void buildMvc(String type, String packageType) throws Exception {
    // VO를 Map 타입으로 변환한다.
    Map<String, String> paramMap = BeanUtils.describe(properties);

    // controller 생성
    paramMap.put("classPackage", properties.getPackageNm() + "." + packageType); // 예) package
                                                                                 // com.barasan.boilerplate.web;
    paramMap.put("packageNm", properties.getPackageNm());
    paramMap.put("classNm", StringUtil.upperFirstLetter(properties.getPackageNm()));

    // template 파일명
    String template = StringUtil.upperFirstLetter(type) + "-Template.java";

    // boilerplate 생성 경로
    String destination = properties.getJavaPath() + "/" + properties.getProjectPackage().replace(".", "/") + "/"
        + properties.getPackageNm() + "/" + packageType + "/" + StringUtil.upperFirstLetter(properties.getPackageNm())
        + StringUtil.upperFirstLetter(type) + ".java";

    // boilerplate 생성
    boilerplate.generate(template, paramMap, destination);
  }
}