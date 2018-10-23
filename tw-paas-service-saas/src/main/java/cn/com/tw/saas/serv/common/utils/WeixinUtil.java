package cn.com.tw.saas.serv.common.utils;

import java.util.Random;

public class WeixinUtil {

	/**
	 * 随机位数的字符串 用来微信加密请求
	 * @param length
	 * @return
	 */
	public static String getRandomStringByLength(int length) {  
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
    }  
}
