package cn.com.tw.common.utils.tools.license;

/**
 * 
 * @author admin
 *
 */
public class ExpireLocal {
	
	private static ThreadLocal<Object> expireLocal = new ThreadLocal<Object>();
	
	public static void setExpireLocal(Object obj){
		expireLocal.set(obj);
	}
	
	public static Object getExpire(){
		return expireLocal.get();
	}

}
