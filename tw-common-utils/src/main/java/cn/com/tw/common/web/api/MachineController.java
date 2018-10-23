package cn.com.tw.common.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.license.HardwareBinder;
import cn.com.tw.common.web.entity.Response;

@RestController
@RequestMapping("machine")
public class MachineController {
	
	@RequestMapping("")
	public Response<?> machine(){
		
		HardwareBinder binder = new HardwareBinder();
		
		try {
			return Response.ok(binder.getMachineIdString());
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, e.getMessage());
		}
	}

}
