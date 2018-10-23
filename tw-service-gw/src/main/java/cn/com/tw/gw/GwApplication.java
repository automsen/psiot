package cn.com.tw.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.com.tw.common.web.anno.module.EnableCustomScan;

@SpringBootApplication
@EnableCustomScan
public class GwApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(GwApplication.class, args);
    }
 
}