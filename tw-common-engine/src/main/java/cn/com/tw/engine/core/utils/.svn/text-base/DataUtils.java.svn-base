package cn.com.tw.engine.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.tw.engine.core.entity.DataItemGroup;
import cn.com.tw.engine.core.entity.Meter;

public class DataUtils {

	private final static String DEVICE_CACHE_FILE_PATH = "data/local.d";
	
	private static Map<String, Object> localCache = new ConcurrentHashMap<String, Object>();

	/**
	 * 保存缓存配置
	 * @param fileName
	 * @param cacheObj
	 */
	public static synchronized void saveDataConfig(DataItemGroup cacheObj) {
		
		List<DataItemGroup> dataItemGroups = readDataAll();
		
		if (dataItemGroups == null){
			List<DataItemGroup> cacheObjList = new ArrayList<DataItemGroup>();
			cacheObjList.add(cacheObj);
			writeData(cacheObjList);
		}else{
			
			if (dataItemGroups.contains(cacheObj)){
				
				Iterator<DataItemGroup> it = dataItemGroups.iterator();
				while(it.hasNext()){
					DataItemGroup dataItem = it.next();
				    if(dataItem.getDataItemNum().equals(cacheObj.getDataItemNum())){
				        it.remove();
				    }
				}
			}
			
			dataItemGroups.add(cacheObj);
			
			writeData(dataItemGroups);
		}
		
		localCache.clear();
	}
	
	private static void writeData(Object cacheObj){
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(DEVICE_CACHE_FILE_PATH);
			
			oos = new ObjectOutputStream(os);
			
			oos.writeObject(cacheObj);
			
			localCache.clear();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		} finally {
			try {
				if (os != null){
					os.close();
				}
				
				if (oos != null){
					oos.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<DataItemGroup> readDataAll(){
		
		List<DataItemGroup> dataGroupList = (List<DataItemGroup>) localCache.get("all");
		
		if (dataGroupList == null) {
			
			isExists(DEVICE_CACHE_FILE_PATH);
			
			InputStream is = null;
			ObjectInputStream ois = null;
			try {
				
				is = new FileInputStream(DEVICE_CACHE_FILE_PATH);
				ois = new ObjectInputStream(is);
				
				List<DataItemGroup> fileDataGroupList = (List<DataItemGroup>) ois.readObject();
				
				localCache.put("all", fileDataGroupList);
				
				return fileDataGroupList;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (ois != null){
					try {
						ois.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		return dataGroupList;
	}
	
	/**
	 * 删除数据
	 */
	public static synchronized void deleteData(String key){
		List<DataItemGroup> dataItemGroups = readDataAll();
		if(dataItemGroups != null){
			Iterator<DataItemGroup> it = dataItemGroups.iterator();
			while(it.hasNext()){
				DataItemGroup dataItem = it.next();
			    if(dataItem.getDataItemNum().equals(key)){
			        it.remove();
			    }
			}
			
		}
		
		writeData(dataItemGroups);
		
		localCache.clear();
	}
	
	public static synchronized DataItemGroup readDataByKey(String key){
		List<DataItemGroup> dataItemGroups = readDataAll();
		if (dataItemGroups == null){
			return null;
		}
		for (DataItemGroup dataItem : dataItemGroups){
			if (dataItem.getDataItemNum().equals(key)){
				return dataItem;
			}
		}
		return null;
	}
	
	public static synchronized void deleteDataAll(){
		File file = new File(DEVICE_CACHE_FILE_PATH);
		
		if (file.exists()){
			file.delete();
		}
		
		localCache.clear();
	}
	
	public static synchronized String getGwId(String meterId){
		
		List<DataItemGroup> dataGroupList = readDataAll();
		
		for (DataItemGroup dataItem : dataGroupList) {
			
			List<Meter> meters = dataItem.getMeters();
			
			if (meters == null) {
				continue;
			}
			
			for (Meter meter : meters) {
				
				if (meter.getMeterAddr() == null) {
					continue;
				}
				
				if (meter.getMeterAddr().equals(meterId)) {
					return meter.getGwId();
				}
			}
		}
		
		return null;
	}
	
	public static synchronized Set<String> getGws(){
		
		Set<String> gwSet = new HashSet<String>();
		
		List<DataItemGroup> dataGroupList = readDataAll();
		
		for (DataItemGroup dataItem : dataGroupList) {
			
			List<Meter> meters = dataItem.getMeters();
			
			if (meters == null) {
				continue;
			}
			
			for (Meter meter : meters) {
				
				if (meter.getMeterAddr() == null) {
					continue;
				}
				
				gwSet.add(meter.getGwId());
			}
		}
		
		return gwSet;
	}
	
	private static void isExists(String filePath){
		
		File file = new File(filePath);
		
		/**
		 * 如果不存在
		 */
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();//不存在则创建父目录
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
