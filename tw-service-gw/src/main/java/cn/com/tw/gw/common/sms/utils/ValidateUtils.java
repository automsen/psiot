package cn.com.tw.gw.common.sms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {
	
	/**
    * @param regex 正则表达式字符串
    * @param str   要匹配的字符串
    * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
    */
    private static boolean match(String regex, String str) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
    }
	
	/**
	 * 校验手机号
	 * @param phone 手机号
	 * @return 验证通过返回true 
	 */
	public static boolean isMobile(String mobilephone) {   
        String regex ="^[1][0-9][0-9]{9}$";
        return match(regex, mobilephone);
    }  
	
	/** 
     * 电话号码验证 
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String phone) {   
    	//带区号的电话号码
        String regex="^[0][1-9]{2,3}-[0-9]{5,10}$";
        //不带区号的电话号码
        String regex1="^[1-9]{1}[0-9]{5,8}$";
        if(phone.length() < 9){
        	return match(regex1,phone);
        }
    	return match(regex,phone);
    }  
	
    /**
    * 验证邮箱
    * 
    * @param 待验证的字符串
    * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
    */
    public static boolean isEmail(String str) {
    	String regex = "^([\\w-\\.]+)@((http://www.cnblogs.com/yaojian/admin/file://[[0-9]%7b1,3%7d//.[0-9]%7B1,3%7D//.[0-9]%7B1,3%7D//.)%7C(([//w-]+//.)+))([a-zA-Z]%7B2,4%7D%7C[0-9]%7B1,3%7D)(//]?)$";
    	return match(regex, str);
    }
    
}
