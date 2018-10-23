package cn.com.tw.common.utils.tools.license;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.security.DESUtils;
import cn.com.tw.common.web.utils.env.Env;

public class ExpireUtils {
	
	public static LicExpireHandle licExpire;
	
	public static long[] keys =new long[] {0xBA19BE3797F54117L, 0x78772BAF542B5289L, 0x78710400E6DE26F8L, 0x2CF5390EC8BC0857L};
	
	private static String fileInfo;
	
	public static void setLic(LicExpireHandle licExpire){
		ExpireUtils.licExpire = licExpire;
	}
	
	public static LicExpireHandle getLic(){
		return licExpire;
	}
	
	/**
	 * 获取存储路径
	 * @return
	 */
	public static String fileStorePath(){
		//保存当前时间
		String libPath = Env.getVal("user.home");
		String projectJarName = Env.getVal("LOG_FILE");
		projectJarName = projectJarName.substring(projectJarName.lastIndexOf("/") + 1, projectJarName.lastIndexOf("."));
		projectJarName = DigestUtils.md5Hex(projectJarName);
		File file = new File(libPath + "/app/");
		if (!file.exists()){
			file.mkdirs();
		}
		
		String filePath = libPath + "/app/" + projectJarName + ".sec";
		return filePath;
	}
	
	public static String getEnStr(String curDate) throws Exception{
		if (StringUtils.isEmpty(curDate)){
			curDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		}
		String encryStr = DESUtils.encrypt(curDate, new ObfuscatedString(ExpireUtils.keys).toString());
		
		return encryStr;
	}
	
	public static String getDeStr(String enStr) throws Exception{
		return DESUtils.decrypt(enStr, new ObfuscatedString(ExpireUtils.keys).toString());
		
	}
	
	public static void storeInfoInFile(String info, File file) throws IOException, Exception{
		FileCopyUtils.copy(ExpireUtils.getEnStr(info).getBytes(), file);
		fileInfo = info;
	}
	
	public static String getInfoFormFile(File file) throws Exception{
		
		if (StringUtils.isEmpty(fileInfo)){
			String enStr = FileCopyUtils.copyToString(new FileReader(file));
			String store = ExpireUtils.getDeStr(enStr);
			fileInfo = store;
			return store;
		}
		
		return fileInfo;
		
	}

}
