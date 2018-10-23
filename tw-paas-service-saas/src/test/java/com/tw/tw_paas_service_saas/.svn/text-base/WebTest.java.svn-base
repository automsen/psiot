package com.tw.tw_paas_service_saas;

import org.apache.poi.xslf.model.geom.MoveToCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cn.com.tw.saas.serv.SaasApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MockServletContext.class)
@WebAppConfiguration
public class WebTest {

    @Autowired
    private MockMvc mvc;
    
    @Test
    public void getStudentList() throws Exception {
    	  MvcResult result = mvc.perform(MockMvcRequestBuilders.get("net/12323")
					)
					.andReturn();
    }
}
