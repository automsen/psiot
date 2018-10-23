package cn.com.tw.engine.core.handler.store.device;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import cn.com.tw.engine.core.entity.GwMeters;
import cn.com.tw.engine.core.handler.DeviceDataHandler;
import cn.com.tw.engine.core.handler.store.device.utils.DeviceDataUtils;

public class FileDeviceDataHandler implements DeviceDataHandler{
	
	private static Map<String, Object> deviceCache = new ConcurrentHashMap<String, Object>();

	@Override
	public void add(GwMeters gwMeters) {
		try {
			DeviceDataUtils.saveDataConfig(gwMeters);
			deviceCache.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(String gwId) {
		try {
			DeviceDataUtils.deleteData(gwId);
			deviceCache.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GwMeters> all() {
		List<GwMeters> metersList = (List<GwMeters>) deviceCache.get("all");
		if (metersList == null) {
			try {
				List<GwMeters> gwMs = DeviceDataUtils.readDataAll();
				deviceCache.put("all", gwMs);
				return gwMs;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return metersList;
	}

	@Override
	public void clear() {
		deviceCache.clear();
		DeviceDataUtils.deleteDataAll();
	}

	@Override
	public GwMeters getMeters(String gwId) {
		
		GwMeters meters = (GwMeters) deviceCache.get(gwId);
		if (meters == null) {
			try {
				GwMeters gwM = DeviceDataUtils.readDataByKey(gwId);
				deviceCache.put(gwId, gwM);
				return gwM;
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return meters;
	}

	@Override
	public String getGwId(String meterId) {
		
		if (StringUtils.isEmpty(meterId)) {
			return null;
		}
		
		List<GwMeters> gwMeters = all();
		
		if (gwMeters == null || gwMeters.isEmpty()){
			return null;
		}
		
		for (GwMeters gwM : gwMeters) {
			
			String meterIds = gwM.getMeterIds();
			
			if (StringUtils.isEmpty(meterIds)) {
				continue;
			}
			
			String[] meterIdArray = meterIds.split(",");
			
			for (String met : meterIdArray) {
				if (meterId.equals(met)) {
					return gwM.getGwId();
				}
			}
			
			if (meterId.equals(gwM.getGwId())) {
				return gwM.getGwId();
			}
		}
		
		return null;
	}

}
