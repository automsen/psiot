package cn.com.tw.paas.monit.controller.statis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.service.statis.StatisService;

@RestController
@RequestMapping("statis")
public class StatisController {
	
	@Autowired
	private StatisService statisService;

	@GetMapping("equip")
	public Response<?> getEquipStatis(){
		return Response.ok();
	}
}
