package cn.com.tw.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import cn.com.tw.common.core.anno.CacheTypeEum;
import cn.com.tw.common.core.anno.EnableCusService;
import cn.com.tw.common.core.anno.MessgeTypeEum;
import cn.com.tw.common.web.anno.cors.EnableCors;
import cn.com.tw.common.web.anno.excep.EnableGExcep;
import cn.com.tw.common.web.anno.module.EnableCustomScan;

@SpringBootApplication
@ServletComponentScan
@EnableCors
@EnableGExcep
@EnableCustomScan
@EnableCusService(mq = MessgeTypeEum.KAFKA, cache = CacheTypeEum.REDIS)
public class CMApplication {
    public static void main(String[] args) {
    	SpringApplication.run(CMApplication.class, args);
    }
}