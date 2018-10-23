package cn.com.tw.saas.serv.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

public class ExcelUtils {

	/**
	 *  集合中添加一行统计的数据
	 * @param lists 
	 * @param firstColumnName
	 * @param totalColumns
	 */
	public static<T> Object  loadTotalColumns(List<T> lists,String firstColumnName,String...totalColumns){
		try {
			
			// 数据源为空
			if(lists == null || lists.isEmpty() || 
					totalColumns == null || totalColumns.length == 0){
				return null;
			}
			Object newIns = lists.get(0).getClass().newInstance();
			
			//设置第一列信息
			setObjectValue(newIns,firstColumnName,"合计",getObjectField(newIns.getClass(),firstColumnName));
			// 设置所有统计列
			for (String cl : totalColumns) {
				BigDecimal totalDecimal = BigDecimal.ZERO;
				Object tempObj = null;
				
				for (Object tpObj : lists) {
					tempObj = getObjectValue(tpObj,cl, getObjectField(newIns.getClass(),cl));
					if(tempObj!= null){
						totalDecimal = totalDecimal.add(new BigDecimal(tempObj.toString()));
					}
				}
				// 合计插入对应的列中
				setObjectValue(newIns,cl,totalDecimal.toString(),getObjectField(newIns.getClass(),cl));
			}
			return newIns;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static void setObjectValue(Object t ,String columns,String value,Class<?> paramType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String setMethodName ="set"+columns.substring(0,1).toUpperCase()+columns.substring(1);
		Method tplMth = t.getClass().getMethod(setMethodName,paramType);
		if(paramType.getName().equals("java.lang.String")){
			tplMth.invoke(t, value);
		}else if(paramType.getName().equals("java.math.BigDecimal")){
			tplMth.invoke(t, new BigDecimal(value));
		}else if(paramType.getName().equals("java.lang.Integer")){
			tplMth.invoke(t, Integer.valueOf(value));
		}
		
	}
	private static Object getObjectValue(Object t ,String columns, Class<?> paramType) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String setMethodName ="get"+columns.substring(0,1).toUpperCase()+columns.substring(1);
		Method tplMth = t.getClass().getMethod(setMethodName );
		return tplMth.invoke(t);
	}

	private static Class<?> getObjectField(Class<?> t,String fieldName) throws NoSuchFieldException, SecurityException{
		Field f = t.getDeclaredField(fieldName);
		return f.getType();
	}
}
