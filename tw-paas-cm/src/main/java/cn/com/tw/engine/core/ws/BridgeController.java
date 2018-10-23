package cn.com.tw.engine.core.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.engine.core.ChannelManaFactory;
import cn.com.tw.engine.core.bridge.ChannelBridge;

@RestController
@RequestMapping("bridge")
public class BridgeController {
	
	@GetMapping("/{targetId}")
	public Response<?> getQueueInfo(@PathVariable String targetId){
		
		ChannelBridge bridge = ChannelManaFactory.getInstanc().getBridgeByGwId(targetId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (bridge == null) {
			return Response.ok(result);
		}
		
		result.put("connectCount", ChannelManaFactory.getInstanc().build().getSize());
		result.put("reqQueue", bridge.getPriorityQueue());
		result.put("resultQueue", bridge.getResultPriorityQueue());
		return Response.ok(result);
		
	}
	
	@GetMapping("/status")
	public Response<?> getStatusInfo(){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("connectCount", ChannelManaFactory.getInstanc().build().getSize());
		
		Map<String, ChannelBridge> deviceMap = ChannelManaFactory.getInstanc().build().getAll();
		
		List<DeviceStatus> deviceStatus = new ArrayList<DeviceStatus>();
		for (Map.Entry<String, ChannelBridge> device : deviceMap.entrySet()) {
			ChannelBridge bridge = device.getValue();
			
			if (bridge.getChannel() == null) {
				deviceStatus.add(new DeviceStatus(device.getKey(), "channel null", bridge.getPriorityQueue().size(), bridge.getResultPriorityQueue().size()));
				continue;
			}
			
			deviceStatus.add(new DeviceStatus(device.getKey(), bridge.getChannel().isActive() ? "active" : "no active", bridge.getPriorityQueue().size(), bridge.getResultPriorityQueue().size()));
		}
		result.put("connect", deviceStatus);
		return Response.ok(result);
		
	}
	
	public static class DeviceStatus {
		private String id;
		
		private String status;
		
		private int goCount;
		
		private int backCount;
		
		public DeviceStatus(String id, String status, int goCount, int backCount) {
			this.id = id;
			this.status = status;
			this.goCount = goCount;
			this.backCount = backCount;
		}

		public int getGoCount() {
			return goCount;
		}

		public void setGoCount(int goCount) {
			this.goCount = goCount;
		}

		public int getBackCount() {
			return backCount;
		}

		public void setBackCount(int backCount) {
			this.backCount = backCount;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
	}

}
