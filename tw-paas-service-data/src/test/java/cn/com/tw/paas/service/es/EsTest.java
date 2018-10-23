package cn.com.tw.paas.service.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EsApplication.class)
@WebAppConfiguration
public class EsTest {

	private MockMvc mockMvc;
	
	@Autowired
	private TransportClient client;
	
/*	@Autowired
	private WorkFlowController workflowController;
*/
	/*@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(workflowController).build();
		
	}*/
	
	/*@Test
	public void addProcess() throws Exception {
		//MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/wf/test")).andReturn();
	    System.out.println("####" + client);
   }*/
	
	/**
	 * 创建信息（创建一个document）
	 * @param client
	 */
	//@Test
	public void createEmployee() throws Exception {
		IndexResponse response = client.prepareIndex("company", "employee", "2")
				.setSource(XContentFactory.jsonBuilder().startObject()
				.field("name", "jack1")
				.field("age", 27)
				.field("position", "technique")
				.field("country", "china")
				.field("join_date", "2017-01-01")
				.field("salary", 10000).endObject()).get();
		System.out.println(response.getShardInfo()); 
	}
	
	/**
	 * 获取员工信息
	 * @param client
	 * @throws Exception
	 */
	@Test
	public void getEmployee() throws Exception {
		GetResponse response = client.prepareGet("company", "employee", "2").get();
		System.out.println(response.getSourceAsString()); 
	}
	
	/**
	 * 修改员工信息
	 * @param client
	 * @throws Exception
	 */
	public void updateEmployee() throws Exception {
		UpdateResponse response = client.prepareUpdate("company", "employee", "1") 
				.setDoc(XContentFactory.jsonBuilder().startObject().field("position", "technique manager").endObject())
				.get();
		System.out.println(response.getShardInfo());  
 	}
	
	/**
	 * 删除 员工信息
	 * @param client
	 * @throws Exception
	 */
	public void deleteEmployee() throws Exception {
		DeleteResponse response = client.prepareDelete("company", "employee", "1").get();
		System.out.println(response.getShardInfo());  
	}
	
	/*@Test
	@Ignore
	public void addProcess() throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/wf/test")).andReturn();
	    System.out.println(result.getResponse().getContentAsString());
   }
   
   @Test
   @Ignore
   public void taskList() throws Exception {
	  
   }
   
   @Test
   public void completeTask() throws Exception {
	   MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wf/completeTask").param("taskId", "7502"))
			   .andDo(MockMvcResultHandlers.print())
				.andReturn();
	   System.out.println(result.getResponse().getContentAsString());
   }
   
   @Test
   @Ignore
   public void activeTask() throws Exception {
	   MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wf/activeTask").contentType(MediaType.ALL).characterEncoding("UTF-8").param("bussnessId", "111111"))
			   .andDo(MockMvcResultHandlers.print())
				.andReturn();
	   System.out.println(result.getResponse().getContentAsString());
   }*/
	   
}