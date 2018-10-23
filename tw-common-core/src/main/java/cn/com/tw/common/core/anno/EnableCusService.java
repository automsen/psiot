package cn.com.tw.common.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ CusServiceImportSelector.class })
public @interface EnableCusService {
	public abstract MessgeTypeEum mq() default MessgeTypeEum.ACTIVEMQ;
	public abstract CacheTypeEum cache() default CacheTypeEum.DEFAULT;
}