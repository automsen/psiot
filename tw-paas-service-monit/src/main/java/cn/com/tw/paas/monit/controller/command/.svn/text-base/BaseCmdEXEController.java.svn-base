package cn.com.tw.paas.monit.controller.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.service.command.BaseCmdEXEService;

@RestController
@RequestMapping("cmd")
public class BaseCmdEXEController {
	
	@Autowired
	private BaseCmdEXEService baseCmdEXEService;
	
	@GetMapping("all")
	public Response<?>  selectAll(BaseCmdEXE baseCmdEXE){
		List<BaseCmdEXE> baseCmdEXEs = baseCmdEXEService.selectAll(baseCmdEXE);
		return Response.ok(baseCmdEXEs);
	}
	
	
	

}
