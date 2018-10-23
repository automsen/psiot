package cn.com.tw.engine.core.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.engine.core.entity.GwMeters;
import cn.com.tw.engine.core.handler.DeviceDataHandler;

@RestController
@RequestMapping("device")
public class DeviceController {
	
	@Autowired
	private DeviceDataHandler deviceDataHandler;
			
	@PostMapping
	public Response<?> addDevice(@RequestBody GwMeters gwMeters){
		
		deviceDataHandler.add(gwMeters);
		
		return Response.ok();
	}
	
	@DeleteMapping("{gwId}")
	public Response<?> removeDevice(@PathVariable String gwId){
		deviceDataHandler.delete(gwId);
		return Response.ok();
	}
	
	@DeleteMapping
	public Response<?> removeDevices(){
		deviceDataHandler.clear();
		return Response.ok();
	}
	
	@GetMapping("{gwId}")
	public Response<?> getDeviceById(@PathVariable String gwId){
		return Response.ok(deviceDataHandler.getMeters(gwId));
	}
	
	@GetMapping()
	public Response<?> getDeviceAll(){
		return Response.ok(deviceDataHandler.all());
	}
}
