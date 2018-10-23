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
import org.apache.hadoop.hbase.client.ResultScanner;
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
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.utils.tools.security.MD5Utils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.common.utils.EntityUtils;
import cn.com.tw.paas.service.queue.eum.HisColEnum;

@Repository
public class HReadHisDao extends HBaseOperaUtils{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String readDataTable = "h_read_his";
	
	//基本信息
	private String cfName = "d";
	
	@Autowired
	private CacheLocalService cacheService;
	
	/**
	 * 创建表
	 */
	public void createReadHisTable(){
		createTable(readDataTable, new String[]{cfName});
	}
	
	public void putRadHis(Map<String, Object> hisStoreInfo){
		try {
			String appId = (String) hisStoreInfo.get(HisColEnum.aId.name());
			String termType = (String) hisStoreInfo.get(HisColEnum.tTy.name());
			String meterAddr = (String) hisStoreInfo.get(HisColEnum.tNo.name());
			
			if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(termType) || StringUtils.isEmpty(meterAddr)) {
				throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
			}
			
			long currTime = (long) hisStoreInfo.get(HisColEnum.cTm.name());//读取时间
			
			long time = Long.MAX_VALUE - currTime;
			//rowkey 由MD5(appId)|maxLong - time|orgId|MD5(meterId)
			
			StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(time).append("|")
					.append(termType).append("|").append(MD5Utils.digest(meterAddr));
			
			putRowValueBatch(readDataTable, sb.toString(), cfName, hisStoreInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public void updatePushInfo(Map<String, Object> pushInfo){
		try {
			String rowKey = (String)pushInfo.get("rowKey");
			if (StringUtils.isEmpty(rowKey)) {
				return;
			}
			putRowValueBatch(readDataTable, rowKey, cfName, pushInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public Map<String, String> queryReadByKey(String key) {
		return null;
	}
	
	public List<Map<String, Object>> queryList(Map<String, String> params){
		
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			//获取查询参数
			String[] paramsStr = getRowParams(params);
			List<ColumnInfo> colList = getColParams(params);
			ResultScanner result = scanListRowKey(readDataTable, paramsStr[0], paramsStr[1], colList);
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
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
			
			return resultList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	/**
	 * 查询历史表，同条件查询，noNullQualifierName表示它不等于null的记录
	 * 如果是一进而出的表， 查询结果保留住回路，去掉其他回路
	 * 
	 * 通过时间区间内 查询指定列值，指定列名的数据
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryDateListByRowKeyQualifier(Map<String, String> params, String itemCodes, String noNullQualifierName){
		
		try {
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			List<String> cfNames = null;
			if (!StringUtils.isEmpty(itemCodes)) {
				cfNames = new ArrayList<String> ();
				String[] codes = itemCodes.split(",");
				for (String itemC : codes) {
					cfNames.add(cfName+":"+itemC);
				}
			}
			
			
			//获取查询参数
			String[] paramsStr = getRowParams(params);
			List<ColumnInfo> colList = getColParams(params);
			ResultScanner result = scanListRowKeyAndQualifier(readDataTable, paramsStr[0], paramsStr[1], colList, cfNames, noNullQualifierName);
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					}else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				String lType = (String) resultMap.get(HisColEnum.lTy.name());
				
				if (!StringUtils.isEmpty(lType) && !"0".equals(lType)) {
					continue;
				}
				
				//获取最后一条row
				String rowKey = Bytes.toString(res.getRow());
				resultMap.put("rowId", rowKey);
				resultList.add(resultMap);
			}
			
			return resultList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	
	public List<Map<String, Object>> queryPushByPage(Page page){
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>) page.getParamObj();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			HBPage hbPage = new HBPage();
			
			//获取查询参数
			String[] paramsStr = getRowParams(params);
			List<ColumnInfo> colList = getColParams(params);
			hbPage.setPage(page.getPage());
			hbPage.setRows(page.getRows());
			
			//显示多少列
			String itemCode = (String) params.get("itemCode");
			List<String> cfNames = null;
			if (!StringUtils.isEmpty(itemCode)) {
				cfNames = new ArrayList<String> ();
				String[] itemCodes = itemCode.split(",");
				for (String itemC : itemCodes) {
					cfNames.add(cfName+":"+itemC);
				}
			}
			
			Result[] result = scanPageByPageFiler(readDataTable, paramsStr[0], paramsStr[1], hbPage, colList, cfNames);
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					}else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				String tTy = (String) resultMap.get(HisColEnum.tTy.name());
				
				Map<String, Object> dSetMap = new HashMap<String, Object> ();
				for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
					if ("rowKey".equals(entry.getKey())) {
						continue;
					}
					try {
						Enum.valueOf(HisColEnum.class, entry.getKey());
					} catch (Exception e) {
						dSetMap.put(cacheCode(entry.getKey(), tTy), entry.getValue());
					}
				}
				
				Map<String, Object> pushData = EntityUtils.getPushData((String)resultMap.get(HisColEnum.tNo.name()), (String)resultMap.get(HisColEnum.aKy.name()), String.valueOf(resultMap.get(HisColEnum.rTm.name())), dSetMap, (String)resultMap.get(HisColEnum.tTy.name()), (String)resultMap.get(HisColEnum.tNNo.name()), (String)resultMap.get(HisColEnum.bNo.name()), (String) resultMap.get(HisColEnum.lTy.name()));
				
				resultMap.put("itemData", JsonUtils.objectToJson(pushData));
				
				//获取最后一条row
				String rowKey = Bytes.toString(res.getRow());
				resultMap.put("rowId", rowKey);
				resultList.add(resultMap);
			}
			
			page.setTotalRecord(hbPage.getTotalRecord());
			return resultList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	/**
	 * 分页读取历史数据，页面列表展示
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryReadByPage(Page page) {
		
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> params = (Map<String, String>) page.getParamObj();
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			//获取查询参数
			String[] paramsStr = getRowParams(params);
			//通过哪些参数查询
			List<ColumnInfo> colList = getColParams(params);
			
			HBPage hbPage = new HBPage();
			hbPage.setPage(page.getPage());
			hbPage.setRows(page.getRows());
			
			//显示多少列
			String itemCode = (String) params.get("itemCode");
			List<String> cfNames = null;
			if (!StringUtils.isEmpty(itemCode)) {
				cfNames = new ArrayList<String> ();
				String[] itemCodes = itemCode.split(",");
				for (String itemC : itemCodes) {
					cfNames.add(cfName+":"+itemC);
				}
			}
			
			/**
			 * 指定列 获取数据
			 */
			Result[] result = scanPageByPageFiler(readDataTable, paramsStr[0], paramsStr[1], hbPage, colList, cfNames);
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					}else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				String tTy = (String) resultMap.get(HisColEnum.tTy.name());
				
				Map<String, Object> dSetMap = new HashMap<String, Object> ();
				for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
					
					if ("rowKey".equals(entry.getKey())) {
						continue;
					}
					
					try {
						Enum.valueOf(HisColEnum.class, entry.getKey());
					} catch (Exception e) {
						dSetMap.put(cacheName(entry.getKey(), tTy), entry.getValue());
					}
				}
				
				resultMap.put("itemData", JsonUtils.objectToJson(dSetMap));
				
				
				//获取最后一条row
				String rowKey = Bytes.toString(res.getRow());
				resultMap.put("rowId", rowKey);
				resultList.add(resultMap);
				
			}
			
			page.setTotalRecord(hbPage.getTotalRecord());
			return resultList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException = {}",e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		} catch (Exception e1) {
			logger.error("Exception e1={}",e1);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	private String[] getRowParams(Map<String, String> params) {
		
		String[] paramsStr = new String[3];
		
		String appId = params.get("appId");
		if (StringUtils.isEmpty(appId)) {
			throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "应用ID不能为空");
		}
		
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
		
		/*if (!StringUtils.isEmpty(meterAddr)) {
			//regStr = ".*" +MD5Utils.digest(meterAddr) + "$";
			regStr = meterAddr;
		}*/
		
		paramsStr[0] = startRow;
		paramsStr[1] = stopRow;
		
		return paramsStr;
	}
	
	private List<ColumnInfo> getColParams(Map<String, String> params) {
		
		List<ColumnInfo> colList = new ArrayList<ColumnInfo>();
	
		String meterAddr = params.get("meterAddr");
		
		String termType = params.get("termType");
		if (!StringUtils.isEmpty(meterAddr)) {
			colList.add(new ColumnInfo(cfName, "tNo", meterAddr));
		}
		
		if (!StringUtils.isEmpty(termType)) {
			colList.add(new ColumnInfo(cfName, "tTy", termType));
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
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	public Date getDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    } 
	
	private String cacheCode(String shrotCode, String tTy) {
		
		//如果是热力泵直接返回缩写
		if ("150000".equals(tTy)) {
			return shrotCode;
		}
		
		Map<String, Map<String, String>> insMap = cacheService.itemAll();
		if (null == insMap) {
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "获取缓存数据失败");
		}
		
		Map<String, String> childMap = insMap.get(shrotCode);
		return childMap.get("itemCode");
	}
	
	private String cacheName(String shrotCode, String tTy) {
		//如果是热力泵直接返回缩写
		if ("150000".equals(tTy)) {
			return cacheService.getGeHuaMap(shrotCode);
		}
		
		Map<String, Map<String, String>> insMap = cacheService.itemAll();
		if (null == insMap) {
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "获取缓存数据失败");
		}
		
		Map<String, String> childMap = insMap.get(shrotCode);
		return childMap.get("itemName");
	}
}
