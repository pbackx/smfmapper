package com.byopvr.discourse.smfmapper;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class SpringConfiguration {

	private static final String POSTGRES_CONNECTION_STRING = "jdbc:postgresql://10.0.2.2/discourse";
	private static final String POSTGRES_USERNAME = "postgres";
	private static final String POSTGRES_PASSWORD = "postgres";

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(POSTGRES_CONNECTION_STRING);
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUsername(POSTGRES_USERNAME);
		dataSource.setPassword(POSTGRES_PASSWORD);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
