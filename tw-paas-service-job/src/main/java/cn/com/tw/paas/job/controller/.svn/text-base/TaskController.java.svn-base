package cn.com.tw.paas.job.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.paas.job.cmd.local.cache.OrgListCache;

@RestController
@RequestMapping("task")
public class TaskController {

	
	
	/**
	 *  动态添加任务
	 */
	@RequestMapping("clearOrgList")
	public String clearOrgList(){
		try {
			OrgListCache.clearMap();
		} catch (Exception e) {
			return String.format("clear error Exception{}", e);
		}
		return "success";
	}
	
}
