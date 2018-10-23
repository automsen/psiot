package cn.com.tw.paas.service.queue.dao.read;

import java.io.IOException;
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
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.paas.service.queue.common.cache.CacheLocalService;
import cn.com.tw.paas.service.queue.eum.HisColEnum;

@Repository
public class HReadLastDao extends HBaseOperaUtils{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String readDataTable = "h_read_last";
	
	private String cfName = "d";
	
	@Autowired
	private CacheLocalService cacheService;
	
	/**
	 * 创建表
	 */
	public void createReadHisTable(){
		createTable(readDataTable, new String[]{cfName});
	}
	
	public void putLast(String key, Map<String, Object> hisStoreInfo){
		try {
			putRowValueBatch(readDataTable, key, cfName, hisStoreInfo);
		} catch (IOException e) {
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public Map<String, Object> queryLastByKey(String key, String itemCode) {
		
		try {
			
			List<String> cfNames = null;
			if (!StringUtils.isEmpty(itemCode)) {
				cfNames = new ArrayList<String> ();
				String[] itemCodes = itemCode.split(",");
				for (String itemC : itemCodes) {
					cfNames.add(cfName+":"+itemC);
				}
			}
			
			Result result = getRowValue(readDataTable, key, cfNames);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			if(result.size() == 0 ) {
				return resultMap;
			}
			
			for (Cell cell : result.listCells()) {
				
				String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
				
				String tTy = Bytes.toString(result.getValue(cfName.getBytes(), "tTy".getBytes()));
				
				try {
					Enum.valueOf(HisColEnum.class, coluName);
					
					if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
						resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
					}else {
						resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
					}
					
				} catch (Exception e) {
					resultMap.put(cacheCode(coluName, tTy), Bytes.toString(CellUtil.cloneValue(cell)));
				}
			}
			
			return resultMap;
		} catch (IOException e) {
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	/**
	 * 获取最新数据分页
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryLastByKeys(List<byte[]> termNoList, String itemCode) {
		try {
			
			List<String> cfNames = null;
			if (!StringUtils.isEmpty(itemCode)) {
				cfNames = new ArrayList<String> ();
				String[] itemCodes = itemCode.split(",");
				for (String itemC : itemCodes) {
					cfNames.add(cfName+":"+itemC);
				}
			}
			
			Result[] result = getListByKeys(readDataTable, termNoList, cfNames);
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			//获取查询参数
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				if(res.size() == 0 ) {
					continue;
				}
				
				String tTy = Bytes.toString(res.getValue(cfName.getBytes(), "tTy".getBytes()));
				
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					try {
						Enum.valueOf(HisColEnum.class, coluName);
						
						if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
							resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
						}else {
							resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
						}
						
					} catch (Exception e) {
						resultMap.put(cacheCode(coluName, tTy), Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				resultList.add(resultMap);
			}
			return resultList;
		} catch (IOException e) {
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	/**
	 * 通过列获取行
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> queryLastByColValue(String familyColName, String colValue) {
		
		try {
			
			ResultScanner result = scanByColumnValue(readDataTable, familyColName, colValue);
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			//获取查询参数
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				if(res.size() == 0 ) {
					continue;
				}
				
				String tTy = Bytes.toString(res.getValue(cfName.getBytes(), "tTy".getBytes()));
				
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					try {
						Enum.valueOf(HisColEnum.class, coluName);
						
						if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
							resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
						}else {
							resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
						}
						
					} catch (Exception e) {
						resultMap.put(cacheCode(coluName, tTy), Bytes.toString(CellUtil.cloneValue(cell)));
					}
				}
				
				resultList.add(resultMap);
			}
			return resultList;
		} catch (IOException e) {
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	/**
	 * 查询某个机构下面的所有主回路的数据
	 * @param familyColName
	 * @param colValue
	 * @return
	 */
	public List<Map<String, Object>> queryLastByColValueMainLoop(String familyColName, String colValue) {
		
		try {
			
			ResultScanner result = scanByColumnValue(readDataTable, familyColName, colValue);
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			//获取查询参数
			for (Result res : result) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				if(res.size() == 0 ) {
					continue;
				}
				
				String tTy = Bytes.toString(res.getValue(cfName.getBytes(), "tTy".getBytes()));
				
				for (Cell cell : res.listCells()) {
					
					String coluName = Bytes.toString(CellUtil.cloneQualifier(cell));
					
					try {
						Enum.valueOf(HisColEnum.class, coluName);
						
						if ("cTm".equals(coluName) || "rTm".equals(coluName) || "pTm".equals(coluName)) {
							resultMap.put(coluName, Bytes.toLong(CellUtil.cloneValue(cell)));
						}else {
							resultMap.put(coluName, Bytes.toString(CellUtil.cloneValue(cell)));
						}
						
					} catch (Exception e) {
						resultMap.put(cacheCode(coluName, tTy), Bytes.toString(CellUtil.cloneValue(cell)));
					}
					
				}
				
				String loopType = (String) resultMap.get(HisColEnum.lTy.name());
				//如果0表示主回路，如果是主回路，则返回
				if ("0".equals(loopType)) {
					resultList.add(resultMap);
				}
				
			}
			return resultList;
		} catch (IOException e) {
			logger.error("Exception = {}", e);
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
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
}
