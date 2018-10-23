package cn.com.tw.db.mybatis;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import cn.com.tw.db.mybatis.interceptor.PageInterceptor;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class MybatisAutoConfig {
	
	@Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }
    
    /**
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/config/mybatis.xml"));
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor()});
        return sqlSessionFactoryBean.getObject();
    }
    
    @Bean
    public PageInterceptor pageInterceptor(){
    	PageInterceptor page = new PageInterceptor();
    	page.setDatabaseType("mysql");
    	return page; 
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}
