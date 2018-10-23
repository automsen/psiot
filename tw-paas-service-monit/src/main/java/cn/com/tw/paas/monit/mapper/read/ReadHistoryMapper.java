package cn.com.tw.paas.monit.mapper.read;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.business.read.ReadHistorySimple;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;

public interface ReadHistoryMapper {

    int insert(ReadLast record);

	List<ReadHistory> selectPage(Page page);
	
	List<ReadHistory> selectHistoryShow(Map<String, Object> map);

	List<ReadHistory> selectList(ReadHistoryExtend param);
	
	List<ReadHistorySimple> selectListForApi(Page param);

	List<ReadLast> selectTrace(String meterAddr, Date startTime, Date endTime);

	List<ReadHistoryExtend> selectTrace(ReadHistoryExtend param);

	List<ReadHistory> select24hours(ReadHistoryExtend readHistoryExtend);
	
}