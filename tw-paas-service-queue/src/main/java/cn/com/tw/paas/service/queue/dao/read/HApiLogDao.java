package cn.com.tw.paas.service.queue.dao.read;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.hb.HBaseOperaUtils;
import cn.com.tw.common.hb.entity.ColumnInfo;
import cn.com.tw.common.hb.entity.HBPage;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.eum.LogColEnum;

@Repository
public class HApiLogDao extends HBaseOperaUtils{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String table = "h_read_his";
	
	private String cfName = "item";
	
	@Autowired
	private CacheLocalService cacheService;
	
	/**
	 * 创建表
	 */
	public void createApiLogTable(){
		createTable(table, new String[]{cfName});
	}
	
	public void putApiLog(Map<String, Object> hisStoreInfo){
		try {
			String appId = (String) hisStoreInfo.get(LogColEnum.appId.name());
			String apiUrl = (String) hisStoreInfo.get(LogColEnum.apiUrl.name());
			if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(apiUrl)) {
				throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
			}
			
			long currTime = (long) hisStoreInfo.get(LogColEnum.createTime.name());//读取时间
			
			long time = Long.MAX_VALUE - currTime;
			//rowkey 由MD5(appId)|maxLong - time|orgId|MD5(meterId)
			
			StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(time).append("|").append(MD5Utils.digest(apiUrl));
			
			putRowValueBatch(table, sb.toString(), cfName, hisStoreInfo);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public void updatePushInfo(Map<String, Object> pushInfo){
		try {
			putRowValueBatch(table, (String)pushInfo.get("rowKey"), cfName, pushInfo);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public Map<String, String> queryReadByKey(String key) {
		return null;
	}
	
	public List<Map<String, Object>> queryLogByPage(Page page){
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>) page.getParamObj();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			//获取查询参数
			String[] paramsStr = getRowParams(params);
			List<ColumnInfo> colList = getColParams(params);
			HBPage hbPage = new HBPage();
			hbPage.setPage(page.getPage());
			hbPage.setRows(page.getRows());
			Result[] result = scanPageRowKey(table, paramsStr[0], paramsStr[1], hbPage, colList, null);
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					if ("createTime".equals(coluName) || "readTime".equals(coluName) || "pTime".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					}else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				//获取最后一条row
				String rowKey = Bytes.toString(res.getRow());
				resultMap.put("rowId", rowKey);
				resultList.add(resultMap);
				
			}
			
			page.setTotalRecord(hbPage.getTotalRecord());
			return resultList;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
private String[] getRowParams(Map<String, String> params) {
		
		String[] paramsStr = new String[3];
		
		String appId = params.get("appId");
		if (StringUtils.isEmpty(appId)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "应用ID不能为空");
		}
		
		String meterAddr = params.get("meterAddr");
		String startTime = params.get("startTime");
		String endTime = params.get("endTime");
		
		//最后时间默认是当前时间
		long endTimeStamp = new Date().getTime();
		if (!StringUtils.isEmpty(endTime)) {
			Date date = strToDate(endTime);
			if (date != null) {
				endTimeStamp = date.getTime();
			}
		}
		
		//获取前三天的时间，默认
		long startTimeStamp = getDateBefore(new Date(), 3).getTime();
		if (!StringUtils.isEmpty(startTime)) {
			Date date = strToDate(startTime);
			if (date != null) {
				startTimeStamp = date.getTime();
			}
		}
		
		StringBuffer sb = new StringBuffer();
		String startRow = sb.append(MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - endTimeStamp).toString();
		sb.setLength(0);
		String stopRow = sb.append(MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - startTimeStamp).toString();
		//如果应用Id不为空，机构Id不为空，以应用Id和时间段进行查询
		/*if (!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(orgId)) {
			startRow = sb.append(MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - endTimeStamp).toString();
			stopRow = sb.append(MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - startTimeStamp).toString();
			//startRow = sb.append("^" + MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - endTimeStamp).toString();
		}else if (StringUtils.isEmpty(appId) && StringUtils.isEmpty(orgId)) {
			sb.append(Long.MAX_VALUE - endTimeStamp).append("|").append(orgId);
		}else {
			
		}*/
	/*	
		StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(Long.MAX_VALUE - endTimeStamp).append("|")
				.append(orgId).append("|").append(MD5Utils.digest(meterAddr));*/
		String regStr = null;
		
		if (!StringUtils.isEmpty(meterAddr)) {
			//regStr = ".*" +MD5Utils.digest(meterAddr) + "$";
			regStr = meterAddr;
		}
		
		paramsStr[0] = startRow;
		paramsStr[1] = stopRow;
		paramsStr[2] = regStr;
		
		return paramsStr;
	}
	
	private List<ColumnInfo> getColParams(Map<String, String> params) {
		
		List<ColumnInfo> colList = new ArrayList<ColumnInfo>();
	
		String meterAddr = params.get("meterAddr");
		
		if (!StringUtils.isEmpty(meterAddr)) {
			colList.add(new ColumnInfo(cfName, "termNo", meterAddr));
		}
		
		return colList;
	}
	
	private Date strToDate(String timeStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = formatter.parse(timeStr);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Date getDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    } 
}
