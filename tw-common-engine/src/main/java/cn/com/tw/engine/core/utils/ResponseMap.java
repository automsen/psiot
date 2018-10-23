package cn.com.tw.engine.core.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.tw.engine.core.entity.CmdResponse;

/**
 * 存放返回结果
 * @author admin
 *
 */
public class ResponseMap {
	
	public static ConcurrentHashMap<String, BlockingQueue<CmdResponse>> responseMap = new ConcurrentHashMap<String, BlockingQueue<CmdResponse>>();
	
	public static void put(String id, BlockingQueue<CmdResponse> queue){
		responseMap.put(id, queue);
	}
	
	public static BlockingQueue<CmdResponse> get(String id){
		return responseMap.get(id);
	}
	
	public static void del(String id){
		responseMap.remove(id);
	}
}
