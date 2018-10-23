package cn.com.tw.paas.monit;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.mapper.base.InsDataItemMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MonitApplication.class) //1.4版本之前用的是//@SpringApplicationConfiguration(classes = Application.class)
public class AppTest {
	
	@Autowired
	private InsDataItemMapper insDataItemMapper;

    /**
     * 2017-06-16 14:08:09.884  INFO 13803 --- [           main] com.alibaba.druid.pool.DruidDataSource   : {dataSource-1} inited
     size:5
     -----测试完毕-------
     2017-06-16 14:08:09.955  INFO 13803 --- [       Thread-4] ationConfigEmbeddedWebApplicationContext : Closing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@fd07cbb: startup date [Fri Jun 16 14:08:04 CST 2017]; root of context hierarchy
     */
    @Test
    public void test(){
    	List<Ins> insList = insDataItemMapper.selectAll();
    	System.out.println(insList);
    }
    
    
}