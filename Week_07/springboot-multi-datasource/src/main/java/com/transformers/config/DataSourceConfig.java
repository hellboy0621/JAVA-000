package com.transformers.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置数据库信息
 */
@Configuration
public class DataSourceConfig {
    public static final String DB_TEST_MASTER = "master";
    public static final String DB_TEST_SLAVE = "slave";
    public static final String DB_TEST_SLAVE2 = "slave2";

    @Bean(name = "master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slave2")
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slaveDataSource2() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        Map<Object, Object> databaseMap = new HashMap<>(16);
        databaseMap.put(DB_TEST_MASTER, masterDataSource());
        databaseMap.put(DB_TEST_SLAVE, slaveDataSource());
        databaseMap.put(DB_TEST_SLAVE2, slaveDataSource2());
        dynamicDataSource.setTargetDataSources(databaseMap);
        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 指定扫描的xml文件所在位置，在配置文件里面配置，会报Invalid bound statement
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/*.xml");
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setMapperLocations(resources);
        bean.setConfiguration(configuration);

        return bean.getObject();
    }

    /**
     * 事务管理
     *
     * @param dataSource 数据源
     * @return 事务管理
     */
    @Bean(name = "sqlTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
