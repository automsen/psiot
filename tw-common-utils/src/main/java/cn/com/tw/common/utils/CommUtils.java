package cn.com.tw.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

/**
 * 工具类
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  admin
 * @version  [版本号, 2015年7月21日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommUtils
{
	
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
        "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z" };
	
    /**
     * 生产流水号
     * @return
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 生成短信验证码
     * @param digitNum
     * @return
     */
	public static String getSMSCode(int digitNum) {
		String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		List<String> list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(0, digitNum);
		return result;
	}
	
	/**
	 * 随机生成密码
	 * @return
	 */
	public static String randomPwd(int digit) {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		digit = digit < 6 ? 6 : digit;
		for (int i = 0; i < digit; i++) {
		    String str = uuid.substring(i * 4, i * 4 + 4);
		    int x = Integer.parseInt(str, 16);
		    shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString().toLowerCase();
	}

    
    /**
     * 获取返回响应数据格式
     * <功能详细描述>
     * @param code
     * @param message
     * @param data
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> result(String code, String message, Object data){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("code", code);
        statusMap.put("message", message);
        resultMap.put("status", statusMap);
        
        if(data != null){
            resultMap.put("data", data);
        }
        
        return resultMap;
    }
    
    /**
     * base64编码
     * @param message
     * @return
     */
    public static String base64Encode(String message){
    	if(StringUtils.isEmpty(message)){
    		return null;
    	}
    	
    	try {
			message = URLEncoder.encode(Base64.encodeBase64String(message.getBytes("utf-8")), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	return message;
    }
    
    /**
     * 解码
     * @param message message
     * @return
     */
    public static String base64Decode(String message){
    	if(StringUtils.isEmpty(message)){
    		return null;
    	}
    	
    	message = new String(Base64.decodeBase64(message));
    	return message;
    }
}
