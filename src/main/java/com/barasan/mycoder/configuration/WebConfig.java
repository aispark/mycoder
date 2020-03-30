package com.barasan.mycoder.configuration;

import javax.sql.DataSource;

import com.barasan.mycoder.generator.domain.BoilerplateProperties;
import com.barasan.mycoder.generator.helper.StringUtil;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * bean 설정을 위한 클래스 입니다.
 */
@Configuration
@MapperScan(value = { "com.barasan.mycoder.web.mapper" })
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  BoilerplateProperties properties;

  @Autowired
  ApplicationContext applicationContext;

  /**
   * mybatis SqlSessionFactory bean을 생성하기 위한 메소드 입니다.
   * 
   * @param dataSource
   * @return
   * @throws Exception
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);

    // 첫글자를 대문자로
    String dbms = StringUtil.upperFirstLetter(properties.getDbms().toLowerCase());

    Resource[] arrResource = new PathMatchingResourcePatternResolver()
        .getResources("classpath:mapper/mycoder/" + dbms + "-Mapper.xml");
    sqlSessionFactoryBean.setMapperLocations(arrResource);
    sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mapper/mybatis-config.xml"));
    sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
    return sqlSessionFactoryBean.getObject();
  }

}