package com.barasan.mycoder.generator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;
import com.barasan.mycoder.generator.helper.StringUtil;
import com.barasan.mycoder.generator.helper.Boilerplate;
import com.barasan.mycoder.web.service.MycoderService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Jsp 목록 파일을 생성 합니다.
 */
@Component
public class JspListGenerator implements Generator {

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

    // 조회 조건 컬럼
    List<Map<String, String>> searchList = mycoderService.selectSearchConditionList(paramMap);
    paramMap.put("searchSection", searchTemplate(searchList));

    // 그리드 colGroup
    List<Map<String, String>> gridList = mycoderService.selectColumnList(paramMap);
    paramMap.put("colGroup", gridTemplate(gridList, primaryColumn));

    // template 파일명
    String template = "JspList-Template.jsp";

    // boilerplate 생성 경로
    String destination = properties.getJspPath() + "/" + properties.getPackageNm() + "/select"
        + StringUtil.upperFirstLetter(properties.getPackageNm()) + "List.jsp";

    // boilerplate 생성
    boilerplate.generate(template, paramMap, destination);
  }

  // 검색 조건을 생성한다.
  private String searchTemplate(List<Map<String, String>> searchList) {

    return IntStream.range(0, searchList.size()).limit(3).mapToObj(i -> {
      Map<String, String> item = searchList.get(i);
      StringBuilder sb = new StringBuilder();
      String searchColumnName = StringUtil.camelCase(item.get("searchColumnName"));

      // 한 row에 2개의 컬럼을 보여 주므로 div 태그를 2row 마다 삽입 한다.
      if (i % 2 == 0)
        sb.append("\t\t<div class=\"row\">\n");

      sb.append("\t\t\t<div class=\"col-md-6 mb-3\">\n");
      sb.append(String.format("\t\t\t\t<label  for=\"%s\">%s</label>\n", searchColumnName, item.get("columnComment")));
      sb.append(String.format(
          "\t\t\t\t<form:input class=\"form-control\" path=\"%s\" onkeydown=\"javascript:enterDown()\"/>\n",
          searchColumnName));
      sb.append("\t\t\t</div>\n");

      // 한 row에 2개의 컬럼을 보여 주므로 div 태그를 2row 마다 삽입 한다. 리스트의 마지막일 경우 삽입 한다.
      if (i == searchList.size() - 1 || i % 2 == 1)
        sb.append("\t\t</div>\n");

      return sb.toString();
    }).collect(Collectors.joining("\n"));
  }

  // 그리드 colGroup을 생성한다.
  private String gridTemplate(List<Map<String, String>> gridList, String primaryColumn) {
    return IntStream.range(0, gridList.size()).mapToObj(i -> {
      Map<String, String> item = gridList.get(i);
      return String.format(
          "\t\t{\\key:'%s',\t\tlabel:'%s',\t\ttitle: '%s',\t\twidth:'100',\t\ttooltip :'text',\t\talign:'center'%s}",
          StringUtil.camelCase(item.get("columnName")), item.get("columnComment"), item.get("columnComment"),
          bulidFormatter(item, i, primaryColumn));
    }).collect(Collectors.joining(",\n"));
  }

  // grid의 formatter를 생성 한다.
  private String bulidFormatter(Map<String, String> item, int index, String primaryColumn) {
    if (index == 0) {
      return ",\t\tformatter: 'checkbox'";
    } else if (index == 1) {
      return "\n\t\t\t, formatter: function() {\n"
          + "\t\t\t\treturn\\ \"<a class='cr-pt' href='#contents' data-toggle='modal' "
          + "data-target='#modal-popup-level1' onclick='javascript:fnSelect(\\\\\"\"+this.item."
          + StringUtil.camelCase(primaryColumn) + "+\"\\\\\", this)'>\" + this.item."
          + StringUtil.camelCase(item.get("columnName")) + " + \"</a>\"" + "\n\t\t\t}";
    } else {
      return "";
    }
  }
}