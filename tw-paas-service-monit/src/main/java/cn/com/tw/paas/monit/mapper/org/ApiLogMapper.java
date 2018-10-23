/**
 * TODO: complete the comment
 */
package cn.com.tw.paas.monit.mapper.org;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.ApiLogExpand;
import cn.com.tw.paas.monit.entity.db.org.ApiLog;

public interface ApiLogMapper {
    int deleteByPrimaryKey(String logId);

    int insert(ApiLog record);

    ApiLog selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(ApiLog record);

	List<ApiLogExpand> selectByPage(Page paramPage);
	
	List<ApiLogExpand> selectApiLogExpandByShow(Map<String, Object> map);
}