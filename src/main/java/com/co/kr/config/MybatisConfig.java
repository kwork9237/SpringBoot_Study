package com.co.kr.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(
	basePackages = {"com.co.kr.mapper"},
	annotationClass = org.apache.ibatis.annotations.Mapper.class,
	sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MybatisConfig {
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		//Bean으로 등록할 SqlSessionFactory객체 생성
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/*Mapper.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}
}