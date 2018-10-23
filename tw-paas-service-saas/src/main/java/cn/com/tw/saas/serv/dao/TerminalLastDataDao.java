package cn.com.tw.saas.serv.dao;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.hb.HBaseOperaUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.security.MD5Utils;

@Service
public class TerminalLastDataDao extends HBaseOperaUtils{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String table = "h_ss_term_last";
	
	private String cfName = "item";

	/**
	 * 创建表
	 */
	public void createTerminalLastTable(){
		try {
			createTable(table, new String[]{cfName});
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
	
	public void putLast(Map<String, Object> hisStoreInfo){
		
		try {
			
			String meterAddr = (String) hisStoreInfo.get("termNo");
			
			if (StringUtils.isEmpty(meterAddr)) {
				throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "参数不能为空");
			}
			
			StringBuffer sb = new StringBuffer(MD5Utils.digest(meterAddr));
			
			putRowValueBatch(table, sb.toString(), cfName, hisStoreInfo);
			
		} catch (IOException e) {
			logger.error(e.getMessage());
            throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "req hbase service error!!");
		}
	}
}
