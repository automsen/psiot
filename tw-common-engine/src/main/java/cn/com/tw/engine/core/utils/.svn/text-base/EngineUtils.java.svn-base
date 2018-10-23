package cn.com.tw.engine.core.utils;

import java.util.Map;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.engine.core.entity.CmdResponse;

public class EngineUtils {
	
	public static Map<String, Object> parseResult(CmdResponse cmdRes){
		
		Object obj = cmdRes.getData();
		
		if (obj == null){
			return null;
		}
		
		String JsonStr = JsonUtils.objectToJson(obj);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> result = JsonUtils.jsonToPojo(JsonStr, Map.class);
		
		return result;
		
	}
	
	
	public static String getOpposite(String content){
		StringBuffer opposStr = new StringBuffer();
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '0'){
				opposStr.append("1");
			}else if (content.charAt(i) == '1') {
				opposStr.append("0");
			}else {
				throw new IllegalArgumentException();
			}
		}
		
		return opposStr.toString();
	}

}
