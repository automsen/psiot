package cn.com.tw.paas.monit.mapper.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.PushLogExpand;
import cn.com.tw.paas.monit.entity.db.org.PushLog;

public interface PushLogMapper {
    int deleteByPrimaryKey(String logId);

    int insert(PushLog record);

    int insertSelective(PushLog record);

    PushLog selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(PushLog record);

    int updateByPrimaryKey(PushLog record);

	List<PushLogExpand> selectPushLogPage(Page page);
	
	List<PushLogExpand> selectPushLogShow(Map<String, Object> Map);
	
	List<PushLog> selectByPage(Page page);
}