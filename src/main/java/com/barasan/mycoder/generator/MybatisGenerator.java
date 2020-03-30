package com.barasan.mycoder.generator;

import java.util.ArrayList;
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
 * Mybatis curd query 파일을 생성 합니다.
 */
@Component
public class MybatisGenerator implements Generator {

  @Autowired
  Boilerplate boilerplate;

  @Autowired
  MycoderService mycoderService;

  @Autowired
  BoilerplateProperties properties;

  List<String> invalidColumnList = new ArrayList<>();

  public void build() throws Exception {

    // VO를 Map 타입으로 변환한다.
    Map<String, String> paramMap = BeanUtils.describe(properties);
    paramMap.put("packageNm", properties.getPackageNm()); // 업무명
    paramMap.put("classNm", StringUtil.upperFirstLetter(properties.getPackageNm())); // 업무명 첫글자는 대문자로

    // 컬럼 목록을 조회 한다.
    List<Map<String, String>> list = mycoderService.selectColumnList(paramMap);
    String columns = list.stream().map(item -> item.get("columnName")).collect(Collectors.joining(", "));
    paramMap.put("columns", columns); // 예) a, b, c ...

    // mybatis binding 포맷으로 변환
    String bindColumns = list.stream().map(item -> StringUtil.bindCase(StringUtil.camelCase(item.get("columnName"))))
        .collect(Collectors.joining(", "));
    paramMap.put("bindColumns", bindColumns); // 예) #{a}, #{b}, #{c} ...

    // 조회 컬럼 목록을 조회 한다.
    List<Map<String, String>> searchColumnList = mycoderService.selectSearchConditionList(paramMap);
    String conditions = searchColumnList.stream()
        .map(item -> conditionTemplate(item.get("columnName"), item.get("searchColumnName")))
        .collect(Collectors.joining("\n"));

    paramMap.put("conditions", conditions); // 조회 조건

    // update query나 상세 query의 조건으로 사용하기 위해 primary column을 조회 한다.
    String primaryColumn = mycoderService.selectPrimaryColumn(paramMap); // primary 컬럼
    paramMap.put("primaryColumn", primaryColumn); // primary 컬럼
    paramMap.put("primaryId", StringUtil.camelCase(primaryColumn)); // primary 컬럼 camel case
    paramMap.put("bindPrimaryId", StringUtil.bindCase(paramMap.get("primaryId"))); // primary 컬럼 바인딩 case
    paramMap.put("sequenceNm", properties.getSequenceNm()); // 시퀀스명

    // update 쿼리에 제거될 컬럼 목록을 설정한다.
    invalidColumnList.add(primaryColumn);
    invalidColumnList.add("regist_id");
    invalidColumnList.add("regist_dt");

    // update 쿼리 컬럼 구문을 생성한다. primary 컬럼 및 제외할 컬럼을 설정한다.
    String updateColumns = list.stream().filter(item -> !isInvalidColumn(item.get("columnName"))).map(item -> {
      String columnName = item.get("columnName");
      return columnName + " = " + StringUtil.bindCase(StringUtil.camelCase(columnName));
    }).collect(Collectors.joining(", \n\t\t\t"));

    paramMap.put("updateColumns", updateColumns); // 예) food_nm_kr = #{foodNmKr}, food_nm_en = #{foodNmEn} ...

    // template 파일명
    String template = "Mybatis-Template.xml";

    // boilerplate 생성 경로
    String destination = properties.getMybatisPath() + "/" + properties.getPackageNm() + "/"
        + StringUtil.upperFirstLetter(properties.getPackageNm()) + "-Mapper.xml";

    // boilerplate 생성
    boilerplate.generate(template, paramMap, destination);
  }

  /**
   * mybatis 조회 조건 template parameter값이 존재 할 때 and 조건을 생성 합니다.
   * 
   * @param columnName
   * @param searchColumnName
   * @return
   */
  private String conditionTemplate(String columnName, String searchColumnName) {
    searchColumnName = StringUtil.camelCase(searchColumnName);
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t\t\t<if test=\"" + searchColumnName + " != null and " + searchColumnName + " != ''\">\n");
    sb.append("\t\t\t\tAND a." + columnName + " LIKE '%' || TRIM(#{" + searchColumnName + "}) || '%'\n");
    sb.append("\t\t\t\t</if>");
    return sb.toString();
  }

  /**
   * primary column 같은 건 update 하면 안되므로 update 쿼리에 invalid한 컬럼인지 검증
   * 
   * @param columnName
   * @return
   */
  private boolean isInvalidColumn(String columnName) {
    return invalidColumnList.stream().anyMatch(item -> item.equals(columnName));
  }
}