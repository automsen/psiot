package cn.com.tw.common.web.anno.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通过角色去判断
 * @author admin
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) // 声明可以使用该注解的目标类型为在方法中使用
public @interface Role {
	/** 
	 * 权限值
	 */
	String value(); // 注解成员
}
