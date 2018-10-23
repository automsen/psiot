package cn.com.tw.common.web.anno.excep;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.com.tw.common.web.config.GlobalExceptionHandler;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ GlobalExceptionHandler.class })
public @interface EnableGExcep {
}