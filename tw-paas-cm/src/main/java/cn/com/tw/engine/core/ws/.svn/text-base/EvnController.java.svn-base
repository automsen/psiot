package cn.com.tw.engine.core.ws;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.utils.env.Env;

@RestController
@RequestMapping("env_api")
public class EvnController {
	
	@RequestMapping("val/{envKey}")
	public String getVal(@PathVariable String envKey) {
		return Env.getVal(envKey);
	}

}
