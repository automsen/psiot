package cn.com.tw.common.hb.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.com.tw.common.hb.HBaseConfigurationBase;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HBaseConfigurationBase.class})
public @interface EnableHB {
}