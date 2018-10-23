package cn.com.tw.saas.serv.common.utils.cons;

public class SysCommons {

	
	/**
	 *  命令状态
	 * @author liming
	 *
	 */
	public interface CmdStatus{
		Byte Ready = new Byte("0");
		Byte Success = new Byte("1");
		Byte Error  = new Byte("2");
		Byte Timeout = new Byte("3");
		Byte Handle = new Byte("10");
	}
	
	/**
	 *  机构配置第三方支付类型
	 */
	public interface payType{
		String Wechat_Pay = "2031";
		String Ali_Pay = "2032";
	}
	
	/**
	 *  订单来源
	 * @author liming
	 *
	 */
	public interface orderSource{
		String wechat_order="1098";
		String web_order = "1099";
	}
	
	/**
	 * 操作员类型（1011 系统用户，1012 客户）
	 * @author liming
	 *
	 */
	public interface operatorType{
		String system_user="1011";
		String customer_user = "1012";
	}
	
	public interface AlarmCache{
		
		String cacheKey = "alarmClose";
		
		String alarmHeader1 = "SMS:SAAS:ALARM:01:";
		
		String alarmHeader2 = "SMS:SAAS:ALARM:02:";
		
		/**
		 *  短信提醒
		 */
		String SMS_ALARM = "1201";  
		/**
		 *  templateNo
		 */
		String templateNo = "SMS_143862865";
		/**
		 *  ali 的sign
		 */
		String signature = "时波网络";
		
		/**
		 *  断电一分钟提醒
		 */
		String CLOSE_METER_ALARM="1204";
	}
}
