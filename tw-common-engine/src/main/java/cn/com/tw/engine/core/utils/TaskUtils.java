package cn.com.tw.engine.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import cn.com.tw.engine.core.entity.Task;

public class TaskUtils {
	
	private final static String TASK_FILE_PATH = "data/task.object";
	
	/**
	 * 读取配置文件
	 */
	public static Task readTaskFromFile(){
		
		isExists();
		
		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			
			is = new FileInputStream(TASK_FILE_PATH);
			ois = new ObjectInputStream(is);
			
			return (Task) ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
		return null;
		
	}
	
	/**
	 * 写
	 * @param task
	 */
	public static void writeTaskToFile(Task task){
		
		isExists();
		
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = new FileOutputStream(TASK_FILE_PATH);
			
			oos = new ObjectOutputStream(os);
			
			oos.writeObject(task);
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private static void isExists(){
		
		File file = new File(TASK_FILE_PATH);
		
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
