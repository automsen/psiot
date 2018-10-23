package cn.com.tw.common.web.anno.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.com.tw.common.web.config.CacheConfiguration;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ CacheConfiguration.class })
public @interface EnableEhCache {
}

