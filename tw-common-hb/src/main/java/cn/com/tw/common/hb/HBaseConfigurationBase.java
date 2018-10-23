package cn.com.tw.common.hb;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
/*@ConfigurationProperties(prefix = "hbase.zookeeper")*/
public class HBaseConfigurationBase {

    //private Logger logger= LoggerFactory.getLogger(HBaseConfigurationBase.class);

    /*private String quorum;*/
	
	@Value("${hadoop.hbase.zookeeper.property.clientPort}")
	private String clientProt;
	
	@Value("${hadoop.hbase.zookeeper.quorum}")
	private String quorum;
	
	@Value("${hadoop.hbase.master}")
	private String master;

    /**
     * 产生HBaseConfiguration实例化Bean
     * @return
     */
    @Bean
    public Configuration configuration() {
        Configuration conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", clientProt); 
        conf.set("hbase.zookeeper.quorum", quorum); 
        conf.set("hbase.master", master); 
        return conf;
    }
    
    @Bean
    public Connection connection() throws IOException {
    	Connection connection = ConnectionFactory.createConnection(configuration());
    	return connection;
    }
}