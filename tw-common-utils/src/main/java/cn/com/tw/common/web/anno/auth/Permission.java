package cn.com.tw.common.web.anno.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * action权限配置
 * @author admin
 *
 */
//声明注解的保留期限
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) // 声明可以使用该注解的目标类型为在方法中使用
public @interface Permission {
	/** 
	 * 权限值
	 */
	String value(); // 注解成员
}
