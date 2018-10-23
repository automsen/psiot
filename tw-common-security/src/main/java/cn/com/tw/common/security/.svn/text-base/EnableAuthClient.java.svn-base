package cn.com.tw.common.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.com.tw.common.security.core.JwtTokenFactory;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JwtTokenFactory.class)
@Documented
@Inherited
public @interface EnableAuthClient {
}