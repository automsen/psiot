package cn.com.tw.common.web.anno.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.tw.common.web.aop.enm.LogTypeNum;

/**
 * 定义日志注解描述
 * @author admin
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogDesc {
	
	LogTypeNum type() default LogTypeNum.DEFAULT;
	
	String value() default "";
    
}
