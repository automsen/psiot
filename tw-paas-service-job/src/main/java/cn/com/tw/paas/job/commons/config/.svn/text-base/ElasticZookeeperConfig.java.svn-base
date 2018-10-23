package cn.com.tw.paas.job.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;


/**
 * zookeeper 启动注册
 * @author liming
 * 2018年8月17日 15:03:51
 */
//@Configuration
//@ConditionalOnExpression("'${elastic.zookeeper.server-lists}'.length() >0")
public class ElasticZookeeperConfig {

    /**
     * 初始化配置
     * @param serverList
     * @param namespace
     * @return
     */
	
	@Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(@Value("${elastic.zookeeper.server-lists}") String serverList
            , @Value("${elaticjob.zookeeper.namespace}") String namespace) {
    	return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList,namespace));
    }
	 
	 

}