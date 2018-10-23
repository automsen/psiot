package cn.com.tw.paas.job.commons.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 此帮助类严格限定为有value的枚举类，如定义枚举类型为 ADMIN(value)这种
 * 
 * ClassName: EnumOperatorUtil.java 
 * Copyright xiehao 
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON.
 * Date:   2017年2月28日
 * @author   xiehao 
 * @version  V1.0
 * @since    JDK 1.8
 * @see
 */
public class EnumHelperUtil{
	/**
	 * indexOf,传入的参数ordinal指的是需要的枚举值在设定的枚举类中的顺序，以0开始
	 * T
	 * @param clazz
	 * @param ordinal
	 * @return
	 * @author   xiehao 
	 */
	public static <T extends Enum<T>> T indexOf(Class<T> clazz, int ordinal){
		return (T) clazz.getEnumConstants()[ordinal];
	}
	/**
	 * nameOf,传入的参数name指的是枚举值的名称，一般是大写加下划线的
	 * T
	 * @param clazz
	 * @param name
	 * @return Enum T
	 * @author   xiehao 
	 */
	public static <T extends Enum<T>> T nameOf(Class<T> clazz, String name){
		
		return (T) Enum.valueOf(clazz, name);
	}
	/**
	 * 使用枚举类型对应的value获取枚举类型
	 * T
	 * @param clazz    枚举类的class
	 * @param getValueMethodName  传入的value的get方法 通常为getValue
	 * @param value  传入的value值，这个方法为String类型
	 * @return
	 * @author   xiehao 
	 */
	public static <T extends Enum<T>> T getByStringTypeCode(Class<T> clazz,String getValueMethodName, String value){
		T result = null;
		try{
			T[] arr = clazz.getEnumConstants();
			Method targetMethod = clazz.getDeclaredMethod(getValueMethodName);
			String valueStr = null;
			for(T entity:arr){
				valueStr = targetMethod.invoke(entity).toString();
				if(valueStr.contentEquals(value)){
					result = entity;
					break;
				}
			}			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return result;
	}

}

