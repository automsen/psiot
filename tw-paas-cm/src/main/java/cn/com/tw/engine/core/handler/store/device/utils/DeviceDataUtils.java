package cn.com.tw.engine.core.handler.store.device.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.com.tw.engine.core.entity.GwMeters;

public class DeviceDataUtils {

	private final static String DEVICE_CACHE_FILE_PATH = "data/device.d";

	/**
	 * 保存缓存配置
	 * @param fileName
	 * @param cacheObj
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void saveDataConfig(GwMeters gwMeters) throws IOException{
		
		List<GwMeters> gwMetersList = null;
		try {
			gwMetersList = readDataAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (gwMetersList == null){
			List<GwMeters> cacheObjList = new ArrayList<GwMeters>();
			cacheObjList.add(gwMeters);
			writeData(cacheObjList);
		}else{
			
			Iterator<GwMeters> it = gwMetersList.iterator();
			while(it.hasNext()) {
				GwMeters dataItem = it.next();
				if (dataItem.getGwId().equals(gwMeters.getGwId())) {
					it.remove();
				}
			}
			
			gwMetersList.add(gwMeters);
			
			writeData(gwMetersList);
		}
		
		
	}
	
	private static synchronized void writeData(Object cacheObj) throws IOException{
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(DEVICE_CACHE_FILE_PATH);
			
			oos = new ObjectOutputStream(os);
			
			oos.writeObject(cacheObj);
			
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<GwMeters> readDataAll() throws IOException, ClassNotFoundException{
		
		isExists(DEVICE_CACHE_FILE_PATH);
		
		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			
			is = new FileInputStream(DEVICE_CACHE_FILE_PATH);
			ois = new ObjectInputStream(is);
			
			return (List<GwMeters>) ois.readObject();
			
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
	
	/**
	 * 删除数据
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static synchronized void deleteData(String key) throws IOException, ClassNotFoundException{
		List<GwMeters> gwMeters = readDataAll();
		if(gwMeters != null){
			Iterator<GwMeters> it = gwMeters.iterator();
			while(it.hasNext()){
				GwMeters dataItem = it.next();
			    if(dataItem.getGwId().equals(key)){
			        it.remove();
			    }
			}
			
		}
		
		writeData(gwMeters);
	}
	
	public static synchronized GwMeters readDataByKey(String key) throws ClassNotFoundException, IOException{
		List<GwMeters> gwMeters = readDataAll();
		if (gwMeters == null){
			return null;
		}
		for (GwMeters dataItem : gwMeters){
			if (dataItem.getGwId().equals(key)){
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
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
