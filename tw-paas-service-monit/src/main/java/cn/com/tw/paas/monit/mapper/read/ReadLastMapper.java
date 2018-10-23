package cn.com.tw.paas.monit.mapper.read;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;

public interface ReadLastMapper {
    
    int replace(ReadLast record);
    
    int updateByAddrAndItem(ReadLast record);
    
    ReadLast selectByAddrAndItem(ReadLast record);

    ReadLast selectByPrimaryKey(String id);

	List<ReadLast> selectLast(ReadLast readLast);

	List<ReadLast> selectRealTimeTrace(String meterAddr);

	List<ReadLast> selectReadLastByPage(Page pageData);
	
	List<ReadLast> selectByMeterAddr(String meterAddr);
}