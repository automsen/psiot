package cn.com.tw.engine.core.handler;

import java.util.List;

import cn.com.tw.engine.core.entity.GwMeters;

public interface DeviceDataHandler {
	
	void add(GwMeters gwMeters);
	
	void delete(String gwId);
	
	List<GwMeters> all();
	
	void clear();
	
	GwMeters getMeters(String gwId);
	
	String getGwId(String meterId);
	
}
