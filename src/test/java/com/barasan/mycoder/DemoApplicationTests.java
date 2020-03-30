package com.barasan.mycoder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.barasan.mycoder.generator.JspAddViewGenerator;
import com.barasan.mycoder.generator.JspListGenerator;
import com.barasan.mycoder.generator.JspViewGenerator;
import com.barasan.mycoder.generator.MybatisGenerator;
import com.barasan.mycoder.generator.SpringMvcGenerator;
import com.barasan.mycoder.generator.ValueObjectGenerator;
import com.barasan.mycoder.web.service.MycoderService;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Resource
	MycoderService mycoderService;

	@Resource
	MybatisGenerator mybatisGenerator;

	@Resource
	SpringMvcGenerator springMvcGenerator;

	@Resource
	ValueObjectGenerator valueObjectGenerator;

	@Resource
	JspListGenerator jspListGenerator;

	@Resource
	JspViewGenerator jspViewGenerator;

	@Resource
	JspAddViewGenerator jspAddViewGenerator;

	@Resource
	SqlSessionFactory sqlSessionFactory;

	Map<String, String> paramMap;

	/**
	 * 테스트 마다 실행된다.
	 */
	@BeforeEach
	void init() {
		paramMap = new HashMap<>();
		paramMap.put("databaseNm", "gsipa2020"); // TABLE_CATALOG
		paramMap.put("tableNm", "t_gsipa_spcprd_m"); // 테이블명
	}

	/**
	 * table column 조회 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void selectColumnListTest() throws Exception {

		List<Map<String, String>> list = mycoderService.selectColumnList(paramMap);
		list.stream().forEach(item -> System.out.println(item));
	}

	/**
	 * primary column 조회 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void selectPrimaryTest() throws Exception {

		String primary = mycoderService.selectPrimaryColumn(paramMap);
		System.out.println(primary);
	}

	/**
	 * 통합 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void integrateTest() throws Exception {
		mybatisGenerator.build();
		springMvcGenerator.build();
		valueObjectGenerator.build();
		jspListGenerator.build();
		jspViewGenerator.build();
		jspAddViewGenerator.build();
	}

	/**
	 * crud 쿼리 mybatis 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void mybatisTest() throws Exception {
		mybatisGenerator.build();
	}

	/**
	 * Controller, Service, Mapper 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void mvcTest() throws Exception {
		springMvcGenerator.build();
	}

	/**
	 * VO 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void voTest() throws Exception {
		valueObjectGenerator.build();
	}

	/**
	 * jsp 목록 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void jspListTest() throws Exception {
		jspListGenerator.build();
	}

	/**
	 * jsp view 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void jspViewTest() throws Exception {
		jspViewGenerator.build();
	}

	/**
	 * jsp add view 생성 테스트
	 * 
	 * @throws Exception
	 */
	@Test
	void jspAddViewTest() throws Exception {
		jspAddViewGenerator.build();
	}

	/**
	 * mybatis 테스트
	 */
	@Test
	void mybatisSessionTest() throws Exception {
		BoundSql boundSql = sqlSessionFactory.getConfiguration()
				.getMappedStatement("com.barasan.mycoder.web.mapper.PostgreMapper.selectColumnList").getSqlSource()
				.getBoundSql(null);
		String query1 = boundSql.getSql();
		// System.out.println(query1);

		Object paramObj = boundSql.getParameterObject();

		// List<ParameterMapping> paramMapping = boundSql.getParameterMappings();

		// System.out.println(paramMapping);

		if (paramObj != null) { // 파라미터가 아무것도 없을 경우
			List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
			for (ParameterMapping mapping : paramMapping) {
				String propValue = mapping.getProperty();
				query1 = query1.replaceFirst("\\?", "#{" + propValue + "}");
			}
		}

		Collection<MappedStatement> names = sqlSessionFactory.getConfiguration().getMappedStatements();
		names.stream().map(item -> item.getId()).forEach(System.out::println);

	}
}
