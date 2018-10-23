package cn.com.tw.db.mybatis.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.com.tw.db.mybatis.MybatisAutoConfig;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ MybatisAutoConfig.class })
public @interface EnableDB {
}