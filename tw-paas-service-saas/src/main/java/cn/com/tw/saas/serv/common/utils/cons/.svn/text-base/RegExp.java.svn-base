package cn.com.tw.saas.serv.common.utils.cons;

/**
 * 常用正则表达式
 * @author admin
 *
 */
public class RegExp {
	
	
	/**
	 * 大于0的数
	 */
	public static String mathExp="^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$";
	
	/**
	 * 数字字母下划线
	 */
	public static String houseExp="^[0-9a-zA-Z_]+$";
	/**
	 * 姓名的校验，只允许中文，英文，和数字^  /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/
	 */
	public static String nameExp="^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$";
	/**
	 * 整数
	 */
	public static String integerExp = "^\\d+$";
	
	/**
	 * 正整数
	 */
	public static String positiveIntegerExp = "^[1-9][0-9]*$";
	
	/**
	 * 小数
	 */
	public static String decimalExp = "^([0-9]{1,}[.][0-9]*)$";
	
	/**
	 * 整数或小数
	 */
	public static String numberExp = "^[0-9]+([.]{1}[0-9]+){0,1}$";
	
	/**
	 * 手机号
	 */
	public static String phoneExp = "^1[3-9]\\d{9}$";
	
    /**
     * 字母数字下划线和横杠,匹配特殊字符~,
     */
	public static String wordNumberUnderlineRungExp = "^[\\w-~]+$";
	//"^(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)$";
	//"^(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)$";
//	public static String wordNumberUnderlineRungExp =  "^(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)+((~|-|)+)+(([A-Z]|[a-z]|[0-9])+)$";
	/**
	 * 字母数字下划线横杠~，除字母数字其它均不可为头尾
	 */
	public static String roomNumberExp = "^(?!_|-|~)[\\w-~]+(?<!_|-|~)$";
}
