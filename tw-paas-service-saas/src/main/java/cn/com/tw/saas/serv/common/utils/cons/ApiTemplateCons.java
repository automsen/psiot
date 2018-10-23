package cn.com.tw.saas.serv.common.utils.cons;

/**
 * API调用模板
 * @author admin
 *
 */
public class ApiTemplateCons {

	public static ApiTemplate switchOn = new ApiTemplate("/directive/open","仪表开闸",1,"switchOnServiceImpl",null);
	public static ApiTemplate switchOff = new ApiTemplate("/directive/close","仪表关闸",1,"switchOffServiceImpl",null);
	public static ApiTemplate loopOn = new ApiTemplate("/directive/openLoop","回路开闸",1,"switchOnServiceImpl",null);
	public static ApiTemplate loopOff = new ApiTemplate("/directive/closeLoop","回路关闸",1,"switchOffServiceImpl",null);
	
	public static ApiTemplate readCommon = new ApiTemplate("/directive/read","读取常用数据块",1,"readCommonServiceImpl",null);
	public static ApiTemplate readTest = new ApiTemplate("/directive/readTest","通讯测试读止码",2,"readTestServiceImpl",null);
	
	public static ApiTemplate wMalignantLoadInterval = new ApiTemplate("/directive/commonWrite","写恶性负载突变周期",2,"readCommonServiceImpl","w_malignant_load_interval");
	public static ApiTemplate wMalignantLoadValue = new ApiTemplate("/directive/commonWrite","写恶性负载阈值",2,"readCommonServiceImpl","w_malignant_load_value");
	public static ApiTemplate wMalignantLoadDelay = new ApiTemplate("/directive/commonWrite","写恶性负载恢复供电延时",2,"readCommonServiceImpl","w_malignant_load_delay");
	public static ApiTemplate wMalignantLoadScope = new ApiTemplate("/directive/commonWrite","写恶性负载保护区间",2,"readCommonServiceImpl","w_malignant_load_scope");
	public static ApiTemplate wMalignantLoadOffTime = new ApiTemplate("/directive/commonWrite","写恶性负载最大连续异常断电次数",2,"readCommonServiceImpl","w_malignant_load_off_time");
}
