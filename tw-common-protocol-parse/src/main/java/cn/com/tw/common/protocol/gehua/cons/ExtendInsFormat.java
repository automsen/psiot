package cn.com.tw.common.protocol.gehua.cons;

import java.util.HashMap;

/**
 * 扩展指令格式
 * @author admin
 *
 */
public class ExtendInsFormat {
	public static HashMap<String,String> keyArray = new HashMap<String,String>(); 
	public static HashMap<String,String> formatArray = new HashMap<String,String>(); 
	static {
		// 波特率及校验位
		keyArray.put("90000006", "Baud");
		formatArray.put("90000006", "2");

		// 上传周期，心跳周期
		keyArray.put("04700404", "RI,HBI");
		formatArray.put("04700404", "h4,h4");
		
		// 上传任务单总数
		keyArray.put("04700208", "RTT");
		formatArray.put("04700208", "2");

		// 清上传任务
		formatArray.put("04700403", "2");
		
		// 报警任务单总数
		keyArray.put("04700405", "WTT");
		formatArray.put("04700405", "2");
		// 报警数据类型
		keyArray.put("0470040A", "WET");
		formatArray.put("0470040A", "2");
		// 清报警任务
		formatArray.put("04700408", "2");
	}
}
