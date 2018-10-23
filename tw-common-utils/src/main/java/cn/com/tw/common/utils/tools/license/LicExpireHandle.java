package cn.com.tw.common.utils.tools.license;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.utils.DateUtils;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.license.exception.LicenseException;

import com.verhas.licensor.License;

public class LicExpireHandle {
	
	private String licenseFileName = "license.lic";
	
	private License lic = new License();
	
	public LicExpireHandle() throws IOException, PGPException{
		lic.loadKeyRingFromResource("pubring.gpg", null);
		lic.setLicenseEncodedFromFile(licenseFileName);
	}
	
	public LicExpireHandle(String licenseFileName){
		this.licenseFileName = licenseFileName;
	}
	
	public void parse(){
		checkDateAndVersionValidity();
		//String edition = lic.getFeature("edition");
	}
	
	public String getIssueDate(){
		
		return lic.getFeature("issue-date");
		
	}
	
	/**
	 * L001标识license文件不合法，或者修改系统时间导致和license文件信息不匹配，这样系统启动不成功
	 * L002标识过期，系统启动可以成功，但有些操作操作不了
	 */
	protected void checkDateAndVersionValidity() {
       String issueDate = lic.getFeature("issue-date");
       
       String validDate = lic.getFeature("expire-date");
       
       if (StringUtils.isEmpty(issueDate) || StringUtils.isEmpty(validDate)){
    	   
    	   throw new LicenseException("L001", "license field can not empty!!");
    	   
       }
       
       String today = getTodayString();
       LicenseDate todayLD = new LicenseDate(today);
       if (!todayLD.isLaterThan(issueDate)) {
    	   throw new LicenseException("L001", "Issue date is too late, probably tampered system time");
       }
       
	   if (todayLD.isLaterThan(validDate)) {
		   
		   SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			
		   int day = 0;
		   try {
			   Date b1 =sdf.parse(validDate);
			   Date b2 = sdf.parse(todayLD.getDate());
			   //获取相隔天数
			   day = (int) ((b2.getTime() - b1.getTime()) / 1000 / 60 / 60 / 24);
		   } catch (ParseException e) {
			   e.printStackTrace();
		   }
			
	       throw new LicenseException("L002_" + day, "License expired.");
	   }
	   
	   String machineId = lic.getFeature("machine-id");
	   
	   if (StringUtils.isEmpty(machineId)){
		   throw new LicenseException("L001", "License illegal. Please contact the manager!!!");
	   }
	   
	   HardwareBinder binder = new HardwareBinder();
	   
	   try {
			String machineStr = binder.getMachineIdString();
			
			if (!machineId.equals(machineStr)){
				throw new LicenseException("L001","License illegal. Application and computer binding , can not move!, licMachineId = " + machineId + ", localMachineId = " + machineStr);
			}
	   } catch (Exception e) {
		   
		   if (e instanceof LicenseException) {
			   throw (LicenseException)e;
		   }
		   
		   throw new LicenseException("L001","License illegal. Application and computer machine get fail !!!!!, There is a problem with the system. e = " + errInfo(e));
	   }	
     
   }
	
   private static String errInfo(Exception e) {  
        StringWriter sw = null;  
        PrintWriter pw = null;  
        try {  
            sw = new StringWriter();  
            pw = new PrintWriter(sw);  
            // 将出错的栈信息输出到printWriter中  
            e.printStackTrace(pw);  
            pw.flush();  
            sw.flush();  
        } catch (Exception e1){
        	return e.toString();
        }finally {  
            if (sw != null) {  
                try {  
                    sw.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (pw != null) {  
                pw.close();  
            }  
        }  
        return sw.toString();  
    }  

	
   private String getTodayString(){
	   return DateUtils.formatDate(new Date(), "yyyyMMdd");
   }
}
