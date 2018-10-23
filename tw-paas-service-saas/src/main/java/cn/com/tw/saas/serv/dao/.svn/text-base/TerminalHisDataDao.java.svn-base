package cn.com.tw.saas.serv.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.hb.HBaseOperaUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.security.MD5Utils;

@Repository
public class TerminalHisDataDao extends HBaseOperaUtils{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String table = "h_ss_term_his";
	
	private String cfName = "item";
	
	/**
	 * 创建表
	 */
	public void createTermHisTable(){
		try {
			createTable(table, new String[]{cfName});
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public void putRadHis(Map<String, Object> hisStoreInfo){
		try {
			
			String appId = (String) hisStoreInfo.get("orgId");
			String termType = (String) hisStoreInfo.get("meterCateg");
			String termNo = (String) hisStoreInfo.get("termNo");
			
			if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(termType) || StringUtils.isEmpty(termNo)) {
				throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
			}
			
			String updateTimeStr = (String) hisStoreInfo.get("updateTime");//读取时间
			long updateTime = Long.parseLong(updateTimeStr);//读取时间
			long time = Long.MAX_VALUE - updateTime;
			//rowkey 由MD5(appId)|maxLong - time|orgId|MD5(meterId)
			
			StringBuffer sb = new StringBuffer(MD5Utils.digest(appId)).append("|").append(time).append("|")
					.append(termType).append("|").append(MD5Utils.digest(termNo));
		
			putRowValueBatch(table, sb.toString(), cfName, hisStoreInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
}
