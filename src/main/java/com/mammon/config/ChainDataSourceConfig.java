package com.mammon.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
public class ChainDataSourceConfig {

    public static final String ChainDbDataSourceName = "ChainDbDataSource";
    public static final String ChainDbNamedParameterJdbcTemplateName = "ChainDbNamedParameterJdbcTemplate";
    public static final String ChainDbJdbcTemplateName = "ChainDbJdbcTemplate";
    public static final String ChainDbTransactionTemplateName = "ChainDbTransactionTemplate";
    private static final String ChainDbTransactionManagerName = "ChainDbTransactionManager";

    @Bean(name = ChainDbDataSourceName)
    @ConfigurationProperties("spring.datasource.hikari.chain")
    public DataSource chainDbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = ChainDbJdbcTemplateName)
    public JdbcTemplate chainDbJdbcTemplate(
            @Autowired @Qualifier(ChainDbDataSourceName) DataSource dataSource,
            JdbcProperties properties) {
        return jdbcTemplateBuild(dataSource, properties);
    }

    @Bean(name = ChainDbNamedParameterJdbcTemplateName)
    public NamedParameterJdbcTemplate chainDbNamedParameterJdbcTemplate(
            @Autowired @Qualifier(ChainDbJdbcTemplateName) JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean(name = ChainDbTransactionManagerName)
    public DataSourceTransactionManager chainDbTransactionManager(
            @Autowired @Qualifier(ChainDbDataSourceName) DataSource dataSource,
            ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        TransactionManagerCustomizers managerCustomizers = transactionManagerCustomizers.getIfAvailable();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        if (managerCustomizers != null) {
            managerCustomizers.customize(transactionManager);
        }
        return transactionManager;
    }

    @Bean(name = ChainDbTransactionTemplateName)
    public TransactionTemplate chainDbTransactionTemplate(
            @Autowired @Qualifier(ChainDbTransactionManagerName) PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    private JdbcTemplate jdbcTemplateBuild(DataSource dataSource, JdbcProperties jdbcProperties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = jdbcProperties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }
}
